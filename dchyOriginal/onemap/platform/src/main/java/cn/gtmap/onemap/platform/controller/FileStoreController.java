package cn.gtmap.onemap.platform.controller;

import cn.gtmap.onemap.platform.dao.CameraDao;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.Camera;
import cn.gtmap.onemap.platform.event.JSONMessageException;
import cn.gtmap.onemap.platform.service.FileStoreService;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import cn.gtmap.onemap.platform.service.VideoService;
import cn.gtmap.onemap.platform.support.spring.BaseController;
import cn.gtmap.onemap.platform.utils.HttpRequest;
import cn.gtmap.onemap.platform.utils.UUIDGenerator;
import com.gtis.fileCenter.model.Space;
import com.gtis.fileCenter.service.FileService;
import com.gtis.fileCenter.service.NodeService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-28 上午9:06
 */
@Controller
@RequestMapping(value = "/file")
public class FileStoreController extends BaseController {

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private FileService fileService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private VideoService videoService;



    @Autowired
    private VideoMetadataService videoMetadataService;

    /**
     * 工作流附件在文件中心的表空间
     */
    private static final String WORK_FLOW_STUFF = "WORK_FLOW_STUFF";

    /**
     * 文件上传
     *
     * @param parentId
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload/{parentId}", method = RequestMethod.POST)
    @ResponseBody
    public String upload(@PathVariable String parentId, HttpServletRequest request) {
        try {
            FileStore fs = fileStoreService.save(request, parentId);
            if (fs != null){
                return fs.getId();
            }
            throw new RuntimeException("未获取上传数据");
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("file.upload.error", e.getLocalizedMessage()));
        }
    }

    /**
     * upload file ,parse by CommonsMultipartResolver
     *
     * @param parentId
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload2/{parentId}", method = RequestMethod.POST)
    @ResponseBody
    public String upload2(@PathVariable String parentId, MultipartHttpServletRequest request) {
        try {
            FileStore fs = fileStoreService.save2(request, parentId, null);
            if (fs != null) {
                return fs.getParentId();
            }
            throw new RuntimeException("未获取上传数据");
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("file.upload.error", e.getLocalizedMessage()));
        }
    }

    /***
     * 照片上传
     * @param parentId
     * @param picId
     * @param request
     * @return
     */
    @RequestMapping(value = "/pic/upload/{parentId}", method = RequestMethod.POST)
    @ResponseBody
    public String upload2(@PathVariable String parentId, @RequestParam(value = "picid", required = false) String picId,
                          MultipartHttpServletRequest request) {
        try {
            FileStore fs = fileStoreService.save2(request, parentId, picId);
            if (fs != null) {
                return fs.getId();
            }
            throw new RuntimeException("未获取上传数据");
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("file.upload.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 获取ids
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/ids/{parentId}")
    @ResponseBody
    public String[] ids(@PathVariable String parentId) {
        try {
            return fileStoreService.getFileIds(parentId);
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("file.ids.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 上传验收报告文件
     * @return
     */
    @RequestMapping(value = "/uploadReport/{parentId}")
    @ResponseBody
    public Map uploadReport(@PathVariable String parentId,HttpServletRequest request){
        FileStore resultFile= fileStoreService.save(request,parentId);
        Map result = new HashMap();
        if(videoService.findById(parentId)==null){
            result.put("error","请检查camaraid");
            return result;
        }
        String fileId= resultFile.getId();
        try {
             String id= videoService.SetFileID(parentId,fileId);
             result.put("success",id);
        }catch (Exception er){
            result.put("error",er.getMessage());
        }
        return  result;
    }

    /**
     * 根据日期获取项目图片
     *
     * @param proId
     * @param date
     * @return
     */
    @RequestMapping(value = "/records/getByDate")
    @ResponseBody
    public String[] filesByDate(@RequestParam(value = "proId") String proId,
                                @RequestParam(value = "date") String date) {
        return fileStoreService.getFileByDate(proId, date);

    }

    /**
     * 根据proid获取项目图片
     *
     * @param proId
     * @return
     */
    @RequestMapping(value = "/records/getByProId")
    @ResponseBody
    public String[] filesByProId(@RequestParam(value = "proId") String proId) {
        return fileStoreService.getFileIds(proId);
    }

    /**
     * 获取预警图片
     * @param proId
     * @param status 0预警 1建设用地 2违法用地
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/rocords/getWarnFile")
    @ResponseBody
    public Object getWarningByPro(@RequestParam(value = "proId")  String proId,
                                @RequestParam(value = "status" , defaultValue= "0") int status,
                                @RequestParam(value = "page",defaultValue= "1") int page,
                                @RequestParam(value = "size",defaultValue= "10") int size
                               )  {
        try {
            return fileStoreService.getWarnFile(proId,status,page,size);
        }catch (Exception er){
            return er;
        }
    }

    /**
     * 文件下载
     *
     * @param id
     * @param response
     */
    @RequestMapping(value = "/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response) {
        try {
            File file = fileStoreService.getFile(id);
            sendFile(new FileInputStream(file), response, file.getName());
        } catch (IOException e) {
            throw new JSONMessageException(getMessage("file.download.error", id, e.getLocalizedMessage()));
        }
    }

    /**
     * 缩略图
     *
     * @param id
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/thumb/{id}")
    public void thumb(@PathVariable String id, HttpServletResponse response) {
        try {
            sendFile(fileStoreService.getThumb(id), response);
        } catch (IOException e) {
            throw new JSONMessageException(getMessage("file.thumb.error", id, e.getLocalizedMessage()));
        }
    }

    /***
     * 原图
     * @param id
     * @param response
     */
    @RequestMapping(value = "/original/{id}")
    public void original(@PathVariable String id, HttpServletResponse response) {
        try {
            sendFile(fileStoreService.getFile(id), response);
        } catch (IOException e) {
            throw new JSONMessageException(getMessage("file.original.error", id, e.getLocalizedMessage()));
        }
    }

    /**
     * @param id
     * @param response
     */
    @RequestMapping(value = "/img/{id}.{ext}")
    public void originalImg(@PathVariable String id,
                            @PathVariable String ext,
                            HttpServletResponse response) {
        try {
            sendFile(fileStoreService.getFile(id), response);
        } catch (IOException e) {
            throw new JSONMessageException(getMessage("file.original.error", id, e.getLocalizedMessage()));
        }
    }

    /**
     *
     * @param fileId
     * @param parentId
     */
    @RequestMapping(value = "/delete/{fileId}/{parentId}")
    @ResponseBody
    public void delete(@PathVariable String fileId,@PathVariable String parentId) {
        try {
            fileStoreService.delete(fileId);
            videoService.SetFileID(parentId,"");
        } catch (Exception e) {
            throw new JSONMessageException(getMessage("file.delete.error", parentId, e.getLocalizedMessage()));
        }

    }

    /**
     * 上传首页logo
     *
     * @return
     */
    @RequestMapping(value = "/logo/upload")
    @ResponseBody
    public Object uploadLogo(MultipartHttpServletRequest request) {
        try {
            return fileStoreService.uploadLog(request);
        } catch (Exception e) {
            throw new JSONMessageException("上传出现异常：" + e.getMessage());
        }
    }

    /**
     * 获取logo文件
     *
     * @param fileName
     * @param response
     */
    @RequestMapping(value = "/logo/{fileName}")
    public void getLogoFile(@PathVariable String fileName, HttpServletResponse response) {
        try {
            sendFile(fileStoreService.getLogoFile(fileName), response);
        } catch (IOException e) {
            throw new JSONMessageException("获取logo异常：" + e.getMessage());
        }
    }

    /**
     * save file by url
     *
     * @param fileUrl
     * @return
     */
    @RequestMapping(value = "/file/save")
    @ResponseBody
    public FileStore saveFileByUrl(@RequestParam String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            URLConnection con = url.openConnection();
            InputStream ins = con.getInputStream();
            File file = new File(System.getProperty("java.io.tmpdir") + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
            FileUtils.copyInputStreamToFile(ins, file);
            if (file.exists()) {
                return fileStoreService.save3(file, UUIDGenerator.generate());
            } else{
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 常州出图文件上传文件中心
     *
     * @param mapUrl
     * @param proid
     * @return
     */
    @RequestMapping(value = "upload/map")
    @ResponseBody
    public Map uploadMap2FileCenter(@RequestParam(value = "mapUrl") String mapUrl,
                                    @RequestParam(value = "proid") String proid) {
        Map result = new HashMap();
        Space space = nodeService.getWorkSpace(WORK_FLOW_STUFF, true);
        //com.gtis.fileCenter.model.Node node = nodeService.getNode(space.getId(), proid, true);
        com.gtis.fileCenter.model.Node node = nodeService.getNode(space.getId(), proid);
        com.gtis.fileCenter.model.Node node1 = nodeService.getNode(node.getId(), "图件");
        logger.error("name:{} path{}", node1.getName(), node1.getPath());
        result.put("success", true);
        try {
            URL url = new URL(mapUrl);
            URLConnection con = url.openConnection();
            InputStream ins = con.getInputStream();
            File file = new File(System.getProperty("java.io.tmpdir") + File.separator + "电子图件.jpg");
            FileUtils.copyInputStreamToFile(ins, file);
            if (file.exists()) {
                com.gtis.fileCenter.model.Node returnNode = fileService.uploadFile(file, node1.getId());
            } else {
                logger.error("文件不存在！");
                result.put("success", false);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            result.put("success", false);
        }
        return result;
    }

}
