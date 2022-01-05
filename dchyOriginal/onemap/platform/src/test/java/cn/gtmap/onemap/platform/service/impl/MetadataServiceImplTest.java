package cn.gtmap.onemap.platform.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gtmap.onemap.model.MapQuery;
import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.service.MetadataService;

public class MetadataServiceImplTest  extends BaseServiceTest{
	@Autowired
    private MetadataService metadataService;
	
	@Test
    public void testFindMaps() throws Exception {
        MapQuery query = MapQuery.query();
        System.out.println(metadataService.findMaps("1", query, null));
    }
}
