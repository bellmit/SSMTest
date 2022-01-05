package cn.gtmap.msurveyplat.promanage.utils;

import cn.gtmap.msurveyplat.common.util.CommonUtil;
import cn.gtmap.msurveyplat.promanage.core.mapper.DchyXmglChxmMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

public class SlbhUtil {
    private static final FastDateFormat nyrFormat = FastDateFormat.getInstance("yyyyMMdd");

    public static String generateSlbh(String prefix, String suffix) {
        String xqfbbhLsh = obtainXqfbbh();
        //需求发布编号为当天日期家三位数序列,如果序列为0的话跳过
        if (StringUtils.equals("000", xqfbbhLsh)) {
            xqfbbhLsh = obtainXqfbbh();
        }
        String xqfbbh = (CommonUtil.ternaryOperator(prefix,StringUtils.EMPTY) + nyrFormat.format(new Date()) + xqfbbhLsh).replaceAll(" ", "") + CommonUtil.ternaryOperator(suffix,StringUtils.EMPTY);
        return xqfbbh;
    }

    private static String obtainXqfbbh() {
        String xqfbbh = "";
        DchyXmglChxmMapper dchyXmglChxmMapper = (DchyXmglChxmMapper) Container.getBean("dchyXmglChxmMapper");
        String xqfbbhLsh = dchyXmglChxmMapper.queryMaxXqfbbh();
        if (xqfbbhLsh.length() > 3) {
            xqfbbh = xqfbbhLsh.substring(xqfbbhLsh.length() - 3, xqfbbhLsh.length());
        } else {
            xqfbbh = xqfbbhLsh;
        }
        return xqfbbh;
    }
}
