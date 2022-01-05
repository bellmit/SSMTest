package cn.gtmap.onemap.platform.dao;

import cn.gtmap.onemap.platform.entity.video.Recognition;
import com.gtis.common.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * dao for camera_recognition
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/11/14 (c) Copyright gtmap Corp.
 */
public interface CameraRecognitionDao extends JpaRepository<Recognition, String> {

    List<Recognition> findByEnabledOrderByCreateAtDesc(boolean enabled);

    List<Recognition> findByOriginFileAndEnabled(String originFileId, boolean enabled);

    List<Recognition> findByOriginFile(String originFileId);

    Recognition findOneByOriginFile(String originFileId);

    Recognition findRecognitionByOriginFile(String originFileId);
}
