package cn.gtmap.msurveyplat.portalol.core.service;

import com.gtis.fileCenter.ex.NodeExistsException;
import com.gtis.fileCenter.ex.NodeNotFoundException;
import com.gtis.fileCenter.model.Downloader;
import com.gtis.fileCenter.model.Node;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2020/12/2 10:08
 * @description
 */
public interface FileService {

    Node uploadFile(File file, Integer parentId) throws NodeNotFoundException, NodeExistsException, IOException;

    Node uploadFile(File file, Integer parentId, String name) throws NodeNotFoundException, NodeExistsException, IOException;

    Node uploadFile(InputStream is, Integer parentId, String name) throws NodeNotFoundException, NodeExistsException, IOException;

    Node uploadFile(InputStream is, Integer parentId, String name, String path, boolean cover, boolean rename) throws NodeNotFoundException, NodeExistsException, IOException;

    Node uploadFile(File file, Integer parentId, String name, String path, boolean cover, boolean rename) throws NodeNotFoundException, NodeExistsException, IOException;

    void download(Integer nodeId, Downloader downloader) throws NodeNotFoundException, IOException;

    void download(Integer nodeId, String path, Downloader downloader) throws NodeNotFoundException, IOException;

    void downloadToFile(Integer nodeId, String savePath) throws NodeNotFoundException, IOException;

    void downloadToFile(Integer nodeId, String path, String savePath) throws NodeNotFoundException, IOException;

}
