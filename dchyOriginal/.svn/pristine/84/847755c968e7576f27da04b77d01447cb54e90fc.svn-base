package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.service.DBAService;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DBAServiceImpl  implements DBAService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public   <T extends Object>Page<T> searchByPage(String sql,Pageable pageable,Class entityClass){
        sql=sql.replace("from","FROM");
        String[] sqlArr = sql.split("FROM");
        String countSql = "SELECT COUNT(*) ";
        //查询sql
        for(int i=1;i<sqlArr.length;i++){
            countSql=countSql+" FROM "+sqlArr[i];
        }

        Query countQuery = em.createNativeQuery(countSql);
        Query query = em.createNativeQuery(sql,entityClass);

        //获取单个结果
        Object countObject =countQuery.getResultList().get(0);
        int total = Integer.parseInt(countObject.toString());
        //List content = total > pageable.getOffset() ? query.getResultList() : Collections.emptyList();
        List  content = query.getResultList() ;
        //分页
        int start=0;
        int end =0;
        if(total>pageable.getOffset()){
            start = (pageable.getPageNumber()-1)*(pageable.getPageSize());
            end = (pageable.getPageNumber())*pageable.getPageSize();
        }else {
            start = (pageable.getPageNumber()-1)*(pageable.getPageSize());
            end =total;
        }
        content = content.subList(start,end);
        return new PageImpl(content, pageable, total);
    }

    @Override
    public List<Map> search(String sql) {
        Query query = em.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List rows = query.getResultList();
        List<Map> result = new ArrayList<Map>();
        for (Object row:rows){
            Map map = (Map)row;
            result.add(map);
        }
        return result;
    }

    @Override
    public String generateWhereIn(List<String> data){
        String whereIn="";
        if(data.size()>0){
            whereIn = "'"+data.get(0).toString()+"'";
        }else {
            return null;
        }
        for(int i=1;i<data.size();i++){
            String item =data.get(i).toString();
            whereIn = whereIn+" , '"+ item+"'";
        }
        return whereIn;
    }

    @Override
    public String generateWhereIn(List<String> data,String fieldName){
        String whereIn="";
        for(int i=0;i<data.size();i=i+999){
            if(whereIn==""){
                whereIn = whereIn +fieldName+"  IN (";
            }else {
                whereIn = whereIn+ " OR "+fieldName+" IN (";
            }
            String items ="'"+data.get(i)+"'";
            for(int j=i+1;j<i+999;j++){
                if(j==data.size()){
                    break;
                }
                String temp = data.get(j);
                items += ","+"'"+temp+"'";
            }
            whereIn +=items+")";
        }
        return whereIn;
    }

    public String toDataYYYYmmDD(Date date){
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
        String dateStr= df.format(date);
        String result=" to_date('"+dateStr+"','yyyy-MM-dd') ";
        return result;
    }


}
