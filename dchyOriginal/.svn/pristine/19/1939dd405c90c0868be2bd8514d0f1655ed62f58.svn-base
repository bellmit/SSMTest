package cn.gtmap.onemap.platform.service;

import cn.gtmap.onemap.platform.entity.FileStore;
import cn.gtmap.onemap.platform.entity.video.Recognition;
import org.dom4j.io.STAXEventReader;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-25 下午5:31
 */
public interface FileStoreService {

    /**
     * get one
     *
     * @param id
     * @return
     */
    FileStore get(String id);

    /**
     * save
     *
     * @param request
     * @param parentId
     * @return
     */
    FileStore save(HttpServletRequest request, String parentId);

    /**
     * save upload file , parse by CommonsMultipartResolver    <br/>
     *
     * @param request
     * @param parentId
     * @return
     * @see org.springframework.web.multipart.commons.CommonsMultipartResolver
     */
    FileStore save2(MultipartHttpServletRequest request, String parentId,String picId);

    /**
     *
     * @param file
     * @param parentId
     * @return
     */
    FileStore save3(File file, String parentId);

    /***
     *
     * @param file
     * @param parentId
     * @return
     */
    FileStore saveWithThumb(File file, String parentId);

    /**
     * 根据proid/status获取预警文件
     * @param proID
     * @param status
     * @param page
     * @param size
     * @return
     */
    Page<FileStore> getWarnFile(String proID, int status, int page, int size) throws IOException;


    /***
     * save file store with create time
     * @param file
     * @param parentId
     * @param datetime
     * @return
     */
    FileStore saveWithCreateTime(File file, String parentId,Date datetime);

    /**
     * delete
     *
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * delete
     *
     * @param fileStore
     * @return
     */
    boolean delete(FileStore fileStore);

    /**
     * get ids by parentid
     *
     * @param parentId
     * @return
     */
    String[] getFileIds(String parentId);


    /**
     *
     * @param proId
     * @param date
     * @return
     */
    String[] getFileByDate(String proId,String date);

    /**
     * get file by id
     *
     * @param id
     * @return
     */
    File getFile(String id) throws IOException;

    /**
     *
     * @param parentId
     * @return
     * @throws IOException
     */
    List<File> getFiles(String parentId) throws IOException;

    /**
     * 获取表中最新的一条记录
     * @param parentId
     * @return
     */
    FileStore getFileStoreNewest(String parentId);

    /**
     * get thumb
     *
     * @param id
     * @return
     */
    File getThumb(String id) throws IOException;

    /***
     * 在filestore目录建文件
     * @param name
     * @return
     * @throws IOException
     */
    File createNewFile(String name) throws IOException;

    /***
     * 转换乱码的文件名
     * @param name
     * @return
     * @throws Exception
     */
    String parseMessyName(String name) throws Exception;

    /**
     * 上传logo
     * @param request
     * @return
     */
    Map uploadLog(MultipartHttpServletRequest request);

    /**
     * 获取logo文件
     * @param fileName
     * @return
     */
    File getLogoFile(String fileName) throws FileNotFoundException;

    FileStore getUpdatedImgByProId(String proId);

    /**
     * find all filestore by parentid
     * @param parentId
     * @return
     */
    List<FileStore> getFileStores(String parentId);

}
