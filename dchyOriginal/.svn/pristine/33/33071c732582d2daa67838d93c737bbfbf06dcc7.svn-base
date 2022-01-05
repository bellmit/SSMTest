package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.entity.FileStore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-9-11 下午4:35
 */
public class FileStoreServiceImplTest extends BaseServiceTest {

    @Autowired
    private FileStoreServiceImpl fileStoreService;

    @Test
    public void testSave() throws Exception {
        FileStore fs = new FileStore();
        fs.setName("tt");
        fs.setFileSize(11100);
        fs.setParentId("sss");
        fs.setPath("dddd");
        fileStoreService.save(fs);
         //

    }


}
