package cn.gtmap.msurveyplat.portalol.service;

public interface TokenService {

    public Object getToken(String code);

    public boolean tokenVaild(String token);

    public String getTokenByCode(String code);
}
