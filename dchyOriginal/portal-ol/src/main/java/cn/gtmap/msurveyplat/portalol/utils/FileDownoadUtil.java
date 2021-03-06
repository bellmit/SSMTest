package cn.gtmap.msurveyplat.portalol.utils;

import cn.gtmap.msurveyplat.portalol.core.service.FileUploadService;
import com.google.common.base.Charsets;
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
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cn.gtmap.msurveyplat.portalol.utils.PlatformUtil.getNodeService;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/12
 * @description 文件下载
 */
public class FileDownoadUtil {
    private static final Log logger = LogFactory.getLog(FileDownoadUtil.class);

    private static PlatformUtil platformUtil = (PlatformUtil) Container.getBean("platformUtil");
    private static FileUploadService fileUploadService = (FileUploadService) Container.getBean("fileUpload");

    private static String fileDownLoadUrl = AppConfig.getFileCenterUrl() + "/file/get.do?fid=";

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
            logger.info("请求失败");
        } catch (IOException e) {
            logger.error("读取xml失败", e);
        } finally {
            if (htResponse != null) {
                try {
                    htResponse.close();
                } catch (IOException e) {
                    logger.error("错误信息:{}", e);
                }
            }
        }
        return output.toByteArray();
    }

    public static int getFileNumberByNodeId(int nodeId) {
        int num = 0;
        Node node = getNodeService().getNode(nodeId);
        if (node.getType() == 1) {
            // 文件
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
                // 文件
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

    public static String downLoadZip(ZipOutputStream zos, int nodeId, String path) throws Exception {
        List<Node> nodeList = getNodeService().getChildNodes(nodeId);
        String name;
        String filName = "";
        Node node = getNodeService().getNode(nodeId);
        if (node != null) {
            filName = node.getName();
        }
        if (CollectionUtils.isNotEmpty(nodeList)) {
            InputStream sbs = null;
            for (Node nodeTemp : nodeList) {
                if (nodeTemp.getType() == 1) {
                    try {
                        sbs = new ByteArrayInputStream(downloadWj(nodeTemp.getId()));
                        if (StringUtils.isNotBlank(path)) {
                            name = path + File.separator + nodeTemp.getName();
                        } else {
                            name = nodeTemp.getName();
                        }
                        zos.putNextEntry(new ZipEntry(name));//压缩文件名称 设置ZipEntry对象
                        int temp = 0;
                        while ((temp = sbs.read()) != -1) {//读取内容
                            zos.write(temp);// 压缩输出
                        }
                    } catch (Exception e) {
                        logger.error("文件下载失败{}", e);
                        throw e;
                    } finally {
                        if (sbs != null) {
                            sbs.close(); //关闭输入流
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(path)) {
                        name = path + File.separator + nodeTemp.getName();
                    } else {
                        name = nodeTemp.getName();
                    }
                    downLoadZip(zos, nodeTemp.getId(), name);
                }
            }
        }
        return filName;
    }

    public static String encodeFileName(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
        filename = URLEncoder.encode(filename, "UTF8");
        String userAgent = request.getHeader("User-Agent");
        String rtn = "filename=\"" + filename + "\"";
        // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            // IE浏览器，只能采用URLEncoder编码
            if (userAgent.indexOf("msie") != -1) {
                rtn = "filename=\"" + filename + "\"";
            }
            // Opera浏览器只能采用filename*
            else if (userAgent.indexOf("opera") != -1) {
                rtn = "filename*=UTF-8''" + filename;
            }
            // Safari浏览器，只能采用ISO编码的中文输出
            else if (userAgent.indexOf("safari") != -1) {
                rtn = "filename=\""
                        + new String(filename.getBytes(Charsets.UTF_8),
                        Charsets.ISO_8859_1) + "\"";
            }
            // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
            else if (userAgent.indexOf("applewebkit") != -1) {
                try {
                    filename = MimeUtility
                            .encodeText(filename, "UTF8", "B");
                } catch (UnsupportedEncodingException e) {
                    logger.error("错误信息:{}", e);
                }
                rtn = "filename=\"" + filename + "\"";
            }
            // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
            else if (userAgent.indexOf("mozilla") != -1) {
                rtn = "filename*=UTF-8''" + filename;
            }
        }
        return rtn;
    }
}
