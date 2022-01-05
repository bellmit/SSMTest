package cn.gtmap.msurveyplat.promanage.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjclpz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglJcgz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglLcgpzDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglSjclpzDto;

import java.util.List;
import java.util.Map;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/15
 * @desc IntelliJ IDEA: IntelliJ IDEA
 */
public interface ConfigureSystemService {

    /**
     * add Configure
     *
     * @param receiveConfigDo ReceiveConfigModel
     * @return Boolean
     */
    String addConfigure(DchyXmglSjclpz receiveConfigDo);

    /**
     * edit Configure info
     *
     * @param receiveConfigDo ReceiveConfigModel
     * @return Boolean
     */
    Boolean editConfigure(DchyXmglSjclpz receiveConfigDo);

    /**
     * drop Configure
     *
     * @param id String
     * @return boolean
     */
    boolean dropConfigure(String id);

    /**
     * findConfigure
     *
     * @return List<ReceiveConfigModel>
     */
    List<Map<String, Object>> findConfigure();

    /**
     * drop Configure By Ids
     *
     * @param ids List<String>
     * @return boolean
     */
    boolean dropConfigureByIds(List<String> ids);

    /**
     * save Or Update List
     *
     * @param receiveConfigDoList List<ReceiveConfigModel>
     * @return boolean
     */
    boolean saveOrUpdateList(List<DchyXmglSjclpz> receiveConfigDoList);

    /**
     * query Clsx
     *
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> queryClsx();

    /**
     * query Ssmk
     *
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> querySsmk();

    /**
     * find Achievement Configuration Tree
     *
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> findAchievementTree();

    /**
     * save Or Update Achievement Tree
     *
     * @param dtos List<DchyXmglLcgpzDto>
     * @return List<Map < String, Object>>
     */
    boolean saveOrUpdateAchievementTree(List<DchyXmglLcgpzDto> dtos);

    /**
     * @return
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param: null
     * @time 2021/4/21 17:08
     * @description 更新测量成果检查规则
     */
    boolean updateClcgjcgz(List<DchyXmglJcgz> dchyXmglJcgzList);

    /**
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author <a href="mailto:xiejianan@gtmap.cn">xiejianan</a>
     * @param:
     * @time 2021/4/21 11:03
     * @description 获取成果检查规则
     */
    List<Map<String, Object>> getCgtjjcgzList();

    /**
     * sava Or Update Dtos
     *
     * @param dchyXmglSjclpzDtoList List<DchyXmglSjclpzDto>
     * @return boolean
     */
    boolean savaOrUpdateDtos(List<DchyXmglSjclpzDto> dchyXmglSjclpzDtoList);

}
