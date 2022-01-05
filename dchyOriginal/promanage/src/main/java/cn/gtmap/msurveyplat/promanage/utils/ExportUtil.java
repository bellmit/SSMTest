package cn.gtmap.msurveyplat.promanage.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/15
 * @description TODO
 */
public class ExportUtil {
    public static String File_Path = "D:/";

    public static void main(String[] args) throws Exception {
//        ExportUtil.createExcel();
        // ExportUtil.createWord();
        ExportUtil.createPDF();

    }


    /**
     * 生成Excel表格
     *
     * @throws Exception
     */
    public static void createExcel() throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建Excel表格的
        HSSFSheet sheet = workbook.createSheet("新建表11");// 创建工作表sheet
        HSSFRow row = sheet.createRow(0); // 在建立的excel中创建一行
        HSSFCell cell = row.createCell(0); // 创建该行的对应列
        cell.setCellValue("我的第一行第一列的值"); // 向该行该列中设置内容
        // TODO 其他操作请自己百度
        FileOutputStream outputStream = new FileOutputStream(File_Path + "Excel.xls"); // 保存文件的路径
        workbook.write(outputStream); // 保存Excel文件
        outputStream.close(); // 关闭文件流

        System.out.println("excel生成成功!");
    }


    /**
     * 生成word文档
     *
     * @throws Exception
     */
    public static void createWord() throws Exception {
        XWPFDocument doc = new XWPFDocument(); //创建word文件
        XWPFParagraph p1 = doc.createParagraph(); //创建段落
        XWPFRun r1 = p1.createRun(); //创建段落文本
        r1.setText("Helloworld"); //设置文本
        r1.addBreak(); // 换行
        r1.setText("世界你好!");

        // TODO 其他操作请自己百度
        FileOutputStream outputStream = new FileOutputStream(File_Path + "Word.docx"); // 保存文件的路径
        doc.write(outputStream); // 保存Excel文件
        outputStream.close(); // 关闭文件流

        System.out.println("word生成成功!");
    }


    /**
     * 生成PDF
     *
     * @throws Exception
     */
    public static void createPDF() throws Exception {
        // 设置字体(由于自己缺少这个包)
       /* BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);*/
        // 第一步，创建document对象
        Rectangle rectPageSize = new Rectangle(PageSize.A4);

        //下面代码设置页面横置
        //rectPageSize = rectPageSize.rotate();

        //创建document对象并指定边距
        Document doc = new Document(rectPageSize, 50, 50, 50, 50);
        Document document = new Document();
        try {
            // 第二步,将Document实例和文件输出流用PdfWriter类绑定在一起
            //从而完成向Document写，即写入PDF文档
            PdfWriter.getInstance(document, new FileOutputStream(File_Path + "PDF.pdf"));
            //第3步,打开文档
            document.open();
            //第3步,向文档添加文字. 文档由段组成
            document.add(new Paragraph("Hello World"));

            document.add(new Paragraph("世界你好"));

            PdfPTable table = new PdfPTable(3);
            // 生成表格   TODO 其他操作请自己百度
            for (int i = 0; i < 12; i++) {
                if (i == 0) {
                    PdfPCell cell = new PdfPCell();
                    cell.setColspan(3);
                    //cell.setBackgroundColor(new Color(180,180,180));
                    cell.addElement(new Paragraph("表格头"));
                    table.addCell(cell);
                } else {
                    PdfPCell cell = new PdfPCell();
                    cell.addElement(new Paragraph("表格内容"));
                    table.addCell(cell);
                }
            }
            document.add(table);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        //关闭document
        document.close();

        System.out.println("PDF生成成功!");
    }

}
