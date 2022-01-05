package cn.gtmap.msurveyplat.portalol.web.util;


import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;

import java.util.Iterator;
import java.util.Map;

/**
 * @param
 * @return
 * @description 2020/11/28 entity工具类
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 */
public class ExampleUtil extends Example {

    public ExampleUtil(Class<?> entityClass, Map<String,Object> param){
        super(entityClass);
        if(param!=null&&param.size()>0){
            Criteria criteria = this.createCriteria();
            for (Iterator it = param.keySet().iterator(); it.hasNext(); ) {
                String key = it.next().toString();
                criteria.andEqualTo(key,param.get(key));
            }
        }
    }

}