package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.CameraPanoramaDao;
import cn.gtmap.onemap.platform.dao.FileStoreDao;
import cn.gtmap.onemap.platform.entity.video.CameraPanorama;
import cn.gtmap.onemap.platform.service.PanoramaService;
import cn.gtmap.onemap.platform.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/4/13 19:20
 */
public class PanoramaServiceImpl extends BaseLogger implements PanoramaService {

    private static final String EGOV_DATA = "${egov.data}";

    private static final String TYPE_JPG = "jpg";

    @Autowired
    private CameraPanoramaDao cameraPanoramaDao;
    @Autowired
    private FileStoreDao fileStoreDao;

    private Resource baseLocation;

    private Resource location;


    /***
     *
     * @param indexCode
     * @param createTime
     * @return
     */
    @Override
    public CameraPanorama get(String indexCode, Date createTime) {
        List<CameraPanorama> cameraPanoramas = cameraPanoramaDao.findByIndexCodeAndCreateAt(indexCode, createTime);
        if (cameraPanoramas.size() > 0) return cameraPanoramas.get(0);
        List<Object> fileStores = fileStoreDao.findByParentIdAndDate(indexCode, DateUtils.formatDate(createTime));
        List<String> picPaths = new ArrayList<String>();
        try {
            for (Object obj : fileStores) {
                if(fileStores.indexOf(obj)>6)break; //最多取6张照片
                Object[] attrs = (Object[]) obj;
                picPaths.add(getRealPath(String.valueOf(attrs[9])));
            }
            int size = picPaths.size();
            if (size == 0) return null;
            File outFile = createPanoramaFile(indexCode, createTime, TYPE_JPG);
            //拼接成全景图
            boolean success = createPanoByOpencv(picPaths,outFile.getAbsolutePath());
            if (success)return save2DB(outFile, indexCode, createTime);
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return null;
    }

    /***
     *
     * @param file
     * @param indexCode
     * @param createAt
     * @return
     */
    @Override
    public CameraPanorama save(File file, String indexCode, Date createAt) {
        return save2DB(file,indexCode,createAt);
    }

    /***
     * get file
     * @param id
     * @return
     * @throws IOException
     */
    @Override
    public File getFile(String id) throws IOException {
        Assert.notNull(id, getMessage("id.notnull"));
        CameraPanorama panorama = cameraPanoramaDao.findOne(id);
        return new File(getRealPath(panorama.getPath()));
    }

    /***
     * save image file to database
     * @param file
     * @param indexCode
     * @param createTime
     * @return
     */
    private CameraPanorama save2DB(File file, String indexCode, Date createTime) {
        try {
            String path = getRealPath(file.getPath());
            CameraPanorama cameraPanorama = new CameraPanorama();
            cameraPanorama.setPath(path);
            cameraPanorama.setIndexCode(indexCode);
            cameraPanorama.setCreateAt(createTime);
            return cameraPanoramaDao.save(cameraPanorama);
        } catch (Exception e) {
            throw new RuntimeException(getMessage("panorama.save.error", e.getLocalizedMessage()));
        }
    }

    /***
     *
     * @param indexCode
     * @param createTime
     * @return
     */
    private File createPanoramaFile(String indexCode,Date createTime,String type) {
        File file = null;
        try {
            String path = location.getURI().getPath();
            File folder = new File(path);
            if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
            file = new File(path.concat("/" + indexCode.concat("_").concat(DateUtils.formatDate(createTime)).concat("."+type)));
            if (file.exists()) {
                file = new File(path.concat("/" + reNameFile(file.getName())));
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        return file;
    }
    /**
     * @param name
     * @return
     */
    private String reNameFile(String name) {
        if (name.lastIndexOf(".") > -1) {
            int index = name.lastIndexOf(".");
            String suffix = name.substring(index, name.length());
            return name.replace(suffix, "_" + System.currentTimeMillis() + suffix);
        } else {
            return name + "_" + System.currentTimeMillis();
        }
    }
    /**
     * 获取实际地址
     *
     * @param path
     * @return
     * @throws IOException
     */
    private String getRealPath(String path) throws IOException {
        return path.replace(EGOV_DATA, baseLocation.getFile().getPath());
    }

    /***
     * 调用stitch.exe生成全景图片
     * @param pics
     * @param output 生成的全景图片路径
     * @return
     */
    protected boolean createPanoByOpencv(List<String> pics,String output){
        String cmdParam="Stitcher ";
        for (String picPath : pics) {
            cmdParam=cmdParam.concat(picPath)+" ";
        }
        cmdParam=cmdParam.concat("--output "+output);
        try {
            Runtime runtime= Runtime.getRuntime();
            String[] cmd = new String[3];
            cmd[0] = "cmd ";
            cmd[1] = "/c ";
            cmd[2] = cmdParam;
            runtime.exec((cmd[0]+cmd[1]+cmd[2]),null,null);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public void setBaseLocation(Resource baseLocation) {
        this.baseLocation = baseLocation;
    }

    public void setLocation(Resource location) {
        this.location = location;
    }
}
