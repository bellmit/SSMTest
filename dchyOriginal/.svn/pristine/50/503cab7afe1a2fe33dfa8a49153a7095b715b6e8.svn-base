package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.service.ProjectService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016/4/5
 */
public class ProjectServiceImplTest extends BaseServiceTest {
    @Autowired
    private ProjectService projectService;

    @Test
    public void testGetPage(){
//        projectService.getPage(1,10);
    }

    @Test
    public void testRemoveProjectRecord(){
        String proid = "f153e640cc704028daad53e63fe70006";
        String[] fileStoreIds = {"015499bb4df64028daad5499b07f0002"};
        projectService.removeProjectRecord(proid, fileStoreIds);
    }
}
