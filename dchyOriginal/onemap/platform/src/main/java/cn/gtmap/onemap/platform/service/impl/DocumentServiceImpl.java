package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.service.DocumentService;
import cn.gtmap.onemap.platform.service.TemplateService;
import cn.gtmap.onemap.platform.utils.AppPropertyUtils;
import com.alibaba.fastjson.JSONArray;
import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.*;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-9-9 上午9:51
 */
@Service
public class DocumentServiceImpl extends BaseLogger implements DocumentService {

    private static final String START_MARK = "[$]";

    private static final String DEFAULT_SHEET = "Sheet1";

    /**
     * 分析导出的XML模板位置
     */
    private static final String ANALYSIS_EXCLE_DIR = "analysis/export/";

    private static final String VIDEO_EXCLE_DIR = "docs/";

    /**
     * 分析展示结果模板文件位置
     */
    private static final String ANALYSIS_FTL_DIR = "analysis/template/";

    private static final String TPL_SUFFIX = ".ftl";

    enum Tag {
        name, header, data, result
    }

    enum FileType {
        txt, xml, shp, dbf, shx, prj, cpg
    }

    @Autowired
    private TemplateService templateService;


    /**
     * resource
     */
    private String location;

    /**
     * read excel
     *
     * @param inputStream
     * @return
     */
    @Override
    public List<List> readExcel(InputStream inputStream) {
        Assert.notNull(inputStream, getMessage("doc.is.not.null"));
        List<List> content = new ArrayList<List>();
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List item = null;
            for (Row row : sheet) {
                item = new ArrayList();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            item.add(cell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            item.add(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell))
                                item.add(cell.getDateCellValue());
                            else
                                item.add(cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            item.add(cell.getCellFormula());
                            break;
                        default:
//                            item.add(null);
                    }
                }
                content.add(item);
            }
            return content;
        } catch (IOException e) {
            throw new RuntimeException(getMessage("doc.read.excle.error", e.getLocalizedMessage()));
        } catch (InvalidFormatException e) {
            throw new RuntimeException(getMessage("doc.excle.format.error", e.getLocalizedMessage()));
        }
    }

    /**
     * write data to excel
     *
     * @param data
     * @param tplName
     * @return
     */
    @Override
    public Document writeExcel(Map<String, List> data, String tplName) {
        if (isNull(tplName)) throw new RuntimeException(getMessage("doc.name.not.null"));
        Workbook workbook = null;
        Document document = Document.getDocByName(tplName);
        InputStream in = null;
        try {
            workbook = WorkbookFactory.create(getExcel(document));
        } catch (IOException e) {
            throw new RuntimeException(getMessage("doc.xls.format.error", tplName));
        } catch (InvalidFormatException e) {
            throw new RuntimeException(getMessage("doc.xls.format.error", tplName));
        }
        if (!isNull(workbook)) {
            for (Map.Entry<String, List> entry : data.entrySet()) {
                String sheetName = entry.getKey();
                List<List> values = entry.getValue();
                Sheet sheet = workbook.getSheet(sheetName);
//                if (isNull(sheet)) throw new RuntimeException(getMessage("doc.sheet.null", sheetName));
                if (isNull(sheet)) {
                    sheet = workbook.getSheet(DEFAULT_SHEET);
                    if (isNotNull(sheet)) workbook.setSheetName(0, sheetName);
                }
                int rowIndex = -1;
                CellStyle cellStyle = null;
                for (Row row : sheet) {
                    Cell start = row.getCell(0);
                    if (!isNull(start) && START_MARK.equals(start.getStringCellValue())) {
                        rowIndex = start.getRowIndex();
                        cellStyle = start.getCellStyle();
                        break;
                    }
                }
                if (rowIndex == -1) throw new RuntimeException(getMessage("doc.start.position.not.set"));
                Row row = null;
                Cell cell = null;
                for (int i = 0; i < values.size(); i++) {
                    row = sheet.createRow(i + rowIndex);
                    List columns = values.get(i);
                    for (int cIndex = 0; cIndex < columns.size(); cIndex++) {
                        cell = row.createCell(cIndex);
                        cell.setCellStyle(cellStyle);
                        Object value = columns.get(cIndex);
                        if (value instanceof Integer) cell.setCellValue(((Integer) value).intValue());
                        else if (value instanceof Double) cell.setCellValue(((Double) value).doubleValue());
                        else cell.setCellValue(String.valueOf(value));
                    }
                }
            }
            //
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                workbook.write(out);
                document.setContent(out.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(getMessage("doc.out.stream.error", e.getLocalizedMessage()));
            }
        }
        return document;
    }

    /**
     * write data to excel
     *
     * @param data [{"name":"Sheet1","header":["column1","column2","column3"],"data":[[1234,"yyyyy","zzzzzz"],[3456,"xxxxx","zzzzzz"]]}]
     * @return
     * @throws Exception
     */
    @Override
    public Document writeExcel(List<Map> data) throws Exception {
        Document document;
        if ("old".equals(AppPropertyUtils.getAppEnv("office.plugin.version")))
            document = new Document(Tag.result.name(), Document.Type.xls);
        else
            document = new Document(Tag.result.name(), Document.Type.xlsx);
        Workbook workbook = new XSSFWorkbook();
        for (Map item : data) {
            String sheetName = isNull(item.get(Tag.name.name())) ? ("Sheet" + data.indexOf(item)) : String.valueOf(item.get(Tag.name.name()));
            //create sheet
            Sheet sheet = workbook.createSheet(sheetName);
            Row row = sheet.createRow(0);
            List<String> header = (List<String>) item.get(Tag.header.name());
            for (int i = 0; i < header.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(header.get(i));
            }
            //set data value
            List<List> values = (List<List>) item.get(Tag.data.name());
            for (int rowIndex = 0; rowIndex < values.size(); rowIndex++) {
                row = sheet.createRow(rowIndex + 1);
                List value = values.get(rowIndex);
                for (int colIndex = 0; colIndex < value.size(); colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    cell.setCellValue(String.valueOf(value.get(colIndex)));
                }
            }
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            document.setContent(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(getMessage("doc.out.stream.error", e.getLocalizedMessage()));
        }
        return document;
    }

    /**
     * write to txt
     *
     * @param data    [{coords:{1,x,y}}]
     * @param tplName 输出文件名
     * @return
     * @throws Exception
     */
    public Document writeTxt(Map<String, List> data, String tplName, String type) throws Exception {
        if (isNull(tplName)) throw new RuntimeException(getMessage("doc.name.not.null"));
        Document document = Document.getDocByName(tplName);
        if (type != null && type.equals("txt")) {
            for (Map.Entry<String, List> entry : data.entrySet()) {
                String keyname = entry.getKey();
                List values = entry.getValue();
                byte[] contents = null;
                String ss = "";//"\r\n";
                //ss += "@\r\n";
                for (int i = 0; i < values.size(); i++) {
                    JSONArray obj = (JSONArray) values.get(i);
                    for (int j = 0; j < obj.size(); j++) {
                        JSONArray jsArr = (JSONArray) obj.get(j);
                        ss += "J" + (j + 1) + "," + (i + 1) + "," + String.valueOf(jsArr.get(1)) + "," + String.valueOf(jsArr.get(0)) + "\r\n";
                        ss = ss.replace("]", "");
                        ss = ss.replace("[", "");
                    }

                }
                contents = ss.getBytes(Constant.UTF_8);
                document.setContent(contents);
            }
        } else {
            if (type != null && type.equals("txt_bj")) {
                for (Map.Entry<String, List> entry : data.entrySet()) {
                    String keyname = entry.getKey();
                    List values = entry.getValue();
                    String ss = "";
                    byte[] contents = null;
                    int tagi = 1;
                    for (int i = 0; i < values.size(); i++) {
                        JSONArray arr = (JSONArray) values.get(i);
                        String coor = "";
                        ss += arr + "\r";
                    }
                    ss = ss.replace("]", "");
                    ss = ss.replace("[", "");
                    contents = ss.getBytes(Constant.UTF_8);
                    document.setContent(contents);
                }

            }
        }
        return document;
    }


    private static final String EXTRA_MARK = "[@]";

    /**
     * 导出分析结果至excel
     *
     * @param data    分析结果数据
     * @param tplName 分析导出模板名称
     * @return
     * @throws Exception
     */
    @Override
    public Document renderAnalysisExcel(Object data, String tplName, Document.Type type) throws Exception {
        Document document = Document.getDocByName(tplName);
        String content = templateService.getTemplate(data, ANALYSIS_EXCLE_DIR.concat(tplName));
        document.setContent(content.getBytes(Constant.UTF_8));
        document.setType(type);
        return document;
    }


    @Override
    public Workbook createVideoExcel(List<Camera> cameras) {
        String[] excelHeader = {"区域名称", "名称", "区域id", "设备id", "X坐标", "Y坐标", "高度", "平台名称", "设备类型"};
        int[] excelHeaderWidth = {180, 180, 180, 180, 180, 180, 150, 150, 150};
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("监控点信息");
        Row row = sheet.createRow(0);
        CellStyle style = wb.createCellStyle();
        // 设置居中样式
        style.setAlignment(CellStyle.ALIGN_LEFT); // 水平居左
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
        row.setHeight((short) 400);

        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setBoldweight((short) 500);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // 设置列宽度（像素）
        for (int i = 0; i < excelHeaderWidth.length; i++) {
            sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
        }

        // 添加表格头
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }

        //添加数据
        for (int i = 1; i < cameras.size(); i++) {
            row = sheet.createRow(i);
            Camera camera = cameras.get(i);
            row.createCell(0).setCellValue(camera.getRegionName());
            row.createCell(1).setCellValue(camera.getName());
            row.createCell(2).setCellValue(camera.getVcuId());
            row.createCell(3).setCellValue(camera.getIndexCode());
            row.createCell(4).setCellValue(camera.getX());
            row.createCell(5).setCellValue(camera.getY());
            row.createCell(6).setCellValue(camera.getHeight());
            row.createCell(7).setCellValue(camera.getPlatform());
            row.createCell(8).setCellValue(camera.getType());
        }

        return wb;
    }


    @Override
    public Workbook renderUnusedVideoExcel(List<Map> data) {
        String[] excelHeader = {"区镇名称", "设备名称", "设备编号", "最近一次使用时间"};
        int[] excelHeaderWidth = {200, 200, 200, 200};
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("监控点告警信息");
        Row row = sheet.createRow((int) 0);
        CellStyle style = wb.createCellStyle();
        // 设置居中样式
        style.setAlignment(CellStyle.ALIGN_LEFT); // 水平居左
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
        row.setHeight((short) 400);

        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setBoldweight((short) 500);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // 设置列宽度（像素）
        for (int i = 0; i < excelHeaderWidth.length; i++) {
            sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
        }

        // 添加表格头
        for (int i = 0; i < excelHeader.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }

        //render data
        for (int i = 0; i < data.size(); i++) {
            row = sheet.createRow(i + 1);
            Map item = data.get(i);
            row.createCell(0).setCellValue(item.get("regionName") == null ? "" : item.get("regionName").toString());
            row.createCell(1).setCellValue(item.get("deviceName") == null ? "" : item.get("deviceName").toString());
            row.createCell(2).setCellValue(item.get("cameraId") == null ? "" : item.get("cameraId").toString());
            row.createCell(3).setCellValue(item.get("time") == null ? "" : item.get("time").toString());
        }
        return wb;
    }

    /**
     * read zip file
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    @Override
    public List<Document> readZip(InputStream inputStream) throws Exception {
        assert inputStream != null : getMessage("doc.zip.is.notnull");
        List<Document> documents = new ArrayList<Document>();
        ZipInputStream zip = new ZipInputStream(inputStream);
        ZipEntry entry = null;
        ByteArrayOutputStream bos = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            if (inputStream.markSupported())
                inputStream.mark(inputStream.available());
            else {
                bufferedInputStream = new BufferedInputStream(inputStream);
                bufferedInputStream.mark(inputStream.available());
            }
            try {
                entry = zip.getNextEntry();
            } catch (IllegalArgumentException e) {
                entry = zip.getNextEntry();
            }
            while (entry != null) {
                logger.debug(" zip entity name : {} ", entry.getName());
                try {
                    Document document = Document.getDocByName(entry.getName());
                    bos = new ByteArrayOutputStream();
                    IOUtils.copy(zip, bos);
                    documents.add(document.setContent(bos.toByteArray()));
                    zip.closeEntry();
                    entry = zip.getNextEntry();
                } catch (IOException e) {
                    throw new RuntimeException(getMessage("doc.zip.entity.error", entry.getName(), e.getLocalizedMessage()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(getMessage("doc.zip.error", e.getLocalizedMessage()));
        } finally {
            if (inputStream.markSupported()) {
                inputStream.reset();
            } else if (bufferedInputStream != null) {
                bufferedInputStream.reset();
            }

        }
        return documents;
    }

    /**
     * read zip file
     *
     * @param file
     * @return
     */
    @Override
    public List<Document> readZipFile(File file) {
        assert file != null : getMessage("doc.zip.is.notnull");
        List<Document> documents = new ArrayList<Document>();
        org.apache.tools.zip.ZipFile zipFile = null;
        try {
            zipFile = new org.apache.tools.zip.ZipFile(file, "GBK");
            Enumeration enumeration = zipFile.getEntries();
            while (enumeration.hasMoreElements()) {
                org.apache.tools.zip.ZipEntry entry = (org.apache.tools.zip.ZipEntry) enumeration.nextElement();
                String entryName = entry.getName().trim();
                if (!isSupportedType(entryName))
                    continue;
                Document document = Document.getDocByName(entry.getName());
                InputStream inputStream = null;
                try {
                    inputStream = zipFile.getInputStream(entry);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    IOUtils.copy(inputStream, bos);
                    document.setContent(bos.toByteArray());
                    documents.add(document);
                } catch (IOException e) {
                    throw new RuntimeException(getMessage("doc.zip.entity.error", entry.getName(), e.getLocalizedMessage()));
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(getMessage("doc.zip.error", e.getLocalizedMessage()));
        } finally {
            if (zipFile != null) try {
                zipFile.close();
            } catch (IOException e) {
            }
        }
        return documents;
    }

    /***
     * read zip inputstream
     * @param inputStream
     * @return
     * @throws Exception
     */
    @Override
    public List<Document> readZipIn(InputStream inputStream) throws Exception {
        List<Document> documents = new ArrayList<Document>();
        if (documents.size() == 0) {
            File tmp = new File(System.getProperty("java.io.tmpdir").concat("\\TEMP_" + System.currentTimeMillis()).concat(".zip"));
            try {
                FileOutputStream output = new FileOutputStream(tmp);
                try {
                    IOUtils.copyLarge(inputStream, output, 0, inputStream.available(), new byte[inputStream.available()]);
                    output.close();
                } finally {
                    IOUtils.closeQuietly(output);
                }
                documents = readZipFile(tmp);
                if (documents.size() == 0)
                    throw new RuntimeException(getMessage("zip.format.error"));
            } finally {
                FileUtils.deleteQuietly(tmp);
            }
        }
        return documents;
    }

    /**
     * read archive <br/>
     * the archiver formats are 7z, ar, arj, cpio, dump, tar and zip
     *
     * @param inputStream
     * @return
     */
    @Override
    public List<Document> readArchive(InputStream inputStream) {
        return null;
    }

    /**
     * the archiver formats are 7z, ar, arj, cpio, dump, tar
     *
     * @param file
     * @return
     */
    @Override
    public List<Document> readArchiveFile(File file) {
        assert file != null : getMessage("doc.zip.is.notnull");
        List<Document> documents = new ArrayList<Document>();
        Archive archive = null;
        try {
            archive = new Archive(file);
            FileHeader fileHeader = archive.nextFileHeader();
            while (!isNull(fileHeader)) {
                String entryName;
                if (fileHeader.isUnicode())
                    entryName = fileHeader.getFileNameW().trim();
                else
                    entryName = fileHeader.getFileNameString().trim();
                if (!isSupportedType(entryName))
                    continue;
                try {

                    Document document = Document.getDocByName(entryName);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    archive.extractFile(fileHeader, bos);
                    document.setContent(bos.toByteArray());
                    documents.add(document);

                } catch (RarException e) {
                    logger.error(getMessage("doc.archive.entity.error", entryName, e.getLocalizedMessage()));
                    throw new RuntimeException(getMessage("doc.archive.entity.error", entryName, e.getLocalizedMessage()));
                }
                fileHeader = archive.nextFileHeader();
            }
            archive.close();
        } catch (RarException e) {
            logger.error(getMessage("doc.archive.error", e.getLocalizedMessage()));
            throw new RuntimeException(getMessage("doc.archive.error", e.getLocalizedMessage()));
        } catch (IOException e) {
            logger.error(getMessage("doc.archive.error", e.getLocalizedMessage()));
            throw new RuntimeException(getMessage("doc.archive.error", e.getLocalizedMessage()));
        } finally {
            try {
                if (!isNull(archive))
                    archive.close();
            } catch (IOException e) {
                logger.error(getMessage("", e.getMessage()));
            }
        }
        return documents;
    }

    /**
     * @param data
     * @param tplName
     * @return
     * @throws Exception
     */
    @Override
    public Document renderExcel(Object data, String tplName) throws Exception {
        Document document = Document.getDocByName(tplName);
        String content = templateService.getTemplate(data, tplName);
        document.setContent(content.getBytes(Constant.UTF_8));
        document.setType(Document.Type.xls);
        return document;
    }

    /**
     * 根据文件名判断是否是支持的文件类型
     *
     * @param name
     * @return
     */
    private boolean isSupportedType(String name) {
        for (int i = 0; i < FileType.values().length; i++) {
            String fileType = FileType.values()[i].name();
            if (name.endsWith(fileType))
                return true;
        }
        return false;
    }

    /**
     * get excel
     *
     * @param doc
     * @return
     * @throws IOException
     */
    private InputStream getExcel(Document doc) throws IOException {
        try {
            return getDocResorce(doc.getFileName()).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(getMessage("doc.not.found", doc.getFileName()));
        }
    }

    /**
     * @param docName
     * @return
     * @throws MalformedURLException
     */
    private Resource getDocResorce(String docName) throws MalformedURLException {
        return new UrlResource(location.concat(docName));
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public Document writeExcelWithWidth(List<Map> data) throws Exception {
        Document document;
        int[] excelHeaderWidth = {180, 180, 180, 180, 180, 180, 150, 150, 150, 150};
        if ("old".equals(AppPropertyUtils.getAppEnv("office.plugin.version"))) {
            document = new Document(Tag.result.name(), Document.Type.xls);
        } else {
            document = new Document(Tag.result.name(), Document.Type.xlsx);
        }
        Workbook workbook = new XSSFWorkbook();
        CellStyle style = workbook.createCellStyle();
        // 设置居中样式
        style.setAlignment(CellStyle.ALIGN_LEFT); // 水平居左
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中

        Font font = workbook.createFont();
        font.setFontName("黑体");
        font.setBoldweight((short) 500);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        for (Map item : data) {
            String sheetName = isNull(item.get(Tag.name.name())) ? ("Sheet" + data.indexOf(item)) : String.valueOf(item.get(Tag.name.name()));
            //create sheet
            Sheet sheet = workbook.createSheet(sheetName);
            Row row = sheet.createRow(0);
            row.setHeight((short) 400);
            List<String> header = (List<String>) item.get(Tag.header.name());
//             设置列宽度（像素）
            for (int i = 0; i < header.size(); i++) {
                sheet.setColumnWidth(i, 32 * excelHeaderWidth[i]);
            }
            for (int i = 0; i < header.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(header.get(i));
            }
            //set data value
            List<List> values = (List<List>) item.get(Tag.data.name());
            for (int rowIndex = 0; rowIndex < values.size(); rowIndex++) {
                row = sheet.createRow(rowIndex + 1);
                List value = values.get(rowIndex);
                for (int colIndex = 0; colIndex < value.size(); colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    cell.setCellValue(String.valueOf(value.get(colIndex)));
                }
            }
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            document.setContent(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(getMessage("doc.out.stream.error", e.getLocalizedMessage()));
        }
        return document;
    }
}
