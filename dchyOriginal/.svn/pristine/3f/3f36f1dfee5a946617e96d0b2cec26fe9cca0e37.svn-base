package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.api.ApiException;
import cn.gtmap.api.InsightClient;
import cn.gtmap.api.cameraGroup.CameraGroupRequest;
import cn.gtmap.api.cameraGroup.CameraGroupResponse;
import cn.gtmap.busi.model.Ztree;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.CameraRegion;
import cn.gtmap.onemap.platform.service.InsightService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者 王建明
 * @创建日期 2019/7/9
 * @创建时间 10:21
 * @版本号 V 1.0
 */
@Service
public class InsightServiceImpl implements InsightService {
	@Override
	public List<CameraRegion> getCameraRegions(InsightClient insightClient, String userId) {
		List<CameraRegion> cameraRegions = new ArrayList<>();

		try {
			CameraGroupResponse cameraGroupResponse = null;
			CameraGroupRequest cameraGroupRequest = new CameraGroupRequest();

			Map<String, String> map = new HashMap<String, String>();
			if (StringUtils.isNotBlank(userId)) {
				map.put("type", "own");
				map.put("userId", userId);
			} else {
				map.put("type", "all");
				map.put("userId", "");
			}

			cameraGroupRequest.setJsonParam(JSON.toJSONString(map));
			cameraGroupResponse = insightClient.execute(cameraGroupRequest);
			Ztree ztreeRoot = cameraGroupResponse.getResult();

			List<Ztree> ztrees = ztreeRoot.getChildren();
			if (CollectionUtils.isNotEmpty(ztrees)) {
				for (Ztree ztree : ztrees) {
					cameraRegions.add(getCameraRegionFromZtree(ztree));
				}
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return cameraRegions;
	}

	@Override
	public List<Camera> getAllCameras(InsightClient insightClient, String userId) {
		List<Camera> cameras = new ArrayList<>();
		try {
			CameraGroupResponse cameraGroupResponse = null;
			CameraGroupRequest cameraGroupRequest = new CameraGroupRequest();

			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "all");
			map.put("userId", "");

			cameraGroupRequest.setJsonParam(JSON.toJSONString(map));
			cameraGroupResponse = insightClient.execute(cameraGroupRequest);
			Ztree ztreeRoot = cameraGroupResponse.getResult();

			addCameraFromZtree(ztreeRoot, cameras);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return cameras;
	}

	private void addCameraFromZtree(Ztree ztree, List<Camera> cameras) {
		String deviceId = ztree.getKz1();
		if (StringUtils.isNotBlank(deviceId)) {
			Camera camera = new Camera();
			camera.setId(ztree.getId());
			camera.setIndexCode(ztree.getKz1());
			camera.setName(ztree.getName());
			camera.setX(ztree.getX().doubleValue());
			camera.setY(ztree.getY().doubleValue());

			cameras.add(camera);
		}
		List<Ztree> children = ztree.getChildren();
		if (CollectionUtils.isNotEmpty(children)) {
			for (Ztree child : children) {
				addCameraFromZtree(child, cameras);
			}
		}
	}

	private CameraRegion getCameraRegionFromZtree(Ztree ztree) {
		CameraRegion cameraRegion = new CameraRegion();

		cameraRegion.setId(ztree.getId());
		cameraRegion.setName(ztree.getName());
		cameraRegion.setIndexCode(ztree.getKz1());
		cameraRegion.setX(ztree.getX());
		cameraRegion.setY(ztree.getY());
		List<Ztree> children = ztree.getChildren();
		if (CollectionUtils.isNotEmpty(children)) {
			List<CameraRegion> childrenCameraRegions = new ArrayList<>();
			for (Ztree child : children) {
				childrenCameraRegions.add(getCameraRegionFromZtree(child));
			}
			cameraRegion.setChildren(childrenCameraRegions);
			if (CollectionUtils.isNotEmpty(childrenCameraRegions)) {
				int childCameraCount = 0;
				for (CameraRegion childrenCameraRegion : childrenCameraRegions) {
					if (StringUtils.isNotBlank(childrenCameraRegion.getIndexCode())) {
						childCameraCount++;
					}
				}
				if (childCameraCount > 0) {
					cameraRegion.setName(cameraRegion.getName() + "【" + childCameraCount + "】");
				}
			}
		} else {
			cameraRegion.setChildren(null);
		}
		return cameraRegion;
	}
}
