/*
 * Project:  onemap
 * Module:   onemap-platform
 * File:     PagesController.java
 * Modifier: Ray Zhang
 * Modified: 2013-7-22 上午9:08:29
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.platform.controller.portal2;

import cn.gtmap.onemap.platform.service.TemplateService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-7-22 上午9:08:29
 */
@Controller
@RequestMapping("/portal2")
public class PagesController {

    @Autowired
    private TemplateService templateService;

    private String URL_PREFIX = "http://";


	@RequestMapping("")
	public String page(){
        return "portal2/home";
	}
	
	@RequestMapping("/api/{page}")
	public String api(@PathVariable String page,@RequestParam(value="type", required = false) String type,Model model){
            model.addAttribute("type",type);
            return "portal2/api/api_" + page;
	}

    @RequestMapping("/onemap")
    public String map(){
        return "portal2/map";
    }

    @RequestMapping("/statistic")
    public String statistic(Model model,HttpServletRequest request){
        String content = templateService.getTemplate("statistic.json");
        HashMap nodesMap = JSON.parseObject(content,HashMap.class);
        List parentList = (List) nodesMap.get("nodes");
        List list = new ArrayList();
        for(int i=0;i<parentList.size();i++)
        {
            JSONObject object = (JSONObject) parentList.get(i);
            HashMap map = new HashMap();
            String url=object.get("url").toString();
            if(StringUtils.isNotBlank(url)&&url.indexOf(URL_PREFIX)<0)
                url = URL_PREFIX + request.getServerName() + ":" + request.getServerPort() + url;
            map.put("id",object.get("id"));
            map.put("name",object.get("name"));
            map.put("url",url);
            map.put("children",getChildren(object,request));
            list.add(map);
        }
        model.addAttribute("nodes",list);
        return "portal2/statisReports";

    }
    @RequestMapping("/forbidden")
    public String unAuthorize(){
        return "portal2/forbidden";
    }

    private List getChildren(JSONObject parent,HttpServletRequest request){

        List list = new ArrayList();
        List children = (List) parent.get("children");
        if(children.size()>0)
        {
            for(int i=0;i<children.size();i++)
            {
                JSONObject child = (JSONObject) children.get(i);
                HashMap map= new HashMap();
                String url="";
                if(StringUtils.isNotBlank(child.get("url").toString()))
                    url = URL_PREFIX + request.getServerName() + ":" + request.getServerPort() + child.get("url");
                map.put("id",child.get("id"));
                map.put("name",child.get("name"));
                map.put("url",url);
                map.put("parentId",child.get("parentId"));
                map.put("children",getChildren(child,request));
                list.add(map);
            }
        }
        return list;

    }
	
}
