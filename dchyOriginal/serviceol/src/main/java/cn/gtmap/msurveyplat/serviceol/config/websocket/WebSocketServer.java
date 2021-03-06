package cn.gtmap.msurveyplat.serviceol.config.websocket;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglYhxx;
import cn.gtmap.msurveyplat.common.dto.ResponseMessage;
import cn.gtmap.msurveyplat.common.util.ResponseUtil;
import cn.gtmap.msurveyplat.serviceol.model.UserInfo;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/yhxxtx", encoders = {WebSocketServerEncoder.class})
@Component
public class WebSocketServer {

    @PostConstruct
    public void init() {
        System.out.println("websocket 加载");
    }

    private static EntityMapper entityMapper;

    @Autowired
    public void setUserService(@Qualifier("entityMapper") EntityMapper entityMapper) {
        WebSocketServer.entityMapper = entityMapper;
    }

    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();
    private static ConcurrentMap<String, CopyOnWriteArraySet<String>> userId2SessionId = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, String> sessionId2UserId = Maps.newConcurrentMap();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入{}，当前连接数为：{}", session.getId(),cnt);

        CasAuthenticationToken casAuthenticationToken = (CasAuthenticationToken)session.getUserPrincipal();
        int i = 0;
        if (null != casAuthenticationToken ) {
            String userId = ((UserInfo) casAuthenticationToken.getPrincipal()).getId();
            CopyOnWriteArraySet<String> userIdSessions = userId2SessionId.get(userId);
            if (null == userIdSessions) {
                userIdSessions = new CopyOnWriteArraySet<>();
                userIdSessions.add(session.getId());
            }
            userId2SessionId.put(userId, userIdSessions);
            sessionId2UserId.put(session.getId(), userId);
            SessionSet.add(session);

            Example example = new Example(DchyXmglYhxx.class);
            example.createCriteria().andEqualTo("dqzt", Constants.INVALID).andEqualTo("jsyhid", userId);
            i = entityMapper.countByExample(example);
        }
        Map yhxxtxMap = Maps.newHashMap();
        ResponseMessage message = ResponseUtil.wrapSuccessResponse();
        yhxxtxMap.put("wdqsl", i);
        message.setData(yhxxtxMap);
        SendMessage(session, message);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SessionSet.remove(session);
        String userId = sessionId2UserId.get(session.getId());
        if (StringUtils.isNotBlank(userId)) {
            CopyOnWriteArraySet<String> userIdSessions = userId2SessionId.get(userId);
            if (CollectionUtils.isEmpty(userIdSessions)) {
                userIdSessions.remove(session.getId());
                userId2SessionId.remove(userId);
            }
        }
        sessionId2UserId.remove(session.getId());
        int cnt = OnlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        log.info("来自客户端的消息：{}", message);
        ResponseMessage responseMessage = ResponseUtil.wrapSuccessResponse();
        SendMessage(session, responseMessage);
    }

    /**
     * 出现错误  `
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}", error, session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, ResponseMessage message) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (Exception e) {
            log.error("发送消息出错：{}", e);
        }
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (Exception e) {
            log.error("发送消息出错：{}", e);
        }
    }

    /**
     * 群发消息
     *
     * @param message
     * @throws IOException
     */
    public static void BroadCastInfo(String message) throws IOException {
        for (Session session : SessionSet) {
            if (session.isOpen()) {
                SendMessage(session, message);
            }
        }
    }

    /**
     * 指定Session发送消息
     *
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void SendMessage(String message, String sessionId) throws IOException {
        Session session = null;
        for (Session s : SessionSet) {
            if (s.getId().equals(sessionId)) {
                session = s;
                break;
            }
        }
        if (session != null) {
            SendMessage(session, message);
        } else {
            log.warn("没有找到你指定ID的会话：{}", sessionId);
        }
    }

}