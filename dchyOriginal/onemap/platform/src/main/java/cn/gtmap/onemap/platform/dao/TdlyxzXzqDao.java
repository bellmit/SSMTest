package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.TdlyxzXzq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TdlyxzXzqDao extends JpaRepository<TdlyxzXzq, String>, JpaSpecificationExecutor<TdlyxzXzq> {
}
