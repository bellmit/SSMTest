package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.CameraAutoShotDao;
import cn.gtmap.onemap.platform.entity.video.CameraAutoShot;
import cn.gtmap.onemap.platform.service.CameraAutoShotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * auto shot
 *
 * @author monarchCheng
 * @create 2018-01-05 19:12
 **/
@Service
public class CameraAutoShotServiceImpl implements CameraAutoShotService {

    @Autowired
    private CameraAutoShotDao cameraAutoShotDao;

    @Override
    public CameraAutoShot findById(String id) {
        return cameraAutoShotDao.findOne(id);
    }

    @Override
    public CameraAutoShot save(CameraAutoShot cameraAutoShot){
        return cameraAutoShotDao.save(cameraAutoShot);
    }

    @Override
    public CameraAutoShot findByDetails(String indexCode, Date shotAt, int shotNum, int presetNum) {
        List<CameraAutoShot> cameraAutoShots = cameraAutoShotDao.findByIndexCodeAndShotAtAndPresetNumAndShotNum(indexCode,shotAt,presetNum,shotNum);
        if(cameraAutoShots!=null&&cameraAutoShots.size()>0){
            return cameraAutoShots.get(0);
        }
        return null;
    }
}
