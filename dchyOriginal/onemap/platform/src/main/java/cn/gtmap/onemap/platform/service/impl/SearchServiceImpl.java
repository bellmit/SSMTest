package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.event.SearchException;
import cn.gtmap.onemap.platform.service.GISManager;
import cn.gtmap.onemap.platform.service.SearchService;
import com.alibaba.fastjson.JSON;
import com.vividsolutions.jts.geom.Geometry;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NRTCachingDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-8-6 下午2:18
 */
public class SearchServiceImpl extends BaseLogger implements SearchService {

    private static final String SHAPE = Constant.SE_SHAPE_FIELD;

    enum S {
        id, title, subtitle, content, shape, group, link
    }

    private static final int LIMIT = 100;

    @Autowired
    private GISManager gisManager;

    private Directory directory = null;

    private Analyzer analyzer = null;

    private IndexReader reader = null;

    private IndexSearcher searcher = null;

    /**
     * 全文检索存放位置
     */
    private Resource indexDir;

    /**
     *
     */
    private Resource stopWord;

    /**
     * 内存中存档索引最大值 <br/> 单位 MB
     */
    private int maxCacheSize = 128;

    /**
     * 导入索引配置
     */
    private Map config;

    /**
     * 是否更新索引
     */
    private boolean update = true;

    /**
     * 是否使用内存索引
     */
    private boolean useRamDir = false;

    private String[] fields = new String[]{S.title.name(), S.subtitle.name(), S.group.name()};


    @PostConstruct
    private void init() {
        try {
            analyzer = new StandardAnalyzer(Version.LUCENE_44, new FileReader(stopWord.getFile()));
        } catch (IOException e) {
            analyzer = new StandardAnalyzer(Version.LUCENE_44);
            logger.trace(" stopword.txt not found ");
        }
        if (update) createIndex();
    }

    /**
     * 创建
     */
    @Override
    public boolean createIndex() {
        logger.info("start create search index -- time : [{}]", (new Date()).toString());
        try {
            logger.info("创建检索索引路径："+indexDir.getFile().toString());
            FSDirectory fsDirectory = FSDirectory.open(indexDir.getFile());
            if (this.useRamDir)
                directory = new NRTCachingDirectory(fsDirectory, 10, maxCacheSize);
            else
                directory = fsDirectory;

            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
            config.setMaxThreadStates(20);
            config.setRAMBufferSizeMB(this.maxCacheSize);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            IndexWriter writer = new IndexWriter(directory, config);
            // do some import
            importLayers(writer);
            //
            writer.close();

            logger.info("create index success -- time : [{}]", (new Date()).toString());
            return true;
        } catch (Exception e) {
            logger.error("create index error [{}]", e.getLocalizedMessage());
        }
        return false;
    }

    /**
     * 导入信息表
     */
    private void importLayers(IndexWriter writer) {
        if (config == null) {
            logger.info("无快速检索配置信息");
            return;
        }
        List<Map> layers = (List<Map>) config.get("layers");
        for (Map layer : layers) {
            String layerName = (String) layer.get("layer");
            String[] fields = ((String) layer.get("fields")).split(",");
            String id = (String) layer.get(S.id.name());
            String title = (String) layer.get(S.title.name());
            String subtitle = (String) layer.get(S.subtitle.name());
            String group = (String) layer.get(S.group.name());
            String link = (String) layer.get(S.link.name());
            String ds = (String) layer.get("dataSource");
            //
            logger.info("start create index by layer :[{}]", layerName);
            try {
                List<Map> results = (List<Map>) gisManager.getGISService().query(layerName, "OBJECTID > 0", fields, true, ds);
                Document doc = null;
                for (Map result : results) {
                    doc = new Document();
                    doc.add(new TextField(S.id.name(), entityValue2String(result.get(id)), Field.Store.YES));
                    doc.add(new TextField(S.title.name(), entityValue2String(result.get(title)), Field.Store.YES));
                    doc.add(new TextField(S.subtitle.name(), entityValue2String(result.get(subtitle)), Field.Store.YES));
                    doc.add(new TextField(S.shape.name(), entityShapeValue(result.get(SHAPE)), Field.Store.YES));
                    List<String> content = new ArrayList<String>();
                    for (Object e : result.entrySet()) {
                        Map.Entry entry = (Map.Entry) e;
                        if (entry.getKey().equals(id) || entry.getKey().equals(title) || entry.getKey().equals(subtitle) ||
                                entry.getKey().equals(SHAPE)) continue;
                        else
                            content.add(entityValue2String(entry.getValue()));
                    }
                    doc.add(new TextField(S.content.name(), StringUtils.join(content.toArray(new String[0]), " "), Field.Store.YES));
                    doc.add(new TextField(S.group.name(), group, Field.Store.YES));
                    doc.add(new StringField(S.link.name(), link, Field.Store.YES));
                    try {
                        writer.addDocument(doc, analyzer);
                    } catch (IOException e1) {
                        logger.error("document write error [{}]", e1.getLocalizedMessage());
                    }
                }
                writer.commit();
                logger.info(" [{}] records has added to index", writer.numDocs());
            } catch (Exception e) {
                logger.error("layer [{}] create index error, error detail [{}] ", layerName, e.getLocalizedMessage());
            }
        }
        System.gc();
    }

    /**
     * @param entry
     * @return
     */
    private Field entry2Field(Map.Entry entry) {
        Object value = entry.getValue();
        if (value instanceof Integer)
            return new IntField((String) entry.getKey(), Integer.valueOf((String) value), Field.Store.YES);
        else if (value instanceof Float)
            return new FloatField((String) entry.getKey(), Float.valueOf(String.valueOf(value)), Field.Store.YES);
        else if (value instanceof Double)
            return new DoubleField((String) entry.getKey(), Double.valueOf(String.valueOf(value)), Field.Store.YES);
        else if (value instanceof Calendar)
            return new LongField((String) entry.getKey(), ((Calendar) value).getTimeInMillis(), Field.Store.YES);
        else if (entry.getKey().equals(SHAPE))
            return new StringField((String) entry.getKey(), String.valueOf(value), Field.Store.YES);
        else return new TextField((String) entry.getKey(), String.valueOf(value), Field.Store.YES);
    }

    /**
     * @param value
     * @return
     */
    private String entityValue2String(Object value) {
        if (value instanceof String) return (String) value;
        // do some
        return String.valueOf(value);
    }

    /**
     * shape field
     *
     * @param value
     * @return
     */
    private String entityShapeValue(Object value) {
        if (value instanceof String) {
            Geometry geometry = gisManager.getGeoService().forceSimplify(gisManager.getGeoService().readWKT((String) value),
                    gisManager.getGeoService().getSimplifyTolerance());
            return gisManager.getGeoService().toGeoJSON(geometry);
        }
        return String.valueOf(value);
    }

    /**
     * 检索
     *
     * @param value
     * @param limit
     * @return
     */
    @Override
    public Set search(String value, int limit, boolean simple) throws SearchException {
        if (limit < 1) limit = LIMIT;
        Set<Map> results = new HashSet<Map>();
        QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, fields, analyzer);
        parser.setDefaultOperator(QueryParser.AND_OPERATOR);
        Query query = null;
        try {
            query = parser.parse(value);
        } catch (ParseException e) {
            throw new SearchException(SearchException.Type.PARSE_QUERY, e.getLocalizedMessage());
        }
        try {
            if (reader == null)
                reader = DirectoryReader.open(directory != null ? directory : FSDirectory.open(indexDir.getFile()));
            if (searcher == null) searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(query, limit);
            ScoreDoc[] hits = docs.scoreDocs;
            for (ScoreDoc hit : hits) {
                results.add(doc2Map(searcher.doc(hit.doc), simple));
            }
        } catch (IOException e) {
            throw new SearchException(SearchException.Type.INDEX_DIR, e.getLocalizedMessage());
        }
        return results;
    }

    /**
     * 检索 分页
     * @param queryVal
     * @param page
     * @param size
     * @param simple
     * @return
     * @throws SearchException
     */
    @Override
    public Page search(String queryVal, int page, int size, boolean simple) throws SearchException {
        List<Map> results = new ArrayList<Map>();
        QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, fields, analyzer);
        parser.setDefaultOperator(QueryParser.AND_OPERATOR);
        Query query = null;
        int total = LIMIT;
        try {
            query = parser.parse(queryVal);
        } catch (ParseException e) {
            throw new SearchException(SearchException.Type.PARSE_QUERY, e.getLocalizedMessage());
        }
        try {
            if (reader == null)
                reader = DirectoryReader.open(directory != null ? directory : FSDirectory.open(indexDir.getFile()));
            if (searcher == null) searcher = new IndexSearcher(reader);
            TopScoreDocCollector topCollector = TopScoreDocCollector.create(LIMIT, false);
            searcher.search(query, topCollector);
            total = topCollector.getTotalHits();
            ScoreDoc[] hits = topCollector.topDocs(page * size, size).scoreDocs;
            for (ScoreDoc hit : hits) {
                results.add(doc2Map(searcher.doc(hit.doc), simple));
            }
        } catch (IOException e) {
            throw new SearchException(SearchException.Type.INDEX_DIR, e.getLocalizedMessage());
        }
        return new PageImpl<Map>(results, new PageRequest(page, size, null), total);
    }

    /**
     * @return
     */
    @Override
    public String[] groups() {
        List<Map> layers = (List<Map>) config.get("layers");
        List<String> groups = new ArrayList<String>();
        for (Map item : layers) {
            groups.add((String) item.get("group"));
        }
        return groups.toArray(new String[0]);
    }

    /**
     * @param doc
     * @return
     */
    private Map doc2Map(Document doc, boolean simple) {
        Map<String, Object> map = new HashMap<String, Object>();
        String name = null;
        for (IndexableField field : doc.getFields()) {
            name = field.name();
            if (simple) {
                if (name.equals(S.content.name()) || name.equals(S.group.name())
                        || name.equals(S.shape.name()) || name.equals(S.link.name())) continue;
            }
            map.put(field.name(), field.stringValue());
        }
        return map;
    }

    public void setIndexDir(Resource indexDir) {
        this.indexDir = indexDir;
        try {
            if (!indexDir.exists()) indexDir.getFile().mkdirs();
        } catch (IOException e) {
            logger.error("create index directory error [{}]", e.getLocalizedMessage());
        }
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public void setConfig(Resource path) {
        try {
            config = JSON.parseObject(IOUtils.toString(path.getURI(), Constant.UTF_8), Map.class);
        } catch (IOException e) {
            logger.error(" search config can't found ");
        } catch (Exception e) {
            logger.error(getMessage("search.config.file.error", e.getLocalizedMessage()));
        }
    }

    public void setStopWord(Resource stopWord) {
        this.stopWord = stopWord;
    }

    @PreDestroy
    private void destory() {
        try {
            if (reader != null) reader.close();
            if (directory != null) directory.close();
        } catch (IOException e) {
            logger.error("close index reader error [{}]", e.getLocalizedMessage());
        }
        System.gc();
    }

    public void setUpdate(boolean enable) {
        this.update = enable;
    }

    public void setUseRamDir(boolean useRamDir) {
        this.useRamDir = useRamDir;
    }
}
