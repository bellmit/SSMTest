package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.model.Privilege;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.*;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.entity.video.CameraAutoShot;
import cn.gtmap.onemap.platform.entity.video.CameraRegion;
import cn.gtmap.onemap.platform.service.DBAService;
import cn.gtmap.onemap.platform.service.GeometryService;
import cn.gtmap.onemap.platform.service.TemplateService;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.platform.utils.*;
import cn.gtmap.onemap.security.AuthorizationService;
import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.security.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfUserVo;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.gtmap.onemap.platform.service.VideoService.VIDEO_RESOURCE;

/**
 * 监控点配置
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/2/16 9:09
 */
@Service
public class VideoMetadataServiceImpl extends BaseLogger implements VideoMetadataService {

	private static final String VIDEO_CFG = "video.cfg";

	private final TemplateService templateService;

	private final GeometryService geometryService;

	private final CameraDao cameraDao;

	@Autowired
	private CameraLogDao cameraLogDao;

	@Autowired
	private CameraAutoShotDao cameraAutoShotDao;


	@Autowired
	private DBAService dbaService;

	@Autowired
	private SysUserService sysUserService;

	private final CameraRegionDao cameraRegionDao;

	private final AuthorizationService authorizationService;

	@Autowired
	public FileCacheDao fileCacheDao;

	@Autowired
	public VideoMetadataServiceImpl(TemplateService templateService, GeometryService geometryService, CameraDao cameraDao, CameraRegionDao cameraRegionDao, AuthorizationService authorizationService) {
		this.templateService = templateService;
		this.geometryService = geometryService;
		this.cameraDao = cameraDao;
		this.cameraRegionDao = cameraRegionDao;
		this.authorizationService = authorizationService;
	}


	private Map layerCache;

	public void setLayerCache(Resource path) {
		try {
			Yaml yaml = new Yaml();
			this.layerCache = yaml.loadAs(new FileInputStream(new File(path.getURI())), Map.class);
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	@Override
	public Map getLayerCache() {
		return layerCache;
	}

	/**
	 * key
	 */
	private enum Vkey {
		id, indexCode, name, enabled, counties, devices, regions, x, y, viewRadius, ip, port, username, password, type, platform
	}

	@Override
	public Map getConfig() {
		try {
			Map map = JSON.parseObject(templateService.getTemplate(VIDEO_CFG), Map.class);
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	/**
	 * save camera
	 *
	 * @param camera
	 * @return
	 */
	@Override
	public Camera saveCamera(Camera camera) {
		if (camera.getCreateAt() == null) {
			camera.setCreateAt(new Date());
		}
		return cameraDao.save(camera);
	}

	/**
	 * 从 video.cfg 中获取数据集合 并入库 迁移数据用
	 *
	 * @param sr
	 * @return
	 */
	@Override
	public List<Camera> parseCfgToDb(String sr, boolean replaceAll) {
		List<Camera> ret = Lists.newArrayList();
		try {
			Map regionsMap = JSON.parseObject(templateService.getTemplate(VIDEO_CFG), Map.class);
			CoordinateReferenceSystem targetCrs = StringUtils.isNotBlank(sr) ? geometryService.parseUndefineSR(sr) : null;
			if (isNotNull(regionsMap) && !regionsMap.isEmpty()) {
				// 清空数据库表
				if (replaceAll) {
					cameraDao.deleteAll();
				}
				List<Map> vcus = (List<Map>) regionsMap.get(Vkey.regions.name());
				//遍历监控点区域
				for (Map vcu : vcus) {
					int enable = MapUtils.getIntValue(vcu, Vkey.enabled.name(), 1);
					if (enable > 0) {
						List<Map> devices = (List<Map>) vcu.get(Vkey.devices.name());
						//遍历各个区域的设备
						for (Map device : devices) {
							if (MapUtils.getInteger(device, Vkey.enabled.name(), 1) == 1) {
								Camera camera = new Camera();
								camera.setCreateAt(new Date());
								// 设备单元id
								camera.setVcuId(MapUtils.getString(vcu, Vkey.id.name()));
								// 区域名称
								camera.setRegionName(MapUtils.getString(vcu, Vkey.name.name()));
								camera.setIndexCode(MapUtils.getString(device, Vkey.indexCode.name()));
								camera.setName(MapUtils.getString(device, Vkey.name.name()));
								camera.setPlatform(MapUtils.getString(device, Vkey.platform.name()));
								camera.setIp(MapUtils.getString(device, Vkey.ip.name()));
								camera.setPort(MapUtils.getString(device, Vkey.port.name()));
								//处理坐标点
								double x = StringUtils.isBlank(MapUtils.getString(device, Vkey.x.name())) ? 0 : MapUtils.getDoubleValue(device, Vkey.x.name(), 0);
								double y = StringUtils.isBlank(MapUtils.getString(device, Vkey.y.name())) ? 0 : MapUtils.getDoubleValue(device, Vkey.y.name(), 0);
								if (x > 0 && y > 0) {
									List point = new ArrayList();
									point.add(x);
									point.add(y);
									try {
										CoordinateReferenceSystem srcCrs = geometryService.getCrsByCoordX(x);
										if (isNotNull(targetCrs) && !targetCrs.equals(srcCrs)) {

											Point targetPnt = (Point) geometryService.project(GeometryUtils.createPoint(new JSONArray(point)), srcCrs, targetCrs);
											x = targetPnt.getX();
											y = targetPnt.getY();

										}
									} catch (Exception e) {
										logger.warn("监控点坐标转换异常: {}", e.getLocalizedMessage());
										continue;
									}
								}
								camera.setX(x);
								camera.setY(y);
								String devType = StringUtils.trimToEmpty(MapUtils.getString(device, Vkey.type.name(), Constant.CAMERA_TYPE_NORMAL));
								if (StringUtils.isNotBlank(devType)) {
									camera.setType(devType);
								}
								// 跳过已存在的
								Camera exists = cameraDao.findByIndexCode(camera.getIndexCode());
								if (isNotNull(exists)) continue;
								// 入库
								ret.add(cameraDao.save(camera));
							}
						}
					}
				}
				cameraDao.flush();
			}
		} catch (Exception e) {
			logger.error("解析监控点配置文件异常: {}", e.getLocalizedMessage());
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return ret;
	}

	/**
	 * 通过摄像头名称获取
	 *
	 * @param name
	 * @return
	 */
	@Override
	public List<Camera> getByCameraName(String name) {
		return cameraDao.findByName(name);
	}

	/**
	 * get by indexcode
	 *
	 * @param indexCode
	 * @return
	 */
	@Override
	public Camera getByIndexCode(String indexCode) {
		if (isNull(SecHelper.getUser()) || SecHelper.isAdmin() || SecHelper.isGuest()) {
			return cameraDao.findByIndexCode(indexCode);
		} else {
			Set<String> indexCodes = getAuthorizedRes(Operation.VIEW);
			if (indexCodes.contains(indexCode)) {
				return cameraDao.findByIndexCode(indexCode);
			} else {
				return null;
			}
		}
	}

	@Override
	public List<Camera> getByIndexCodeIn(Set<String> indexCodes) {
		return cameraDao.findByIndexCodeIn(indexCodes);
	}


	/**
	 * 分页
	 *
	 * @param pageRequest
	 * @return
	 */
	@Override
	public Page<Camera> getPage(PageRequest pageRequest) {
		if (isNull(SecHelper.getUser()) || SecHelper.isAdmin() || SecHelper.isGuest()) {
			return cameraDao.findAll(pageRequest);
		} else {
			Set<String> indexCodes = getAuthorizedRes(Operation.VIEW);
			return cameraDao.findByIndexCodeIn(indexCodes, pageRequest);
		}
	}

	/**
	 * 获取所有设备
	 *
	 * @return
	 */
	@Override
	public List<Camera> getAll() {
		if (isNull(SecHelper.getUser()) || SecHelper.isAdmin() || SecHelper.isGuest()) {
			return cameraDao.findAll();
		} else {
			Set<String> indexCodes = getAuthorizedRes(Operation.VIEW);
			if (indexCodes.size() == 0) {
				logger.warn("未找到用户 {} 授权的点位信息.", SecHelper.getUser().getName());
				return Lists.newArrayList();
			}
			List<Camera> list = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(indexCodes)) {
				int count = 0;
				Set<String> indexCodesTemp = new HashSet<>();
				for (String indexCode : indexCodes) {
					if (count == 999) {
						list.addAll(cameraDao.findByIndexCodeIn(indexCodesTemp));

						indexCodesTemp.clear();
						count = 0;
					}
					indexCodesTemp.add(indexCode);
					count++;
				}
				if (CollectionUtils.isNotEmpty(indexCodesTemp)) {
					list.addAll(cameraDao.findByIndexCodeIn(indexCodesTemp));
				}
			}
			return list;
		}
	}

	/**
	 * 根据userName过滤设备
	 *
	 * @return
	 */
	@Override
	public List<Camera> getAll(String loginName) throws Exception {
		PfUserVo user = null;
		Set<String> set = Sets.newHashSet();
		try {
			logger.info("***获取用户{}信息***", loginName);
			user = sysUserService.getUserByloginName(loginName);
			if (user == null) {
				logger.info("***没有查找到{}用户信息", loginName);
				return new ArrayList<>();
			}
			logger.info("***获取用户信息***");
		} catch (Exception er) {
			logger.error("获取用户信息失败" + loginName + er.getMessage());
			return new ArrayList<>();
		}
		String[] operaNames = new String[]{Operation.VIEW};
		Set<String> indexCodes = Sets.newHashSet();
		Set<Privilege> privileges = authorizationService.getPermittedPrivileges(user.getUserId(), VIDEO_RESOURCE);
		for (Privilege privilege : privileges) {
			for (Operation operation : privilege.getOperations()) {
				if (ArrayUtils.contains(operaNames, operation.getName(), true)) {
					indexCodes.add(privilege.getResource());
					break;
				}
			}
		}
		if (CollectionUtils.isNotEmpty(indexCodes)) {
			List<Camera> list = new ArrayList<>();
			int count = 0;
			Set<String> indexCodesTemp = new HashSet<>();
			for (String indexCode : indexCodes) {
				if (count == 999) {
					list.addAll(cameraDao.findByIndexCodeIn(indexCodesTemp));

					indexCodesTemp.clear();
					count = 0;
				}
				indexCodesTemp.add(indexCode);
				count++;
			}
			if (CollectionUtils.isNotEmpty(indexCodesTemp)) {
				list.addAll(cameraDao.findByIndexCodeIn(indexCodesTemp));
			}
			return list;
		} else {
			return new ArrayList<>();
		}

	}


	/**
	 * find RegionNames
	 *
	 * @return
	 */
	@Override
	public List<String> findRegionNames() {
		return cameraDao.findRegionNames();
	}

	/***
	 * 获取当前用户针对当前操作已授权的所有资源
	 * @param operaNames
	 * @return
	 */
	private Set<String> getAuthorizedRes(String... operaNames) {
		if (isNull(operaNames)) {
			operaNames = new String[]{Operation.VIEW};
		}
		User user = SecHelper.getUser();
		Set<String> set = Sets.newHashSet();
		try {
			SecHelper.isAdmin();
		} catch (Exception e) {
			return set;
		}
		if (isNull(user) || SecHelper.isAdmin() || SecHelper.isGuest()) {
			return set;
		}

		Set<Privilege> privileges = authorizationService.getPermittedPrivileges(user.getId(), VIDEO_RESOURCE);
		for (Privilege privilege : privileges) {
			for (Operation operation : privilege.getOperations()) {
				if (ArrayUtils.contains(operaNames, operation.getName(), true)) {
					set.add(privilege.getResource());
					break;
				}
			}
		}
		return set;
	}

	private Set<String> getAuthorizedResByUserName(String userName, String... operaNames) {
		if (isNull(userName)) {
			operaNames = new String[]{Operation.VIEW};
		}
		String userId = getUserId(userName);
		Set<String> set = Sets.newHashSet();
		if (userId.isEmpty()) {
			return set;
		}
		Set<Privilege> privileges = authorizationService.getPermittedPrivileges(userId, VIDEO_RESOURCE);
		for (Privilege privilege : privileges) {
			for (Operation operation : privilege.getOperations()) {
				if (ArrayUtils.contains(operaNames, operation.getName(), true)) {
					set.add(privilege.getResource());
					break;
				}
			}
		}
		return set;
	}

	private String getUserId(String userName) {
		String userId = "";
		String sql = "select t.id from OMP_CAMERA t where t.name = ?";
		try (PreparedStatement pstmt = DBUtils.getConn().prepareStatement(sql);) {
			pstmt.setString(1, userName);
			//建立一个结果集，用来保存查询出来的结果
			try (ResultSet rs = pstmt.executeQuery();) {
				while (rs.next()) {
					userId = rs.getString("id");
				}
			}
			DBUtils.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userId;
	}


	/**
	 * 查询行政区根目录
	 *
	 * @return
	 */
	@Override
	public List<CameraRegion> queryRootRegion() {
		return cameraRegionDao.findByParentNullOrderBySerialNumberAsc();
	}

	/**
	 * 查询行政区根目录
	 *
	 * @return
	 */
	@Override
	public List<CameraRegion> queryAllRegion() {
		return cameraRegionDao.findAll();
	}

	/**
	 * 查询子行政区目录
	 *
	 * @param parentId
	 * @return
	 */
	@Override
	public List<CameraRegion> queryRegionByParent(String parentId) {
		return cameraRegionDao.findByParent(parentId);
	}

	/**
	 * 根据行政区查询摄像头
	 *
	 * @param regionNames
	 * @return
	 */
	@Override
	public List<Camera> queryCameraByRegion(String regionNames) {
		if (isNull(SecHelper.getUser()) || SecHelper.isAdmin() || SecHelper.isGuest()) {
			return cameraDao.findByRegionName(regionNames);
		} else {
			Set<String> indexCodes = getAuthorizedRes(Operation.VIEW);
			return cameraDao.findByRegionNameAndIndexCodeIn(regionNames, indexCodes);
		}
	}

	/**
	 * @param region
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Camera> queryCameraByRegion(String region, Pageable pageable) {
		if (isNull(SecHelper.getUser()) || SecHelper.isAdmin() || SecHelper.isGuest()) {
			return cameraDao.findByRegionName(region, pageable);
		} else {
			Set<String> indexCodes = getAuthorizedRes(Operation.VIEW);
			return cameraDao.findByRegionNameAndIndexCodeIn(region, indexCodes, pageable);
		}
	}

	@Override
	public Page<Camera> queryCameraByRegions(Set<String> regions, Pageable pageable) {
		if (isNull(SecHelper.getUser()) || SecHelper.isAdmin() || SecHelper.isGuest()) {
			return cameraDao.findByRegionNameIn(regions, pageable);
		} else {
			Set<String> indexCodes = getAuthorizedRes(Operation.VIEW);
			return cameraDao.findByRegionNameInAndIndexCodeIn(regions, indexCodes, pageable);
		}
	}

	/**
	 * 根据名称或 indexcode 查询
	 *
	 * @param nameOrIndexCode
	 * @param page
	 * @param size
	 * @return
	 */
	@Override
	public Page<Camera> findByNameLikeOrIndexCode(String nameOrIndexCode, int page, int size) {
		if (isNull(SecHelper.getUser()) || SecHelper.isAdmin() || SecHelper.isGuest()) {
			return cameraDao.findByNameContainingOrIndexCode(nameOrIndexCode, nameOrIndexCode, new PageRequest(page, size));
		} else {
			Set<String> indexCodes = getAuthorizedRes(Operation.VIEW);
			return cameraDao.findByNameContainingAndIndexCodeIn(nameOrIndexCode, indexCodes, new PageRequest(page, size));
		}
	}

	/**
	 * 获取缓冲区内的摄像头
	 *
	 * @param geo
	 * @param bufferSize
	 * @return
	 */
	@Override
	public List<Camera> getByGeo(String geo, double bufferSize) {
		Object feature = geometryService.readUnTypeGeoJSON(geo);
		Geometry poiGeo;
		List<Camera> result = Lists.newArrayList();
		if (feature instanceof SimpleFeature) {
			SimpleFeature simpleFeature = (SimpleFeature) feature;
			poiGeo = (Geometry) simpleFeature.getDefaultGeometry();
			CoordinateReferenceSystem crs = simpleFeature.getFeatureType().getCoordinateReferenceSystem();
			if (isNotNull(crs)) {
				poiGeo = geometryService.project(poiGeo, crs, geometryService.getDefaultCrs());
			}
		} else if (feature instanceof Geometry) {
			poiGeo = (Geometry) feature;
		} else {
			throw new RuntimeException(getMessage("geometry.type.unsupported"));
		}
		if (isNotNull(poiGeo)) {
			if (bufferSize > 0) {
				poiGeo = geometryService.buffer(poiGeo, bufferSize);
			}
			List<Camera> cameras = cameraDao.findAll();
			for (Camera camera : cameras) {
				List tmp = new ArrayList(2);
				tmp.add(camera.getX());
				tmp.add(camera.getY());
				Point point = GeometryUtils.createPoint(new JSONArray(tmp));
				Geometry intersectGeo = poiGeo.intersection(point);
				if (isNotNull(intersectGeo) && !intersectGeo.isEmpty()) {
					result.add(camera);
				}

			}
		}
		return result;
	}


	/**
	 * get regions by name
	 *
	 * @param name
	 * @return
	 */
	@Override
	public List<Map> getRegions(final String name) {
		Map map = getConfig();
		List regions = (List) map.get("regions");
		Collection<Map> result = Collections2.filter(regions, new Predicate<Map>() {
			@Override
			public boolean apply(Map input) {
				return name.equalsIgnoreCase(MapUtils.getString(input, "name"));
			}
		});
		return Lists.newArrayList(result);
	}

	/**
	 * get region by id
	 *
	 * @param regionId
	 * @return
	 */
	@Override
	public Map getRegion(String regionId) {
		Map map = getConfig();
		List list = (List) map.get("regions");
		for (int i = 0; i < list.size(); i++) {
			Map region = (Map) list.get(i);
			try {
				if (regionId.equals(region.get("id"))) {
					return region;
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@Override
	public List<Map> getDevicesByRegion(String regionId) {
		List<Map> devices = new ArrayList<Map>();
		Map map = getConfig();
		List<Map> lists = (List<Map>) map.get("regions");
		if (regionId == null) {
			for (Map item : lists) {
				if (item.containsKey("devices")) {
					for (Map device : (List<Map>) item.get("devices")) {
						device.put("area", item.get("name"));
						device.put("areaId", item.get("id"));
						String type = MapUtils.getString(item, "type");
						if (StringUtils.isBlank(type))
							type = "normal";
						device.put("type", type);
						devices.add(device);
					}
				}
			}
		} else {
			for (Map item : lists) {
				if (regionId.equals(item.get("id").toString()) && item.containsKey("devices")) {
					devices.addAll((List<Map>) item.get("devices"));
					break;
				}
			}
		}
		return devices;
	}

	/***
	 *
	 * @param name
	 * @return
	 */
	@Override
	public String updateUnitName(String name) {
		Map map = getConfig();
		map.put("unitName", name);
		saveVideoCfg(map);
		return name;
	}


	@Override
	public Map updateDevice(String deviceId, String regionId, String content) {
		Map newDevice = JSON.parseObject(content, Map.class);
		if (newDevice.get("createAt") == null || newDevice.get("createAt").equals("")) {
			String s = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			newDevice.put("createAt", s);
		}
		Map map = getConfig();
		List list = (List) map.get("regions");
		for (int i = 0; i < list.size(); i++) {
			Map region = (Map) list.get(i);
			if (regionId.equals(region.get("id"))) {
				List deviceList = (List) region.get("devices");
				int j;
				for (j = 0; j < deviceList.size(); j++) {
					Map device = (Map) deviceList.get(j);
					try {
						if (deviceId.equals(device.get("id"))) {
							deviceList.remove(j);
							break;
						}
					} catch (Exception e) {
						return null;
					}
				}
				deviceList.add(j, newDevice);
				region.put("devices", deviceList);
				break;
			}
		}
		map.put("regions", list);
		saveVideoCfg(map);
		return null;
	}

	@Override
	public void deleteRegion(String regionId) {
		Map map = getConfig();
		List list = (List) map.get("regions");
		for (int i = 0; i < list.size(); i++) {
			Map region = (Map) list.get(i);
			try {
				if (regionId.equals(region.get("id"))) {
					list.remove(i);
					break;
				}
			} catch (Exception e) {
				return;
			}
		}
		map.put("regions", list);
		saveVideoCfg(map);
	}

	/**
	 * 删除设备
	 *
	 * @param deviceId
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteDevice(String deviceId) {
		cameraDao.delete(deviceId);
	}


	/**
	 * 导入服务图层解析图层当中的设备信息生成配置文件
	 *
	 * @param queryUrl
	 */
	@Override
	public void importConfig(String queryUrl, String pt) {
		try {
			String result = HttpRequest.sendRequest2(queryUrl, null);
			Map map = JSON.parseObject(result, Map.class);
			Map videoCfg = null;
			List<Map> regions = null;
			if (map != null && !map.isEmpty()) {
				videoCfg = getConfig();
				regions = (List<Map>) videoCfg.get("regions");  //加载当前已有配置的区域信息
				List<Map> features = (List<Map>) MapUtils.getObject(map, "features");
				for (Map deviceMap : features) {
					Map item = new HashMap();
					Map geometry = (Map) deviceMap.get("geometry");
					Map attr = (Map) deviceMap.get("attributes");
					String regionId = null;
					String regionName = null;
					String type = null;
					String indexCode = null;
					String cameraId = null;
					Iterator iterator = attr.keySet().iterator();
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						if ("region".equalsIgnoreCase(key) || "xzqdm".equalsIgnoreCase(key)) {
							regionName = MapUtils.getString(attr, key);
						} else if ("regionid".equalsIgnoreCase(key)) {
							regionId = MapUtils.getString(attr, key);
						} else if ("type".equalsIgnoreCase(key)) {
							type = MapUtils.getString(attr, key);
						} else if ("cameraid".equalsIgnoreCase(key) || "deviceid".equalsIgnoreCase(key)) {
							cameraId = MapUtils.getString(attr, key);
						} else if ("indexcode".equalsIgnoreCase(key)) {
							indexCode = MapUtils.getString(attr, key);
						} else if ("viewradius".equalsIgnoreCase(key)) {
							item.put("viewRadius", MapUtils.getString(attr, key));
						} else if ("deviceip".equalsIgnoreCase(key)) {
							item.put("ip", MapUtils.getString(attr, key));
						} else if ("deviceport".equalsIgnoreCase(key)) {
							item.put("port", MapUtils.getString(attr, key));
						}
					}
					Map region = getArea(regions, regionName, regionId, type); //获取当前的设备区域分组
					String id = cameraId == null ? UUIDGenerator.generate() : cameraId;
					item.put("createAt", DateUtils.getCurrentTime(""));
					item.put("enabled", 1);
					item.put("id", id);
					item.put("indexCode", indexCode);
					item.put("name", attr.get("name".toUpperCase()));
					item.put("x", geometry.get("x"));
					item.put("y", geometry.get("y"));
					item.put("username", "");
					item.put("password", "");
					item.put("height", 40);

					((List) region.get("devices")).add(item);
				}
			}
			videoCfg.put("regions", regions);
			saveVideoCfg(videoCfg);
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	/**
	 * @param request
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void importConfigByFile(MultipartHttpServletRequest request) {
		Iterator<String> iterator = request.getFileNames();
//        List<Camera> cameras = new ArrayList<Camera>();
		while (iterator.hasNext()) {
			MultipartFile file = request.getFile(iterator.next());
			try {
				Workbook workbook = WorkbookFactory.create(file.getInputStream());
				Sheet sheet = workbook.getSheetAt(0);
				for (Row row : sheet) {
					if (row.getRowNum() == 0) continue;
					Camera camera = new Camera();
					List<Object> pointList = new ArrayList<Object>();
					pointList.add(Double.valueOf(String.valueOf(getCellValue(row.getCell(4)))));
					pointList.add(Double.valueOf(String.valueOf(getCellValue(row.getCell(5)))));
					Point point = transPoint(GeometryUtils.createPoint(new JSONArray(pointList)));
					camera.setRegionName(String.valueOf(getCellValue(row.getCell(0))))
							.setName((String.valueOf(getCellValue(row.getCell(1)))))
							.setVcuId((String.valueOf(getCellValue(row.getCell(2)))))
							.setIndexCode((String.valueOf(getCellValue(row.getCell(3)))))
							.setX(point.getX())
							.setY(point.getY())
							.setHeight(Double.valueOf(String.valueOf(getCellValue(row.getCell(6)))))
							.setPlatform(String.valueOf(getCellValue(row.getCell(7))))
							.setType(String.valueOf(getCellValue(row.getCell(8))))
							.setCreateAt(new Date());
					try {
						cameraDao.save(camera);
					} catch (Exception e) {
						logger.error(JSON.toJSONString(camera) + "保存失败！");
						continue;
					}
//                    cameras.add(camera);
				}
//                cameraDao.save(cameras);
			} catch (Exception e) {
				throw new RuntimeException(e.getLocalizedMessage());
			}
		}
	}


	/**
	 * point坐标转换
	 *
	 * @param point
	 * @return
	 */
	private Point transPoint(Point point) {
		try {
			CoordinateReferenceSystem srcCrs = geometryService.getCrsByCoordX(point.getX());
			if (!geometryService.getDefaultCrs().equals(srcCrs)) {
				return (Point) geometryService.project(point, srcCrs, geometryService.getDefaultCrs());
			} else {
				return point;
			}
		} catch (Exception e) {
			logger.warn("监控点坐标转换异常: {}", e.getLocalizedMessage());
			return point;
		}
	}

	/**
	 * 根据类型获取列的值
	 *
	 * @param cell
	 * @return
	 */
	private Object getCellValue(Cell cell) {
		if (isNull(cell)) return "";
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
			case Cell.CELL_TYPE_FORMULA:
				try {
					return StringUtils.trimToEmpty(cell.getStringCellValue());
				} catch (Exception e) {
					return String.valueOf(cell.getNumericCellValue());
				}
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell))
					return cell.getDateCellValue();
				else
					return cell.getNumericCellValue();
			default:
				return "";
		}
	}

	/**
	 * 获取area分组信息
	 *
	 * @param areaGroup
	 * @param areaName
	 * @return
	 */
	private Map getArea(List<Map> areaGroup, String areaName, String areaId, String type) {
		Map map;
		for (Map area : areaGroup) {
			if (!StringUtils.isBlank(areaId) && area.get("id").toString().equals(areaId))
				return area;
			else if (StringUtils.isBlank(areaId) && area.get("id").toString().equals("NoName"))
				return area;
		}
		//以上配置分组当中无此区域分组信息，将新建分组并且添加至集合
		List<Map> devices = new ArrayList<Map>();
		map = new HashMap();
		map.put("enabled", 1);
		map.put("id", StringUtils.isNotBlank(areaId) ? areaId : UUIDGenerator.generate());
		map.put("type", "mobile".equalsIgnoreCase(type) ? "mobile" : "normal");
		map.put("name", StringUtils.isBlank(areaName) ? "NoName" : areaName);
		map.put("devices", devices);
		areaGroup.add(map);
		return map;
	}

	/**
	 * @param data
	 */
	private void saveVideoCfg(Map data) {
		templateService.modify(VIDEO_CFG, JSON.toJSONString(data, true));
	}

	@Override
	public Integer countCameraByRegionId(String parentId) {
		return cameraRegionDao.countCameraByRegionId(parentId);
	}

	@Override
	public List<CameraRegion> findAllByNameIn(List<String> names) {
		List<CameraRegion> allRegions = new ArrayList<>();
		List<CameraRegion> regions = cameraRegionDao.findAllByNameIn(names);
		//获取所有上级关系点
		for (int i = 0; i < regions.size(); i++) {
			CameraRegion region = regions.get(i);
			List<CameraRegion> tmpRegions = getAllNodes(region, allRegions);
		}
		for (int i = 0; i < allRegions.size(); i++) {
			CameraRegion region = allRegions.get(i);
			CameraRegion parent = region.getParent();
			if (parent != null) {
				if (parent.getChildren() != null) {
					parent.getChildren().add(region);
				}
			}
		}
		for (int i = allRegions.size() - 1; i >= 0; i--) {
			CameraRegion region = allRegions.get(i);
			if (region.getParent() != null) {
				allRegions.remove(region);
			}
		}
		return allRegions;
	}

	/**
	 * 获取所有点
	 * 删除子元素
	 *
	 * @param region
	 * @return
	 */
	private List<CameraRegion> getAllNodes(CameraRegion region, List<CameraRegion> allRegions) {
		//防止重复添加
		if (!checkContains(allRegions, region)) {
			allRegions.add(region);
		}
		//清空子节点
		if (region.getChildren() != null) {
			region.setChildren(new ArrayList<CameraRegion>());
		}
		if (region.getParent() != null) {
			getAllNodes(region.getParent(), allRegions);
		}
		return allRegions;
	}

	private boolean checkContains(List<CameraRegion> regions, CameraRegion region) {
		for (int i = 0; i < regions.size(); i++) {
			if (regions.get(i).getId() == region.getId()) {
				return true;
			}
		}
		return false;
	}


	/**
	 * 获取摄像头与图层叠加结果
	 *
	 * @param layerName
	 * @param where
	 * @param useCache
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<String> getOverlayCamera(String layerName, String where, boolean useCache) throws IOException {
		Map targetLayer = null;
		String dataSource = null;
		//读取配置
		logger.info("****开始获取叠加摄像头信息");
		try {
			ArrayList<Map> lrArr = (ArrayList<Map>) this.layerCache.get("layers");
			for (int i = 0; i < lrArr.size(); i++) {
				Map temp = lrArr.get(i);
				if (temp.get("name").toString().equals(layerName)) {
					targetLayer = temp;
					logger.info("****此图层已配置");
					break;
				}
			}

			if (targetLayer != null && useCache) {
				//使用缓存
				if (fileCacheDao.isExist(targetLayer.get("name").toString())) {
					logger.info("****此图层已缓存");
					String content = fileCacheDao.getCacheContent(layerName);
					if (!content.isEmpty()) {
						return convertToStrList(content);
					}
				}
				//使用配置
				logger.info("****此图层未缓存，需要重写运算");
				if (targetLayer.get("where") != null) {
					if (where == null) {
						where = targetLayer.get("where").toString();
					} else {
						where = where + " or " + targetLayer.get("where").toString();
					}

				}
				if (targetLayer.get("dataSource") != null) {
					dataSource = targetLayer.get("dataSource").toString();
				}
				if (targetLayer.get("enable").equals("false")) {
					//禁用图层分析功能
					return null;
				}
			}


			List<Camera> cameras = cameraDao.findAll();
			List<String> result = geometryService.getOverlayCamera(layerName, where, cameras, dataSource);
			//设置缓存数据
			String content = convertToStr(result);
			fileCacheDao.setCache(content, layerName);
			return result;
		} catch (Exception er) {
			logger.error(er.getLocalizedMessage());
			return new ArrayList<String>();
		}
	}


	/**
	 * 解析缓存中string数组
	 *
	 * @param content
	 * @return
	 */
	private List<String> convertToStrList(String content) {
		if (!content.isEmpty()) {
			String[] strArr = content.split("\\|");
			List<String> result = new ArrayList<String>();
			for (int i = 0; i < strArr.length; i++) {
				result.add(strArr[i]);
			}
			return result;
		} else {
			return new ArrayList<String>();
		}
	}

	/**
	 * 数组转为String
	 *
	 * @param strArr
	 * @return
	 */
	private String convertToStr(List<String> strArr) {
		String content = "";
		for (int i = 0; i < strArr.size(); i++) {
			content = content.concat("|").concat(strArr.get(i));
		}
		return content;
	}

	@Override
	public Page<CameraAutoShot> findCameraAutoShot(Date start, Date end, List<String> cameraIds, Pageable pageable) {
		List<Set<String>> sets = new ArrayList<Set<String>>();
		Set<String> firstSet = Sets.newHashSet();
		sets.add(firstSet);
		int current = 0;
		for (String item : cameraIds) {
			sets.get(current).add(item);
			if (sets.get(current).size() == 1000) {
				current++;
				Set<String> set = Sets.newHashSet();
				sets.add(set);
			}
		}
		switch (sets.size()) {
			case 1:
				return cameraAutoShotDao.findByIndexCodeInAndShotAtBetweenAndImgIdNotNull(start, end, sets.get(0), pageable);
			case 2:
				return cameraAutoShotDao.findByIndexCodeInAndShotAtBetweenAndImgIdNotNull(start, end, sets.get(0), sets.get(1), pageable);
			default:
				return cameraAutoShotDao.findByIndexCodeInAndShotAtBetweenAndImgIdNotNull(start, end, sets.get(0), pageable);
		}

	}

	@Override
	public Page<CameraAutoShot> findCameraAutoShot(Date start, Date end, Pageable pageable) {
		return cameraAutoShotDao.findByShotAtBetweenAndImgIdNotNullOrderByShotAtDesc(start, end, pageable);
	}

	@Override
	public List<Map> searchCameraLog(Date start, Date end, List<String> cameraIDs, List<String> userIds) throws Exception {
		//获取当前用户同部门下所有同事
		String deviceIdWhereIn = dbaService.generateWhereIn(cameraIDs);
		String userIdWhereIn = dbaService.generateWhereIn(userIds);

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String startStr = f.format(start);
		String endStr = f.format(end);
		//生成sql
		String sql =
				"SELECT * FROM " +
						"(SELECT DEVICE_ID,NAME FROM OMP_CAMERA  WHERE DEVICE_ID IN (" + deviceIdWhereIn + ")) a " +
						" LEFT JOIN " +
						"(SELECT CAMERAID,COUNT(id) AS COUNT" +
						" FROM  OMP_CAMERA_LOG " +
						"WHERE OMP_CAMERA_LOG.CREATEAT BETWEEN " + "TO_DATE('" + startStr + "','yyyy-MM-dd')" + " AND " + " TO_DATE('" + endStr + "','yyyy-MM-dd')" +
						" AND USERID IN(" + userIdWhereIn + ")" +
						" GROUP BY CAMERAID" +
						") b ON a.DEVICE_ID=b.CAMERAID ORDER BY b.COUNT";
		List<Map> result = dbaService.search(sql);
		return result;
	}

	@Override
	public List<Map> searchCameraLogST(Date start, Date end, List<String> cameraIDs) throws Exception {
		//获取当前用户同部门下所有同事
		if (cameraIDs.size() == 0) {
			return new ArrayList<Map>();
		}
		String deviceIdWhereIn = dbaService.generateWhereIn(cameraIDs, "DEVICE_ID");
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startStr = f.format(start);
		String endStr = f.format(end);
		//生成sql
		String sql =
				"SELECT DEVICE_ID,COUNT(DISTINCT DEVICE_ID) OVER() as COUNT FROM " +
						"(SELECT DEVICE_ID,NAME FROM OMP_CAMERA  WHERE " + deviceIdWhereIn + ") a " +
						"  JOIN " +
						"(SELECT CAMERAID " +
						" FROM  OMP_CAMERA_LOG " +
						"WHERE OMP_CAMERA_LOG.CREATEAT BETWEEN " + "TO_DATE('" + startStr + "','yyyy-MM-dd HH24:mi:ss')" + " AND " + " TO_DATE('" + endStr + "','yyyy-MM-dd HH24:mi:ss')" +
						") b ON a.DEVICE_ID=b.CAMERAID";
		List<Map> result = dbaService.search(sql);
		return result;
	}

	@Override
	public List<Map> searchCameraLogByIndexCode(Date start, Date end, String cameraID, List<String> userIds) {
		String userIdWhereIn = dbaService.generateWhereIn(userIds);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String startStr = f.format(start);
		String endStr = f.format(end);
		String sql = "SELECT * FROM OMP_CAMERA_LOG " +
				"WHERE OMP_CAMERA_LOG.CREATEAT BETWEEN " + "TO_DATE('" + startStr + "','yyyy-MM-dd')" + " AND " + " TO_DATE('" + endStr + "','yyyy-MM-dd')" +
				"AND USERID IN(" + userIdWhereIn + ")" +
				"AND CAMERAID ='" + cameraID + "'";
		List<Map> result = dbaService.search(sql);
		return result;
	}

	/**
	 * 解析摄像头虚数量文件缓存
	 *
	 * @return
	 */
	@Override
	public Map getCameraCountData() {
		HashMap result = new HashMap();
		String content = "";
		try {
			content = fileCacheDao.getCacheContent("cameraRegionCount");
		} catch (Exception er) {
			return result;
		}
		content = content.replace("\n", "").replace("\t", " ");
		String[] data = content.split("\r");
		for (int i = 0; i < data.length; i++) {
			String lineItem = data[i];
			lineItem = lineItem.trim();
			String[] lineDatas = lineItem.split("\\s+");
			if (lineDatas.length == 3 && lineDatas[2] != "") {
				result.put(lineDatas[0], lineDatas[2]);
			}
		}
		return result;
	}

	@Override
	public List<String> getCameraIdByRegionsId(String RegionsId) {
		Set<String> regionNames = getCameraRegionNamesByParent(RegionsId);
		logger.info("regionNames为", regionNames.size());
		List<String> cameraIds = cameraDao.findIndexCodeByRegionNameIn(regionNames);
		return cameraIds;
	}

	@Override
	public CameraRegion getRegionById(String id) {
		return cameraRegionDao.getOne(id);
	}

	/*
	获取与项目关联的摄像头
	 */
	@Override
	public List<String> getProCameraIdByRegionsId(String RegionsId) {
		Set<String> regionNames = getCameraRegionNamesByParent(RegionsId);
		Object[] regionArr = regionNames.toArray();
		String sql = "SELECT a.CAMERA_ID  FROM OMP_PROJECT_CAMERA_REF a JOIN OMP_CAMERA b ON a.CAMERA_ID = b.DEVICE_ID AND b.REGION_NAME in";
		String whereIn = "";
		if (regionArr.length > 0) {
			whereIn = "'" + regionArr[0].toString() + "'";
		}
		for (int i = 1; i < regionArr.length; i++) {
			whereIn = whereIn + ",'" + regionArr[i].toString() + "'";
		}
		sql = sql + "(" + whereIn + ")";
		List<String> result = new ArrayList<String>();
		List<Map> data = dbaService.search(sql);
		for (int i = 0; i < data.size(); i++) {
			Map item = data.get(i);
			String str = item.get("CAMERA_ID").toString();
			result.add(str);
		}
		return result;
	}

	/**
	 * 获取所有行政区集合
	 *
	 * @return
	 */
	public Set<String> getCameraRegionNamesByParent(String id) {
		try {
			CameraRegion region = cameraRegionDao.getOne(id);
			Set<String> regionIds = new HashSet<String>();
			if (region == null || region.getId() == null) {
				return null;
			}
			List<CameraRegion> children = region.getChildren();
			if (children.size() > 0) {
				for (int i = 0; i < children.size(); i++) {
					CameraRegion item = children.get(i);
					regionIds.add(item.getName());
					Set<String> temp = getCameraRegionNamesByParent(item.getId());
					if (temp != null) {
						regionIds.addAll(temp);
					}
				}
			}
			regionIds.add(region.getName());
			return regionIds;
		} catch (Exception er) {
			return null;
		}
	}

	/**
	 * 连云港定制版本
	 * 统计行政区规定时间内使用情况
	 * 摄像头使用频次
	 * 摄像头个数
	 * 摄像头使用强度  次/个/天
	 * 摄像头掉线数
	 *
	 * @param start
	 * @param end
	 * @param departMent
	 * @return
	 */
	@Override
	public Map countCameraLog(Date start, Date end, String organName, String departMent) throws Exception {
		int useCount = 0;
		int offLine = 0;
		double useFre = 0;
		int offDays = 0;
		Map result = new HashMap();
		List deList = new ArrayList();
		HashMap<String, List> organMap = getDepartment();
		List<Camera> cameras;
		//判断是否是组
		if (organMap.containsKey(departMent)) {
			cameras = cameraDao.findCamerasByDepartmentIn(organMap.get(departMent));
			result.put("isZS", true);
		} else {
			List tmp = new ArrayList<String>();
			tmp.add(departMent);
			cameras = cameraDao.findCamerasByDepartmentIn(tmp);
		}

		List<String> cameraStrList = new ArrayList<>();
		for (Camera camera : cameras) {
			//获取在线离线个数
			if (camera.getStatus() == 0) {
				offLine++;
			}
			cameraStrList.add(camera.getIndexCode());
		}
		logger.info("***查询organName" + organName);
		if (cameraStrList.size() <= 0) {
			return null;
		}

		List<String> userIds = new ArrayList<String>();
		List<PfUserVo> users = sysUserService.getUsersByOrganName(organName);
		for (int i = 0; i < users.size(); i++) {
			PfUserVo user = users.get(i);
			userIds.add(user.getUserId());
			logger.info("***查询" + organName + i + user.getUserId());
		}
		;
		if (userIds.size() == 0) {
			logger.error("***未配置权限！organName为" + organName);
			return null;
		}
		int days = (int) ((end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000));
		//获取这批摄像头在指定时间内使用总次数
		String cameraIdWhereIn = dbaService.generateWhereIn(cameraStrList, "cameraId");
		String userIdWhereIn = dbaService.generateWhereIn(userIds, "userId");

		String countSql = "SELECT COUNT(*) as COUNT FROM OMP_CAMERA_LOG WHERE " + cameraIdWhereIn + " AND " + userIdWhereIn;
		countSql += "AND CREATEAt > " + dbaService.toDataYYYYmmDD(start) + " AND CREATEAT < " + dbaService.toDataYYYYmmDD(end);
		List<Map> countDt = dbaService.search(countSql);
		logger.info("***查询" + organName + ":" + countSql);
		useCount = ((BigDecimal) countDt.get(0).get("COUNT")).intValue();

		if (cameras.size() != 0) {
			useFre = (double) useCount / cameras.size() / days;
			DecimalFormat decimalFormat = new DecimalFormat("#.0000");
			useFre = Double.valueOf(decimalFormat.format(useFre));
		}
		result.put("cameraCount", cameras.size());
		result.put("offLine", offLine);
		result.put("useCount", useCount);
		result.put("useFre", useFre);
		return result;
	}

	private HashMap<String, List> getDepartment() {
		String organStr = AppConfig.getProperty("cameraGoup");
		//黄伟，海州，
		HashMap<String, List> result = new HashMap();
		String[] groups = organStr.split("\\|");
		for (int i = 0; i < groups.length; i++) {
			String groupName = groups[i].split(":")[0];
			String[] organList = groups[i].split(":")[1].split(",");
			result.put(groupName, Arrays.asList(organList));
		}
		return result;
	}

	/**
	 * 统计行政区规定时间内使用情况(省厅)
	 * 摄像头使用频次
	 * 摄像头个数
	 * 摄像头使用强度  次/个/天
	 * 摄像头掉线数
	 *
	 * @param start
	 * @param end
	 * @param regionCode
	 * @return
	 */
	@Override
	public Map countCameraLogST(Date start, Date end, String regionCode) throws Exception {
		int useCount = 0;
		int offLine = 0;
		double useFre = 0;
		int offDays = 0;
		int useCameraCount = 0;
		Map result = new HashMap();
		Set<String> regionNames = getCameraRegionNamesByParent(regionCode);
		List<Camera> cameras = cameraDao.findByRegionNameIn(regionNames);
		//List<Camera> cameras = cameraDao.findCamerasByRegionName(regionCode);
		List<String> cameraStrList = new ArrayList<>();
		for (Camera camera : cameras) {
			//获取在线离线个数
			if (camera.getStatus() == 0) {
				offLine++;
			}
			String indexcode = camera.getIndexCode().replace("'", "").replace("‘", "").replace("’", "");
			cameraStrList.add(indexcode);
		}
		logger.info("***查询regionName" + regionCode);

		int days = (int) ((end.getTime() - start.getTime()) / (24 * 60 * 60 * 1000));
		//获取使用次数
		List<Map> logData = searchCameraLogST(start, end, cameraStrList);
		if (logData.size() > 0) {
			Object temp = logData.get(0).get("COUNT");
			useCameraCount = Integer.parseInt(temp.toString());
		}
		useCount = logData.size();
		if (cameras.size() != 0) {
			useFre = (double) useCount / cameras.size();
			DecimalFormat decimalFormat = new DecimalFormat("#.00");
			useFre = Double.valueOf(decimalFormat.format(useFre));

		}
		//循环map判断摄像头使用个数

		result.put("cameraCount", cameras.size());
		result.put("offLine", offLine);
		result.put("useCount", useCount);
		result.put("useFre", useFre);
		result.put("noUseCount", cameras.size() - useCameraCount);
		return result;
	}
}
