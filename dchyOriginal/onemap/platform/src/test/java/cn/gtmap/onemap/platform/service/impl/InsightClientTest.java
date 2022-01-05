package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.api.InsightClient;
import cn.gtmap.api.cameraGroup.CameraGroupRequest;
import cn.gtmap.api.cameraGroup.CameraGroupResponse;
import cn.gtmap.busi.model.Ztree;
import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.entity.video.CameraRegion;
import cn.gtmap.onemap.platform.service.InsightService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者 王建明
 * @创建日期 2019/7/9
 * @创建时间 9:42
 * @版本号 V 1.0
 */
public class InsightClientTest extends BaseServiceTest {
	@Autowired
	private InsightClient insightClient;
	@Autowired
	private InsightService insightService;

	@Test
	public void testInsightClient() throws Exception {
		List<CameraRegion> cameraRegions = insightService.getCameraRegions(insightClient,null);

		System.out.println(JSON.toJSONString(cameraRegions));
	}
}
