package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.Document;
import cn.gtmap.onemap.platform.entity.video.Camera;
import freemarker.template.TemplateException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * . 文档处理
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-9-9 上午9:51
 */
public interface DocumentService {

    /**
     * 读取 Excel <br/>
     * 此处不关闭文件流
     *
     * @param inputStream
     * @return
     */
    List<List> readExcel(InputStream inputStream);

    /**
     * write data to excel
     *
     * @param data    {"Sheet1":[[1234,"yyyyy","zzzzzz"],[3456,"xxxxx","zzzzzz"]],"Sheet2":[[33333,"yyyyy","zzzzzz"],[555555,"xxxxx","zzzzzz"]]}
     * @param tplName excel file name ,such as "test.xlsx"
     * @return
     */
    Document writeExcel(Map<String, List> data, String tplName) throws Exception;

    /**
     * write data to excel
     *
     * @param data [{"name":"Sheet1","header":["column1","column2","column3"],"data":[[1234,"yyyyy","zzzzzz"],[3456,"xxxxx","zzzzzz"]]}]
     * @return
     * @throws Exception
     */
    Document writeExcel(List<Map> data) throws Exception;

    /***
     *
     * @param data
     * @param tplName
     * @param type
     * @return
     * @throws Exception
     */
    Document writeTxt(Map<String,List> data,String tplName,String type) throws Exception;

    /**
     * read zip file <br/>
     * <p/>
     * 必须为标准Zip流格式
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    List<Document> readZip(InputStream inputStream) throws Exception;

    /**
     * read zip file
     *
     * @param file
     * @return
     */
    List<Document> readZipFile(File file);


    /***
     * read zip inputstream
     * @param inputStream
     * @return
     * @throws Exception
     */
    List<Document> readZipIn(InputStream inputStream) throws Exception;

    /**
     * read archive <br/>
     * the archiver formats are 7z, ar, arj, cpio, dump, tar and zip
     *
     * @param inputStream
     * @return
     */
    List<Document> readArchive(InputStream inputStream);

    /**
     *
     * @param file
     * @return
     */
    List<Document> readArchiveFile(File file);

    /**
     *
     * @param data
     * @param tplName
     * @return
     * @throws Exception
     */
    Document renderExcel(Object data, String tplName) throws Exception;

    /**
     * 导出分析结果至excel
     * @param data     分析结果数据
     * @param tplName  分析导出模板名称
     * @return
     * @throws Exception
     */
    Document renderAnalysisExcel(Object data, String tplName,Document.Type type) throws Exception;

    /**
     * 生成excel导出监控点配置的结果
     * @param cameras
     */
    Workbook createVideoExcel(List<Camera> cameras);

    /**
     * renderUnusedVideoExcel
     * @param data
     * @return
     */
    Workbook renderUnusedVideoExcel(List<Map> data);

    Document writeExcelWithWidth(List<Map> data) throws Exception;
}
