package cn.gtmap.msurveyplat.portalol.core.service;

import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglTzgg;

public interface DchyXmglTzggService {

    boolean insertTzggxx(DchyXmglTzgg dchyXmglTzgg);

    DchyXmglTzgg getDchyXmglTzggByid(String tzggid);
}
