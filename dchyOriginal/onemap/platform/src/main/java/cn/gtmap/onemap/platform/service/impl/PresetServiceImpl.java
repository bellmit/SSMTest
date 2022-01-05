package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.dao.PresetDao;
import cn.gtmap.onemap.platform.dao.ProjectJpaDao;
import cn.gtmap.onemap.platform.entity.video.*;
import cn.gtmap.onemap.platform.service.PresetService;
import cn.gtmap.onemap.platform.service.ProjectService;
import cn.gtmap.onemap.platform.service.VideoManager;
import cn.gtmap.onemap.platform.service.VideoMetadataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2016/3/2 18:00
 */
@Service
public class PresetServiceImpl extends BaseLogger implements PresetService {

    @Autowired
    private PresetDao presetDao;

    @Autowired
    private ProjectJpaDao projectDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private VideoMetadataService videoMetadataService;

    @Autowired
    private VideoManager videoManager;

    /***
     * 新增预设位
     * @param proId
     * @param indexCode
     * @return
     */
    @Transactional
    @Override
    public Preset insert(String id, String proId, String indexCode) {
        Preset preset = new Preset();
        preset.setIndexCode(indexCode);
        preset.setProId(id);
        preset.setCreateAt(new Date());
        Camera camera = videoMetadataService.getByIndexCode(indexCode);
        VideoPlats.Plat plat = videoManager.getPlat(camera.getPlatform());
        if (plat != null && StringUtils.isNotBlank(plat.getPlatType()) && plat.getPlatType().equalsIgnoreCase("ivs")) {
            try {
                Ptz ptz = videoManager.getVideoService(camera.getPlatform()).getPTZ(indexCode);
                preset.setX(ptz.getP());
                preset.setY(ptz.getT());
                preset.setZ(Double.valueOf(ptz.getZ()));
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
        int presetNo = 51;  //基数50,此处不可设为100以上
        try {
            List<Preset> presetsOfDevice = presetDao.findByIndexCode(indexCode);
            if (presetsOfDevice.size() > 0) {
                Collections.sort(presetsOfDevice, new Comparator<Preset>() {
                    @Override
                    public int compare(Preset preset, Preset preset2) {
                        return preset2.getPresetNo() - preset.getPresetNo();
                    }
                });
                presetNo = presetsOfDevice.get(0).getPresetNo() + 1;
            }
            preset.setPresetNo(presetNo);
            preset.setName("预设位".concat(String.valueOf(presetNo > 50 ? (presetNo - 50) : presetNo)));
            preset = presetDao.save(preset);
            projectService.insertRefAboutPreset(proId, indexCode, preset.getId());
            return preset;
        } catch (Exception e) {
            logger.error("新增预设位信息出现异常：{}", e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public Preset insertPanoramaPreset(String proId, String indexCode) {
        List<Project> projects = projectDao.findByProId(proId);
        if(projects.size()>0){
            return this.insert(projects.get(0).getId(), projects.get(0).getProId(), indexCode);
        } else {  //没有全景预设位项目的先插入项目（此项目为无效的项目）在插入预设位
            Project project = new Project();
            project.setProId(proId);
            project.setCameraId(null);
            project.setName("全景初始化预设位项目");
            project.setProName("全景初始化预设位项目");
            project.setProId(proId);
            project.setEnabled(false);  //必须为无效状态，否则会在项目列表展示出来的
            project.setStatus(0);
            project.setProType("---");
            project.setCreateAt(new Date());
            try {
                Project project1 = projectDao.save(project);
               return this.insert(project1.getId(), project1.getProId(), indexCode);
            } catch (Exception e) {
                logger.error("创建全景项目出现异常：", e.toString());
            }
        }
        return null;
    }

    /***
     * insert preset
     * @param preset
     * @return
     */
    @Override
    public Preset insert(Preset preset) {
        return presetDao.saveAndFlush(preset);
    }

    /***
     * 根据proid和设备编码找出所有相关预设位
     * @param proId
     * @param indexCode
     * @return
     */
    @Override
    public List<Preset> find(final String proId, final String indexCode) {
        List<Preset> presets = presetDao.findAll(new Specification<Preset>() {
            @Override
            public Predicate toPredicate(Root<Preset> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(criteriaBuilder.equal(root.<String>get("proId"), proId));
                expressions.add(criteriaBuilder.equal(root.<String>get("indexCode"), indexCode));
                return predicate;
            }
        });
        return presets;
    }

    /***
     *
     * @param indexCode
     * @return
     */
    @Override
    public List<Preset> findByDevice(String indexCode) {
        return presetDao.findByIndexCode(indexCode);
    }

    /***
     *
     * @param proid
     * @return
     */
    @Override
    public List<Preset> findByPro(String proid) {
        return presetDao.findByProId(proid);
    }

    /***
     *
     * @param proid
     */
    @Override
    public boolean deleteByProId(String proid) {
        List<Preset> presets = presetDao.findByProId(proid);
        for (Preset preset : presets) {
            presetDao.delete(preset);
        }
        return true;
    }

    /***
     *
     * @param indexCode
     */
    @Override
    public boolean deleteByDevice(String indexCode) {
        List<Preset> presets = presetDao.findByIndexCode(indexCode);
        for (Preset preset : presets) {
            presetDao.delete(preset);
        }
        return true;
    }

    /***
     *
     * @param id
     */
    @Override
    public boolean deleteById(String id, String proId) {
        Preset preset = presetDao.findOne(id);
        projectService.delRefAboutPreset(proId, preset.getIndexCode(), preset.getId());
        presetDao.delete(preset);
        return true;
    }


    @Override
    public void setEnabled(boolean enabled, String id) {
        Preset preset = presetDao.findOne(id);
        preset.setEnabled(enabled);
        presetDao.save(preset);
    }

    @Override
    public void delete(Preset preset) {
        presetDao.delete(preset);
    }
}
