package cn.gtmap.onemap.platform.utils;

import com.gtis.spring.Container;
import freemarker.template.Template;
import oracle.sql.CLOB;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExportExcelUtil {
    protected static final Logger logger = LoggerFactory.getLogger(ExportExcelUtil.class);
    public static final String DEFUALT_TPL_PATH = "/views/exportFile/";
    private FileOutputStream fos;
    private static int NUM = 100000;// 一个sheet的记录数  
    private static String level1 = "1";
    private static String level2 = "2";
    private static String level3 = "3";
    private static String level4 = "4";
  
    /** 
     * @param title 
     *            每个Sheet里的顶部大标题 
     * @param column 
     *            单个sheet里每行数据的列对应的对象属性名称  
     *            column ="rule_name,cityName,specName,ivrName,contactGroup,specName,RulestCont".split(","); 
     * @param data 
     *            数据 
     * @param fileName 
     *            文件名 
     */  
    public HSSFWorkbook getOutputFile(String[] title, String[] column,List<Map> data, String fileName) {
        if (title == null || title.equals("")) {  
            System.out.println("Excel表格 标题(表头)为空");
        }  
        if (column == null || column.equals("")) {  
            System.out.println("没有定义取值字段集合");
        }  
        if (data == null || data.equals("")) {  
            System.out.println("没有定义导出数据集合");
        }  
        if (fileName == null || fileName.equals("")) {  
            System.out.println("没有定义输出文件名");
        }  
  
        HSSFWorkbook workbook = null;  
  
        try {  
            workbook = new HSSFWorkbook();// 创建Excel  
            HSSFSheet sheet = null; // 工作表  
            HSSFRow row = null; // 行  
            HSSFCell cell = null; // 行--列  
  
            Iterator it = data.iterator();
            int i = 0;  
  
            // 字体  
            HSSFFont font = workbook.createFont();  
            font.setColor(HSSFColor.BLACK.index);  
            font.setFontHeightInPoints((short) 10);  
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
  
            // 父告警标题样式  
            HSSFCellStyle pStyle = workbook.createCellStyle();  
            pStyle.setFont(font);  
  
            // 子告警标题样式  
            // HSSFCellStyle sStyle = workbook.createCellStyle();  
            // sStyle.setFont(font);  
            // sStyle.setFillBackgroundColor((short) 0x3399CC);  
  
            // 告警样式  
            HSSFCellStyle level1Style = workbook.createCellStyle();  
            HSSFPalette palette = workbook.getCustomPalette();  
            palette.setColorAtIndex((short) 9, (byte) (0xFF), (byte) (0x00),  
                    (byte) (0x00));  
            palette.setColorAtIndex((short) 10, (byte) (0xFF), (byte) (0xA5),  
                    (byte) (0x00));  
            palette.setColorAtIndex((short) 11, (byte) (0xFF), (byte) (0xFF),  
                    (byte) (0x00));  
            palette.setColorAtIndex((short) 12, (byte) (0x41), (byte) (0x69),  
                    (byte) (0xE1));  
            level1Style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
            level1Style.setFillForegroundColor((short) 9);  
            HSSFCellStyle level2Style = workbook.createCellStyle();  
            level2Style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
            level2Style.setFillForegroundColor((short) 10);  
            HSSFCellStyle level3Style = workbook.createCellStyle();  
            level3Style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
            level3Style.setFillForegroundColor((short) 11);  
            HSSFCellStyle level4Style = workbook.createCellStyle();  
            level4Style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
            level4Style.setFillForegroundColor((short) 12);  
  
              
            sheet = workbook.createSheet("Sheet0"); // 工作簿  
            row = sheet.createRow(0);  
  
            // 在每一页的第一行输入标题  
            for (int j = 0; j < title.length; j++) {  
                cell = row.createCell(j);  
                cell.setCellValue(new HSSFRichTextString(title[j]));  
                cell.setCellStyle(pStyle);  
            }  
  
            // 逐行添加数据  
            int k = 0;  
            while (it.hasNext()) {  
                if (i / NUM > k) { // 每50000条记录分一页  
                    k = i / NUM;  
                    sheet = workbook.createSheet("Sheet" + k);  
                    row = sheet.createRow(0);  
                    for (int j = 0; j < title.length; j++) {  
                        cell = row.createCell(j);  
                        cell.setCellValue(new HSSFRichTextString(title[i]));  
                    }  
                }  
  
                Map dataMap = (Map) it.next();
                row = sheet.createRow(i - NUM * k + 1);  
  
                // 输出数据  
                for (int j = 0; j < column.length; j++) {  
                    cell = row.createCell(j);  
                    // 按字段取值  
                    String columnName = column[j];  //取值的key
                    cell.setCellValue(new HSSFRichTextString(String.valueOf(dataMap.get(columnName))));
                    String value = String.valueOf(dataMap.get(columnName));
                    if (value.equalsIgnoreCase("null") && !value.equals("0")) {  
                        String level = String.valueOf(dataMap.get(columnName)
                                + "_level");  
                        if (!level.equalsIgnoreCase("null") && !level.equalsIgnoreCase("")) {  
                            if (level1.equals(level)) {  
                                cell.setCellStyle(level1Style);  
                            } else if (level2.equals(level)) {  
                                cell.setCellStyle(level2Style);  
                            } else if (level3.equals(level)) {  
                                cell.setCellStyle(level3Style);  
                            } else if (level4.equals(level)) {  
                                cell.setCellStyle(level4Style);  
                            }  
                        } else {  
                            cell.setCellStyle(level1Style);  
                        }  
                    }  
                }  
                i++;  
            }
            System.out.println("Excel 文件导出完成");
        } catch (Exception e) {
            System.out.println("Excel导出错误: ");
            e.printStackTrace();  
        }
		return workbook;
    }
    
	public void excel2Response(String fileName,HSSFWorkbook workbook,HttpServletResponse response) throws IOException {
        OutputStream outputStream = null;
        try {
            response.reset();
            response.setContentType("application/x-xls; charset="+Charsets.UTF8);
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(Charsets.CHARSET_GBK), Charsets.CHARSET_ISO88591));
            outputStream = response.getOutputStream();
            // 写入流  
            workbook.write(outputStream);  
            outputStream.flush();
        } finally {
            if(outputStream!=null){
                outputStream.close();
            }
        }
	}

    /**
     * 将msword的模板写入到response中，实现Excel文件导出功能
     *
     * @param ftlName   模板文件名称，带相对路径，例如/views/my/offerfail.ftl
     * @param fileName  导出文件名称
     * @param dataModel freemarker模板数据模型
     * @param response
     * @throws Exception
     */
    public static void msExcelTpl2Response(String ftlName, String fileName, Object dataModel, HttpServletResponse response) throws Exception {
        OutputStream outputStream = null;
        Writer bufferedWriter = null;
        try {
            response.reset();
            response.setContentType("application/msexcel; charset=" + Charsets.UTF8);
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(Charsets.CHARSET_GBK), Charsets.CHARSET_ISO88591));
            FreeMarkerConfigurer freemarkerConfig = (FreeMarkerConfigurer)SpringContextUtil.getBean("freemarkerConfig");
            Template template = freemarkerConfig.getConfiguration().getTemplate(ftlName, Charsets.UTF8);
            outputStream = response.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, Charsets.CHARSET_UTF8));
            template.process(dataModel, bufferedWriter);
            outputStream.flush();
        } catch(Exception e){
            logger.error(e.getMessage());
        }finally {
            if (bufferedWriter != null)
                bufferedWriter.close();
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static String getTplPath(String tplName, String tplPath) {
        String tplPathTemp = DEFUALT_TPL_PATH;//默认路径
        if (StringUtils.isNotBlank(tplPath)) {
            tplPathTemp = tplPathTemp + tplPath + "/";
        }
        return tplPathTemp + tplName;
    }


    /**
     * 描述：转换Clob类型字段为String类型字段
     *
     * @return
     * @author 杨文军
     * @create 9:39 2019/8/28
     **/
    public static String getStringFromClob(Object column) {
        if(column != null && column instanceof CLOB){
            Reader is = null;
            try {
                is = ((CLOB)column).getCharacterStream();// 得到流
                BufferedReader br = new BufferedReader(is);
                String s = br.readLine();
                StringBuffer sb = new StringBuffer();
                while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
                    sb.append(s);
                    s = br.readLine();
                    if (s != null)
                        sb.append("\n");
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(is != null){
                    try {
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "";
    }
}
