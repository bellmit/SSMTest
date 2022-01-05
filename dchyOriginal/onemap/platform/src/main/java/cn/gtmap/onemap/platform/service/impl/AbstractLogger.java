package cn.gtmap.onemap.platform.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:zhayuwen@gtmap.cn">zhayuwen</a>
 * @version V1.0, 2016-08-19 14:41:00
 */
public abstract class AbstractLogger<T> extends BaseLogger {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 获取JPA的查询规范，子类亦可覆写此方法自己定义具体内容
     * @param condition
     * @return
     */
    Specification getSpecification(final Map condition){
       return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Predicate predicate =  criteriaBuilder.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                if(condition.get("year")!=null && !StringUtils.isEmpty(condition.get("year").toString())){
                    expressions.add(criteriaBuilder.equal(root.<String>get("year"), condition.get("year").toString()));
                }
                if(condition.get("month")!=null&&!StringUtils.isEmpty(condition.get("month").toString())){
                    expressions.add(criteriaBuilder.equal(root.<String>get("month"), condition.get("month").toString()));
                }
                if(condition.get("regionName")!=null&&!StringUtils.isEmpty(condition.get("regionName").toString())){
                    expressions.add(criteriaBuilder.equal(root.<String>get("regionName"), condition.get("regionName").toString()));
                }
                if(condition.get("startDate")!=null && !StringUtils.isEmpty(condition.get("startDate").toString())){
                    try {
                        expressions.add(criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createAt"), dateFormat.parse(condition.get("startDate").toString())));
                    } catch (ParseException e) {
                        logger.error("错误："+e);
                    }
                }

                if(condition.get("endDate")!=null && !StringUtils.isEmpty(condition.get("endDate").toString())){
                    try {
                        expressions.add(criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createAt"), dateFormat.parse(condition.get("endDate").toString())));
                    } catch (ParseException e) {
                        logger.error("错误："+e);
                    }
                }

                if(condition.get("username")!=null && !StringUtils.isEmpty(condition.get("username").toString())){
                    expressions.add(criteriaBuilder.like(root.<String>get("userName"), "%"+condition.get("username").toString()+"%"));
                }
                if(condition.get("cameraName")!=null && !StringUtils.isEmpty(condition.get("cameraName").toString())){
                    expressions.add(criteriaBuilder.like(root.<String>get("cameraName"), "%"+condition.get("cameraName").toString()+"%"));
                }
                if(condition.get("userDpet")!=null && !StringUtils.isEmpty(condition.get("userDpet").toString())){
                    try {
                        expressions.add(criteriaBuilder.like(root.<String>get("userDept"), "%"+condition.get("userDpet").toString()+"%"));
                    }catch (Exception e){
                        logger.error("错误："+e);
                    }
                }


                return predicate;
            }
        };
    }
}
