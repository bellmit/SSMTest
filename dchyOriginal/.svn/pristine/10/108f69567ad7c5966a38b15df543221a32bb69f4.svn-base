package cn.gtmap.msurveyplat.server.service.share;

import cn.gtmap.msurveyplat.common.dto.GxchgcClsxDTO;
import cn.gtmap.msurveyplat.common.dto.GxchgcxxDTO;
import cn.gtmap.msurveyplat.common.dto.UserInfo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
 * @version 1.0, 2021/1/11
 * @description 共享测绘工程信息接口
 */
public interface GxchgcxxService {

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param page 第几页
     * @param size 每页大小
     * @param gxchgcxxDTO 测绘工程信息参数对象
     * @return
     * @description 获取共享测绘工程分页信息
     */
    Page<Map> getDchyXmglChgcxxByPage(int page, int size, GxchgcxxDTO gxchgcxxDTO);

    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param page 第几页
     * @param size 每页大小
     * @param gxchgcxxDTO 测绘工程信息参数对象
     * @return
     * @description 获取共享测量成果分页信息
     */
    Page<Map> getDchyXmglClcgxxByPage(int page, int size, GxchgcxxDTO gxchgcxxDTO);



    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param page 第几页
     * @param size 每页大小
     * @param gxchgcxxDTO 测绘工程信息参数对象
     * @return
     * @description 获取共享测量成果分页信息
     */
    Page<Map> getDchyClcgxxByPage(int page, int size, GxchgcxxDTO gxchgcxxDTO,UserInfo userInfo);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param chgcid 测绘工程ID
     * @return
     * @description 获取测绘工程测绘事项信息
     */
    List<GxchgcClsxDTO> getGxChgcClsxListById(String chgcid);


    /**
     * @author <a href="mailto:liujie@gtmap.cn">liujie</a>
     * @param babh 工程编号
     * @param gxywid 共享业务ID
     * @param userInfo 用户信息
     * @return
     * @description 获取共享测绘工程材料下载地址
     */
    Map getGxchgcclDownUrl(String babh, String gxywid, UserInfo userInfo);
}
