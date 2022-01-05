package cn.gtmap.msurveyplat.serviceol.core.service.mq.service;

import cn.gtmap.msurveyplat.common.dto.*;

public interface PushDataToMqService {

    /**
     * @param mlkid
     * @param sqxxid
     * @return
     * @description 2020/12/14 名录库办结时进行消息推送
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushMlkMsgToMq(String mlkid, String sqxxid);

    /**
     * @param mlkid
     * @return
     * @description 2020/12/14 管理单位进行考评的时候进行消息推送
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushKpMsgToMq(String mlkid);

    void pushMlkXxToMq(String mlkid);

    /**
     * @param chxmid
     * @return
     * @description 2020/12/14 建设单位对测绘项目进行评价的时候进行消息推送
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushXmpjMsgToMq(String chxmid);

    /**
     * @param chxmid
     * @return
     * @description 2020/12/14
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushJsdwFbfwMsgToMqSave(String chxmid);

    /**
     * @param jsdwFbxfwModel 推送时删除的实体
     * @return
     * @description 2021/4/16
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushJsdwFbfwMsgToMqDel(DchyXmglChxmDto jsdwFbxfwModel);

    void pushMlkDtoToMq(DchyXmglMlkDto dchyXmglMlkDto);
    /**
     * @param mlkid
     * @return
     * @description 2021/4/15 冻结名录库，把状态的修改信息推到线下
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushSfdjMlkMsgToMq(String mlkid);

    /**
     * @param dchyXmglCgJcDto
     * @return
     * @description 2021/3/14 推送成果包文件到线下解析
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushCgjcMsgToMq(DchyXmglCgJcDto dchyXmglCgJcDto);

    /**
     * @param dchyXmglCgTjDto
     * @return
     * @description 2021/3/14 推送成果提交数据
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushCgtjMsgToMq(DchyXmglCgtjDto dchyXmglCgTjDto);

    /**
     * @param dchyXmglChxmDto
     * @return
     * @description 2020/12/15 线下库同步数据到线上备份库的实现类
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushSlxxMsgToMq(DchyXmglChxmDto dchyXmglChxmDto);

    /**
     * 备案合同变更同步到线下
     * @param dchyXmglChxmDto
     */
    void pushHtbgMsgToMq(DchyXmglChxmDto dchyXmglChxmDto);

    /**
     * 线上办结推线下处理
     *
     * @param dchyXmglZxbjDto
     */
    void pushOnlineCompleteToXx(DchyXmglZxbjDto dchyXmglZxbjDto);

    void pushOnlineGcViewToXx(DchyXmglZxbjDto dchyXmglZxbjDto);
    /**
     * 推送线上文件至线下
     *
     * @param dchyXmglChxmDto
     */
    void pushOnlineWjFileToxx(DchyXmglChxmDto dchyXmglChxmDto);

    /**
     * @param dchyXmglJcsjsqDto
     * @return
     * @description 2021/4/12 推送基础数据申请数据
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushJcsjMsgToMq(DchyXmglJcsjsqDto dchyXmglJcsjsqDto);

    /**
     * @param dchyXmglXxtxDto
     * @return
     * @description 2021/4/12 推送消息提醒数据
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushXxtxMsgToMq(DchyXmglXxtxDto dchyXmglXxtxDto);

    /**
     * @param dchyXmglTjfxDto
     * @return
     * @description 2021/4/21 线上统计分析
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushTjfxMsgToMq(DchyXmglTjfxDto dchyXmglTjfxDto);

     /**
      * @param dchyXmglJsdwlrDto
      * @return
      * @description 2021/5/10 推送线上注册的建设单位信息
      * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
      */
    void pushJsdwlrMsgToMq(DchyXmglJsdwlrDto dchyXmglJsdwlrDto);
}
