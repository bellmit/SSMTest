package cn.gtmap.onemap.platform.service.impl;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.entity.TplType;
import cn.gtmap.onemap.platform.service.TplTypeService;

public class TplServiceTest extends BaseServiceTest{
	@Autowired TplTypeService tplService;
	
	@Test
	public void testCreateFolder(){
		TplType type = new TplType();
		type.setName("土地利用规划");
		type.setDescription("2009年建设用地审批专题应用，包括各种影像电视里看见了就放到空间看见啦开机速度撒的艰苦撒的撒撒胡椒倒萨。");
		type.setCreateAt(new Date());
		
		type = tplService.saveTplType(type);
		System.out.println(type);
	}


}
