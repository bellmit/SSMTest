package cn.gtmap.onemap.server.log.audit;

import cn.gtmap.onemap.security.IdentityService;
import cn.gtmap.onemap.security.User;
import cn.gtmap.onemap.service.AuditService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-6-17
 */
@Aspect
public class IdentityServiceAspect {
    private AuditService auditService;
    private IdentityService identityService;

    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Around(value = "execution(* cn.gtmap.onemap.server.service.impl.IdentityServiceImpl.login(..)) && args(userName,password)", argNames = "joinPoint,userName,password")
    public Object login(ProceedingJoinPoint joinPoint, String userName, String password) throws Throwable {
        try {
            Object retVal = joinPoint.proceed();
            auditService.audit("用户登陆", "用户 " + userName + " 登陆成功");
            return retVal;
        } catch (Throwable e) {
            auditService.audit("用户登陆", "用户 " + userName + " 登陆失败," + e.getMessage());
            throw e;
        }
    }

    @Before("execution(* cn.gtmap.onemap.server.service.impl.IdentityServiceImpl.logout(..)) && args(token)")
    public void logout(String token) throws Throwable {
        User user = identityService.getUserByToken(token);
        if (user != null) {
            auditService.audit("用户注销", "用户 " + user.getName() + " 注销成功");
        }
    }
}
