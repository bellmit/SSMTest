package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.Constant;
//import cn.gtmap.onemap.platform.entity.Camera;
import cn.gtmap.onemap.platform.entity.video.CameraLog;
import cn.gtmap.onemap.platform.service.VideoManager;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysUserService;
import junit.framework.TestCase;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/23 17:21
 */
public class VideoManagerImplTest extends BaseServiceTest {

    @Autowired
    private VideoManager videoManager;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CameraLoggerServiceImpl cameraLoggerService;

    @Test
    public void testtime(){
        cameraLoggerService.save(new CameraLog());
    }

    @Test
    public void testCaptureImage() throws Exception {
        //videoManager.captureImage();
    }

    @Test
    public void testFindPresetNo(){
//        videoManager.getVideoService().findPresetNo("1", "2");
    }


    @Test
    public void testGetVideoService(){
         videoManager.getVideoService("hw").getCameraView("025200255430101");
    }
}
