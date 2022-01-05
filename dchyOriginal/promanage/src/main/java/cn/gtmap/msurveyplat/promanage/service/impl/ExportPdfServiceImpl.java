package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmMapper;
import cn.gtmap.msurveyplat.promanage.core.service.DchyXmglZdService;
import cn.gtmap.msurveyplat.promanage.service.ExportPdfService;
import cn.gtmap.msurveyplat.promanage.utils.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.gtis.config.AppConfig;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/18
 * @description 打印回执单
 */
@Service
public class ExportPdfServiceImpl implements ExportPdfService {

    private static final Log LOGGER = LogFactory.getLog(ExportPdfServiceImpl.class);

    private static BaseFont simsunbf = null;
    private static String modelPath = null;

    static {
        try {
            // 获取路径地址
            modelPath = AppConfig.getProperty("egov.conf") + "promanage/";
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
        } catch (DocumentException e) {
            LOGGER.error("pdf 字体设置异常 {}", e);
        } catch (IOException e) {
            LOGGER.error("classpath获取异常 {}", e);
        }
    }


    @Autowired
    private DchyXmglChxmMapper dchyXmglChxmMapper;

    @Autowired
    private DchyXmglZdService dchyXmglZdService;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public byte[] exportPdf(String chxmid) throws IOException {
//        chxmid = "568A3535GUX2P051";
        Map<String, Object> dataMap = Maps.newHashMap();
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("chxmid", chxmid);

        List<Map<String, Object>> baxxList = dchyXmglChxmMapper.queryBaxxByChxmid(paramMap);
        List<Map<String, Object>> chdwList = dchyXmglChxmMapper.queryChdwByChxmid(paramMap);
//        List<Map<String, Object>> htxxList = dchyXmglDyhzdMapper.getHtxxList(paramMap);


        if (CollectionUtils.isNotEmpty(baxxList)) {
            Map<String, Object> baxx = baxxList.get(0);
            String slr = MapUtils.getString(baxx, "SLR");

            Date slsj = CalendarUtil.formatDate(MapUtils.getString(baxx, "SLSJ"));
            Calendar calendarSlsj = Calendar.getInstance();
            Calendar calendarLqsj = Calendar.getInstance();
            calendarSlsj.setTime(slsj);
            dataMap.put("gcmc", MapUtils.getString(baxx, "GCMC"));
            dataMap.put("jsdw", MapUtils.getString(baxx, "WTDW"));
            dataMap.put("slr", UserUtil.getUserNameById(slr));
            dataMap.put("lqr", UserUtil.getCurrentUser().getUsername());
            dataMap.put("slrq", calendarSlsj.get(Calendar.YEAR) + "年" + calendarSlsj.get(Calendar.MONTH) + "月" + calendarSlsj.get(Calendar.DATE) + "日");
            dataMap.put("lqrq", calendarLqsj.get(Calendar.YEAR) + "年" + calendarLqsj.get(Calendar.MONTH) + "月" + calendarLqsj.get(Calendar.DATE) + "日");
        }

        if (CollectionUtils.isNotEmpty(chdwList)) {
            Map<String, Object> chdw = chdwList.get(0);
            dataMap.put("chdw", MapUtils.getString(chdw, "CHDWMC"));
        }

        dataMap.put("slclList", this.generateClmx(chxmid));
        return PdfUtils.pdfout(Constants.DCHY_XMGL_HZDMC, dataMap);
    }

    public String convertCllx(String cllx) {
        String cllxMC = dchyXmglZdService.getZdmcByZdlxAndDm("CLLX", cllx);
        return cllxMC;
    }

    @Override
    public byte[] tyjfdPdf(String chxmid) {
        Map pdfDataMap = Maps.newHashMap();
        if (StringUtils.isNotEmpty(chxmid)) {
            DchyXmglChxm dchyXmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);

            if (null != dchyXmglChxm) {
                if (StringUtils.isNotBlank(dchyXmglChxm.getChgcid())) {
                    DchyXmglChgc dchyXmglChgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, dchyXmglChxm.getChgcid());
                    pdfDataMap.put("gcmc", dchyXmglChgc.getGcmc());
                    pdfDataMap.put("jsdw", dchyXmglChgc.getWtdw());
                }
                Example chdwxxExample = new Example(DchyXmglChxmChdwxx.class);
                chdwxxExample.createCriteria().andEqualTo("chxmid", chxmid);
                List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList = entityMapper.selectByExample(chdwxxExample);
                if (CollectionUtils.isNotEmpty(dchyXmglChxmChdwxxList)) {
                    LinkedHashSet<String> chdwmxList = Sets.newLinkedHashSet();
                    for (DchyXmglChxmChdwxx dchyXmglChxmChdwxx : dchyXmglChxmChdwxxList) {
                        chdwmxList.add(dchyXmglChxmChdwxx.getChdwmc());
                    }
                    pdfDataMap.put("chdw", StringUtils.join(chdwmxList, ", "));
                }
                Example clsxExample = new Example(DchyXmglChxmClsx.class);
                clsxExample.createCriteria().andEqualTo("chxmid", chxmid);
                List<DchyXmglChxmClsx> dchyXmglChxmClsxList = entityMapper.selectByExample(clsxExample);
                if (CollectionUtils.isNotEmpty(dchyXmglChxmChdwxxList)) {
                    int i = 1;
                    for (DchyXmglChxmClsx dchyXmglChxmClsx : dchyXmglChxmClsxList) {
                        DchyXmglZd dchyXmglZd = dchyXmglZdService.getDchyXmglByZdlxAndDm("CLSX", dchyXmglChxmClsx.getClsx());
                        if (null != dchyXmglZd) {
                            pdfDataMap.put("clsx" + i, dchyXmglZd.getMc());
                            DchyXmglZd dchyXmglZdP = dchyXmglZdService.getDchyXmglByZdlxAndDm("CLSX", dchyXmglZd.getFdm());
                            if (null != dchyXmglZdP) {
                                pdfDataMap.put("cljd" + i, dchyXmglZdP.getMc());
                            }
                        }
                        pdfDataMap.put("xh" + i, String.valueOf(i));
                        i++;
                    }
                }
            }
        }
        return PdfUtils.pdfout(Constants.DCHY_XMGL_TYJFQRD, pdfDataMap);
    }

    /**
     * @param chxmid
     * @return
     * @description 2021/6/8 通过chxmid受理材料明细
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    @Override
    public List<String> generateClmx(String chxmid) {
        List<String> slclList = Lists.newArrayList();
        if (StringUtils.isNotBlank(chxmid)) {
            Example exampleSjxx = new Example(DchyXmglSjxx.class);
            Example exampleHtxx = new Example(DchyXmglHtxx.class);
            exampleSjxx.createCriteria().andEqualTo("glsxid", chxmid);
            exampleHtxx.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglSjxx> dchyXmglSjxxList = entityMapper.selectByExampleNotNull(exampleSjxx);
            List<DchyXmglHtxx> dchyXmglHtxxList = entityMapper.selectByExampleNotNull(exampleHtxx);
            if (CollectionUtils.isNotEmpty(dchyXmglSjxxList)) {
                DchyXmglSjxx dchyXmglSjxx = dchyXmglSjxxList.get(0);
                Example exampleSjcl = new Example(DchyXmglSjcl.class);
                exampleSjcl.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                List<DchyXmglSjcl> dchyXmglSjclList = entityMapper.selectByExampleNotNull(exampleSjcl);
                if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                    for (DchyXmglSjcl dchyXmglSjcl : dchyXmglSjclList) {
                        slclList.add(dchyXmglSjcl.getClmc());
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(dchyXmglHtxxList)) {
                for (DchyXmglHtxx dchyXmglHtxx : dchyXmglHtxxList) {
                    Example example = new Example(DchyXmglSjxx.class);
                    example.createCriteria().andEqualTo("glsxid", dchyXmglHtxx.getHtxxid());
                    List<DchyXmglSjxx> sjxxList = entityMapper.selectByExampleNotNull(example);
                    if (CollectionUtils.isNotEmpty(sjxxList)) {
                        DchyXmglSjxx dchyXmglSjxx = sjxxList.get(0);
                        Example exampleSjcl = new Example(DchyXmglSjcl.class);
                        exampleSjcl.createCriteria().andEqualTo("sjxxid", dchyXmglSjxx.getSjxxid());
                        List<DchyXmglSjcl> dchyXmglSjclList = entityMapper.selectByExampleNotNull(exampleSjcl);
                        if (CollectionUtils.isNotEmpty(dchyXmglSjclList)) {
                            for (DchyXmglSjcl dchyXmglSjcl : dchyXmglSjclList) {
                                slclList.add(dchyXmglSjcl.getClmc());
                            }
                        }
                    }
                }
            }
        }
        return slclList;
    }

    @Override
    public byte[] pdfReport(String chxmid) {
        String filePath = modelPath + Constants.DCHY_XMGL_BLANK + ".pdf";
        if (StringUtils.isNoneBlank(chxmid)) {
            String[] strings = StringUtils.split(chxmid, ";");

            Map<String, Object> dataMap = Maps.newHashMap();
            try {
                // 1.新建document对象
                Document document = new Document(PageSize.A4);// 建立一个Document对象

                // 2.建立一个书写器(Writer)与document对象关联
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                } else {
                    file.createNewFile();
                }

                ByteArrayOutputStream ba = new ByteArrayOutputStream();
                PdfWriter writer = PdfWriter.getInstance(document, ba);
//                writer.setPageEvent(new Watermark("GTMAP"));// 水印
//            writer.setPageEvent(new MyHeaderFooter());// 页眉/页脚

                // 3.打开文档
                document.open();
                document.addTitle("Title@PDF-Java");// 标题
                document.addAuthor("liuqiang@gtmap.cn");// 作者
                document.addSubject("Subject@iText pdf sample");// 主题
                document.addKeywords("Keywords@iTextpdf");// 关键字
                document.addCreator("Creator@umizs");// 创建者

                if (strings.length == 0) {
                    // 4.向文档中添加内容
                    new PdfReport().generatePDF(document, dataMap);
                } else if (strings.length == 1) {
                    dataMap = generateDataByChxmid(strings[0]);
                    new PdfReport().generatePDF(document, dataMap);
                } else {
                    dataMap = generateDataByChxmid(strings[0]);
                    new PdfReport().generatePDF(document, dataMap);
                    for (int i = 1; i < strings.length; i++) {
                        document.newPage();
                        new PdfReport().generatePDF(document, generateDataByChxmid(strings[i]));
                    }
                }
                // 5.关闭文档
                document.close();
                return ba.toByteArray();
            } catch (Exception e) {
                LOGGER.error("错误原因:{}", e);
                return new byte[0];
            }
        }
        return new byte[0];
    }

    @Override
    public Map<String, Object> generateDataByChxmid(String chxmid) {
        Map<String, Object> dataMap = Maps.newHashMap();
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("chxmid", chxmid);

        List<Map<String, Object>> baxxList = dchyXmglChxmMapper.queryBaxxByChxmid(paramMap);
        List<Map<String, Object>> chdwList = dchyXmglChxmMapper.queryChdwByChxmid(paramMap);

        if (CollectionUtils.isNotEmpty(baxxList)) {
            Map<String, Object> baxx = baxxList.get(0);
            String slr = MapUtils.getString(baxx, "SLR");

            Date slsj = CalendarUtil.formatDate(MapUtils.getString(baxx, "SLSJ"));
            Calendar calendarSlsj = Calendar.getInstance();
            Calendar calendarLqsj = Calendar.getInstance();
            calendarSlsj.setTime(slsj);
            dataMap.put("gcmc", MapUtils.getString(baxx, "GCMC"));
            dataMap.put("jsdw", MapUtils.getString(baxx, "WTDW"));
//            dataMap.put("slr", UserUtil.getUserNameById(slr));
//            dataMap.put("slr", "liuqiang");
//            dataMap.put("lqr", UserUtil.getCurrentUser().getUsername());
//            dataMap.put("lqr", "liuqiang");
//            dataMap.put("slrq", calendarSlsj.get(Calendar.YEAR) + "年" + calendarSlsj.get(Calendar.MONTH) + "月" + calendarSlsj.get(Calendar.DATE) + "日");
//            dataMap.put("lqrq", calendarLqsj.get(Calendar.YEAR) + "年" + calendarLqsj.get(Calendar.MONTH) + "月" + calendarLqsj.get(Calendar.DATE) + "日");
        }

        if (CollectionUtils.isNotEmpty(chdwList)) {
            Map<String, Object> chdw = chdwList.get(0);
            dataMap.put("chdw", MapUtils.getString(chdw, "CHDWMC"));
        }

        dataMap.put("slclList", this.generateClmx(chxmid));
        return dataMap;
    }
}
