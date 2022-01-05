package cn.gtmap.msurveyplat.serviceol.utils;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/6/21
 * @description 参考地址:https://blog.csdn.net/weixin_37848710/article/details/89522862
 */

import cn.gtmap.msurveyplat.common.util.CommonUtil;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PdfReport {
    private static final Log LOGGER = LogFactory.getLog(PdfReport.class);

    // main测试
    public static void main(String[] args) throws Exception {
        Map<String, Object> dataMap = Maps.newHashMap();
        try {
            // 1.新建document对象
            Document document = new Document(PageSize.A4);// 建立一个Document对象

            // 2.建立一个书写器(Writer)与document对象关联
            File file = new File("D:\\PDFDemo.pdf");
            file.createNewFile();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            writer.setPageEvent(new Watermark("GTMAP"));// 水印
//            writer.setPageEvent(new MyHeaderFooter());// 页眉/页脚

            // 3.打开文档
            document.open();
            document.addTitle("Title@PDF-Java");// 标题
            document.addAuthor("liuqiang@gtmap.cn");// 作者
            document.addSubject("Subject@iText pdf sample");// 主题
            document.addKeywords("Keywords@iTextpdf");// 关键字
            document.addCreator("Creator@umizs");// 创建者

            // 4.向文档中添加内容
            new PdfReport().generatePDF(document, dataMap);

//            document.newPage();
//            new PdfReport().generatePDF(document);


            // 5.关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 定义全局的字体静态变量
    private static Font titlefont;
    private static Font headfont;
    private static Font keyfont;
    private static Font textfont;
    private static BaseFont simsunbf;
    private static String modelPath;
    // 最大宽度
    private static int maxWidth = 520;

    // 静态代码块
    static {
        try {
            // 获取路径地址
            modelPath = AppConfig.getProperty("egov.conf") + "/serviceol/model.pdf";
            String simsunbfPath = ResourceUtils.getURL("classpath:").getPath() + "static/fonts/simsun.ttc,1";

            // 处理前缀
            String osName = System.getProperty("os.name");
            if (osName.contains("Windows")) {
                modelPath = modelPath.substring(modelPath.indexOf("/") + 1);
                simsunbfPath = simsunbfPath.substring(simsunbfPath.indexOf("/") + 1);
            } else {
                modelPath = modelPath.substring(modelPath.indexOf(":") + 1);
                simsunbfPath = simsunbfPath.substring(simsunbfPath.indexOf(":") + 1);
            }

            // 定义字体信息
            simsunbf = BaseFont.createFont(simsunbfPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            titlefont = new Font(simsunbf, 16, Font.BOLD);
            headfont = new Font(simsunbf, 14, Font.BOLD);
            keyfont = new Font(simsunbf, 10, Font.BOLD);
            textfont = new Font(simsunbf, 10, Font.NORMAL);

        } catch (DocumentException e) {
            LOGGER.error("pdf 字体设置异常 {}", e);
        } catch (IOException e) {
            LOGGER.error("classpath获取异常 {}", e);
        }
    }

    // 生成PDF文件
    public void generatePDF(Document document, Map<String, Object> dataMap) throws Exception {
        // 段落
        Paragraph paragraph = new Paragraph("多测合一项目备案回执单", titlefont);
        paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
        paragraph.setIndentationLeft(12); //设置左缩进
        paragraph.setIndentationRight(12); //设置右缩进
        paragraph.setFirstLineIndent(24); //设置首行缩进
        paragraph.setLeading(20f); //行间距
        paragraph.setSpacingBefore(5f); //设置段落上空白
        paragraph.setSpacingAfter(10f); //设置段落下空白

        // 直线
        Paragraph p1 = new Paragraph();
        p1.add(new Chunk(new LineSeparator()));

        // 表格
        PdfPTable table = this.generateTable(dataMap);

        document.add(paragraph);
        document.add(p1);
        document.add(table);
    }

    public PdfPTable generateTable(Map<String, Object> dataMap) {
        // 表格
        PdfPTable table = createTable(new float[]{30, 75, 30, 75});

        String gcmc = CommonUtil.formatEmptyValue(MapUtils.getString(dataMap, "gcmc"));
        String jsdw = CommonUtil.formatEmptyValue(MapUtils.getString(dataMap, "jsdw"));
        String chdw = CommonUtil.formatEmptyValue(MapUtils.getString(dataMap, "chdw"));
        String slr = CommonUtil.formatEmptyValue(MapUtils.getString(dataMap, "slr"));
        String lqr = CommonUtil.formatEmptyValue(MapUtils.getString(dataMap, "lqr"));
        String slrq = CommonUtil.formatEmptyValue(MapUtils.getString(dataMap, "slrq"));
        String lqrq = CommonUtil.formatEmptyValue(MapUtils.getString(dataMap, "lqrq"));

        List<String> sjclList = (List<String>) dataMap.get("slclList");

//        table.addCell(createCell("美好的一天", headfont, Element.ALIGN_LEFT, 6, false));

        table.addCell(createCell("回执单", headfont, Element.ALIGN_CENTER, 4));

        table.addCell(createCell("工程名称", textfont, Element.ALIGN_CENTER));
        table.addCell(createCell(gcmc, textfont, Element.ALIGN_CENTER, 3));

        table.addCell(createCell("建设单位", textfont, Element.ALIGN_CENTER));
        table.addCell(createCell(jsdw, textfont, Element.ALIGN_CENTER));
        table.addCell(createCell("测绘单位", textfont, Element.ALIGN_CENTER));
        table.addCell(createCell(chdw, textfont, Element.ALIGN_CENTER));

        table.addCell(createCell("受理材料清单", textfont, Element.ALIGN_CENTER, 4));


        if (CollectionUtils.isNotEmpty(sjclList)) {
            for (int i = 0; i < sjclList.size(); i++) {
                table.addCell(createCell(i + "", textfont, Element.ALIGN_CENTER));
                table.addCell(createCell(sjclList.get(i), textfont, Element.ALIGN_CENTER, 3));
            }
        } else {
            table.addCell(createCell("", textfont, Element.ALIGN_CENTER));
            table.addCell(createCell("", textfont, Element.ALIGN_CENTER, 3));
        }

        table.addCell(createCellWithoutBorder("受理人:", textfont, Element.ALIGN_CENTER));
        table.addCell(createCellWithoutBorder(slr, textfont, Element.ALIGN_LEFT));
        table.addCell(createCellWithoutBorder("领取人:", textfont, Element.ALIGN_CENTER));
        table.addCell(createCellWithoutBorder(lqr, textfont, Element.ALIGN_LEFT));

        table.addCell(createCellWithoutBorder("日期:", textfont, Element.ALIGN_CENTER));
        table.addCell(createCellWithoutBorder(slrq, textfont, Element.ALIGN_LEFT));
        table.addCell(createCellWithoutBorder("日期:", textfont, Element.ALIGN_CENTER));
        table.addCell(createCellWithoutBorder(lqrq, textfont, Element.ALIGN_LEFT));

        return table;
    }


/**------------------------创建表格单元格的方法start----------------------------*/
    /**
     * 创建单元格(指定字体)
     *
     * @param value
     * @param font
     * @return
     */
    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        cell.setMinimumHeight(20);
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平..）
     *
     * @param value
     * @param font
     * @param align
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        cell.setMinimumHeight(20);
        return cell;
    }

    /**
     * 创建无边框的单元格（指定字体、水平..）
     *
     * @param value
     * @param font
     * @param align
     * @return
     */
    public PdfPCell createCellWithoutBorder(String value, Font font, int align) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        cell.setMinimumHeight(20);
        cell.disableBorderSide(15);

        return cell;
    }

    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并）
     *
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setMinimumHeight(20);
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并）
     *
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @return
     */
    public PdfPCell createCellWithoutBorder(String value, Font font, int align, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setMinimumHeight(20);
        cell.disableBorderSide(15);
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平居..、单元格跨x列合并、设置单元格内边距）
     *
     * @param value
     * @param font
     * @param align
     * @param colspan
     * @param boderFlag
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        cell.setMinimumHeight(20);
        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        } else if (boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(0.0f);
            cell.setPaddingBottom(15.0f);
        }
        return cell;
    }

    /**
     * 创建单元格（指定字体、水平..、边框宽度：0表示无边框、内边距）
     *
     * @param value
     * @param font
     * @param align
     * @param borderWidth
     * @param paddingSize
     * @param flag
     * @return
     */
    public PdfPCell createCell(String value, Font font, int align, float[] borderWidth, float[] paddingSize, boolean flag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        cell.setBorderWidthLeft(borderWidth[0]);
        cell.setBorderWidthRight(borderWidth[1]);
        cell.setBorderWidthTop(borderWidth[2]);
        cell.setBorderWidthBottom(borderWidth[3]);
        cell.setPaddingTop(paddingSize[0]);
        cell.setPaddingBottom(paddingSize[1]);
        cell.setMinimumHeight(20);
        if (flag) {
            cell.setColspan(2);
        }
        return cell;
    }
/**------------------------创建表格单元格的方法end----------------------------*/


/**--------------------------创建表格的方法start------------------- ---------*/
    /**
     * 创建默认列宽，指定列数、水平(居中、右、左)的表格
     *
     * @param colNumber
     * @param align
     * @return
     */
    public PdfPTable createTable(int colNumber, int align) {
        PdfPTable table = new PdfPTable(colNumber);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(align);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    /**
     * 创建指定列宽、列数的表格
     *
     * @param widths
     * @return
     */
    public PdfPTable createTable(float[] widths) {
        PdfPTable table = new PdfPTable(widths);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);

            PdfPCell blankCell = new PdfPCell();
            blankCell.setMinimumHeight(20);
            table.addCell(blankCell);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }

    /**
     * 创建空白的表格
     *
     * @return
     */
    public PdfPTable createBlankTable() {
        PdfPTable table = new PdfPTable(1);
        table.getDefaultCell().setBorder(0);
        table.addCell(createCell("", keyfont));
        table.setSpacingAfter(20.0f);
        table.setSpacingBefore(20.0f);
        return table;
    }
}
/**
 * --------------------------创建表格的方法end------------------- ---------
 */

