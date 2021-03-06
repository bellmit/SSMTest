package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.FileCacheDao;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.CameraView;
import cn.gtmap.onemap.platform.entity.video.Ptz;
import cn.gtmap.onemap.platform.entity.video.VideoPlats;
import cn.gtmap.onemap.platform.event.CmsException;
import cn.gtmap.onemap.platform.service.FileStoreService;
import cn.gtmap.onemap.platform.service.VideoManager;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.platform.service.VideoService;
import cn.gtmap.onemap.platform.utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.hikvision.cms.webservice.bo.xsd.CameraViewDTO;
import com.hikvision.cms.webservice.bo.xsd.CameraViewResult;
import com.hikvision.ivms6.vms.ws.ThirdServiceLocator;
import com.hikvision.ivms6.vms.ws.ThirdServicePortType;
import com.hikvision.nms.webservice.omp.dto.xsd.CameraInfoDTO;
import com.hikvision.nms.webservice.omp.dto.xsd.CameraInfoResult;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import javax.xml.namespace.QName;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ????????????????????????
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/9 19:05
 */
public class HikVideoServiceImpl extends VideoServiceImpl {

    private ThirdServiceLocator thirdService;

    private ThirdServicePortType thirdServicePort;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private VideoManager videoManager;

    @Autowired
    private FileCacheDao fileCacheDao;

    /**
     * ???????????????????????????
     */
    private String userName;
    /**
     * ????????????????????????
     */
    private String userPwd;

    private String ThirdServiceHttpSoap12EndpointWSDDServiceName = "ThirdServiceHttpSoap12Endpoint";

    /**
     * ????????????
     */
    private VideoPlats.Plat plat;

    public void setPlat(VideoPlats.Plat plat) {
        this.plat = plat;
    }


    /**
     *
     * @return
     */
    private String getHikRequestXmlStr() throws Exception {
        String location = fileCacheDao.getLocation();
        logger.info("location{}",location);
        Resource resource = new UrlResource(location.concat("/hikRequestXml.xml"));
        String xmlStr= IOUtils.toString(resource.getURI());
        logger.info("xmlStr?????????{}",xmlStr);
        return xmlStr;
    }

    /**
     * ????????? ????????????
     */
    @Override
    public void init() {
        if (plat == null) {
            return;
        }
        try {
            logger.info(getMessage("hik.init.starting", plat.getName()));
            String wsdl = "http://" + plat.getServer() + "/vms/services/ThirdService?wsdl";
            thirdService = new ThirdServiceLocator(wsdl, new QName("http://ws.vms.ivms6.hikvision.com", "ThirdService"));
            thirdService.setEndpointAddress(ThirdServiceHttpSoap12EndpointWSDDServiceName, wsdl.split("\\?")[0].concat(".ThirdServiceHttpSoap12Endpoint/"));
            thirdServicePort = thirdService.getThirdServiceHttpSoap12Endpoint();
            userName = plat.getUserName();
            userPwd = plat.getPassword();
            logger.info(getMessage("hik.init.succeed", plat.getName()));
        } catch (Exception e) {
            logger.error(getMessage("hik.init.error", plat.getName(), e.getLocalizedMessage()));
        }
    }

    /***
     * ??????????????????????????????(??????webservice?????? for hk)
     * @param outSR
     * @return
     */
    @Override
    public List<Camera> getAllCamerasByWs(String outSR) {
        List<Camera> list = new ArrayList<Camera>();
        CoordinateReferenceSystem targetCrs = geometryService.parseUndefineSR(outSR);
        CoordinateReferenceSystem srcCrs = geometryService.parseUndefineSR("4610");
        try {
            detectServicePort();
            CameraInfoResult cameraInfoResult = thirdServicePort.getCameraInfoPage(0, Integer.MAX_VALUE);
            if (isNotNull(cameraInfoResult)) {
                if (cameraInfoResult.getResult()) {
                    List<CameraInfoDTO> cameraInfoDTOs = Arrays.asList(cameraInfoResult.getCameraInfoDTOs());
                    if (targetCrs instanceof ProjectedCRS) {
                        for (CameraInfoDTO cameraInfo : cameraInfoDTOs) {
                            if (cameraInfo.getLongitude() != null && cameraInfo.getLatitude() != null) {
                                List point = new ArrayList();
                                point.add(cameraInfo.getLongitude());
                                point.add(cameraInfo.getLatitude());
                                Point targetPnt = (Point) geometryService.project(GeometryUtils.createPoint(new JSONArray(point)), srcCrs, targetCrs);
                                cameraInfo.setLongitude(targetPnt.getX());
                                cameraInfo.setLatitude(targetPnt.getY());
                            }
                        }
                    }
                    for (CameraInfoDTO cameraInfoDTO : cameraInfoDTOs) {
                        Camera camera = new Camera();
                        camera.setIndexCode(cameraInfoDTO.getIndexCode());
                        camera.setId(String.valueOf(cameraInfoDTO.getCameraId()));
                        camera.setName(cameraInfoDTO.getName());
                        camera.setVcuId(String.valueOf(cameraInfoDTO.getRegionId()));
                        camera.setRegionName(String.valueOf(cameraInfoDTO.getRegionId()));
                        camera.setX(isNull(cameraInfoDTO.getLongitude()) ? 0 : cameraInfoDTO.getLongitude());
                        camera.setY(isNull(cameraInfoDTO.getLatitude()) ? 0 : cameraInfoDTO.getLatitude());
                        list.add(camera);
                    }
                } else
                    throw new CmsException(cameraInfoResult.getErrorCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return list;
    }

    /**
     * ??????camera???indexCode????????????xml(???????????????)
     *
     * @param operaType
     * @param cameraIndexCode
     * @return
     */
    @Override
    public String getCameraXml(OperaType operaType, String cameraIndexCode) {
        Assert.notNull(cameraIndexCode);
        String result = "";
        Document xmlDoc;
        Element root;
        SAXReader reader = new SAXReader();
        detectServicePort();
        try {
            switch (operaType) {
                case preview:
                    String previewXml = thirdServicePort.getStreamServiceByCameraIndexCodes(null, 0, cameraIndexCode, Long.valueOf(100001));
                    if (isNotNull(previewXml) && !previewXml.equals("-1")) {
                        xmlDoc = reader.read(new InputStreamReader(new ByteArrayInputStream(previewXml.getBytes()), "gbk"));
                        root = xmlDoc.getRootElement();
                        Dom4jUtils.setAttribute((Element) root.selectSingleNode("Privilege"), "Priority", "60");//??????60??????????????????????????????
                        Dom4jUtils.setAttribute((Element) root.selectSingleNode("Privilege"), "Code", "31");
                        Element option = (Element) root.selectSingleNode("Option");
                        Dom4jUtils.setElementValue((Element) option.selectSingleNode("Talk"), "1");
                        OutputFormat format = OutputFormat.createCompactFormat(); //????????????
                        //????????????xml???????????????
                        format.setEncoding("UTF-8");
                        StringWriter writer = new StringWriter();
                        //???????????????????????????
                        XMLWriter xmlwriter = new XMLWriter(writer, format);
                        //??????????????????xml??????????????????
                        xmlwriter.write(xmlDoc);
                        result = writer.toString();
                    } else
                        logger.error("??????????????????{" + cameraIndexCode + "}??????????????????xml?????????");
                    break;
                case playback:
                    String playbackXml = thirdServicePort.getVrmServiceByCameraIndexCodes(null, cameraIndexCode, Long.valueOf(100001), "23", 0);
                    if (isNotNull(playbackXml) && !playbackXml.equals("-1")) {
                        xmlDoc = reader.read(new ByteArrayInputStream(playbackXml.getBytes()));
                        root = xmlDoc.getRootElement();
                        Element imp = (Element) root.selectSingleNode("Intelligence").selectSingleNode("Imp");
                        Dom4jUtils.addAttribute(imp, "userName", userName);
                        Dom4jUtils.addAttribute(imp, "password", userPwd);
                        Dom4jUtils.setElementValue((Element) root.selectSingleNode("Privilege"), "7");
                        Dom4jUtils.addElement(root, "AutoPlay");
                        Dom4jUtils.setElementValue((Element) root.selectSingleNode("AutoPlay"), "0");
                        OutputFormat format = OutputFormat.createCompactFormat(); //????????????
                        //????????????xml???????????????
                        format.setEncoding("UTF-8");
                        StringWriter writer = new StringWriter();
                        //???????????????????????????
                        XMLWriter xmlwriter = new XMLWriter(writer, format);
                        //??????????????????xml??????????????????
                        xmlwriter.write(xmlDoc);
                        result = writer.toString();
                    } else
                        logger.error("??????????????????{" + cameraIndexCode + "}??????????????????xml?????????");
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("get " + operaType.name() + " xml error:" + e.getLocalizedMessage());
        }
        return result;
    }

    public String getPTZCameraXml(String cameraIndexCode) throws Exception {
        String template = getHikRequestXmlStr();
        Map data = new HashMap();
        data.put("cameraIndexCode",cameraIndexCode);
        Camera camera = videoMetadataService.getByIndexCode(cameraIndexCode);
        if(camera==null){
            throw new Exception("??????????????????????????????,???????????????????????????!");
        }
        data.put("Name",camera.getName());
        //??????camra??????ip
        VideoPlats.Plat hkPlat= videoManager.getPlat("hk");
        if(hkPlat==null){
            throw new Exception("???????????????!");
        }
        String userId = hkPlat.getUserName();
        String password =hkPlat.getPassword();
        String serverPort =hkPlat.getPort();
        String serverIp = hkPlat.getServer();
        String cameraIp = camera.getIp();
        String vcuId = camera.getVcuId();
        data.put("userId",userId);
        data.put("IndexCode",cameraIndexCode);
        data.put("IndexCode1",vcuId);
        data.put("password",password);
        data.put("serverPort",serverPort);
        data.put("serverIp",serverIp);
        data.put("cameraIp",cameraIp);

        for(Object key:data.keySet()){
            String tplName ="{{"+(String)key+"}}";
            template= template.replace(tplName,(String)data.get(key));
        }
        logger.info("template{}",template);
        return template;
    }

    /**
     * ???????????????
     * @param indexCode ????????????????????????????????????????????????????????????
     * @return
     */
    @Override
    public List<CameraView> getCameraView(String indexCode) {
        List<CameraView> result = new ArrayList<CameraView>();
        logger.info("start getCameraView,indexCode: {}",indexCode);
        try {
            boolean isHttp = AppConfig.getBooleanProperty("hik.ptz.http.enable");
            if (isHttp) {
                // ??????????????? http ?????????????????? ptz ?????? ????????????
                String url = AppConfig.getProperty("hik.ptz.get.url");
                Map data = Maps.newHashMap();
                data.put("indexCode", indexCode);
                Map ret = (Map) HttpRequest.post(url, data, "json");
                boolean succeed = MapUtils.getBooleanValue(ret, "success");
                if (!succeed) {
                    String msg = MapUtils.getString(ret, "msg");
                    logger.error("get ptz from hik error: {}", msg);
                    throw new RuntimeException("get ptz from hik error: " + msg);
                } else {
                    // ptz ????????????
                    Map ptzMap = Maps.newHashMap();
                    double p = MapUtils.getDoubleValue(ptzMap, "pan");
                    double t = MapUtils.getDoubleValue(ptzMap, "tile");
                    double z = MapUtils.getDoubleValue(ptzMap, "zoom");
                    result.add(new CameraView(indexCode, p, t, 1500));
                }
            } else {
                detectServicePort();
                return getCameraViewByPtz(indexCode);
                //??????indexCodes????????????
//                CameraViewResult cameraViewResult = thirdServicePort.getCameraViewedByIndexCodes(indexCode);
//                if (cameraViewResult != null) {
//                    if (cameraViewResult.getResult()) {
//                        List<CameraViewDTO> cameraViews = Arrays.asList(cameraViewResult.getCameraViewDTOArray());
//                        logger.info("cameraViews?????????:"+cameraViews.size());
//                        for (CameraViewDTO cameraViewDTO : cameraViews) {
//                            logger.info("cameraViews:1");
//                            CameraView cameraView = new CameraView();
//                            logger.info("cameraViews:2");
//                            cameraView.setIndexCode(cameraViewDTO.getCameraIndexCode());
//                            logger.info("cameraViews:3{}",cameraViewDTO.getViewString());
//                            if (isNull(cameraViewDTO.getViewString())) {
//                                continue;
//                            }
//                            Map viewMap = JSON.parseObject(cameraViewDTO.getViewString(), Map.class);
//                            cameraView.setAzimuth(Double.valueOf(MapUtils.getString(viewMap, "azimuth")));
//                            cameraView.setHorizontalAngle(MapUtils.getDouble(viewMap, "horizontalValue"));
//                            cameraView.setViewRadius(Double.valueOf(MapUtils.getString(viewMap, "maxViewRadius")));
//                            logger.info(getMessage("camera.scope.view.info", cameraView.getIndexCode(), cameraView.toString()));
//                            result.add(cameraView);
//                            logger.info("cameraViewResult");
//                        }
//                    } else{
//                        throw new CmsException(cameraViewResult.getErrorCode());
//                    }
//                    logger.info("???????????????:"+result.size());
//                }

            }
        } catch (Exception e) {
            logger.error("error:"+e.getMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
        logger.info("????????????");
        return result;
    }

    /**
     * ??????ptz???????????????indexcode
     *
     * @param indexCodes
     * @return
     */
    public List<CameraView> getCameraViewByPtz(String indexCodes) {
        if (StringUtils.isBlank(indexCodes))
            return new ArrayList<CameraView>();
        String[] indexCodeArray = indexCodes.split(",");
        List<CameraView> cameraViewList = new ArrayList<CameraView>();
        for (String indexCode : indexCodeArray) {
            try {
                logger.warn("***???????????????????????????{}",indexCode);
                String ptzInfo = thirdServicePort.getCameraPTZInfoByIndexCode(indexCode);
                logger.warn("***??????ptz??????{},{}",indexCode,ptzInfo);
                Document document = DocumentHelper.parseText(ptzInfo);
                Element element = document.getRootElement();
                Node rootNode = element.selectSingleNode("Pack").selectSingleNode("Params");
                String p = rootNode.selectSingleNode("PanPos").getText();
                String t = rootNode.selectSingleNode("TiltPos").getText();
                String z = rootNode.selectSingleNode("ZoomPos").getText();
                logger.warn("***??????ptz??????{}???{}???{}???{},{}",p,t,z,indexCode,ptzInfo);
                CameraView cameraView = new CameraView();
                cameraView.setAzimuth(Double.valueOf(p));
                double zoom = (Double.valueOf(z));
                double horizontalValue = zoom > 20 ? 5 : getHorizontalValue(zoom);
                cameraView.setHorizontalAngle(horizontalValue);
                Double vRadius = Math.abs(20 / Math.tan(Math.toRadians(horizontalValue)));//??????Z????????????????????????
                if (vRadius > 200)
                    vRadius = 200D;
                Double radius = Math.tan(Math.toRadians(90 - Double.valueOf(t))) * 30;//??????T??????????????????  30?????????
                radius = Math.abs(radius);
                cameraView.setViewRadius(radius > vRadius ? vRadius : radius);
                cameraViewList.add(cameraView);
            } catch (RemoteException e) {
                logger.error(e.getLocalizedMessage());
            } catch (DocumentException e) {
                logger.error(e.getLocalizedMessage());
            }
        }
        return cameraViewList;
    }

    /**
     * ???????????????
     *
     * @param zoom
     * @return
     */
    private double getHorizontalValue(double zoom) {
        return 2 * Math.toDegrees(Math.atan(Math.sqrt(3) / (zoom + 1) / 2));
    }

    /***
     * ??????ptz ??????
     * @param indexCode ???????????????
     * @param target    ??????????????????
     */
    @Override
    public void setPTZ(String indexCode, Point target) {
        assert indexCode != null;
        assert target != null;
        String url;
        Camera camera = videoMetadataService.getByIndexCode(indexCode);
        logger.debug("{}????????????setPtz??????",indexCode);
        List list = new ArrayList();
        list.add(camera.getX());
        list.add(camera.getY());
        Point source = GeometryUtils.createPoint(new JSONArray(list));
        //????????????????????????????????????????????????
        CoordinateReferenceSystem srcCrs = null;
        CoordinateReferenceSystem targetCrs = null;
        try {
            srcCrs = geometryService.getCrsByCoordX(source.getX());
            targetCrs = geometryService.getCrsByCoordX(target.getX());
        } catch (Exception e) {
            logger.error("getCrsByCoordX with exception:" + e.toString());
            throw new RuntimeException(e);
        }
        try {
            if (srcCrs instanceof GeographicCRS) {
                source = (Point) geometryService.project(source, srcCrs, geometryService.parseUndefineSR(Constant.EPSG_2364));
            }
            if (targetCrs instanceof GeographicCRS) {
                target = (Point) geometryService.project(target, srcCrs, geometryService.parseUndefineSR(Constant.EPSG_2364));
            }
            //??????p t z
            logger.debug("start calculate ....");
            Ptz ptz = VideoUtils.calculatePtz(source, target, camera);
            boolean isHttp = AppConfig.getBooleanProperty("hik.ptz.http.enable");
            logger.debug("{}hik.ptz.http.enable{}",indexCode,isHttp);
            if (isHttp) {
                // ??????????????? http ?????????????????? ptz ??????
                url = AppConfig.getProperty("hik.ptz.set.url");
                Map data = Maps.newHashMap();
                data.put("indexCode", indexCode);
                data.put("p", ptz.getP());
                data.put("t", ptz.getT());
                data.put("z", ptz.getZ());
                Map ret = (Map) HttpRequest.post(url, data, "json");
                boolean succeed = MapUtils.getBooleanValue(ret, "success");
                if (!succeed) {
                    String msg = MapUtils.getString(ret, "msg");
                    logger.error("set ptz to hik error: {}", msg);
                    throw new RuntimeException("set ptz to hik error: " + msg);
                }
            } else {
                //???????????? ??????????????????
                logger.debug("??????????????????????????????{}",indexCode);
                String response="";
                String deviceinfo="";
                deviceinfo = getCameraXml(OperaType.preview, indexCode);
                logger.debug("deviceinfo?????????{}",deviceinfo);
                Pattern p = Pattern.compile("\t|\r|\n");
                Matcher m = p.matcher(deviceinfo);
                String deviceInfo = m.replaceAll("");
                url = plat.getPtzServer() + "/runptz?p=" + ptz.getP() + "&t=" + ptz.getT() + "&z=" + ptz.getZ() + "&deviceinfo=".concat(URLEncoder.encode(deviceInfo, "UTF-8"));
                logger.info("url:" + url);
                try {
                    response = HttpRequest.sendRequest2(url, null);
                }catch (Exception er){
                    logger.debug("sendRequest?????????????????????{}",er.getMessage());
                }
                logger.debug("setPTZResponse-----" + response);
            }

        } catch (Exception e) {
            logger.error("setPTZ with error:" + e.toString());
            throw new RuntimeException(e);
        }
    }

    /***
     * ??????ptz
     * @param indexCode
     * @param p
     * @param t
     * @param z
     */
    @Override
    public void setPTZ(String indexCode, String p, String t, String z) {
        String xml = getCameraXml(OperaType.preview, indexCode);
        Pattern pattern = Pattern.compile("\t|\r|\n");
        Matcher m = pattern.matcher(xml);
        String deviceInfo = m.replaceAll("");
        String cServer = plat.getPtzServer();
        String url;
        try {
            boolean isHttp = AppConfig.getBooleanProperty("hik.ptz.http.enable");
            if (isHttp) {
                // ??????????????? http ?????????????????? ptz ??????
                url = AppConfig.getProperty("hik.ptz.set.url");
                Map data = Maps.newHashMap();
                data.put("indexCode", indexCode);
                data.put("p", p);
                data.put("t", t);
                data.put("z", z);
                Map ret = (Map) HttpRequest.post(url, data, "json");
                boolean succeed = MapUtils.getBooleanValue(ret, "success");
                if (!succeed) {
                    String msg = MapUtils.getString(ret, "msg");
                    logger.error("set ptz from hik error: {}", msg);
                    throw new RuntimeException("set ptz from hik error: " + msg);
                }
            } else {
                url = cServer.concat("/runptz").concat("?p=" + p).concat("&t=" + t).concat("&z=" + z).concat("&deviceinfo=" + URLEncoder.encode(deviceInfo, "UTF-8"));
                String response = HttpRequest.sendRequest2(url, null);
                logger.debug(response);
            }

        } catch (Exception e) {
            logger.error("set ptz error: " + e.toString());
        }
    }

    /**
     * ????????????????????????????????????
     */
    private void detectServicePort() {
        if (isNull(thirdServicePort)) throw new RuntimeException("???????????????????????????");
    }

}
