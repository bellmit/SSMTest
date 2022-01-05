package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.entity.Document;
import com.alibaba.fastjson.JSON;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-9-9 上午10:45
 */
public class DocumentServiceImplTest extends BaseServiceTest {

    @Autowired
    private DocumentServiceImpl documentService;

    @Test
    public void testReadExcle() throws Exception {
        InputStream is = getClass().getResourceAsStream("/text.xls");
        List list = documentService.readExcel(is);
        System.out.print(list);
    }

    @Test
    public void testWriteExcle() throws Exception {
        Map<String, List> data = new LinkedHashMap<String, List>();
        List<List> d = new ArrayList<List>();
        List item = new ArrayList();
        item.add("dddd");
        item.add(21312);
        item.add("sssss");
        item.add("qqqqqqq");
        item.add(4444444);
        d.add(item);
        d.add(item);
        data.put("Sheet1", d);
//        data.put("Sheet2", d);

        Document document = documentService.writeExcel(data, "test.xlsx");
        FileOutputStream outputStream = new FileOutputStream("G:\\Temp\\3.xlsx");
        IOUtils.copy(new ByteArrayInputStream(document.getContent()), outputStream);
        outputStream.close();

        //
        List<Map> data2 = JSON.parseObject("[{\"name\":\"Sheet1\",\"header\":[\"column1\",\"column2\",\"column3\"],\"data\":[[1234,\"yyyyy\",\"zzzzzz\"],[3456,\"xxxxx\",\"zzzzzz\"]]}]", List.class);

        document = documentService.writeExcel(data2);
        outputStream = new FileOutputStream("G:\\Temp\\4.xlsx");
        IOUtils.copy(new ByteArrayInputStream(document.getContent()), outputStream);
        outputStream.close();
        print("----");

    }

    @Test
    public void testReadZip() throws Exception {
//        InputStream is = IOUtils.toInputStream("‪E:\\temp\\321000131100000000.zip");
//        InputStream is = IOUtils.toInputStream("‪E:\\temp\\321000131200000000.zip");
        InputStream is = getClass().getResourceAsStream("/bj/321000131200000000.ZIP");
//        InputStream is = getClass().getResourceAsStream("/321100132300000000.ZIP");
        List<Document> documents = documentService.readZip(is);
        ZipFile zipFile = new ZipFile(new File(getClass().getResource("/bj/321000131200000000.ZIP").toURI()));
        Enumeration  enumeration = zipFile.entries();
        while (enumeration.hasMoreElements()){
            ZipEntry entry = (ZipEntry) enumeration.nextElement();
            InputStream inputStream = zipFile.getInputStream(entry);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(inputStream,byteArrayOutputStream);
            Document document = Document.getDocByName(entry.getName());
            document.setContent(byteArrayOutputStream.toByteArray());
            print("--");
        }
       /* ZipInputStream zipInputStream = new ZipInputStream(getClass().getResourceAsStream("/321100132300000000.ZIP"));
        ZipEntry entry = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            print(" entity name: " + entry.getName());
            Document document = Document.getDocByName(entry.getName());
            byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(zipInputStream, byteArrayOutputStream);
            documents.add(document.setContent(byteArrayOutputStream.toByteArray()));
        }*/

        //read zip by compress
        try {
            ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(is);
            ArchiveEntry entry = null;
            while ((entry = inputStream.getNextEntry()) != null) {
//                byte[] content = new byte[entry.getSize()];
                print(entry.getName());
            }
            inputStream.close();
        } catch (ArchiveException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        print(null);


    }

    @Test
    public void testReadArchive() throws Exception {
        try {

            File file = new File("E:\\Temp\\shape.rar");
            List<Document> documents = documentService.readArchiveFile(file);
            print(documents.size());

        } catch (Exception e) {
            e.printStackTrace();
        }


        print("-----");

    }
}
