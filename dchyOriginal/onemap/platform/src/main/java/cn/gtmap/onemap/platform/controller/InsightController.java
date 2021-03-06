package cn.gtmap.onemap.platform.controller;

import cn.gtmap.api.InsightClient;
import cn.gtmap.api.cameraGroup.CameraGroupRequest;
import cn.gtmap.api.cameraGroup.CameraGroupResponse;
import cn.gtmap.busi.model.Ztree;
import cn.gtmap.onemap.core.util.DateUtils;
import cn.gtmap.onemap.model.Operation;
import cn.gtmap.onemap.platform.Constant;
import cn.gtmap.onemap.platform.dao.CameraRegionDao;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.dict.Dict;
import cn.gtmap.onemap.platform.entity.video.*;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.*;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import cn.gtmap.onemap.platform.utils.AppPropertyUtils;
import cn.gtmap.onemap.platform.utils.GeometryUtils;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import cn.gtmap.onemap.platform.utils.HttpRequestUtils;
import cn.gtmap.onemap.security.Constants;
import cn.gtmap.onemap.security.SecHelper;
import cn.gtmap.onemap.security.User;
import cn.gtmap.onemap.service.GeoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gtis.config.AppConfig;
import com.gtis.plat.service.SysUserService;
import com.gtis.plat.vo.PfOrganVo;
import com.gtis.plat.vo.PfUserVo;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ?????????????????????
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/12/9 18:39
 */
@Controller
@RequestMapping("/insight")
public class InsightController extends BaseController {
	@Autowired
	private InsightClient insightClient;
	@Autowired
	private InsightService insightService;
	@Autowired
	private VideoManager videoManager;
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private FileStoreService fileStoreService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private VideoMetadataService videoMetadataService;
	@Autowired
	private PresetService presetService;
	@Autowired
	private CameraLoggerService cameraLoggerService;
	@Autowired
	private PanoramaService panoramaService;
	@Autowired
	private GeometryService geometryService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private VideoRecognizeService videoRecognizeService;
	@Autowired
	private QuartzScheduleManager quartzScheduleManager;
	@Autowired
	private FFmpegManagerService fFmpegManagerService;
	@Autowired
	private CameraOfflineService cameraOfflineService;
	@Autowired
	private CameraAutoShotService cameraAutoShotService;
	@Autowired
	private CameraRegionDao cameraRegionDao;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private DictService dictService;

	@Autowired
	private GISService gisService;

	@Autowired
	private GeoService geoService;

	@Autowired
	private VideoService videoService;

	@Autowired
	private SysUserService sysUserService;

	private final static String CACHE_CAPTURE = "captureCache";

	private final static String CACHE_PANORAMA = "panoramicCache";

	private final static String CACHE_TOKEN = "tokenCache";

	private final static String TOKEN_KEY = "token";

	@Autowired
	public MapQueryService MapQueryService;


	/**
	 * set cache
	 *
	 * @param token
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/cache/set/{token}")
	@ResponseBody
	public String setCache(@PathVariable String token,
	                       @RequestParam(value = "data") String data,
	                       HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			if (StringUtils.isNotBlank(data)) {
				cacheManager.getCache(CACHE_TOKEN).put(token, data);
			}
			return token;
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * get cache by token
	 *
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/cache/get")
	@ResponseBody
	public Object getCache(@RequestParam(value = "token") String token,
	                       HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			if (StringUtils.isNotBlank(token)) {
				Object content = cacheManager.getCache(CACHE_TOKEN).get(token).get();
				//???????????? ??????????????????????????????
				String userId = token.split("token")[0];
				Map param = JSON.parseObject((String) content, Map.class);
				List<Map> vculist = (List<Map>) param.get("vculist");
				for (Map vcu : vculist) {
					List<Map> deviceList = (List<Map>) vcu.get("devicelist");
					if (deviceList == null) {
						vcu.put("CanCap", true);
						vcu.put("CanRec", true);
						vcu.put("CanOpt", true);
					} else {
						for (Map device : deviceList) {
							String indexCode = MapUtils.getString(device, "IndexCode");
							//?????????????????????????????????
							device.put("CanCap", true);
							device.put("CanRec", false);
							//??????????????????????????????????????????
							if (Constants.USER_ADMIN_ID.equals(userId) || SecHelper.isGuest()) {
								device.put("CanCap", true);
								device.put("CanRec", true);
							} else {
								Set<Operation> operations = videoManager.getVideoService().getAuthorizedOperations(indexCode, userId);
								for (Operation operation : operations) {
									if (operation.getName().equalsIgnoreCase(Operation.CAPTURE)) {
										device.put("CanCap", true);
									}
									if (operation.getName().equalsIgnoreCase(Operation.RECORD)) {
										device.put("CanRec", true);
									}
								}
							}
							device.put("CanOpt", true);
						}
					}
				}
				param.put("operid", userId);
				return JSON.toJSONString(param, SerializerFeature.UseSingleQuotes);
			}
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * ?????????????????????
	 *
	 * @return
	 */
	@RequestMapping(value = "/fetch/root/groups")
	@ResponseBody
	public List<CameraRegion> getRootGroups(@RequestParam(defaultValue = "", required = false) String loginName) {
		try {
			cn.gtmap.onemap.security.User user = SecHelper.getUser();
			if ((isNull(user) || SecHelper.isAdmin() || SecHelper.isGuest()) && loginName.isEmpty()) {
				List<CameraRegion> cameraRegions = insightService.getCameraRegions(insightClient, null);
				return cameraRegions;
			} else {
				List<CameraRegion> cameraRegions = insightService.getCameraRegions(insightClient, user.getId());
				return cameraRegions;
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * @?????? ?????????
	 * @???????????? 2019/7/9
	 * @???????????? 9:59
	 * @?????? ?????? ???insight??????????????????????????????????????????
	 */
	private List<CameraRegion> getCameraRegionGroupData() {
		List<CameraRegion> cameraRegions = new ArrayList<>();
		try {
			CameraGroupResponse cameraGroupResponse = null;
			CameraGroupRequest cameraGroupRequest = new CameraGroupRequest();

			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "all");
			map.put("userId", "");

			cameraGroupRequest.setJsonParam(JSON.toJSONString(map));
			cameraGroupResponse = insightClient.execute(cameraGroupRequest);
			Ztree ztree = cameraGroupResponse.getResult();
			System.out.println(JSON.toJSONString(ztree));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cameraRegions;
	}

	/**
	 * ????????????????????????????????????
	 *
	 * @param proId
	 * @return
	 */
	@RequestMapping("/ref/cameras")
	@ResponseBody
	public Object getRefCameras(@RequestParam String proId) {
		return projectService.getRefCameras(proId);
	}

	/**
	 * ???????????????
	 *
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/fetch/child/group/{parentId}")
	@ResponseBody
	public List<CameraRegion> getChildRegion(@PathVariable(value = "parentId") String parentId) {
		return videoMetadataService.queryRegionByParent(parentId);
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param regionName
	 * @return
	 */
	@RequestMapping(value = "/fetch/region/cameras")
	@ResponseBody
	public List<Camera> getCameraByRegion(@RequestParam String regionName) {
		return videoMetadataService.queryCameraByRegion(regionName);
	}

	@RequestMapping(value = "/query/indexcode/{indexcode}")
	@ResponseBody
	public Camera findByIndexCode(@PathVariable(value = "indexcode") String indexCode) {
		return videoMetadataService.getByIndexCode(indexCode);
	}

	/**
	 * ??????camera
	 *
	 * @param nameOrIndexCode
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "/fetch/condition", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Page<Camera> getSearchCameras(@RequestParam(value = "condition") String nameOrIndexCode,
	                                     @RequestParam(defaultValue = "0", required = false) int page,
	                                     @RequestParam(defaultValue = "10", required = false) int size) {
		return videoMetadataService.findByNameLikeOrIndexCode(nameOrIndexCode, page, size);
	}


	/**
	 * ????????????camera
	 *
	 * @param page
	 * @param size
	 * @param orderDirection
	 * @param orderField
	 * @return
	 */
	@RequestMapping(value = "/fetch/page")
	@ResponseBody
	public Page<Camera> getCameraPage(@RequestParam(defaultValue = "0", required = false) int page,
	                                  @RequestParam(defaultValue = "10", required = false) int size,
	                                  @RequestParam(defaultValue = "desc", required = false) String orderDirection,
	                                  @RequestParam(defaultValue = "createAt", required = false) String orderField) {
		try {
			return videoMetadataService.getPage(new PageRequest(page, size, Sort.Direction.valueOf(orderDirection.toUpperCase()), orderField));
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ????????????camera
	 *
	 * @param page
	 * @param size
	 * @param orderDirection
	 * @param orderField
	 * @return
	 */
	@RequestMapping(value = "/config/region/page")
	@ResponseBody
	public Page<Camera> getCameraConfigPage(@RequestParam(defaultValue = "1", required = false) int page,
	                                        @RequestParam(defaultValue = "10", required = false) int size,
	                                        @RequestParam(defaultValue = "", required = false) String regions,
	                                        @RequestParam(defaultValue = "", required = false) String condition,
	                                        @RequestParam(defaultValue = "desc", required = false) String orderDirection,
	                                        @RequestParam(defaultValue = "createAt", required = false) String orderField) {
		try {
			if (StringUtils.isNotBlank(regions)) {
				//string->arr->hashmap
				String[] regionStrs = regions.split("\\|");
				Set<String> regionSet = new HashSet<String>();
				//??????????????????????????????
				Collections.addAll(regionSet, regionStrs);
	            /*for (int i = 0; i < regionStrs.length; i++) {
                    regionSet.add(regionStrs[i]);
                }*/
				return videoMetadataService.queryCameraByRegions(regionSet, new PageRequest(page - 1, size, Sort.Direction.valueOf(orderDirection.toUpperCase()), orderField));
			} else if (StringUtils.isNotBlank(condition)) {
				return videoMetadataService.findByNameLikeOrIndexCode(condition, page - 1, size);
			}
			Page<Camera> resultSet = videoMetadataService.getPage(new PageRequest(page - 1, size, Sort.Direction.valueOf(orderDirection.toUpperCase()), orderField));
			return resultSet;
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	@RequestMapping(value = "/getOverlayCamera")
	@ResponseBody
	public Map getOverlayCamera(
			@RequestParam(required = false) String where,
			@RequestParam String layerName
	) throws IOException {
		Map result = new HashMap();
		try {
			List<String> data = videoMetadataService.getOverlayCamera(layerName, where, true);
			result.put("data", data);
		} catch (Exception er) {
			result.put("error", er.getMessage());
		}
		return result;
	}


	/**
	 * ????????????camera
	 *
	 * @param page
	 * @param size
	 * @param orderDirection
	 * @param orderField
	 * @return
	 */
	@RequestMapping(value = "/fetch/all")
	@ResponseBody
	public List<Camera> getCameraAll(@RequestParam(defaultValue = "0", required = false) int page,
	                                 @RequestParam(defaultValue = "10", required = false) int size,
	                                 @RequestParam(defaultValue = "desc", required = false) String orderDirection,
	                                 @RequestParam(defaultValue = "createAt", required = false) String orderField,
	                                 @RequestParam(defaultValue = "", required = false) String loginName) {
		try {
			List<Camera> result;
			if (loginName.isEmpty()) {
				result = insightService.getAllCameras(insightClient, null);
			} else {
				result = insightService.getAllCameras(insightClient, null);
			}
			return result;
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}


	/**
	 * ??????geometry???????????????????????????
	 *
	 * @param geometry   geojson
	 * @param bufferSize ??????distance ?????????(??????????????????)
	 * @return
	 */
	@RequestMapping(value = "/fetch/poi")
	@ResponseBody
	public List<Camera> getPoiCameras(
			@RequestParam(value = "geometry") String geometry,
			@RequestParam(value = "bufferSize", defaultValue = "2000", required = false) double bufferSize,
			HttpServletResponse res) {
		res.setHeader(Constant.ACCESS_CONTROL_ALLOW_ORIGN, "*");
		return videoMetadataService.getByGeo(geometry, bufferSize);
	}

	/**
	 * ?????????????????????
	 *
	 * @param indexCode ????????????????????????????????????????????????????????????
	 * @return eg.[{indexCode:xx,viewString:""}]
	 */
	@RequestMapping(value = "/camera/view/scope")
	@ResponseBody
	public List getCameraViewScope(@RequestParam(value = "indexCode") String indexCode,
	                               @RequestParam(value = "platform", required = false, defaultValue = "") String platform) {
		try {
			List result = videoManager.getVideoService(platform).getCameraView(indexCode);
			return result;
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}

	}

	/**
	 * camera set with ptz
	 *
	 * @param indexCode
	 * @param point
	 * @return
	 */
	@RequestMapping(value = "/camera/ptz")
	@ResponseBody
	public Map setPtz(@RequestParam String indexCode,
	                  @RequestParam String point,
	                  @RequestParam(required = false, defaultValue = "") String platform,
	                  HttpServletRequest request) {
		try {
			Map paramMap = request.getParameterMap();
			logger.debug("????????????ptz???indexCode???{},platform???{},userName???{}", indexCode, platform, request.getParameter("username"));
			if (paramMap.containsKey("username")) {
				Map tmp = new HashMap();
				tmp.put("username", request.getParameter("username"));
				tmp.put("password", request.getParameter("password"));
				tmp.put("server", request.getParameter("wsuServer"));
				videoManager.getVideoService(platform.toLowerCase()).setPTZ(indexCode, (Point) geometryService.readUnTypeGeoJSON(point), tmp);
			} else {
				videoManager.getVideoService(platform.toLowerCase()).setPTZ(indexCode, (Point) geometryService.readUnTypeGeoJSON(point));
			}
			return result(true);
		} catch (Exception e) {
			logger.error(getMessage("set.ptz.error", indexCode, point, e.getLocalizedMessage()));
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ??????
	 *
	 * @param indexCode
	 * @param p
	 * @param t
	 * @param z
	 * @return
	 */
	@RequestMapping(value = "/camera/ptz2")
	@ResponseBody
	public Map setPtz2(@RequestParam(value = "indexCode") String indexCode,
	                   @RequestParam(value = "p") String p,
	                   @RequestParam(value = "t") String t,
	                   @RequestParam(value = "z") String z) {
		try {
			videoManager.getVideoService().setPTZ(indexCode, p, t, z);
			return result(true);
		} catch (Exception e) {
			throw new JSONMessageException(e.getMessage());
		}
	}

	/**
	 * ??????indexcode ??????ptz??????
	 *
	 * @param indexCode
	 * @return ptz??????
	 */
	@RequestMapping(value = "/ptz/{indexCode}")
	@ResponseBody
	public Ptz getPtz(@PathVariable(value = "indexCode") String indexCode) {
		try {
			return videoManager.getVideoService().getPTZ(indexCode);
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ???????????????ptz??????
	 *
	 * @param vcuId
	 * @param indexCode
	 * @param p
	 * @param t
	 * @param z
	 * @return ???????????????id ???????????????id??????????????????????????????????????????
	 */
	@RequestMapping(value = "/capture")
	@ResponseBody
	public String capturePtz(@RequestParam(value = "vcuId") String vcuId,
	                         @RequestParam(value = "indexCode") String indexCode,
	                         @RequestParam(value = "p", required = false) String p,
	                         @RequestParam(value = "t", required = false) String t,
	                         @RequestParam(value = "z", required = false) String z) {

		try {
			if (StringUtils.isNotBlank(p) && StringUtils.isNotBlank(t) && StringUtils.isNotBlank(z)) {
				videoManager.getVideoService().setPTZ(indexCode, p, t, z);
			}
			return videoManager.capture(vcuId, indexCode, null);
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ???????????????????????? ????????????
	 *
	 * @param proId
	 * @param indexCode
	 * @param preset
	 * @return
	 */
	@RequestMapping(value = "/capture/preset")
	@ResponseBody
	public String capturePreset(@RequestParam String proId,
	                            @RequestParam String indexCode,
	                            @RequestParam int preset) {
		try {
			return videoManager.captureWithPreset(preset, indexCode, proId);
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ???????????????????????????
	 *
	 * @param guid
	 * @param file
	 */
	@RequestMapping(value = "/auto/shot/receive", method = RequestMethod.POST)
	@ResponseBody
	public void autoShotImg(@RequestParam(value = "guid") String guid,
	                        @RequestParam(value = "file") MultipartFile file,
	                        @RequestParam(required = false) String indexCode) {
		try {
			logger.info("????????????????????????????????????guid???{}???,", guid);
			Cache.ValueWrapper valueWrapper = cacheManager.getCache(CACHE_CAPTURE).get(guid);
			CameraAutoShot cameraAutoShot = null;
			if (isNotNull(valueWrapper) && null != file) {
				String id = String.valueOf(valueWrapper.get());
				cameraAutoShot = cameraAutoShotService.findById(id);
				String fileName = file.getOriginalFilename();
				try {
					fileName = fileStoreService.parseMessyName(file.getOriginalFilename());
				} catch (Exception e) {
					fileName = guid + ".jpg";
				}
				File imgFile = fileStoreService.createNewFile(fileName);
				if (!imgFile.exists()) {
					imgFile = fileStoreService.createNewFile(guid + ".jpg");
				}
				try {
					file.transferTo(imgFile);
					logger.info("???????????????????????????{}", guid);
					if (isNotNull(cameraAutoShot)) {
						FileStore fileStore = fileStoreService.saveWithThumb(imgFile, cameraAutoShot.getId());
						logger.info("?????????????????????{}", guid);
						cameraAutoShot.setImgId(fileStore.getId());
						cameraAutoShotService.save(cameraAutoShot);
					} else {
						logger.info("????????????????????????:cameraAutoShot_Id" + id);
					}
				} catch (Exception ex) {
					logger.error("????????????????????????:{}", ex.getLocalizedMessage());
				}
			} else {
				logger.info("?????????????????????????????????????????????{}?????????" + guid);
			}
		} catch (Exception e) {
			logger.info("???????????????????????????");
			logger.error(e.getMessage());
		}
	}

	/**
	 * ???????????????????????????
	 *
	 * @param guid
	 * @param file
	 */
	@RequestMapping(value = "/capture/receive", method = RequestMethod.POST)
	@ResponseBody
	public void captureImg(@RequestParam(value = "guid") String guid,
	                       @RequestParam(value = "file") MultipartFile file,
	                       @RequestParam(required = false) String indexCode) {
		try {
			logger.info("??????????????????,??????taskid: {}" + guid);
			Cache.ValueWrapper valueWrapper = cacheManager.getCache(CACHE_CAPTURE).get(guid);
			Project project = null;
			if (isNotNull(valueWrapper)) {
				String proId = String.valueOf(valueWrapper.get());
				logger.info("??????????????????: {}" + proId);
				project = projectService.getByProid(proId);
			} else {
				logger.warn("?????????????????????");
			}
			String fileName = fileStoreService.parseMessyName(file.getOriginalFilename());
			logger.info("???????????????: {}", fileName);
			File imgFile = null;
			try {
				imgFile = fileStoreService.createNewFile(fileName);
			} catch (Exception er) {
				logger.error("create file error {}", er.getMessage());
				imgFile = fileStoreService.createNewFile(guid + ".jpg");
			}

			if (!imgFile.exists()) {
				imgFile = fileStoreService.createNewFile(guid + ".jpg");
			}
			try {
				file.transferTo(imgFile);
				logger.info("");
				logger.info("????????????,");
				if (isNotNull(project)) {
					FileStore fileStore = fileStoreService.saveWithThumb(imgFile, project.getProId());
					logger.info("???????????????id???: {}", fileStore.getId());
					logger.info("?????? {} ????????????????????????!", project.getProId());
					logger.info("??????????????????...");
					logger.info("intelligent.recognition.analysis??????:{}", AppConfig.getBooleanProperty("intelligent.recognition.analysis"));
					if (AppConfig.getBooleanProperty("intelligent.recognition.analysis", false)) {
						// ????????????????????????
						videoRecognizeService.execute(fileStore);
					}
				} else if (isNotNull(indexCode)) {
					fileStoreService.save3(imgFile, StringUtils.substring(guid, 0, 31));
				}
			} catch (Exception ex) {
				logger.error("????????????????????????:{}", ex.getLocalizedMessage());
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}


	/**
	 * ???????????????
	 *
	 * @param indexCode
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/panorama/view")
	public String panoramaView(@RequestParam(value = "indexCode") String indexCode, Model model) {
		Camera camera = videoMetadataService.getByIndexCode(indexCode);
		model.addAttribute("today", DateTime.now().toString(Constant.DEFAULT_DATE_FORMATE));
		model.addAttribute("indexCode", indexCode);
		if (camera != null) {
			model.addAttribute("cameraName", camera.getName());
		} else {
			model.addAttribute("cameraName", "NoName");
		}
		return "/video/panorama";
	}

	/**
	 * ??????????????????
	 *
	 * @param indexCode
	 * @param date
	 * @param response
	 */
	@RequestMapping(value = "/panorama/fetch")
	@ResponseBody
	public void panoramaImage(@RequestParam(value = "indexCode") String indexCode,
	                          @RequestParam(value = "date") Date date,
	                          HttpServletResponse response) {
		try {
			CameraPanorama cameraPanorama = panoramaService.get(indexCode, date);
			sendFile(panoramaService.getFile(cameraPanorama.getId()), response);
		} catch (IOException e) {
			throw new JSONMessageException(getMessage("panorama.get.error", indexCode, date.toString(), e.getLocalizedMessage()));
		}
	}

	/**
	 * ???????????????????????????
	 *
	 * @param guid
	 * @param file
	 */
	@RequestMapping(value = "/panorama/receive", method = RequestMethod.POST)
	@ResponseBody
	public void panoramicReceive(@RequestParam(value = "guid") String guid,
	                             @RequestParam(value = "file") MultipartFile file) {
		try {
			logger.info("??????????????????,??????:" + guid);
			String indexCode = String.valueOf(cacheManager.getCache(CACHE_PANORAMA).get(guid).get());
			String fileName = fileStoreService.parseMessyName(file.getOriginalFilename());
			//?????????????????????????????? ????????????????????? ???????????????????????????????????????????????????
			String tmp = fileName.split("_")[1] + fileName.split("_")[2];
			DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMddHHmmss");
			DateTime dateTime = DateTime.parse(tmp.substring(0, tmp.indexOf("(")), format);

			File imgFile = fileStoreService.createNewFile(fileName);
			try (FileOutputStream output = new FileOutputStream(imgFile);) {
				IOUtils.copyLarge(file.getInputStream(), output, 0, file.getInputStream().available(), new byte[file.getInputStream().available()]);
				output.close();
				fileStoreService.saveWithCreateTime(imgFile, indexCode, dateTime.toDate());
			} catch (Exception ex) {
				logger.error("??????????????????????????????:" + ex.getLocalizedMessage());
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	/**
	 * ???????????????
	 *
	 * @return
	 */
	@RequestMapping(value = "/preset/insert")
	@ResponseBody
	public Map insertPreset(@RequestParam String id, @RequestParam String proId, @RequestParam String indexCode) {
		return result(presetService.insert(id, proId, indexCode));
	}

	/**
	 * ?????????????????????
	 *
	 * @param proId
	 * @param indexCode
	 * @return
	 */
	@RequestMapping(value = "/panorama/preset/insert")
	@ResponseBody
	public Map insertPanoramaPreset(@RequestParam String proId, @RequestParam String indexCode) {
		Preset preset = presetService.insertPanoramaPreset(proId, indexCode);
		if (preset != null)
			return result(preset);
		else
			return result(false);
	}

	/**
	 * ???????????????
	 *
	 * @param presetId
	 * @return
	 */
	@RequestMapping(value = "/preset/delete")
	@ResponseBody
	public Map deletePreset(@RequestParam String presetId, @RequestParam String proId) {
		return result(presetService.deleteById(presetId, proId));
	}


	/**
	 * ????????????????????????
	 *
	 * @param enabled
	 * @return
	 */
	@RequestMapping("/preset/enabled")
	@ResponseBody
	public Map setPresetUseful(@RequestParam(defaultValue = "true") boolean enabled, @RequestParam String id) {
		try {
			presetService.setEnabled(enabled, id);
		} catch (Exception e) {
			logger.error("????????????????????????????????????{0}", e.getMessage());
			return result(false);
		}
		return result(true);
	}

	/**
	 * ???????????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/preset/isCapture")
	@ResponseBody
	public Map getCaptureIsRunning() {
		boolean flag = false;
		try {
			flag = quartzScheduleManager.isRunning("capture");
		} catch (Exception e) {
			logger.error("????????????????????????????????????????????????????????????{0}", e.getMessage());
		}
		return result(flag);
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/metadata")
	public String cfg(Model model) {
		Dict onLineDict = dictService.getDictByName("camera.onLine");
		Dict offLineDict = dictService.getDictByName("camera.offLine");
		Dict isUseCache = dictService.getDictByName("camera.status");
		String offLine = null;
		String onLine = null;
		if (isUseCache != null && Integer.parseInt(isUseCache.getValue()) != 0) {
			if (onLineDict != null) {
				onLine = onLineDict.getValue();
			}
			if (offLineDict != null) {
				offLine = offLineDict.getValue();
			}
		}
		Map result = videoManager.getVideoService().getCamerasCountByRegID(null);
		if (onLine != null && !onLine.isEmpty()) {
			result.put("onLine", onLine);
		}
		if (offLine != null && !offLine.isEmpty()) {
			result.put("offLine", offLine);
		}
		result.put("cameraStatus", result);
		model.addAttribute("currentStatus", result);
		return "video/metadata";
	}

	/**
	 * ?????????
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/cameraView")
	public String cameraView(Model model) {
		return "video/cameraView";
	}

	/**
	 * * ???????????????????????????
	 * ?????????????????????????????????
	 *
	 * @param useCache -1???????????? 1?????? 0?????????
	 */
	@RequestMapping("/useCameraCache")
	@ResponseBody
	public Map cameraCountCache(@RequestParam(required = false, defaultValue = "-1") int useCache) {
		Map result = new HashMap();
		Dict statusD = dictService.getDictByName("camera.status");
		//??????
		if (useCache == -1) {
			if (statusD == null) {
				//???????????????
				result.put("status", "0");
			} else {
				result.put("status", statusD.getValue());
			}
			return result;
		}

		if (statusD == null) {
			statusD = new Dict();
			statusD.setName("camera.status");
		}
		statusD.setValue(Integer.toString(useCache));
		dictService.saveDict(statusD);
		result.put("sucess", true);
		return result;
	}


	/**
	 * get regions by name
	 *
	 * @param regionName
	 * @return
	 */
	@RequestMapping("/metadata/regions")
	@ResponseBody
	public List regions(@RequestParam String regionName) {
		return videoMetadataService.getRegions(regionName);
	}

	/**
	 * ???????????????????????? ?????????????????????????????????
	 *
	 * @param layerUrl
	 * @return
	 */
	@RequestMapping("/metadata/import/layer")
	@ResponseBody
	public Map cfgImportLayer(@RequestParam(value = "layerUrl") String layerUrl) {
		try {
			String pt = (String) AppPropertyUtils.getAppEnv("video.platform");
			String reqUrl = layerUrl.concat("/query?where=1=1&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=json");
			videoMetadataService.importConfig(reqUrl, pt);
			return result(true);
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ??????Excel?????????????????????????????? ?????????????????????????????????
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/metadata/import/file")
	@ResponseBody
	public Map cfgImportFile(MultipartHttpServletRequest request) {
		try {
			videoMetadataService.importConfigByFile(request);
			return result(true);
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ??????????????????
	 *
	 * @param response
	 */
	@RequestMapping(value = "/metadata/export")
	public void metadataExport(HttpServletResponse response) {
		try {
			List<Camera> cameras = videoMetadataService.getAll();
			Workbook workbook = documentService.createVideoExcel(cameras);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("?????????????????????.xls", Constant.UTF_8));
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			throw new JSONMessageException(getMessage("doc.excel.export.error", e.getLocalizedMessage()));
		}
	}

	/**
	 * ??????????????????????????????
	 */
	@RequestMapping("/metadata/reload")
	@ResponseBody
	public void cfgReload() {
//        videoManager.getVideoService().reloadConfig();
	}

	/**
	 * ?????????????????????
	 *
	 * @param id
	 */
	@RequestMapping(value = "/metadata/region/delete")
	@ResponseBody
	public void deleteRegion(@RequestParam String id) {
		try {
			Assert.notNull(id, getMessage("id.notnull"));
			videoMetadataService.deleteRegion(id);
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ?????????????????????
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/metadata/region/fetch")
	@ResponseBody
	public Map fetchRegion(@RequestParam String id) {
		try {
			Assert.notNull(id, getMessage("id.notnull"));
			return videoMetadataService.getRegion(id);
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param id
	 * @param regionId
	 * @return
	 */
	@RequestMapping(value = "/metadata/device/fetch")
	@ResponseBody
	public Camera fetchDevice(@RequestParam String id, @RequestParam String regionId) {
		try {
			Assert.notNull(id, getMessage("id.notnull"));
			return videoMetadataService.getByIndexCode(id);
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}


	/**
	 * update??????????????????
	 *
	 * @param camera
	 * @return
	 */
	@RequestMapping(value = "/metadata/update")
	@ResponseBody
	public Camera UpdateDevice(@RequestParam String camera) {
		try {
			return videoMetadataService.saveCamera(JSON.parseObject(camera, Camera.class));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * update??????????????????
	 *
	 * @param camera
	 * @return
	 */
	@RequestMapping(value = "/metadata/delete")
	@ResponseBody
	public String deleteDevice(@RequestParam String camera) {
		try {
			videoMetadataService.deleteDevice(camera);
			return "success";
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * update unit name
	 *
	 * @param unitName
	 * @return
	 */
	@RequestMapping(value = "/metadata/unit/update")
	@ResponseBody
	public String UpdateUnitName(@RequestParam String unitName) {
		try {
			Assert.notNull(unitName);
			return videoMetadataService.updateUnitName(unitName);
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/log")
	public String cameraLog(Model model) {
		List<CameraRegion> cameraRegions = videoMetadataService.queryAllRegion();
		model.addAttribute("regions", cameraRegions);
		return "/video/log";
	}

	/**
	 * ????????????
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping("/log/save")
	@ResponseBody
	public Map saveLog(HttpServletRequest request) {
		try {
			Map map = HttpRequestUtils.getRequestValueMap(request);
			if (map.containsKey("loginUserName")) {
				String loginUserName = map.get("loginUserName").toString();
				logger.info("***????????????{}??????***", loginUserName);
				PfUserVo user = sysUserService.getUserByloginName(loginUserName);
				if (user == null) {
					logger.info("***???????????????{}????????????", loginUserName);
				} else {
					logger.error("loginUserId:" + user.getUserId());
					map.put("loginUserId", user.getUserId());
				}
				logger.info("***??????????????????***");
			}
			CameraLog cameraLog = CameraLog.fromHashMap(map);
			cameraLoggerService.save(cameraLog);
			return result(true);
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping("/log/opt")
	@ResponseBody
	public Map optLog(/*@RequestParam(value = "vcuid", required = true)*/ String vcuid,
	                  @RequestParam(value = "token") String token,
	                  @RequestParam(value = "operation") int operation) {
		try {
			logger.info("vcu_id???{},token???{},operation???{}", vcuid, token, operation);
			String[] tokens = token.split(TOKEN_KEY);
			try {
				if (operation != Constant.VideoOptType.HEARTBEAT.getType()) {
					//???????????????
					cameraLoggerService.saveOptLog(vcuid, tokens[0], operation, token);
				} else {
					//????????????
					Object content = cacheManager.getCache(CACHE_TOKEN).get(token).get();
					logger.info("?????????????????????" + content.toString());
					Map param = JSON.parseObject((String) content, Map.class);
					List<Map> vculist = (List<Map>) param.get("vculist");
					for (Map vcu : vculist) {
						try {
							String deviceId = (String) ((Map) ((JSONArray) vcu.get("devicelist")).get(0)).get("DeviceId");
							cameraLoggerService.saveOptLog(deviceId, tokens[0], operation, token);
						} catch (Exception er) {
							logger.info("?????????????????????????????????{}", er.getMessage());
						}
					}
				}
			} catch (Exception e) {
				//e.printStackTrace();
				logger.info("?????????????????????????????????{}", e.getMessage());
			}

			String callbackUrlStr = AppConfig.getProperty("video.callback.url");
			if (StringUtils.isNotBlank(callbackUrlStr)) {
				String[] callbackUrls = callbackUrlStr.split(";");
				for (String callbackUrl : callbackUrls) {
					Map tokenMap = new HashMap();
					tokenMap.put("token", token);

					Map params = new HashMap();
					params.put("json", JSON.toJSONString(tokenMap));
					try {
						Object responseObj = HttpRequest.post(callbackUrl, params, null);
						logger.info("??????????????????" + callbackUrl + "?????????", responseObj);
					} catch (Exception e) {
						//e.printStackTrace();
						logger.error("??????????????????" + callbackUrl + "?????????", e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			logger.error(getMessage("save.video.opt.log"), e.getMessage());
			return result(false);
		}
		return result(true);
	}

	/**
	 * ????????????????????????
	 *
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("/log/search")
	@ResponseBody
	public Map searchLogs(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
	                      @RequestParam(defaultValue = "{}") String condition) {
		Map conditions = JSONObject.parseObject(condition, Map.class);
		Page<CameraLog> cameraLogs;
		try {
			cameraLogs = cameraLoggerService.search(conditions, page - 1, size);
			if (cameraLogs != null && cameraLogs.getContent().size() > 0) {
				for (int i = 0; i < cameraLogs.getSize(); i++) {
					CameraLog log = cameraLogs.getContent().get(i);
					Camera camera = null;
					if (log != null) {
						camera = videoMetadataService.getByIndexCode(log.getCameraId());
						if (camera != null) {
							camera.setRegionName(camera.getRegionName());
						}
					}
				}
			}
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
		return result(cameraLogs);
	}


	@RequestMapping("/log/download/{id}")
	public void downloadDeviveInfo(@PathVariable(value = "id") String id, HttpServletResponse response) {
		CameraLog cameraLog = cameraLoggerService.getById(id);
		if (cameraLog != null) {
			try {
				sendFile(IOUtils.toInputStream(cameraLog.getRemark()), response, "??????xml??????.xml");
			} catch (IOException e) {
				logger.error("?????????" + e);
			}
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/statistic")
	public String cameraUseLedger(Model model) {
		model.addAttribute("userId", SecHelper.getUserId());
		return "/video/statistic";
	}

	/**
	 * ????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/statistic/detail")
	@ResponseBody
	public Map getCameraUseInfo(@RequestParam(required = true, defaultValue = "{}") String data) {
		return cameraLoggerService.showUseCameraInfo(JSONObject.parseObject(data, Map.class));
	}

	/**
	 * ???????????????????????????
	 *
	 * @param condition
	 * @param response
	 */
	@RequestMapping("/log/export")
	@ResponseBody
	public void exportLogs(@RequestParam(defaultValue = "{}") String condition, HttpServletResponse response) {
		Map conditions = JSONObject.parseObject(condition, Map.class);
		try {
			List<CameraLog> list = cameraLoggerService.export(conditions);
			Workbook workbook = cameraLoggerService.getExportExcel(list);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("?????????????????????.xls", Constant.UTF_8));
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			throw new JSONMessageException(e.getLocalizedMessage());
		}
	}


	/**
	 * ??????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/statistic/detail/export")
	@ResponseBody
	public void getCameraUseInfoExport(@RequestParam String data, HttpServletResponse response) {
		try {
			Map dataMap = JSONObject.parseObject(data, Map.class);
			Map map = cameraLoggerService.showUseCameraInfo(JSONObject.parseObject(data, Map.class));
			String title = MapUtils.getString(dataMap, "camera", "").concat(MapUtils.getString(dataMap, "user", ""));
			Workbook workbook = cameraLoggerService.getExportStatisticExcel(map, title);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("?????????????????????.xls", Constant.UTF_8));
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	/**
	 * ??????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/statistic/detail/exportByOrgan")
	@ResponseBody
	public void exportCameraLogByOrgan(@RequestParam String data, HttpServletResponse response) {
		try {
			Map dataMap = JSONObject.parseObject(data, Map.class);
			Map map = cameraLoggerService.getCameraLogDetail(JSONObject.parseObject(data, Map.class));
			//String title = MapUtils.getString(dataMap, "camera", "").concat(MapUtils.getString(dataMap, "user", ""));
			logger.info("????????????excel??????");
			Workbook workbook = cameraLoggerService.getAllExportGroupByOrgan(map);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("?????????????????????.xls", Constant.UTF_8));
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}


	/**
	 * ??????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/statistic/detail/exportAll")
	@ResponseBody
	public void getCameraUseInfoExportAll(@RequestParam String data, HttpServletResponse response) {
		try {
			List<Map> list = cameraLoggerService.showAllUseCameraInfo(JSONObject.parseObject(data, Map.class));
			Workbook workbook = cameraLoggerService.getAlLExportAllStatisticExcel(list);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("?????????????????????.xls", Constant.UTF_8));
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}


	/**
	 * ?????????????????????????????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/statistic/fetch")
	@ResponseBody
	public Map getCameraOrUser(@RequestParam(defaultValue = "") String type,
	                           @RequestParam(required = false) String start,
	                           @RequestParam(required = false) String end) {
		try {
			return result(cameraLoggerService.getDistinctUserOrCamera(type, start, end));
		} catch (Exception e) {
			logger.error("????????????????????????????????????????????????????????????????????????{0}???", e.getMessage());
			return result(false);
		}
	}

	/**
	 * ??????proid????????????
	 *
	 * @param proId
	 * @return
	 */
	@RequestMapping("/cameras/fetch/{proId}")
	@ResponseBody
	public List<Camera> getCamerasByProId(@PathVariable(value = "proId") String proId) {
		List<Camera> list = videoManager.getVideoService().getCamerasByProId(proId);
		return list;
	}

	/**
	 * ????????????????????????
	 *
	 * @param platName ????????? ??????????????? ????????????
	 * @return
	 */
	@RequestMapping("/fetch/plat")
	@ResponseBody
	public VideoPlats.Plat getPlat(@RequestParam(value = "platName") String platName) {
		return videoManager.getPlat(platName);
	}

	/**
	 * mp4 player
	 *
	 * @param fileId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mp4player")
	public String tools(@RequestParam(value = "fileId") String fileId,
	                    Model model) {
		model.addAttribute("fileId", fileId);
		return "video/mp4Player";
	}


	/**
	 * ????????????
	 *
	 * @param requestUrl
	 * @param warnDays
	 * @param warnRegion
	 * @param start
	 * @param end
	 * @param beginIndex
	 * @param loadSize
	 * @return
	 */
	@RequestMapping(value = "/queryWarnings")
	@ResponseBody
	public List queryWarning(@RequestParam(value = "requestUrl") String requestUrl,
	                         @RequestParam(value = "warnDays", required = false, defaultValue = "30") int warnDays,
	                         @RequestParam(value = "warnRegion", required = false) String warnRegion,
	                         @RequestParam(value = "start", required = false) String start,
	                         @RequestParam(value = "end", required = false) String end,
	                         @RequestParam(value = "beginIndex", defaultValue = "0", required = false) int beginIndex,
	                         @RequestParam(value = "loadSize", defaultValue = "999999", required = false) int loadSize) {
		requestUrl = requestUrl + "/api/alarm/query";
		Date date = new Date();
		String nowTime = DateFormatUtils.format(date, "yyyy-MM-dd+hh:mm:ss");
		String startTime = DateFormatUtils.format(DateUtils.addDays(date, 0 - warnDays), "yyyy-MM-dd+hh:mm:ss");
		if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
			nowTime = end;
			startTime = start;
		}
		//List<String> indexCodes = videoService.getIndexCodesByXzq(warnRegion);
		//indexCodes = videoService.getIndexCodesByRegion("?????????");
		List warns = new ArrayList();

		String json = "{\"alarmType\":\"ALARM_MOTION_OTHER\",\"resultCode\":\"1\",\"alarmList\":[{\"id\":1474161,\"cameraId\":\"021200519260101\",\"cameraName\":\"?????????\",\"ptzId\":1,\"ptzName\":\"set001\",\"eventTime\":\"2019-04-16 11:06:09\",\"eventType\":\"2\",\"eventName\":\"????????????\",\"manualTag\":1},{\"id\":1473037,\"cameraId\":\"021200519260101\",\"cameraName\":\"?????????\",\"ptzId\":1,\"ptzName\":\"set001\",\"eventTime\":\"2019-04-15 11:06:10\",\"eventType\":\"2\",\"eventName\":\"????????????\",\"manualTag\":0}],\"totalCount\":2,\"resultDesc\":\"succ\"}";

		for (int i = 1; i < 99; i++) {
			Map map = new HashMap();
			map.put("id", i + "");
			map.put("cameraId", i + "02120051926");
			map.put("cameraName", "????????????" + i);
			map.put("ptzId", "1");
			map.put("ptzName", "set" + 1);
			map.put("eventTime", nowTime);
			map.put("eventType", "2");
			map.put("eventName", "????????????");
			map.put("manualTag", 1);

			warns.add(map);
		}

        /*Map jsonMap = (Map) JSON.parse(json);
        if (jsonMap.containsKey("alarmList")) {
            List<Map> list = (List<Map>) jsonMap.get("alarmList");
            if (list.size() > 0) {
                warns.addAll(list);
            }
        }*/

		return warns;
	}

	/**
	 * @?????? ?????????
	 * @???????????? 2019/4/17
	 * @???????????? 10:43
	 * @?????? ?????? ??????????????????
	 */
	@RequestMapping("/alarm/tag")
	@ResponseBody
	public Object alarmTag(String requestUrl, String camera_id, String event_time, String result) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(requestUrl);
		postMethod.setRequestHeader("token", AppConfig.getProperty("alarm.token"));
		postMethod.setParameter("camera_id", camera_id);
		postMethod.setParameter("event_time", event_time);
		postMethod.setParameter("result", result);
		try {
			Object json = HttpRequest.request(httpClient, postMethod, "json");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/rest/unusedRecently")
	@ResponseBody
	public List unusedIn3Days() {
		return cameraLoggerService.unusedCameraIn3Days();
	}


	/**
	 * ?????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/unusedRecently/export")
	@ResponseBody
	public void unusedIn3Days(@RequestParam String data, HttpServletResponse response) {
		try {
			List list = JSON.parseObject(data, List.class);
			Workbook workbook = documentService.renderUnusedVideoExcel(list);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("?????????????????????.xls", Constant.UTF_8));
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	/**
	 * ???????????????????????????????????????????????????
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/unusedRecently")
	public String unusedIn3Days(Model model) {
		List warning = cameraLoggerService.unusedCameraIn3Days();
		model.addAttribute("unused", warning);
		model.addAttribute("unusedStr", JSON.toJSONString(warning, SerializerFeature.WriteDateUseDateFormat));
		return "video/unusedRecently";
	}

	/**
	 * ????????????
	 *
	 * @param indexCode ?????????????????????
	 * @param hideTp
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bigScreen", method = RequestMethod.GET)
	public String realPlay(@RequestParam String indexCode,
	                       @RequestParam(required = false, defaultValue = "????????????") String cameraName,
	                       @RequestParam(defaultValue = "false") String hideTp,
	                       @RequestParam(required = false) Double height,
	                       @RequestParam(required = false) Double width,
	                       Model model) {
		Camera camera = videoMetadataService.getByIndexCode(indexCode);
		if (isNull(camera)) {
			logger.error("camera of {} not found.", indexCode);
			throw new RuntimeException("????????????????????????!");
		}
		VideoPlats.Plat plat = videoManager.getPlat(camera.getPlatform());
		String username = plat.getUserName();
		String[] strs = username.split("@");
		model.addAttribute("user", strs[0]);
		model.addAttribute("domain", strs[1]);
		model.addAttribute("pwd", plat.getPassword());
		model.addAttribute("server", plat.getServer());
		model.addAttribute("vcuId", camera.getVcuId());
		model.addAttribute("indexCode", indexCode);
		model.addAttribute("cameraName", cameraName);
		model.addAttribute("hideToolPanel", hideTp);
		model.addAttribute("height", height);
		model.addAttribute("width", width);
		return "video/bigScreenRealPlay";
	}

	/**
	 * ????????????
	 *
	 * @param indexCode ?????????????????????
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/realplay", method = RequestMethod.GET)
	public String realPlay(@RequestParam String indexCode,
	                       Model model) {
		Camera camera = videoMetadataService.getByIndexCode(indexCode);
		if (isNull(camera)) {
			logger.error("camera of {} not found.", indexCode);
			throw new RuntimeException("????????????????????????!");
		}
		VideoPlats.Plat plat = videoManager.getPlat(camera.getPlatform());
		model.addAttribute("user", plat.getUserName());
		model.addAttribute("pwd", plat.getPassword());
		model.addAttribute("server", plat.getServer());
		model.addAttribute("port", plat.getPort());
		model.addAttribute("camera", camera);
		return "video/realplay";
	}

	/**
	 * ???????????? video.cfg ?????????????????????????????????
	 *
	 * @param sr
	 * @return
	 */
	@RequestMapping("/fly2db")
	@ResponseBody
	public List<Camera> migrateToDb(@RequestParam String sr, @RequestParam boolean replaceAll) {
		try {
			return videoMetadataService.parseCfgToDb(sr, replaceAll);
		} catch (Exception e) {
			throw new RuntimeException(getMessage("migrate.db.error", e.getLocalizedMessage()));
		}
	}

	/**
	 * ????????????
	 *
	 * @param proId
	 * @return
	 */
	@RequestMapping("/recognize")
	@ResponseBody
	public Recognition doRecognize(@RequestParam String proId) {
		try {
			return videoRecognizeService.execute(fileStoreService.getFileStoreNewest(proId));
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	/**
	 * ???????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/recogs")
	@ResponseBody
	public List getRecognition() {
		try {
			return videoRecognizeService.getAllRecords();
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	/**
	 * ???????????????????????? ????????????
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping("/recogs/origin")
	@ResponseBody
	public List getRecognition2(@RequestParam String ids) {
		try {
			return videoRecognizeService.getRecogsByOrigin(JSON.parseObject(ids, List.class));
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}


	/**
	 * add to whitelist
	 *
	 * @param oid
	 * @return
	 */
	@RequestMapping("/recogs/whitelist/{oid}")
	@ResponseBody
	public Map add2WhiteList(@PathVariable String oid) {
		try {
			return result(videoRecognizeService.add2WhiteList(oid));
		} catch (Exception e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	/**
	 * ?????????????????? (ocx??????)
	 *
	 * @param indexCode
	 * @param cameraName
	 * @param model
	 * @return
	 */
	@RequestMapping("/realplayiframe")
	public String realPlayIframe(@RequestParam String indexCode,
	                             @RequestParam(required = false, defaultValue = "????????????") String cameraName,
	                             Model model) {
		model.addAttribute("indexCode", indexCode);
		model.addAttribute("cameraName", cameraName);
		return "video/realplayiframe";
	}

	/**
	 * stream page
	 *
	 * @param cameraCode
	 * @param domainCode
	 * @param model
	 * @return
	 */
	@RequestMapping("/stream")
	public String streamPlay(@RequestParam String cameraCode,
	                         @RequestParam String domainCode,
	                         Model model) {
		try {
			String liveUrl = fFmpegManagerService.startStream(cameraCode, domainCode);
			model.addAttribute("liveUrl", liveUrl);
			model.addAttribute("cameraCode", cameraCode);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return "/video/streamLive";
	}

	/**
	 * ????????? rest
	 *
	 * @param type       ??????/?????????
	 * @param cameraCode
	 * @param domainCode
	 * @return
	 */
	@RequestMapping("/stream/rest/{type}")
	@ResponseBody
	public Object streamRest(@PathVariable String type,
	                         @RequestParam String cameraCode,
	                         @RequestParam String domainCode) {
		try {
			Constant.StreamHandleType handleType = Constant.StreamHandleType.valueOf(type);
			switch (handleType) {
				case play:
					return fFmpegManagerService.startStream(cameraCode, domainCode);
				case stop:
					return fFmpegManagerService.terminate(cameraCode);
				default:
					break;
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * ivs ??????????????????
	 *
	 * @param cameraCode
	 * @param controlCode
	 * @param param1      1??????????????? 2??????????????? 3?????????
	 * @param param2      (????????????)??????(1-10) / (????????????)??????(1-10)
	 */
	@RequestMapping("/ptz/move")
	@ResponseBody
	public int ptzMoveControl(@RequestParam String cameraCode,
	                          @RequestParam String controlCode,
	                          @RequestParam(required = false, defaultValue = "3") String param1,
	                          @RequestParam(required = false, defaultValue = "") String param2) {
		Camera camera = videoMetadataService.getByIndexCode(cameraCode);
		return videoManager.getVideoService(camera.getPlatform().toLowerCase()).ptzMoveControl(cameraCode, Constant.IVS_PTZ_CODE.valueOf(controlCode.toUpperCase()), param1, param2);
	}

	/**
	 * ??????rtsp??????
	 *
	 * @param cameraId
	 * @return
	 */
	@RequestMapping("/rtsp/url")
	@ResponseBody
	public String getRtspUrl(String cameraId) {
		Camera camera = videoMetadataService.getByIndexCode(cameraId);
		String url = videoManager.getVideoService(camera.getPlatform()).getRtspUrl(cameraId);
		System.out.println(url);
		return url;
	}

	/**
	 * ??????vlc??????rtsp??????
	 *
	 * @return
	 */
	@RequestMapping("/rtsp/play")
	public String rtspPlay(String cameraId, Model model) {
		String url = null;
		try {
			url = getRtspUrl(cameraId);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		model.addAttribute("url", url);
		return "video/rtspPlay";
	}


	/**
	 * ????????????????????????
	 *
	 * @param status
	 * @param fileId ????????????id
	 * @return
	 */
	@RequestMapping("/changeCameraStatus")
	@ResponseBody
	public Map changeCameraRecogStatus(@RequestParam int status,
	                                   @RequestParam String fileId
	) {
		//????????????
		Map result = new HashMap();
		try {
			//??????fileID??????recogID
			videoRecognizeService.updateRecogStatusByFileID(fileId, status);
			result.put("success", "excute success");
		} catch (Exception er) {
			result.put("error", er.getMessage());
		}
		return result;
	}

	/**
	 * ????????????
	 * ??????recog???filstore??????
	 *
	 * @param fileId
	 * @return
	 */
	@RequestMapping("/releaseWarning")
	@ResponseBody
	public Map releaseWarning(@RequestParam String fileId) {
		Map result = new HashMap();
		try {
			Recognition recognition = videoRecognizeService.findByOriginal(fileId);
			videoRecognizeService.deleteRecog(recognition.getId());
			result.put("success", "excute success");
		} catch (Exception er) {
			result.put("error", er.getMessage());
		}
		return result;
	}

	/**
	 * ????????????????????????????????????
	 *
	 * @param regionID
	 * @return
	 */
	@RequestMapping("/getCamerasCountByRegID")
	@ResponseBody
	public Map getCamerasCountByRegID(@RequestParam(required = false, value = "") String regionID) {
		Map result = new HashMap();
		try {
			Dict isUseCache = dictService.getDictByName("camera.status");
			result = videoManager.getVideoService().getCamerasCountByRegID(regionID);
			Map unrealData = videoMetadataService.getCameraCountData();
			if (isUseCache != null && Integer.parseInt(isUseCache.getValue()) != 0) {
				//???result??????????????????
				for (Object regionId : result.keySet()) {
					if (unrealData.get(regionId) != null) {
						//??????????????????????????????
						HashMap dataItem = (HashMap) result.get(regionId);
						int tempOnLine = (int) dataItem.get("regOnLine");
						int tempOffLine = (int) dataItem.get("regOffLine");
						//??????????????????????????????
						int unrealCount = Integer.parseInt((String) unrealData.get(regionId));
						tempOffLine = tempOffLine + (unrealCount - tempOnLine - tempOffLine);
						dataItem.put("regOffLine", tempOffLine);
					}
				}
				//??????????????????
				if (unrealData.get("00") != null) {
					int unrealCount = Integer.parseInt((String) unrealData.get("00"));
					int offLine = (int) result.get("offLine");
					int onLine = (int) result.get("onLine");
					offLine = offLine + (unrealCount - offLine - onLine);
					result.put("offLine", offLine);
				}

			}
		} catch (Exception er) {
			result.put("error", er.getMessage());
		}
		return result;
	}


	@RequestMapping("/getCameraRegionCount")
	@ResponseBody
	public Map getCameraRegionCount() throws IOException {
		Dict isUseCache = dictService.getDictByName("camera.status");
		Map result = videoManager.getVideoService().getRegionCount();
		Map unrealData = videoMetadataService.getCameraCountData();
		if (isUseCache != null && Integer.parseInt(isUseCache.getValue()) != 0) {
			//???result??????????????????
			for (Object regionId : result.keySet()) {
				if (unrealData.get(regionId) != null) {
					//??????????????????????????????
					HashMap dataItem = (HashMap) result.get(regionId);
					int onLine = (int) dataItem.get("regOnLine");
					int offLine = (int) dataItem.get("regOffLine");
					//??????????????????????????????
					int unrealCount = Integer.parseInt((String) unrealData.get(regionId));
					offLine = offLine + (unrealCount - onLine - offLine);
					dataItem.put("regOffLine", offLine);
				}
			}
		}
		return result;
	}

	@RequestMapping("/createFlow")
	@ResponseBody
	public Object createFlow(String indexCode) throws Exception {
		Map<String, File> files = null;
		String postUrl = AppConfig.getProperty("createFlowUrl") + "?";
		Camera camera = videoMetadataService.getByIndexCode(indexCode);
		if (camera == null) {
			throw new Exception("IndexCode???????????????");
		}
		String userName = SecHelper.getUserId();
		Date dt = new Date();
		//???????????????????????????
		String cameraName = camera.getName();
		String regionName = camera.getRegionName();
		//????????????????????????????????????
		Project project = null;
		Set<Project> projectSet = projectService.getByCamera(indexCode);
		if (projectSet != null && projectSet.size() > 0) {
			project = projectSet.iterator().next();
		}
		postUrl = postUrl + "userId=" + userName;
		postUrl = postUrl + "&" + "cameraName=" + cameraName;
		postUrl = postUrl + "&" + "regionName=" + regionName;
		if (project != null) {
			//???????????????????????????
			FileStore fileStore = fileStoreService.getUpdatedImgByProId(project.getProId());
			if (fileStore != null) {
				String url = AppConfig.getProperty("omp.url").concat("/file/original/").concat(fileStore.getId());
				postUrl = postUrl + "&" + "imgUrl=" + url;
			}
		}
		return postUrl;
	}

	@RequestMapping("/findAutoshotImgs")
	@ResponseBody
	public Map findAutoshotImgs(
			@RequestParam(required = false) String cameraId,
			@RequestParam(required = false) String regionNames,
			@RequestParam(required = false) String start,
			@RequestParam(required = false) String end,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "16") int size
	) {
		Map result = new HashMap();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date startTime = format.parse(start);
			Date endTime = format.parse(end);

			Pageable pageable = new PageRequest(page, size);
			//?????????????????????
			Page<CameraAutoShot> data = null;
			if (regionNames != null && !regionNames.equals("")) {
				List<String> cameras = videoService.getIndexCodesByRegion(regionNames);
				data = videoMetadataService.findCameraAutoShot(startTime, endTime, cameras, pageable);
			} else if (cameraId != null && !cameraId.equals("")) {
				//?????????????????????
				List<String> autoShots = new ArrayList<String>();
				autoShots.add(cameraId);
				data = videoMetadataService.findCameraAutoShot(startTime, endTime, autoShots, pageable);
			} else {
				//????????????
				data = videoMetadataService.findCameraAutoShot(startTime, endTime, pageable);
			}
			if (data != null && data.getSize() > 0) {
				for (int i = 0; i < data.getContent().size(); i++) {
					CameraAutoShot shot = data.getContent().get(i);
					String indexCode = shot.getIndexCode();
					Camera camera = videoMetadataService.getByIndexCode(indexCode);
					shot.setCamera(camera);
				}
			}
			result.put("data", data);
		} catch (Exception er) {
			result.put("error", er.getMessage());
		}
		return result;
	}

	/**
	 * ????????????????????????????????????????????????
	 *
	 * @param start
	 * @param end
	 * @return
	 * @author ?????????
	 * 2019???4???15??? ??????11:25:57
	 */
	@RequestMapping("/findPresetProjImgs")
	@ResponseBody
	public List<FileStore> findPresetProjImgs(
			@RequestParam(required = false) String proId,
			@RequestParam(required = true) String start,
			@RequestParam(required = true) String end
	) {
		try {
			if(start.split(" ").length>0){
				start = start.concat(" 00:00:00");
			}
			if(end.split(" ").length>0){
				end = end.concat(" 23:59:59");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = format.parse(start);
			Date endTime = format.parse(end);
			//????????????
			if (!StringUtils.isBlank(proId)) {
				return projectService.getImgRecordsByTimeSpanAndPro(proId, startTime, endTime);
			}
			return projectService.getImgRecordsByTimeSpan(startTime, endTime);
		} catch (Exception er) {
			logger.error("error", er.getMessage());
		}
		return null;
	}

	/**
	 * ????????????????????????????????????????????????????????????
	 *
	 * @param oldNodeId     ?????????id
	 * @param diffXyJsonStr ??????????????? (JsonStr)
	 * @return
	 * @author ?????????
	 * 2019???4???15??? ??????11:25:57
	 */
	@RequestMapping("/overrideProjImg")
	@ResponseBody
	public Map overrideProjImg(HttpSession session,
	                           @RequestParam(required = true) String oldNodeId,
	                           @RequestParam(required = true) String diffXyJsonStr
	) {
		Map result = new HashMap();
		result.put("success", false);
		try {
			if (StringUtils.isBlank(diffXyJsonStr)) {
				return result;
			}
			FileStore oldFileStore = fileStoreService.get(oldNodeId);
			if (oldFileStore != null) {
				//??????????????????
				videoRecognizeService.saveRecogByLwsz(oldFileStore, diffXyJsonStr);
			}
			result.put("success", true);
		} catch (Exception er) {
			logger.error("error", er.getMessage());
			result.put("success", false);
		}
		return result;
	}

	/**
	 * ??????????????????????????????
	 *
	 * @param weekIndex    ?????????
	 * @param yearIndex    ??????
	 * @param isCommonTask ?????????????????????(??????????????????/??????????????????)
	 * @return
	 */
	@RequestMapping("/searchCameraPatrolLog")
	@ResponseBody
	public Map searchCameraPatrolLog(
			@RequestParam(required = false, defaultValue = "0") int weekIndex,
			@RequestParam(required = false, defaultValue = "0") int yearIndex,
			@RequestParam(required = false, defaultValue = "true") boolean isCommonTask
	) {
		Map result = new HashMap();
		Date dt = new Date();
		String regionCode = "";
		if (weekIndex == 0 || yearIndex == 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.setTime(dt);
			weekIndex = calendar.get(Calendar.WEEK_OF_YEAR);
			yearIndex = calendar.get(Calendar.YEAR);
		}
		Calendar firstCalendar = Calendar.getInstance();
		firstCalendar.set(Calendar.YEAR, yearIndex); // 2016???
		firstCalendar.set(Calendar.WEEK_OF_YEAR, weekIndex); // ?????????2016?????????10???
		firstCalendar.set(Calendar.DAY_OF_WEEK, 2); // 1???????????????2???????????????7????????????
		Date start = firstCalendar.getTime();

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(start);
		endCalendar.add(Calendar.DAY_OF_WEEK, 7);
		Date end = endCalendar.getTime();
		List<String> cameraIds;
		try {
			logger.info("***????????????????????????");
			User user = SecHelper.getUser();
			String regionName = "";
			if (user != null) {
				regionCode = user.getRegionCode();
				logger.info("***??????Id?????????{}", user.getId());
				logger.info("***??????regionCode?????????{}", regionCode);
			}
			if (regionCode == null) {
				throw new Exception("?????????regionCode??????");
			}
			CameraRegion region = videoMetadataService.getRegionById(regionCode);
			if (region != null) {
				logger.info("***Name???{}", region.getName());
				result.put("regionName", region.getName());
			} else {
				throw new Exception("?????????region??????");
			}
			String shiju = AppConfig.getProperty("shiju");
			logger.info("***shiju{}", shiju);
			if (shiju.contains(regionCode)) {
				//??????
				result.put("isSJ", true);
			}
			if (isCommonTask) {
				//????????????
				if (user != null) {
					logger.info("??????????????????{}", user.getId());
				}
				cameraIds = videoMetadataService.getCameraIdByRegionsId(regionCode);
			} else {
				//????????????
				if (user != null) {
					logger.info("??????????????????{}", user.getId());
				}
				cameraIds = videoMetadataService.getProCameraIdByRegionsId(regionCode);
			}
			//????????????  ?????????/??????/??????
			logger.info("cameraIds?????????", cameraIds.size());
			List<String> userIds = getOrganUserIds();
			List<Map> data = videoMetadataService.searchCameraLog(start, end, cameraIds, userIds);
			result.put("data", data);
		} catch (Exception er) {
			result.put("error", er.getMessage());
		}
		return result;
	}

	/**
	 * ???????????????????????????????????????????????????
	 *
	 * @param weekIndex
	 * @param years
	 * @param indexCode
	 * @return
	 */
	@RequestMapping("/getCameraDetails")
	@ResponseBody
	public Map getCameraDetails(@RequestParam(required = false, defaultValue = "0") int weekIndex,
	                            @RequestParam(required = false, defaultValue = "0") int years,
	                            @RequestParam(required = true) String indexCode) {
		Date dt = new Date();
		Map result = new HashMap();
		try {
			if (weekIndex == 0 || years == 0) {
				Calendar calendar = Calendar.getInstance();
				calendar.setFirstDayOfWeek(Calendar.MONDAY);
				calendar.setTime(dt);
				weekIndex = calendar.get(Calendar.WEEK_OF_YEAR);
				years = calendar.get(Calendar.YEAR);
			}
			Calendar firstCalendar = Calendar.getInstance();
			firstCalendar.set(Calendar.YEAR, years); // 2016???
			firstCalendar.set(Calendar.WEEK_OF_YEAR, weekIndex); // ?????????2016?????????10???
			firstCalendar.set(Calendar.DAY_OF_WEEK, 2); // 1???????????????2???????????????7????????????
			Date start = firstCalendar.getTime();

			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(start);
			endCalendar.add(Calendar.DAY_OF_WEEK, 7);
			Date end = endCalendar.getTime();

			List<String> userIds = getOrganUserIds();
			//List<String> userIds = new ArrayList<>();
			//userIds.add("1");
			List<Map> data = videoMetadataService.searchCameraLogByIndexCode(start, end, indexCode, userIds);
			result.put("data", data);
		} catch (Exception er) {
			result.put("error", er.getMessage());
		}
		return result;
	}

	//    /**
//     * ??????????????????id
//     * @return
//     * @throws Exception
//     */
	private List<String> getOrganUserIds() throws Exception {
		String userId = SecHelper.getUserId();
		//??????
		List<PfOrganVo> pfOrganVoList = sysUserService.getOrganListByUser(userId);
		String pfName = "";
		if (pfOrganVoList.size() > 0) {
			pfName = pfOrganVoList.get(0).getOrganName();
		} else {
			throw new Exception("?????????????????????????????????");
		}
		List<PfUserVo> users = sysUserService.getUsersByOrganName(pfName);
		List<String> userIds = new ArrayList<String>();
		for (int i = 0; i < users.size(); i++) {
			PfUserVo user = users.get(i);
			userIds.add(user.getUserId());
		}
		return userIds;
	}


	/**
	 * ??????????????????
	 *
	 * @param startDateStr
	 * @param endDateStr
	 * @param showParent   ??????????????????
	 * @return
	 */
	@RequestMapping("/countCameraLog")
	@ResponseBody
	public Map countCameraLog(
			@RequestParam String startDateStr,
			@RequestParam String endDateStr,
			@RequestParam(required = false, defaultValue = "false") boolean showParent
	) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sf.parse(startDateStr);
		Date endDate = sf.parse(endDateStr);
		HashMap result = new HashMap();
		HashMap organConfig = getCameraLogOrganFromConfig();
		if (organConfig != null) {
			//???????????????
			for (Object organName : organConfig.keySet()) {
				String organ = (String) organName;
				String xzq = (String) organConfig.get(organ);
				Map itemData = videoMetadataService.countCameraLog(startDate, endDate, organ, xzq);
				if (itemData == null) {
					continue;
				}
				result.put(organName, itemData);
			}
		} else {
			List<CameraRegion> regions = videoMetadataService.queryRootRegion();
			for (CameraRegion xzq : regions) {
				if (showParent) {
					for (CameraRegion region : xzq.getChildren()) {
						String regionName = region.getName();
						Map itemData = videoMetadataService.countCameraLogST(startDate, endDate, region.getId());
						result.put(regionName, itemData);
					}
				} else {
					Map itemData = videoMetadataService.countCameraLogST(startDate, endDate, xzq.getId());
					result.put(xzq.getName(), itemData);
				}
			}
		}
		return result;
	}

	@RequestMapping("/exportExcel")
	public void exportExcel(@RequestParam String data,
	                        HttpServletResponse response) throws Exception {
		try {
			Map dataMap = JSONObject.parseObject(data, Map.class);
			Map result = cameraLoggerService.getCameraLogDetail(dataMap);
			Workbook workbook = cameraLoggerService.getAllExportGroupByOrgan(result);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("????????????????????????????????????.xls", Constant.UTF_8));
			OutputStream os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			throw new JSONMessageException(getMessage("doc.excel.export.error", e.getLocalizedMessage()));
		}
	}


	/**
	 * ?????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/cameraStatistics")
	public String cameraStatistics() {
		return "video/cameraLogData";
	}

	/**
	 * ????????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/cameraStatisticsCommon")
	public String cameraStatisticsCommon() {
		return "video/cameraLogDataCommon";
	}


	/**
	 * ?????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/cameraStatisticsST")
	public String cameraStatisticsST() {
		return "video/cameraLogDataST";
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping("/cameraRegions")
	@ResponseBody
	public Map cameraRegions() {
		Map results = new HashMap();
		results.put("results", videoMetadataService.queryRootRegion());
		return results;
	}

	/**
	 * ????????????
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/offline")
	public String offlineTrend(Model model) {
		try {
			List<CameraOfflineTrend> trends = cameraOfflineService.getTrendAllTime();
			Map<String, String> cacheMap = new HashMap<String, String>();
			if (trends != null && trends.size() > 0) {
				for (CameraOfflineTrend trend : trends) {
					if (cacheMap.get(trend.getRegion()) == null) {
						String parentName = cameraRegionDao.findParentNameByName(trend.getRegion());
						cacheMap.put(trend.getRegion(), parentName);
						trend.setXzq(parentName);
					} else {
						trend.setXzq(cacheMap.get(trend.getRegion()));
					}
				}
			}
			model.addAttribute("data", JSON.toJSONString(trends));
			model.addAttribute("trends", trends);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getLocalizedMessage());
		}
		return "video/offlineTrend";
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @param type
	 * @return
	 */
	@RequestMapping("/reports/{type}")
	@ResponseBody
	public List<FileStore> getReports(@PathVariable String type) {
		List<FileStore> fileStores = null;
		try {
			if ("weekReport".equals(type)) {
				fileStores = fileStoreService.getFileStores("weekReport");
			} else {
				fileStores = fileStoreService.getFileStores("monthReport");
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		if (fileStores != null && fileStores.size() > 0) {
			for (FileStore fileStore : fileStores) {
				try {
					fileStore.setName(URLDecoder.decode(fileStore.getName(), "UTF-8"));
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage());
				}
			}
		}
		return fileStores;
	}

	private HashMap getCameraLogOrganFromConfig() {
		String organStr = AppConfig.getProperty("cameraLogOrgan");
		if (organStr != null) {
			String[] strList = organStr.split("\\|");
			HashMap result = new HashMap();
			for (String date : strList) {
				String[] dtArr = date.split(",");
				if (dtArr.length >= 2) {
					result.put(dtArr[0], dtArr[1]);
				}
			}
			return result;
		} else {
			return null;
		}

	}


	/**
	 *
	 */
	private Preset getPresetByProId(String proId) {
		List<Preset> presetList = presetService.findByPro(proId);
		if (presetList.size() > 0) {
			return presetList.get(0);
		} else {
			return null;
		}
	}

	/**
	 * ??????????????????
	 * ?????????????????????
	 *
	 * @param type
	 * @param pNo
	 * @return
	 */
	@RequestMapping("/startUpVideo")
	public String startUpVideo(
			@RequestParam(defaultValue = "0") String errorCode,
			@RequestParam(defaultValue = "0") String type,
			@RequestParam(defaultValue = "") String indexCode,
			@RequestParam(defaultValue = "") String pNo,
			@RequestParam(defaultValue = "") String indexCodes,
			@RequestParam(defaultValue = "0") double x,
			@RequestParam(defaultValue = "0") double y,
			Model model
	) {

		List<Camera> cameras = Lists.newArrayList();
		List<Camera> ptzCamera = Lists.newArrayList();
		if (errorCode.equals("1")) {
			model.addAttribute("indexCode", "");
			model.addAttribute("type", type);
			model.addAttribute("pNo", "");
			model.addAttribute("errorCode", errorCode);
			model.addAttribute("errorCode", "1");
			model.addAttribute("indexCodes", indexCodes);
			String camerasStr = JSON.toJSONString(cameras);
			model.addAttribute("cameras", camerasStr);
			return "video/startUpVideo";
		}
		Set<String> indexCodeSet = new HashSet<String>();
		String firstIndexCode = "";
		if (indexCodes.contains(",")) {
			String[] indexArr = indexCodes.split(",");
			for (int i = 0; i < indexArr.length; i++) {
				String curIndexCode = indexArr[i];
				curIndexCode = StringUtils.replace(curIndexCode, " ", "");
				curIndexCode = StringUtils.replace(curIndexCode, "%20", "");
				if (curIndexCode.equals("")) {
					continue;
				}
				if (indexCodeSet.size() == 0) {
					firstIndexCode = curIndexCode;
				}
				indexCodeSet.add(curIndexCode);
			}
			cameras = videoMetadataService.getByIndexCodeIn(indexCodeSet);
			if (cameras == null) {
				cameras = Lists.newArrayList();
			}
			String camerasStr = JSON.toJSONString(cameras);
			model.addAttribute("cameras", camerasStr);
			model.addAttribute("indexCode", firstIndexCode);
		} else {
			cameras = Lists.newArrayList();
			Camera camera = videoMetadataService.getByIndexCode(indexCodes);
			if (camera != null) {
				cameras.add(camera);
			}
			String camerasStr = JSON.toJSONString(cameras);
			model.addAttribute("cameras", camerasStr);
			model.addAttribute("indexCode", indexCodes);
		}
		//???????????????????????????
		if (x != 0 && y != 0) {
			if (cameras.size() < 6) {
				ptzCamera = cameras;
			} else {
				ptzCamera = cameras.subList(0, 5);
			}
			videoManager.setPtz(ptzCamera, x, y);
		}
//        for(int i=0;i<6;i++){
//            //???6?????????????????????????????????
//            if(i>=cameras.size()){
//                break;
//            }
//            //??????cameraXY?????? point
//
//            Camera camera = cameras.get(i);
//            try {
//                Point point = videoService.getPointFromXY(x,y);
//                logger.info("####excute camera setPtz "+i+" indexCode:"+camera.getIndexCode());
//                videoManager.getVideoService(camera.getPlatform().toLowerCase()).setPTZ(camera.getIndexCode(),point);
//            }catch (Exception er) {
//                logger.error("####excute camera setPtz error  indexCode:"+camera.getIndexCode());
//            }

		//       }

		model.addAttribute("indexCode", indexCode);
		model.addAttribute("type", type);
		model.addAttribute("pNo", pNo == "" ? "0" : pNo);
		model.addAttribute("errorCode", errorCode);
		return "video/startUpVideo";
	}

	/**
	 * ????????????????????????
	 *
	 * @param bufferSize
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/openCamerasByXY", method = {RequestMethod.GET, RequestMethod.POST})
	public void openCameraByXY(
			@RequestParam String proId,
			@RequestParam(value = "x", defaultValue = "0") double x,
			@RequestParam(value = "y", defaultValue = "0") double y,
			@RequestParam(value = "xzqdm", defaultValue = "") String xzqdm,
			@RequestParam(value = "queryStyle", defaultValue = "xzq") String queryStyle,
			@RequestParam(value = "bufferSize", defaultValue = "2000") String bufferSize,
			HttpServletResponse res) throws Exception {
		res.setHeader("Access-Control-Allow-Origin", "*");
		String url = "";
		String rootUrl = AppConfig.getProperty("omp.url");
		String firstIndexCode = "";
		int presetNo = 0;
		String indexCodes = "";
		List<Camera> cameras = null;
		List cameraList = Lists.newArrayList();
		try {
			//Preset preset = getPresetByProId(proId);
			//????????????????????????????????????
            /*if(preset!=null){
                url =rootUrl+"/video/startUpVideo?type=1&indexCode="+preset.getIndexCode()+"&pNo="+preset.getPresetNo();
                res.sendRedirect(url);
                return;
            }*/
			if (x == 0 || y == 0) {
				throw new Exception("???????????????x???y????????????");
			}
			//???????????????????????????
			if (!queryStyle.equals("xzq")) {
				Point point = GeometryUtils.createPoint(x, y);
				String layerName = AppConfig.getProperty("video.videoLayer");
				String dataSource = AppConfig.getProperty("video.datasource");
				String result = geoService.intersect(layerName, geoService.toGeoJSON(point), null, dataSource);
				if (!StringUtils.isNotBlank(result)) {
					throw new Exception("???????????????????????????");
				}
				Map mapJson = JSON.parseObject(result);
				JSONArray features = (JSONArray) mapJson.get("features");
				if (features.size() > 0) {
					for (int i = 0; i < features.size(); i++) {
						Map feature = (Map) features.get(i);
						Map pro = (Map) feature.get("properties");
						//String indexCode = (String) pro.get("device_id");
						String indexCode = (String) pro.get("DEVICE_ID");
						if (indexCode != null && indexCode != "") {
							indexCodes = indexCodes + "," + indexCode;
							if (firstIndexCode == "") {
								firstIndexCode = indexCode;
							}
						}
					}
				} else {
					//??????????????????
					url = rootUrl + "/video/startUpVideo?&errorCode=1";
					res.sendRedirect(url);
					return;
				}
				url = rootUrl + "/video/startUpVideo?type=1&indexCode=" + firstIndexCode + "&pNo=50&errorCode=0&indexCodes=" + indexCodes + "&x=" + x + "&y=" + y;
				res.sendRedirect(url);
				return;
			}
			int bufferSizeInt = Integer.valueOf(bufferSize);
			for (int i = 0; i < 3; ++i) {
				Point point = GeometryUtils.createPoint(x, y);
				cameras = this.videoManager.getVideoService().getPoiCameras(point, bufferSize, xzqdm);
				if (cameras.size() > 0) {
					break;
				}
				if (bufferSizeInt >= 20000) {
					break;
				}
				if (bufferSizeInt <= 2000) {
					bufferSizeInt = 5000;
					continue;
				}
				if (bufferSizeInt <= 5000) {
					bufferSizeInt = 20000;
					continue;
				}
			}
			if (cameras.size() == 0) {
				throw new Exception("?????????????????????");
			}
            /*if(cameras.size()>6){
                cameras = cameras.subList(0,6);
            }*/
			for (int i = 0; i < cameras.size(); i++) {
				if (cameras.get(i).getIndexCode() != "") {
					String curIndexCode = cameras.get(i).getIndexCode();

					Map cameraMap = Maps.newHashMap();
					//???????????????
//                    Preset newPreset;
//                    try{
//                        newPreset =  presetService.insert(proId, proId, curIndexCode);
//                        presetNo = newPreset.getPresetNo();
//                    }catch (Exception er){
//                        logger.error(er.getMessage());
//                        throw new Exception("?????????????????????");
//                    }
					cameraMap.put("indexCode", curIndexCode);
					cameraMap.put("presetNo", presetNo);
					cameraList.add(cameraMap);
					indexCodes = indexCodes + "," + curIndexCode;
					if (firstIndexCode == "") {
						firstIndexCode = curIndexCode;
					}
				}
			}


			//???????????????
            /*Preset newPreset;
            try{
                newPreset =  presetService.insert(proId, proId, firstIndexCode);
                presetNo = newPreset.getPresetNo();
            }catch (Exception er){
                *//*logger.error(er.getMessage());
                throw new Exception("?????????????????????");*//*
            }*/

			url = rootUrl + "/video/startUpVideo?type=1&indexCode=" + firstIndexCode + "&pNo=50&errorCode=0&indexCodes=" + indexCodes + "&x=" + x + "&y=" + y;
			res.sendRedirect(url);
		} catch (Exception er) {
			url = rootUrl + "/video/startUpVideo?&errorCode=1";
			res.sendRedirect(url);
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param bufferSize
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/openCamerasOnPageByXY", method = {RequestMethod.GET, RequestMethod.POST})
	public void openCamerasOnPageByXY(
			@RequestParam String proId,
			@RequestParam(value = "x", defaultValue = "0") double x,
			@RequestParam(value = "y", defaultValue = "0") double y,
			@RequestParam(value = "bufferSize", defaultValue = "2000") String bufferSize,
			HttpServletResponse res) throws Exception {
		res.setHeader("Access-Control-Allow-Origin", "*");
		Preset preset = getPresetByProId(proId);
		String rootUrl = AppConfig.getProperty("omp.url");
		String url = "";
		String firstIndexCode = "";

		Geometry projectGeometry = null;

		try {
			if (preset == null && x == 0 || y == 0) {
				throw new Exception("???????????????x???y????????????");
			}

			x += 0.15558;
			y -= -0.010171;
			Point point = GeometryUtils.createPoint(x, y);
			Geometry result = preojectToTY(point);
			projectGeometry = geometryService.project(result, geometryService.parseUndefineSR("4528"), geometryService.parseUndefineSR("4490"));

			//????????????????????????????????????
			if (preset != null) {
				url = rootUrl + "/map/watch/hyst?cameraId=" + preset.getIndexCode() + "&pNo=" + preset.getPresetNo() + "&action=location&params={'type':'coordsLocation','params':{geometry:" + geometryService.toGeoJSON(projectGeometry) + "}}";
				res.sendRedirect(url);
				return;
			}
			//?????????????????????????????????????????????

			List<Camera> cameras = null;
			int bufferSizeInt = Integer.valueOf(bufferSize);
			for (int i = 0; i < 3; ++i) {
				cameras = this.videoManager.getVideoService().getPoiCameras(point, bufferSize, "");
				if (cameras.size() > 0) {
					break;
				}

				if (bufferSizeInt >= 20000) {
					break;
				}

				if (bufferSizeInt <= 2000) {
					bufferSizeInt = 5000;
					continue;
				}
				if (bufferSizeInt <= 5000) {
					bufferSizeInt = 20000;
					continue;
				}

			}
			String indexCodes = "";

			if (cameras.size() > 6) {
				cameras = cameras.subList(0, 6);
			}
			if (cameras.size() == 0) {
				throw new Exception("???????????????????????????");
			}
			for (int i = 0; i < cameras.size(); i++) {
				if (cameras.get(i).getIndexCode() != "") {
					indexCodes = indexCodes + "," + cameras.get(i).getIndexCode();
					if (firstIndexCode == "") {
						firstIndexCode = cameras.get(i).getIndexCode();
					}
				}
			}
			//???????????????
			Preset newPreset = null;
			try {
				newPreset = presetService.insert(proId, proId, firstIndexCode);
			} catch (Exception er) {
				logger.error(er.getMessage());
				throw new Exception("?????????????????????");
			}

			int pNo = 0;
			if (newPreset != null) {
				pNo = newPreset.getPresetNo();
			}
			return;
		} catch (Exception er) {
			url = rootUrl + "/map/watch/hyst?&errorCode=1&action=location&params={'type':'coordsLocation','params':{geometry:" + geometryService.toGeoJSON(projectGeometry) + "}}";
			res.sendRedirect(url);
			return;
		}
	}

	private Geometry preojectToTY(Geometry geometry) {
		Point point = null;
        /*if(geometry instanceof Point){
            point = geometry.getCentroid();
        }else {
            point= geometry.getCentroid();
        }*/
		point = geometry.getCentroid();
		if (point.getX() < 10000) {
			geometry = geometryService.project(geometry, geometryService.getCRSBySRID("4490"), geometryService.getCRSBySRID("4528"));
		}
		return geometry;
	}


	/**
	 * ????????????????????????
	 *
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/openCamerasByLayer", method = {RequestMethod.GET, RequestMethod.POST})
	public void openCamerasByLayer(
			@RequestParam String proId,
			@RequestParam(value = "layerName", required = false) String layerName,
			@RequestParam(value = "where", required = false) String where,
			@RequestParam(value = "dataSource", defaultValue = "") String dataSource,
			@RequestParam(value = "bufferSize", defaultValue = "2000") double bufferSize,
			@RequestParam(value = "queryStyle", defaultValue = "xzq") String queryStyle,
			@RequestParam(value = "xzqdm", defaultValue = "") String xzqdm,
			HttpServletResponse res) throws Exception {
		res.setHeader("Access-Control-Allow-Origin", "*");
		//?????????????????????????????????
		String rootUrl = AppConfig.getProperty("omp.url");
		String url = "";
		String indexCodes = "";
		int presetNo = 0;
		double x = 0;
		double y = 0;
		try {
			Preset preset = getPresetByProId(proId);
            /*if(preset!=null){
                url =rootUrl+"/video/startUpVideo?type=1&indexCode="+preset.getIndexCode()+"&pNo="+preset.getPresetNo();
                res.sendRedirect(url);
                return;
            }*/
			//????????????????????????
			List<Camera> cameras = new ArrayList<>();
			String firstIndexCode = "";
			String videoLayerName = AppConfig.getProperty("video.videoLayer");
			String videoDataSource = AppConfig.getProperty("video.datasource");
			//??????????????????
			//List<Map<String, Object>> queryResult = (ArrayList<Map<String, Object>>)gisService.query(layerName,where,null,true,dataSource==""?null:dataSource);
			String queryUrl = AppConfig.getProperty("GLSJ.GDDK_320000");
			String geojson = MapQueryService.execute(queryUrl, where, null, null, null);
			// Map data = JSON.parse(geojson);
			JSONArray features = ((JSONArray) ((Map) JSON.parse(geojson)).get("features"));
			Map json = new HashMap();


			if (CollectionUtils.isNotEmpty(features)) {
				Map feature = (Map) features.get(0);
				Map geometryMap = (Map) feature.get("geometry");
				if (geometryMap == null) {
					throw new Exception("???????????????????????????!");
				}
				x = ((BigDecimal) ((JSONArray) ((JSONArray) ((JSONArray) geometryMap.get("rings")).get(0)).get(0)).get(0)).doubleValue();
				y = ((BigDecimal) ((JSONArray) ((JSONArray) ((JSONArray) geometryMap.get("rings")).get(0)).get(0)).get(1)).doubleValue();
				json.put("type", "Feature");

//                Map geometry = new HashMap();
//                geometry.put("coordinates", geometryMap.get("rings"));
//                geometry.put("type","Polygon");
//                json.put("geometry",geometry);
			}
            /*for(int i=0;i<features.size();i++){
                Map item = (Map)features.get(i);
            }*/
			//String geojson = geoService.list2FeatureCollection(queryResult,"4490","4490");
			//Object featuresObj = geometryService.readUnTypeGeoJSON(json);
			//FeatureCollection featureCollection = (FeatureCollection)featuresObj;
           /* Geometry poiGeo=null;
            while (featureCollection.features().hasNext()){
                SimpleFeature feature1 = (SimpleFeature)featureCollection.features().next();
                poiGeo =(Geometry) feature1.getDefaultGeometry();
                Geometry center = poiGeo.getCentroid();
                x = ((Point) center).getX();
                y=((Point) center).getY();
                break;
            }
            if(poiGeo==null){
                throw new Exception("????????????????????????");
            }*/
			//????????????????????????????????????
			if (!queryStyle.equals("xzq")) {
				String result = geoService.intersect(videoLayerName, geoService.toGeoJSON(json.toString()), null, videoDataSource);
				if (!StringUtils.isNotBlank(result)) {
					throw new Exception("???????????????????????????");
				}
				Map mapJson = JSON.parseObject(result);
				JSONArray features1 = (JSONArray) mapJson.get("features");
				if (features.size() > 0) {
					for (int i = 0; i < features.size(); i++) {
						Map feature = (Map) features.get(i);
						Map pro = (Map) feature.get("properties");
						//String indexCode = (String) pro.get("device_id");
						String indexCode = (String) pro.get("DEVICE_ID");
						if (indexCode != null && indexCode != "") {
							indexCodes = indexCodes + "," + indexCode;
							if (firstIndexCode == "") {
								firstIndexCode = indexCode;
							}
						}
					}
				} else {
					//??????????????????
					url = rootUrl + "/video/startUpVideo?&errorCode=1";
					res.sendRedirect(url);
					return;
				}
				url = rootUrl + "/video/startUpVideo?type=1&indexCode=" + firstIndexCode + "&pNo=50&errorCode=0&indexCodes=" + indexCodes + "&x=" + x + "&y=" + y;
				res.sendRedirect(url);
				return;
			}

			Point point = GeometryUtils.createPoint(x, y);
			for (int i = 0; i < 3; ++i) {
				logger.info("###?????????" + i + "????????????bufferSize:" + bufferSize + "layerName:" + layerName + "where:" + where);
				cameras = this.videoManager.getVideoService().getPoiCameras(point, String.valueOf(bufferSize), xzqdm);
				if (cameras.size() > 0) {
					break;
				}
				if (bufferSize >= 20000) {
					break;
				}

				if (bufferSize <= 2000) {
					bufferSize = 5000;
					continue;
				}
				if (bufferSize <= 5000) {
					bufferSize = 20000;
					continue;
				}

			}
            /*if (!geometry.isSimple()){
                System.out.println(cameras.size());
                if(cameras.size()>6){
                    break;
                }
                geometry = geometryService.simplify(geometry, geometryService.getSimplifyTolerance());
            }

        }*/
			if (cameras.size() > 0) {
				firstIndexCode = cameras.get(0).getIndexCode();
			} else {
				throw new Exception("???????????????????????????");
			}
			if (cameras.size() > 6) {
				cameras = cameras.subList(0, 1);
			}

			for (int i = 0; i < cameras.size(); i++) {
				if (cameras.get(i).getIndexCode() != "") {
					indexCodes = indexCodes + "," + cameras.get(i).getIndexCode();
				}
			}
//            //???????????????
//            Preset newPreset;
//            try{
//                newPreset =  presetService.insert(proId, proId, firstIndexCode);
//                presetNo = newPreset.getPresetNo();
//            }catch (Exception er){
//                logger.error(er.getMessage());
//                throw new Exception("?????????????????????");
//            }

			url = rootUrl + "/video/startUpVideo?type=1&indexCode=" + firstIndexCode + "&pNo=" + presetNo + "&indexCodes=" + indexCodes + "&x=" + x + "&y=" + y;
			res.sendRedirect(url);
		} catch (Exception er) {
			url = rootUrl + "/video/startUpVideo?errorCode=1";
			res.sendRedirect(url);
		}
	}


	/**
	 * ????????????????????????
	 *
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/openCamerasPageByLayer", method = {RequestMethod.GET, RequestMethod.POST})
	public void openCamerasPageByLayer(
			@RequestParam String proId,
			@RequestParam(value = "layerName", required = false) String layerName,
			@RequestParam(value = "where", required = false) String where,
			@RequestParam(value = "dataSource", defaultValue = "") String dataSource,
			@RequestParam(value = "bufferSize", defaultValue = "2000") double bufferSize,
			HttpServletResponse res) throws Exception {
		res.setHeader("Access-Control-Allow-Origin", "*");
		//?????????????????????????????????
		Preset preset = getPresetByProId(proId);
		String rootUrl = AppConfig.getProperty("omp.url");
		String url = "";
		String geometry = "";
		try {
			Map result = new HashMap();
			List<Camera> cameras = new ArrayList<>();
			String firstIndexCode = "";
			List<Map<String, Object>> queryResult = (ArrayList<Map<String, Object>>) gisService.query(layerName, where, null, true, dataSource == "" ? null : dataSource);
			geometry = geoService.list2FeatureCollection(queryResult, "4490", "4490");

			if (preset != null) {
				url = rootUrl + "/map/watch/hyst?cameraId=" + preset.getIndexCode() + "&pNo=" + preset.getPresetNo() + "&action=location&params={'type':'coordsLocation','params':{geometry:" + geometry + "}}";
				res.sendRedirect(url);
				return;
			}
			//????????????????????????

			for (int i = 0; i < 3; ++i) {
				cameras = this.videoManager.getVideoService().getPoiCameras(geometry, String.valueOf(bufferSize), "");
				if (cameras.size() > 0) {
					break;
				}
				if (bufferSize >= 20000) {
					break;
				}

				if (bufferSize <= 2000) {
					bufferSize = 5000;
					continue;
				}
				if (bufferSize <= 5000) {
					bufferSize = 20000;
					continue;
				}

			}

			if (cameras.size() > 0) {
				firstIndexCode = cameras.get(0).getIndexCode();
			} else {
				throw new Exception("???????????????????????????");
			}

			//???????????????
			Preset newPreset = null;
			try {
				newPreset = presetService.insert(proId, proId, firstIndexCode);
			} catch (Exception er) {
				logger.error(er.getMessage());
				throw new Exception("?????????????????????");
			}

			int pNo = 0;
			if (newPreset != null) {
				pNo = newPreset.getPresetNo();
			}

			url = rootUrl + "/map/watch/hyst?cameraId=" + firstIndexCode + "&pNo=" + pNo + "&action=location&params={'type':'coordsLocation','params':{geometry:" + geometry + "}}";
			res.sendRedirect(url);
		} catch (Exception er) {
			url = rootUrl + "/map/watch/hyst?errorCode=1&action=location&params={'type':'coordsLocation','params':{geometry:" + geometry + "}}";
			res.sendRedirect(url);
		}
	}

	@RequestMapping(value = "/videoPart", method = {RequestMethod.GET, RequestMethod.POST})
	public String videoPart(
			@RequestParam String partType,
			@RequestParam(value = "proId", required = false) String proId,
			@RequestParam(value = "indexCode", required = false) String indexCode,
			@RequestParam(value = "loginName", required = false) String loginName,
			@RequestParam(value = "userId", required = false) String userId,
			Model model
	) {
		model.addAttribute("partType", partType);
		model.addAttribute("indexCode", indexCode);
		model.addAttribute("proId", proId);
		model.addAttribute("loginName", loginName);
		model.addAttribute("userId", userId);
		return "video/videoPart";
	}


}
