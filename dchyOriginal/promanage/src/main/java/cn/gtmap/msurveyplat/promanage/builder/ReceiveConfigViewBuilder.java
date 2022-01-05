package cn.gtmap.msurveyplat.promanage.builder;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglSjclpz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglSjclpzDto;
import cn.gtmap.msurveyplat.promanage.utils.BeanUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author 6k2
 * @email <a href="mailto:laukaye@qq.com">Email To</a>
 * @date 2021/4/15
 * @desc IntelliJ IDEA: IntelliJ IDEA
 */
public class ReceiveConfigViewBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveConfigViewBuilder.class);

    public static DchyXmglSjclpz dto2Model(DchyXmglSjclpzDto dto) {
        DchyXmglSjclpz model = new DchyXmglSjclpz();
        try {
            BeanUtils.copyProperties(dto, model, "ssclsxList");
        } catch (Exception e) {
            logger.error("错误原因{}：", e);
        }
        return model;
    }

    public static List<DchyXmglSjclpz> dtos2Entities(List<DchyXmglSjclpzDto> dtos) {
        List<DchyXmglSjclpz> entities = Lists.newArrayList();
        dtos.forEach(dto -> {
            DchyXmglSjclpz entity = dto2Model(dto);
            entities.add(entity);
        });
        return entities;
    }
}
