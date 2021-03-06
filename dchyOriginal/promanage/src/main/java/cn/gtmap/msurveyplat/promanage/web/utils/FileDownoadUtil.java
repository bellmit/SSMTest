package cn.gtmap.msurveyplat.promanage.web.utils;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglClcg;
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadHtxxService;
import cn.gtmap.msurveyplat.promanage.core.service.FileUploadService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.Container;
import cn.gtmap.msurveyplat.promanage.utils.PlatformUtil;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.fileCenter.model.Node;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cn.gtmap.msurveyplat.promanage.utils.PlatformUtil.getNodeService;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/4 12:22
 * @description
 */
public class FileDownoadUtil {
    private static final Log logger = LogFactory.getLog(FileDownoadUtil.class);

    private static PlatformUtil platformUtil = (PlatformUtil) Container.getBean("platformUtil");
    private static FileUploadService fileUploadService = (FileUploadService) Container.getBean("fileUpload");
    private static FileUploadHtxxService fileUploadHtxxService = (FileUploadHtxxService) Container.getBean("fileUploadHtxx");

    private static String fileDownLoadUrl = AppConfig.getFileCenterUrl() + "/file/get.do?fid=";
    private static Set<String> fileName = new HashSet<>();
    private static Map<String, Integer> fileNameCount = new HashMap<>();

    public static Set<String> getFileName() {
        return fileName;
    }

    public static void setFileName(Set<String> fileName) {
        FileDownoadUtil.fileName = fileName;
    }

    public static Map<String, Integer> getFileNameCount() {
        return fileNameCount;
    }

    public static void setFileNameCount(Map<String, Integer> fileNameCount) {
        FileDownoadUtil.fileNameCount = fileNameCount;
    }

    public static byte[] downloadWj(int nodeId) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse htResponse = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream in = null;

        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(fileDownLoadUrl).append(nodeId);
            HttpGet httpGet = new HttpGet(stringBuilder.toString());
            htResponse = httpClient.execute(httpGet);
            if (htResponse != null && org.apache.http.HttpStatus.SC_OK == htResponse.getStatusLine().getStatusCode()) {
                in = htResponse.getEntity().getContent();
            }
            if (in != null) {
                byte[] buffer = new byte[5120];
                int n;
                while (-1 != (n = in.read(buffer))) {
                    output.write(buffer, 0, n);
                }
            }
        } catch (ClientProtocolException e) {
            logger.info("????????????");
        } catch (IOException e) {
            logger.error("??????xml??????");
        } finally {
            if (htResponse != null) {
                try {
                    htResponse.close();
                } catch (IOException e) {
                    logger.error("????????????{}???", e);
                }
            }
        }
        return output.toByteArray();
    }

    public static int getFileNumberByNodeId(int nodeId) {
        int num = 0;
        Node node = getNodeService().getNode(nodeId);
        if (node.getType() == 1) {
            // ??????
//            downloadZj(nodeId);
            num = 1;
        } else {
            List<Node> nodeList = getNodeService().getAllChildNodes(nodeId);
            if (CollectionUtils.isNotEmpty(nodeList)) {
                for (Node nodeTemp : nodeList) {
                    if (nodeTemp.getType() == 1) {
                        num++;
                    }
                }
            }
        }
        return num;
    }

    public static Map<String, Object> downLoadFile(int nodeId) {
        Map<String, Object> resultMap = Maps.newHashMap();
        Node node = getNodeService().getNode(nodeId);
        if (node != null) {
            if (node.getType() == 1) {
                // ??????
                resultMap.put("wjnr", downloadWj(nodeId));
                resultMap.put("wjmc", node.getName());
            } else {
                List<Node> nodeList = getNodeService().getAllChildNodes(nodeId);
                if (CollectionUtils.isNotEmpty(nodeList)) {
                    for (Node nodeTemp : nodeList) {
                        if (nodeTemp.getType() == 1) {
                            resultMap.put("wjnr", downloadWj(nodeTemp.getId()));
                            resultMap.put("wjmc", nodeTemp.getName());
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    public static String downLoadZip(ZipOutputStream zos, int nodeId, String path, String xzlx) throws Exception {
        List<Node> nodeList = getNodeService().getChildNodes(nodeId);
        String name;
        String filName = "";
        Node node = getNodeService().getNode(nodeId);
        Integer parentId = node.getParentId();
        if (node != null) {
            filName = node.getName();
        }
        if (CollectionUtils.isEmpty(nodeList)) {
            nodeList = getNodeService().getChildNodes(parentId);
        }
        if (CollectionUtils.isNotEmpty(nodeList)) {
            InputStream sbs = null;
            boolean kyxz = true;
            for (Node nodeTemp : nodeList) {
                if (nodeTemp.getType() == 1) {
                    if (StringUtils.equals(xzlx, Constants.XZLX_CGXZ)) {
                        Example dchyXmglkClcgExample = new Example(DchyXmglClcg.class);
                        dchyXmglkClcgExample.createCriteria().andEqualTo("shzt", Constants.DCHY_XMGL_SHZT_SHTG).andEqualTo("wjzxid",nodeTemp.getId());
                        EntityMapper entityMapper = (EntityMapper) Container.getBean("entityMapper");
                        List<DchyXmglClcg> list = entityMapper.selectByExample(dchyXmglkClcgExample);
                        if (CollectionUtils.isEmpty(list)){
                            // ?????????????????????????????????????????????????????????????????????

                            continue;
                        }
                    }
                    try {
                        sbs = new ByteArrayInputStream(downloadWj(nodeTemp.getId()));
                        if (StringUtils.isNotBlank(path)) {
                            name = path + File.separator + nodeTemp.getName();
                        } else {
                            name = nodeTemp.getName();
                        }
                        zos.putNextEntry(new ZipEntry(name));//?????????????????? ??????ZipEntry??????
                        int temp = 0;
                        while ((temp = sbs.read()) != -1) {//????????????
                            zos.write(temp);// ????????????
                        }
                    } catch (Exception e) {
                        logger.error("??????????????????{}", e);
                        throw e;
                    } finally {
                        if (sbs != null) {
                            sbs.close(); //???????????????
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(path)) {
                        name = path + File.separator + nodeTemp.getName();
                    } else {
                        name = nodeTemp.getName();
                    }
                    downLoadZip(zos, nodeTemp.getId(), name, xzlx);
                }
            }
        } else {
            if (node.getType() == 1) {//????????????
                InputStream sbs = null;
                try {
                    sbs = new ByteArrayInputStream(downloadWj(node.getId()));
                    if (StringUtils.isNotBlank(path)) {
                        name = path + File.separator + node.getName();
                    } else {
                        if (fileName.contains(node.getName())) {//??????????????????
                            fileName.add(node.getName());
                            fileNameCount.put(node.getName(), fileNameCount.get(node.getName()) + 1);
                            name = node.getName();
                            int i = name.indexOf(".");
                            String prefixName = name.substring(0, i);
                            String suffixName = name.substring(i);
                            String newFileName = prefixName + "_" + fileNameCount.get(node.getName()) + suffixName;
                            zos.putNextEntry(new ZipEntry(newFileName));//?????????????????? ??????ZipEntry??????
                        } else {//?????????????????????
                            fileName.add(node.getName());
                            fileNameCount.put(node.getName(), 0);
                            name = node.getName();
                            zos.putNextEntry(new ZipEntry(name));//?????????????????? ??????ZipEntry??????
                        }
                    }
                    int len;
                    byte[] buf = new byte[1024 * 8];
                    while ((len = sbs.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                } catch (Exception e) {
                    logger.error("??????????????????{}", e);
                    throw e;
                } finally {
                    if (sbs != null) {
                        sbs.close(); //???????????????
                    }
                }
            }
        }
        return filName;
    }

    public static String downLoadZip4Xs(ZipOutputStream zos, int nodeId, String path) throws Exception {
        List<Node> nodeList = getNodeService().getChildNodes(nodeId);
        String name;
        String filName = "";
        Node node = getNodeService().getNode(nodeId);
        Integer parentId = node.getParentId();
        if (node != null) {
            filName = node.getName();
        }
        if (CollectionUtils.isEmpty(nodeList)) {
            nodeList = getNodeService().getChildNodes(parentId);
        }
        if (CollectionUtils.isNotEmpty(nodeList)) {
            InputStream sbs = null;
            for (Node nodeTemp : nodeList) {
                if (StringUtils.equals(Constants.DCHY_XMGL_ONLINE_SLTJ, nodeTemp.getName()) || StringUtils.equals(Constants.DCHY_XMGL_ONLINE_RKCG, nodeTemp.getName())) {
                    continue;
                }
                if (nodeTemp.getType() == 1) {
                    try {
                        sbs = new ByteArrayInputStream(downloadWj(nodeTemp.getId()));
                        if (StringUtils.isNotBlank(path)) {
                            name = path + File.separator + nodeTemp.getName();
                        } else {
                            name = nodeTemp.getName();
                        }
                        zos.putNextEntry(new ZipEntry(name));//?????????????????? ??????ZipEntry??????
                        int temp = 0;
                        while ((temp = sbs.read()) != -1) {//????????????
                            zos.write(temp);// ????????????
                        }
                    } catch (Exception e) {
                        logger.error("??????????????????{}", e);
                        throw e;
                    } finally {
                        if (sbs != null) {
                            sbs.close(); //???????????????
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(path)) {
                        name = path + File.separator + nodeTemp.getName();
                    } else {
                        name = nodeTemp.getName();
                    }
                    downLoadZip4Xs(zos, nodeTemp.getId(), name);
                }
            }
        } else {
            if (node.getType() == 1) {//????????????
                InputStream sbs = null;
                try {
                    sbs = new ByteArrayInputStream(downloadWj(node.getId()));
                    if (StringUtils.isNotBlank(path)) {
                        name = path + File.separator + node.getName();
                    } else {
                        if (fileName.contains(node.getName())) {//??????????????????
                            fileName.add(node.getName());
                            fileNameCount.put(node.getName(), fileNameCount.get(node.getName()) + 1);
                            name = node.getName();
                            int i = name.indexOf(".");
                            String prefixName = name.substring(0, i);
                            String suffixName = name.substring(i);
                            String newFileName = prefixName + "_" + fileNameCount.get(node.getName()) + suffixName;
                            zos.putNextEntry(new ZipEntry(newFileName));//?????????????????? ??????ZipEntry??????
                        } else {//?????????????????????
                            fileName.add(node.getName());
                            fileNameCount.put(node.getName(), 0);
                            name = node.getName();
                            zos.putNextEntry(new ZipEntry(name));//?????????????????? ??????ZipEntry??????
                        }
                    }
                    int len;
                    byte[] buf = new byte[1024 * 8];
                    while ((len = sbs.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                } catch (Exception e) {
                    logger.error("??????????????????{}", e);
                    throw e;
                } finally {
                    if (sbs != null) {
                        sbs.close(); //???????????????
                    }
                }
            }
        }
        return filName;
    }

    public static String encodeFileName(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
        filename = URLEncoder.encode(filename, "UTF8");
        String userAgent = request.getHeader("User-Agent");
        String rtn = "filename=\"" + filename + "\"";
        // ????????????UA??????????????????IE????????????????????????????????????IE??????????????????
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            // IE????????????????????????URLEncoder??????
            if (userAgent.indexOf("msie") != -1) {
                rtn = "filename=\"" + filename + "\"";
            }
            // Opera?????????????????????filename*
            else if (userAgent.indexOf("opera") != -1) {
                rtn = "filename*=UTF-8''" + filename;
            }
            // Safari????????????????????????ISO?????????????????????
            else if (userAgent.indexOf("safari") != -1) {
                try {
                    rtn = "filename=\""
                            + new String(filename.getBytes("UTF-8"),
                            "ISO8859-1") + "\"";
                } catch (UnsupportedEncodingException e) {
                    logger.error("????????????{}???", e);
                }
            }
            // Chrome????????????????????????MimeUtility?????????ISO?????????????????????
            else if (userAgent.indexOf("applewebkit") != -1) {
                try {
                    filename = MimeUtility
                            .encodeText(filename, "UTF8", "B");
                } catch (UnsupportedEncodingException e) {
                    logger.error("????????????{}???", e);
                }
                rtn = "filename=\"" + filename + "\"";
            }
            // FireFox????????????????????????MimeUtility???filename*???ISO?????????????????????
            else if (userAgent.indexOf("mozilla") != -1) {
                rtn = "filename*=UTF-8''" + filename;
            }
        }
        return rtn;
    }
}
