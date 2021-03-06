package cn.gtmap.msurveyplat.promanage.core.service.mq.service;

import cn.gtmap.msurveyplat.common.dto.*;

import java.util.List;

public interface PushDataToMqService {

    /**
     * @param dchyXmglChxmDto
     * @return
     * @description 2020/12/15 线下库同步数据到线上备份库的实现类
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushSlxxMsgToMq(DchyXmglChxmDto dchyXmglChxmDto);

    /**
     * @param dchyXmglCgJcDto
     * @return
     * @description 2021/3/14 线上的成果检查结果返回
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushCgjcResultToMq(DchyXmglCgJcDto dchyXmglCgJcDto);

    /**
     * @param dchyXmglCgtjDto
     * @return
     * @description 2021/3/14 线上的成果提交结果返回
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushCgtjResultToMq(DchyXmglCgtjDto dchyXmglCgtjDto);

    /**
     * @param dchyXmglCgccDto
     * @return
     * @description 2021/3/15 推送线下的成果抽查数据
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushCgccResultToMq(DchyXmglCgccDto dchyXmglCgccDto);

    /**
     * 在线办结结果返回给网上办事大厅
     *
     * @param dchyXmglZxbjDto
     */
    void pushZxbjResultTo(DchyXmglZxbjDto dchyXmglZxbjDto);

    /**
     * 推送在线成果查看给网上办事大厅
     * @param dchyXmglZxbjDto
     */
    void pushCgViewResultToXs(DchyXmglZxbjDto dchyXmglZxbjDto);

    /**
     * @param dchyXmglJcsjsqDto
     * @return
     * @description 2021/4/2 办结的基础数据申请推送到线上
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushJcsjResultTo(DchyXmglJcsjsqDto dchyXmglJcsjsqDto);

    /**
     * @param dchyXmglXxtxDto
     * @return
     * @description 2021/4/12 推送消息提醒的数据
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushXxtxResultTo(DchyXmglXxtxDto dchyXmglXxtxDto);

    /**
     * @param dchyXmglTjfxDto
     * @return
     * @description 2021/4/21 线上统计分析
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushTjfxMsgToMq(DchyXmglTjfxDto dchyXmglTjfxDto);

    /**
     * @param dchyXmglChxmListDto
     * @return
     * @description 2021/4/21 线下备案拆分，推送线上备份
     * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
     */
    void pushBaxxMsgToMq(DchyXmglChxmListDto dchyXmglChxmListDto);

    /**
     * @param dchyXmglJsdwlrDto
     * @return
     * @description 2021/5/10 推送线下的建设单位录入信息
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    void pushJsdwlrMsgToMq(DchyXmglJsdwlrDto dchyXmglJsdwlrDto);

}
