package cn.gtmap.onemap.platform;

import cn.gtmap.onemap.platform.service.GISManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NRTCachingDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-8-5 上午11:33
 */
public class LuceneServiceTest extends BaseServiceTest {

    @Autowired
    private GISManager gisManager;

    Directory directory;

    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);

    private static String INDEX_DIR = "F:\\index\\index";

    private static String DATA_DIR = "F:\\index";

    @Test
    public void createIndex() throws IOException {

//        directory = new RAMDirectory();
        try {
            directory = FSDirectory.open(new File(INDEX_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }


        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setUseCompoundFile(false);

        IndexWriter writer = new IndexWriter(directory, config);

        Document doc = new Document();
        Field path = new StringField("path", INDEX_DIR, Field.Store.YES);
        Field body = new TextField("body", readTxt(DATA_DIR.concat("\\a.txt")), Field.Store.YES);
        doc.add(path);
        doc.add(body);
        writer.addDocument(doc);

        doc = new Document();
        path = new StringField("path", INDEX_DIR, Field.Store.YES);
        body = new TextField("body", "Students should be allowed to go out with their friends, but not allowed to drink beer search query. close", Field.Store.YES);
        doc.add(path);
        doc.add(body);
        writer.addDocument(doc);

        doc = new Document();
        path = new StringField("path", INDEX_DIR, Field.Store.YES);
        body = new TextField("body", "也就是说CoreContainer显示的open了SolrCore，所以在得到SolrCore实例后，也需要显示的close它。因为 SolrQueryRequestBase用到了SolrCore，所以在处理请求的最后，要确保调用了SolrCore的close方法。当然，对于查 询端，SolrCore实例在整个生命周期内通常并不会真正被close，除非显示的调用了reload等操作。", Field.Store.YES);
        doc.add(path);
        doc.add(body);
        writer.addDocument(doc);


        writer.close();

    }

    @Test
    public void testSearch() throws Exception {

        String[] fields = new String[]{"body"};
        QueryParser parser = new QueryParser(Version.LUCENE_44, "TDZL", analyzer);


        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

//        System.out.println("enter:");
        String line = "通富路西";
        Query query = parser.parse(line);
        IndexReader reader = null;
        try {
            directory = FSDirectory.open(new File(INDEX_DIR));
            reader = DirectoryReader.open(directory);

            IndexSearcher searcher = new IndexSearcher(reader);

            TopDocs topDocs = searcher.search(query, 100);
            ScoreDoc[] hits = topDocs.scoreDocs;

            Document doc = searcher.doc(hits[0].doc);

            String context = doc.get("body");

            System.out.print(context + "\n----\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        reader.close();
    }

    @Test
    public void showAnalysisResult() throws IOException {
        String value = readTxt(INDEX_DIR);
        StringReader reader = new StringReader(value);
        TokenStream token = analyzer.tokenStream(value, reader);

//        Token t = token.

    }

    public static String readTxt(String path) {
        StringBuffer content = new StringBuffer("");// 文档内容
        try {
            FileReader reader = new FileReader(path);
            BufferedReader br = new BufferedReader(reader);
            String s1 = null;

            while ((s1 = br.readLine()) != null) {
                content.append(s1 + "\r");
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString().trim();
    }

    @Test
    public void testSolr() throws Exception {

        Date start = new Date();

        try {
            directory = FSDirectory.open(new File(INDEX_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setUseCompoundFile(false);

        IndexWriter writer = new IndexWriter(directory, config);

        List<Map> results = (List<Map>) gisManager.getGISService().query("sde.gddk_h_320604", "OBJECTID > 0", null, true, null);

        for (Map result : results){
             for (Object e: result.entrySet()){
                 Map.Entry entry = (Map.Entry) e;
                 Document doc = new Document();
                 doc.add(new TextField((String) entry.getKey(),String.valueOf(entry.getValue()), Field.Store.YES));
                 writer.addDocument(doc);
             }
        }
        writer.commit();
        writer.close();

        Date end = new Date();

        print("start : " +start.toString() + "end : "+end.toString());

    }

    @Test
    public void testRamDirectory() throws Exception {

        NRTCachingDirectory cachingDirectory = null;

        long freeM = Runtime.getRuntime().freeMemory();

        Date start = new Date();

        try {
            directory = FSDirectory.open(new File(INDEX_DIR));
            cachingDirectory = new NRTCachingDirectory(directory,10,100);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setUseCompoundFile(false);

        IndexWriter writer = new IndexWriter(cachingDirectory, config);

        List<Map> results = (List<Map>) gisManager.getGISService().query("sde.gddk_h_320604", "OBJECTID > 0", null, true, null);

        for (Map result : results){
            for (Object e: result.entrySet()){
                Map.Entry entry = (Map.Entry) e;
                Document doc = new Document();
                doc.add(new TextField((String) entry.getKey(),String.valueOf(entry.getValue()), Field.Store.YES));
                writer.addDocument(doc);
            }
        }
        writer.commit();
        writer.close();

        Date end = new Date();

        long freeM2 = Runtime.getRuntime().freeMemory();

        print("start : " +start.toString() + "end : "+end.toString());

        // query
        QueryParser parser = new QueryParser(Version.LUCENE_44, "TDZL", analyzer);

        String line = "路西";
        Query query = parser.parse(line);
        IndexReader reader = null;
        try {
//            directory = FSDirectory.open(new File(INDEX_DIR));
            reader = DirectoryReader.open(cachingDirectory);

            IndexSearcher searcher = new IndexSearcher(reader);

            TopDocs topDocs = searcher.search(query, 100);
            ScoreDoc[] hits = topDocs.scoreDocs;

            Document doc = searcher.doc(hits[0].doc);

            String context = doc.get("body");

            System.out.print(context + "\n----\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        reader.close();

        //

//        SpatialContext ctx = SpatialContext.GEO ;
//        ctx.ma

    }

}
