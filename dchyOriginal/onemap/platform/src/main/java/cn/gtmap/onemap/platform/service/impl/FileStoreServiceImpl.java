package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.FileStoreDao;
import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.Recognition;
import cn.gtmap.onemap.platform.event.FileStoreDeleteEvent;
import cn.gtmap.onemap.platform.service.DBAService;
import cn.gtmap.onemap.platform.service.FileStoreService;
import cn.gtmap.onemap.platform.utils.FilesUtils;
import com.gtis.config.AppConfig;
import com.gtis.generic.util.ImageUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-25 下午5:38
 */
public class FileStoreServiceImpl extends BaseLogger implements FileStoreService, ApplicationListener<FileStoreDeleteEvent> {

    private static final String EGOV_DATA = "${egov.data}";

    private static final int THRESHOLD = 1024 * 1024 * 10;

    private static final String THUMB_SUFFIX = "_thumb";

    @Autowired
    private DBAService dbaService;

    @Autowired
    private FileStoreDao fileStoreDao;

    private Resource baseLocation;
    private Resource location;
    private Resource tempFile;
    private long maxSize;

    /**
     * get one
     *
     * @param id
     * @return
     */
    public FileStore get(String id) {
        return fileStoreDao.findOne(id);
    }

    /**
     * save
     *
     * @param request
     * @param parentId
     * @return
     */
    public FileStore save(HttpServletRequest request, String parentId) {
        Assert.notNull(parentId, getMessage("file.parentId.notnull"));
        FileStore fileStore = null;
        try {
            List<FileItem> fileItems = parseRequest(request);
            for (FileItem item : fileItems) {
                if (!item.isFormField()) {
                    DiskFileItem diskFileItem = (DiskFileItem) item;
                    File newFile = getNewFile(item.getName());
                    if (diskFileItem.isInMemory()) {
                        FileUtils.copyInputStreamToFile(diskFileItem.getInputStream(), newFile);
                    } else {
                        File tmpFile = diskFileItem.getStoreLocation();
                        FileUtils.copyFile(tmpFile, newFile, true);
                    }
                    createThumb(newFile);
                    fileStore = save2DB(newFile, parentId, diskFileItem);
                    logger.debug(getMessage("file.upload.success", item.getName()));
                }
            }
        } catch (FileUploadException e) {
            logger.error(getMessage("file.upload.error", e.getLocalizedMessage()));
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        return fileStore;
    }

    public Page getWarnFile(String proID, int status, int page, int size) throws IOException {
        Pageable pageRequest = new PageRequest(page,size);
        String sql  = "SELECT A.* FROM OMP_CAMERA_RECOG A join OMP_FILE_STORE B ON A.originFile=B.ID AND A.STATUS ="+status+" AND B.PARENT_ID='"+proID+ "' ORDER BY PARENT_ID";
        Page<Recognition> result = dbaService.searchByPage(sql,pageRequest,Recognition.class);
        List<Recognition>  content = result.getContent();
        return result;
    }


    /**
     * save upload file , parse by CommonsMultipartResolver    <br/>
     *
     * @param request
     * @param parentId
     * @return
     * @see org.springframework.web.multipart.commons.CommonsMultipartResolver
     */
    @Override
    public FileStore save2(MultipartHttpServletRequest request, String parentId, String picId) {
        Iterator<String> iterator = request.getFileNames();
        FileStore fileStore = null;
        while (iterator.hasNext()) {
            MultipartFile file = request.getFile(iterator.next());
            String fileName = file.getOriginalFilename();
            logger.debug(" upload file : {0}", fileName);
            try {
                File newFile = getNewFile(fileName);
                FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
                createThumb(newFile);
                if (picId == null) {
                    fileStore = save2DB2(newFile, parentId, fileName, null, file.getSize());
                } else {
                    fileStore = update2DB2(newFile, parentId, fileName, file.getSize(), picId);
                }
                logger.debug(getMessage("file.upload.success", fileName));
            } catch (IOException e) {
                logger.error(" save upload file error , detail info : [{0}]", e.getLocalizedMessage());
            }
        }
        return fileStore;
    }

    /**
     * save upload file
     *
     * @param file
     * @param parentId
     * @return
     */
    @Override
    public FileStore save3(File file, String parentId) {
        Assert.notNull(file);
        Assert.notNull(parentId);
        FileStore fileStore = null;
        try {
            fileStore = save2DB2(file, parentId, file.getName(), null, file.getTotalSpace());
            logger.debug(getMessage("file.upload.success", file.getName()));
        } catch (Exception e) {
            logger.error(" save upload file error , detail info : [{0}]", e.getLocalizedMessage());
        }
        return fileStore;
    }

    /***
     *
     * @param file
     * @param parentId
     * @return
     */
    @Override
    public FileStore saveWithThumb(File file, String parentId) {
        Assert.notNull(file);
        Assert.notNull(parentId);
        FileStore fileStore = null;
        try {
            createThumb(file);
            fileStore = save2DB2(file, parentId, file.getName(), null, file.getTotalSpace());
            logger.debug(getMessage("file.upload.success", file.getName()));
        } catch (Exception e) {
            logger.error(" save upload file error , detail info : [{0}]", e.getLocalizedMessage());
        }
        return fileStore;
    }

    /***
     * save file store with create time
     * @param file
     * @param parentId
     * @param datetime
     * @return
     */
    @Override
    public FileStore saveWithCreateTime(File file, String parentId, Date datetime) {
        Assert.notNull(file);
        Assert.notNull(parentId);
        FileStore fileStore = null;
        try {
            fileStore = save2DB2(file, parentId, file.getName(), datetime, file.getTotalSpace());
            logger.debug(getMessage("file.upload.success", file.getName()));
        } catch (Exception e) {
            logger.error(" save upload file error , detail info : [{0}]", e.getLocalizedMessage());
        }
        return fileStore;
    }

    /**
     * save
     *
     * @param fileStore
     * @return
     */
    @Transactional
    public FileStore save(FileStore fileStore) {
        return fileStoreDao.save(fileStore);
    }

    /**
     * delete
     *
     * @return
     */
    @Transactional
    public boolean delete(String id) {
        fileStoreDao.delete(id);
        return true;
    }

    /**
     * delete
     *
     * @param fileStore
     * @return
     */
    @Transactional
    public boolean delete(FileStore fileStore) {
        fileStoreDao.delete(fileStore);
        return true;
    }

    /**
     * @param parentId
     * @return
     */
    public String[] getFileIds(String parentId) {
        List<FileStore> files = fileStoreDao.findByParentIdOrderByCreateTimeAsc(parentId);
        if (files != null && files.size() > 0) {
            String[] ids = new String[files.size()];
            for (int i = 0; i < files.size(); i++) {
                ids[i] = files.get(i).getId();
            }
            return ids;
        }
        return new String[0];
    }

    /**
     * 根据日期获取图片
     *
     * @param proId
     * @param date
     * @return
     */
    public String[] getFileByDate(String proId, String date) {
        List<FileStore> files = fileStoreDao.findByParentIdAndCreateDate(proId, date);
        if (files != null && files.size() > 0) {
            String[] ids = new String[files.size()];
            for (int i = 0; i < files.size(); i++) {
                ids[i] = files.get(i).getId();
            }
            return ids;
        }
        return new String[0];
    }

    /**
     * get file by id
     *
     * @param id
     * @return
     */
    public File getFile(String id) throws IOException {
        Assert.notNull(id, getMessage("id.notnull"));
        FileStore fileStore = fileStoreDao.findOne(id);
        return new File(getRealPath(fileStore.getPath()));
    }

    /**
     * 获取最新项目拍照
     * @return
     */
    @Override
    public FileStore getUpdatedImgByProId(String proId){
        FileStore fileStore =null;
        Object dt = fileStoreDao.getUpdatedDate(proId);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        if(dt==null){
            return null;
        }
        String dateStr = f.format(dt);
        List<FileStore> fileStoreList = fileStoreDao.findByParentIdAndCreateDate(proId,dateStr);
        if(fileStoreList!=null&&fileStoreList.size()>0) {
            fileStore=fileStoreList.get(0);
        }
        return fileStore;
    }

    /**
     * get files by parentid
     *
     * @param parentId
     * @return
     * @throws IOException
     */
    @Override
    public List<File> getFiles(String parentId) throws IOException {
        Assert.notNull(parentId);
        List<FileStore> fileStores = fileStoreDao.findByParentId(parentId);
        List<File> files = new ArrayList<File>();
        if (fileStores != null && fileStores.size() > 0) {
            for (FileStore fileStore : fileStores) {
                files.add(new File(getRealPath(fileStore.getPath())));
            }
        }
        return files;
    }

    /**
     * 获取表中最新的一条记录
     *
     * @param parentId
     * @return
     */
    @Override
    public FileStore getFileStoreNewest(String parentId) {
        List<FileStore> stores = fileStoreDao.findByParentIdOrderByCreateAtDesc(parentId);
        if (isNotNull(stores) && stores.size() > 0) {
            return stores.get(0);
        }
        return null;
    }

    /**
     * get thumb
     *
     * @param id
     * @return
     */
    public File getThumb(String id) throws IOException {
        Assert.notNull(id, getMessage("id.notnull"));
        FileStore fileStore = fileStoreDao.findOne(id);
        String path = getRealPath(fileStore.getPath());
        File thumbFile = new File(getThumbPath(path));
        if (thumbFile.exists()) return thumbFile;
        return null;
    }

    /***
     * 在filestore目录建文件
     * @param name
     * @return
     * @throws IOException
     */
    @Override
    public File createNewFile(String name) throws IOException {
        return getNewFile(name);
    }

    /***
     * 处理文件名称乱码(gb2312乱码)
     * @param name
     * @return
     */
    @Override
    public String parseMessyName(String name) {
        StringBuilder nName = new StringBuilder("");
        if (StringUtils.isNotBlank(name) && name.startsWith("=?GB2312")) {
            String[] array = name.split(" ");//按空格分割处理
            for (String s : array) {
                String[] strs = s.split("\\?");
                try {
                    nName.append(new String(Base64.decodeBase64(strs[3]), "gbk"));
                } catch (UnsupportedEncodingException e) {
                    logger.error(getMessage("", e.getMessage()));
                }
            }
        }
        return StringUtils.isBlank(nName.toString()) ? name : nName.toString();
    }

    /**
     * 上传logo到配置目录下
     *
     * @param request
     * @return
     */
    @Override
    public Map uploadLog(MultipartHttpServletRequest request) {
        Map result = new HashMap();
        String configPath = AppConfig.getConfHome() + "omp" + File.separator + "logo";
        configPath = configPath.substring(6, configPath.length());
        Iterator<String> iterator = request.getFileNames();
        while (iterator.hasNext()) {
            MultipartFile file = request.getFile(iterator.next());
            File logoFilePath = new File(configPath);
            File outputFile = new File(configPath + File.separator + file.getOriginalFilename());

            if (!logoFilePath.exists()) {
                logoFilePath.mkdirs();
            }

            if (outputFile.exists()) {
                boolean flag = outputFile.delete();
                if (!flag){
                    logger.error("删除失败！");
                }
            }

            try {
                IOUtils.copy(file.getInputStream(), new FileOutputStream(outputFile));
            } catch (IOException e) {
                logger.error("文件上传异常：" + e.getMessage());
                throw new RuntimeException(e);
            }

            result.put("logo", file.getOriginalFilename().split("\\.")[0]); //此处只会循环（迭代）一次，直接put
        }

        return result;
    }

    @Override
    public File getLogoFile(String fileName) throws FileNotFoundException {
        String configPath = AppConfig.getConfHome() + "omp" + File.separator + "logo";
        configPath = configPath.substring(6, configPath.length());
        File logoFile = null;
        String realpath = configPath + File.separator + fileName + ".jpg";
        logoFile = new File(realpath);
        if (!logoFile.exists()) {
            realpath = configPath + File.separator + fileName + ".png";
            logoFile = new File(realpath);
            if (!logoFile.exists()) {
                throw new FileNotFoundException(realpath + "文件未找到!");
            }
        }
        return logoFile;
    }

    /**
     * 缩略图地址
     *
     * @param path
     * @return
     */
    private String getThumbPath(String path) {
        Assert.notNull(path, getMessage("path.notnull"));
        String suffix = path.substring(path.lastIndexOf("."), path.length());
        return path.replace(suffix, THUMB_SUFFIX.concat(suffix));
    }

    /**
     * 解析请求
     *
     * @param servletRequest
     * @return
     * @throws FileUploadException
     */
    private List<FileItem> parseRequest(HttpServletRequest servletRequest) throws FileUploadException, IOException {
        boolean isM = ServletFileUpload.isMultipartContent(servletRequest);

        DiskFileItemFactory fac = createDiskFileItemFactory(tempFile.getURI().getPath(), servletRequest);
        ServletFileUpload upload = new ServletFileUpload(fac);
        upload.setSizeMax(maxSize);
        return upload.parseRequest(servletRequest);
    }

    /**
     * @param saveDir
     * @return
     */
    private DiskFileItemFactory createDiskFileItemFactory(String saveDir, HttpServletRequest request) {
        DiskFileItemFactory fac = new DiskFileItemFactory();
        fac.setSizeThreshold(THRESHOLD);
        if (saveDir != null) {
            fac.setRepository(new File(saveDir));
        }
        fac.setFileCleaningTracker(FileCleanerCleanup.getFileCleaningTracker(request.getSession().getServletContext()));
        return fac;
    }

    /**
     * @param name
     * @return
     */
    private File getNewFile(String name) {
        File file;
        try {
            String path = location.getURI().getPath();
            if (StringUtils.isNotBlank(AppConfig.getProperty("file.store.location"))) {
                path = AppConfig.getProperty("file.store.location");
            }
            logger.info("start get new file operation,{}",path);
            File folder = new File(path);
            if (!folder.exists() || !folder.isDirectory()) {
                folder.mkdirs();
            }
            logger.info("file name is {}",path.concat(File.separator + name));
            file = new File(path.concat(File.separator + name));
            if (file.exists()) {
                file = new File(path.concat(File.separator + reNameFile(name)));
            } else {
                boolean flag = file.createNewFile();
                if (!flag){
                    logger.error("创建失败！");
                }
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException(e.getLocalizedMessage());
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
     * 生成缩略图
     *
     * @param file
     */
    private void createThumb(File file) {
        Assert.notNull(file, getMessage("file.notexist"));
        try {
            String path = file.getPath();
            String suffix = path.substring(path.lastIndexOf("."), path.length());
            String aPath = path.replace(suffix, THUMB_SUFFIX.concat(suffix));
            File aFile = new File(aPath);
            if (FilesUtils.isImage(file)) {
                ImageUtils.resizeImageWithMaxWidth(file.getPath(), aFile.getPath(), 160);
            }
            logger.debug(getMessage("file.thumb.success", file.getPath()));
        } catch (Exception e) {
            logger.error(getMessage("file.thumb.fail", e.getLocalizedMessage()));
        }
    }

    /**
     * 数据库保存数据
     *
     * @param file
     * @param parentId
     * @param itemInfo
     */
    private FileStore save2DB(File file, String parentId, DiskFileItem itemInfo) {
        try {
            String path = getRealPath(file.getPath());
            FileStore fs = new FileStore();
            fs.setName(itemInfo.getName());
            fs.setPath(path);
            fs.setParentId(parentId);
            fs.setCreateTime(Calendar.getInstance().getTime());
            fs.setFileSize(itemInfo.getSize());
            return fileStoreDao.save(fs);
        } catch (Exception e) {
            throw new RuntimeException(getMessage("file.database.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 数据库保存数据
     *
     * @param file
     * @param parentId
     * @param name
     * @param size
     * @return
     */
    private FileStore save2DB2(File file, String parentId, String name, Date createTime, double size) {
        try {
            String path = getRealPath(file.getPath());
            FileStore fs = new FileStore();
            fs.setName(name);
            fs.setPath(path);
            fs.setParentId(parentId);
            fs.setCreateTime(isNull(createTime) ? Calendar.getInstance().getTime() : createTime);
            fs.setFileSize(size);
            return fileStoreDao.save(fs);
        } catch (Exception e) {
            throw new RuntimeException(getMessage("file.database.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 数据库更新数据
     *
     * @param file
     * @param parentId
     * @param name
     * @param size
     * @return
     */
    private FileStore update2DB2(File file, String parentId, String name, double size, String picId) {
        try {
            String path = getRealPath(file.getPath());
            FileStore fs = fileStoreDao.findOne(picId);
            if (fs != null) {
                fs.setName(name);
                fs.setPath(path);
                fs.setFileSize(size);
            } else {
                fs = new FileStore();
                fs.setName(name);
                fs.setPath(path);
                fs.setFileSize(size);
                fs.setParentId(parentId);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //当天没有照片的picId为当日的日期格式，可以转为createTime存储
                Date date = sdf.parse(picId);
                fs.setCreateTime(date);
            }

            return fileStoreDao.save(fs);
        } catch (Exception e) {
            throw new RuntimeException(getMessage("file.database.error", e.getLocalizedMessage()));
        }
    }

    /**
     * 获取实际地址
     *
     * @param path
     * @return
     * @throws IOException
     */
    public String getRealPath(String path) throws IOException {
        return path.replace(EGOV_DATA, baseLocation.getFile().getPath());
    }


    public void setBaseLocation(Resource baseLocation) {
        this.baseLocation = baseLocation;
    }

    public void setLocation(Resource location) {
        this.location = location;
    }

    public void setTempFile(Resource tempFile) {
        this.tempFile = tempFile;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Transactional
    @Override
    public void onApplicationEvent(FileStoreDeleteEvent event) {
        String parentId = event.getSource();
        if (isNotNull(parentId)) {
            List<FileStore> fileStores = fileStoreDao.findByParentId(parentId);
            for (FileStore fileStore : fileStores) {
                delete(fileStore);
            }
        }
    }

    /**
     * find all filestore by parentid
     *
     * @param parentId
     * @return
     */
    @Override
    public List<FileStore> getFileStores(String parentId) {
        return fileStoreDao.findByParentId(parentId);
    }

}
