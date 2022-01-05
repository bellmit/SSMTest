package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.VideoPlats;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.FFmpegManagerService;
import cn.gtmap.onemap.platform.service.GeometryService;
import cn.gtmap.onemap.platform.service.VideoManager;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import cn.gtmap.onemap.platform.utils.GeometryUtils;
import cn.gtmap.onemap.security.IdentityService;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * . 提供第三方调用的 api
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2018/3/22 (c) Copyright gtmap Corp.
 */
@Controller
@RequestMapping("/api")
public class GatewayController extends BaseController {

    private final static String CACHE_NAME = "gatewayTokenCache";

    private final CacheManager cacheManager;

    private final IdentityService identityService;

    private final VideoMetadataService videoMetadataService;

    private final VideoManager videoManager;

    private final GeometryService geometryService;

    private final FFmpegManagerService fFmpegManagerService;

    @Autowired
    public GatewayController(CacheManager cacheManager, IdentityService identityService,
                             VideoMetadataService videoMetadataService, VideoManager videoManager,
                             GeometryService geometryService, FFmpegManagerService fFmpegManagerService) {
        this.cacheManager = cacheManager;
        this.identityService = identityService;
        this.videoMetadataService = videoMetadataService;
        this.videoManager = videoManager;
        this.geometryService = geometryService;
        this.fFmpegManagerService = fFmpegManagerService;
    }

    /**
     * 生成 token
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/generateToken")
    @ResponseBody
    public String generateToken(@RequestParam String username,
                                @RequestParam String password,
                                HttpServletResponse response) {
        try {
            response.setHeader(Constant.ACCESS_CONTROL_ALLOW_ORIGN, "*");
            String token = identityService.login(username, password);
            cacheManager.getCache(CACHE_NAME).put(token, StringUtils.EMPTY);
            return token;
        } catch (SecurityException e) {
            throw new JSONMessageException(e.getLocalizedMessage());
        }

    }

    /**
     * 获取所有的视频点信息
     *
     * @param token
     * @return
     */
    @RequestMapping("/cameras/all")
    @ResponseBody
    public List<Camera> getCameras(@RequestParam String token,
                                   HttpServletResponse response) {
        try {
            response.setHeader(Constant.ACCESS_CONTROL_ALLOW_ORIGN, "*");
            preHandle(token);
            return videoMetadataService.getAll();
        } catch (Exception e) {
            throw new JSONMessageException(e.getMessage());
        }
    }

    /**
     * poi 查找 2km 范围内的cameras
     *
     * @param token
     * @param x
     * @param y
     * @param bufferSize
     * @return
     */
    @RequestMapping("/cameras/poi")
    @ResponseBody
    public List<Camera> getCamerasPoi(@RequestParam String token,
                                      @RequestParam double x,
                                      @RequestParam double y,
                                      @RequestParam(required = false, defaultValue = "2000") double bufferSize,
                                      HttpServletResponse response) {
        try {
            response.setHeader(Constant.ACCESS_CONTROL_ALLOW_ORIGN, "*");
            preHandle(token);
            return videoMetadataService.getByGeo(point2GeoJson(x, y), bufferSize);
        } catch (Exception e) {
            throw new JSONMessageException(e.getMessage());
        }
    }

    /**
     * 视频流查看页面
     * @param token
     * @param indexCode
     * @param model
     * @return
     */
    @RequestMapping("/camera/stream")
    public String streamPlay(@RequestParam String token,
                             @RequestParam String indexCode,
                             Model model) {
        try {
            preHandle(token);
            Camera camera = videoMetadataService.getByIndexCode(indexCode);
            if (isNull(camera)) {
                logger.error("camera of {} not found.", indexCode);
                throw new JSONMessageException("找不到摄像头信息!");
            }
            String liveUrl = fFmpegManagerService.startStream(indexCode, camera.getVcuId());
            model.addAttribute("liveUrl", liveUrl);
            model.addAttribute("cameraCode", indexCode);
        } catch (Exception e) {
            throw new JSONMessageException(e.getMessage());
        }
        return "/video/streamLive";
    }

    /**
     * 视频预览
     *
     * @param indexCode
     * @param model
     * @return
     */
    @RequestMapping(value = "/camera/realplay", method = RequestMethod.GET)
    public String realPlay(@RequestParam String token,
                           @RequestParam String indexCode,
                           Model model) {
        try {
            preHandle(token);
            Camera camera = videoMetadataService.getByIndexCode(indexCode);
            if (isNull(camera)) {
                logger.error("camera of {} not found.", indexCode);
                throw new JSONMessageException("找不到摄像头信息!");
            }
            VideoPlats.Plat plat = videoManager.getPlat(camera.getPlatform());
            model.addAttribute("user", plat.getUserName());
            model.addAttribute("pwd", plat.getPassword());
            model.addAttribute("server", plat.getServer());
            model.addAttribute("port", plat.getPort());
            model.addAttribute("camera", camera);
            return "video/realplay";
        } catch (RuntimeException e) {
            throw new JSONMessageException(e.getMessage());
        }
    }


    /**
     * 验证token是否有效
     *
     * @param token
     * @return
     */
    private void preHandle(String token) {
        Cache.ValueWrapper wrapper = cacheManager.getCache(CACHE_NAME).get(token);
        if (isNull(wrapper)) {
            throw new JSONMessageException("token is invalid.");
        }
    }

    /**
     * 点转成 geojson
     *
     * @param x
     * @param y
     * @return
     */
    private String point2GeoJson(double x, double y) {
        Point point = GeometryUtils.createPoint(x, y);
        return geometryService.toGeoJSON(point);
    }
}
