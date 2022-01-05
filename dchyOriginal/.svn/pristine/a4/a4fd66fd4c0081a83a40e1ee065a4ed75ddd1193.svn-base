package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.platform.entity.network.NetworkRegion;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.VideoPlats;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.NetworkService;
import cn.gtmap.onemap.platform.service.VideoManager;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.platform.service.VideoService;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import com.hikvision.ivms6.vms.ws.ThirdServiceLocator;
import com.hikvision.ivms6.vms.ws.ThirdServicePortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * @Author：fanyoudu
 * @Date：2018/8/2.9:17
 * @Description:
 */
@Controller
@RequestMapping("/network")
public class NetworkController extends BaseController {

    private ThirdServiceLocator thirdService;

    private ThirdServicePortType thirdServicePort;

    private String ThirdServiceHttpSoap12EndpointWSDDServiceName = "ThirdServiceHttpSoap12Endpoint";

    @Autowired
    private NetworkService networkService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private VideoManager videoManager;


    /***
     * 打开海康视频
     * @return
     */
//    @RequestMapping(value = "/open/video")
//    public String openVideo(
//            @RequestParam(value = "iProtocol",defaultValue = "554",required = false) String iProtocol,
//            @RequestParam(value = "indexCode", required = true) String indexCode,
//            @RequestParam(value = "iStreamType", defaultValue = "1", required = false) String iStreamType,
//            Model model
//    ) throws Exception {
//        Camera camera = videoMetadataService.getByIndexCode(indexCode);
//        if(camera==null){
//            throw new Exception("请检查IndexCode参数是否正确");
//        }
//        VideoPlats.Plat plat= videoManager.getPlat(camera.getPlatform());
//        if(plat==null){
//            throw new Exception("请检查摄像头配置信息");
//        }
//        model.addAttribute("szIP", camera.getIp());
//        if(camera.getPort()==null||camera.getPort()==""){
//            model.addAttribute("szPort","80" );
//        }else {
//            model.addAttribute("szPort",camera.getPort() );
//        }
//        model.addAttribute("szUsername", plat.getUserName());
//        model.addAttribute("szPassword", plat.getPassword());
//        return "network/video";
//    }

    @RequestMapping(value = "/open/video")
    public String openVideo(
            @RequestParam(value = "token",defaultValue = "1",required = false) String token,
            @RequestParam(value = "iStreamType", defaultValue = "1", required = false) String iStreamType,
            @RequestParam(value = "indexCode", required = true) String indexCode,
            @RequestParam(value = "netZoneId", defaultValue = "100001", required = false) String netZoneId,
            Model model
    ) {
        model.addAttribute("token", token);
        model.addAttribute("iStreamType", iStreamType);
        model.addAttribute("indexCode", indexCode);
        model.addAttribute("netZoneId", netZoneId);
        return "network/hk-video";
    }

    @RequestMapping(value = "/open/getResponse")
    @ResponseBody
    public String openVideoResponse (
            @RequestParam(value = "token",defaultValue = "1",required = false) String token,
            @RequestParam(value = "iStreamType", defaultValue = "1", required = false) String iStreamType,
            @RequestParam(value = "indexCode", required = true) String indexCode,
            @RequestParam(value = "netZoneId", defaultValue = "100001", required = false) String netZoneId
    )  throws Exception {
        Camera camera = videoMetadataService.getByIndexCode(indexCode);
        if(camera==null){
            logger.error("请检查IndexCode参数是否正确");
            throw new Exception("请检查IndexCode参数是否正确");

        }
        logger.info(camera.getPlatform());

        VideoPlats.Plat plat= videoManager.getPlat(camera.getPlatform());
        if(plat==null){
            logger.error("请检查摄像头配置信息");
            throw new Exception("请检查摄像头配置信息");
        }
        logger.info(plat.getName());
        if(!plat.getName().equals("hk")) {
            logger.error("必须是海康平台");
            throw new Exception("必须是海康平台");
        }
        String videoXmlString = "";
        try {
            logger.info(getMessage("hik.init.starting", plat.getName()));
            String wsdl = "http://" + plat.getServer() + "/vms/services/ThirdService?wsdl";
            thirdService = new ThirdServiceLocator(wsdl, new QName("http://ws.vms.ivms6.hikvision.com", "ThirdService"));
            thirdService.setEndpointAddress(ThirdServiceHttpSoap12EndpointWSDDServiceName, wsdl.split("\\?")[0].concat(".ThirdServiceHttpSoap12Endpoint/"));
            thirdServicePort = thirdService.getThirdServiceHttpSoap12Endpoint();
            videoXmlString = thirdServicePort.getStreamServiceByCameraIndexCodes(token, Integer.parseInt(iStreamType), indexCode, Long.parseLong(netZoneId));
            logger.info("hik.init.succeed，{}", videoXmlString);
            videoXmlString = videoXmlString
                    .replaceAll("Priority=\"0\" Code=\"0\"", "Priority=\"50\" Code=\"31\"")
                    .replaceAll("<Talk>null</Talk>", "<Talk>1</Talk>");
            logger.info("预览回访报文：{}", videoXmlString);
        } catch (Exception e) {
            logger.error("hik.init.error,{}", e.getLocalizedMessage());
        }
        return videoXmlString;
    }



    /***
     * 打开大华的平台
     * @return
     */
    @RequestMapping(value = "/open/dhVideo")
    public String opendhVideo(@RequestParam(value = "szIP", required = true) String szIP,
                              @RequestParam(value = "szPort", defaultValue = "80") String szPort,
                              @RequestParam(value = "szUsername", required = true) String szUsername,
                              @RequestParam(value = "szPassword", required = true) String szPassword,
                              @RequestParam(value = "deviceId", required = false) String deviceId,
                              @RequestParam(value = "coding", required = true) String coding,
                              Model model) {

        model.addAttribute("szIP", szIP);
        model.addAttribute("szPort", szPort);
        model.addAttribute("szUsername", szUsername);
        model.addAttribute("szPassword", szPassword);
        model.addAttribute("deviceId", deviceId);
        model.addAttribute("coding", coding);

        return "network/dh-video";
    }

    /**
     * 获取监控点分组
     *
     * @return
     */
    @RequestMapping(value = "/fetch/root/groups", method = RequestMethod.GET)
    @ResponseBody
    public List<NetworkRegion> getRootGroups() {
        try {
            List<NetworkRegion> cameraRegions = networkService.queryRootRegion();
            return cameraRegions;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new JSONMessageException(e.getLocalizedMessage());
        }
    }
}
