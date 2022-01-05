/*
 * Project:  onemap
 * Module:   server
 * File:     AuthController.java
 * Modifier: xyang
 * Modified: 2013-06-04 03:19:26
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */
package cn.gtmap.onemap.server.web.console.security;

import cn.gtmap.onemap.model.User;
import cn.gtmap.onemap.security.IdentityService;
import cn.gtmap.onemap.security.SessionProvider;
import cn.gtmap.onemap.security.ex.SecurityException;
import cn.gtmap.onemap.server.web.console.CtrlUtil;
import cn.gtmap.onemap.service.UserService;
import com.gtis.config.AppConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.suggest.fst.ExternalRefSorter;
import org.hsqldb.lib.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Externalizable;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:rayzy1991@163.com">zhangyang</a>
 * @version V1.0, 2013-5-31 下午1:34:00
 */
@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    IdentityService identityService;

    @Autowired
    UserService userService;

    @Autowired
    SessionProvider sessionProvider;

    @RequestMapping("login")
    public String loginPage(Model model, @RequestParam(value = "url", required = false) String url, HttpServletRequest req) {
        model.addAttribute("url", url);
        Cookie[] cookies;
        if ((cookies = req.getCookies()) != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("username")) {
                    model.addAttribute("username", c.getValue());
                }
            }
        }
        return "/login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(HttpServletRequest req, RedirectAttributes ra, HttpServletResponse resp) {
        String name = req.getParameter("username");
        String pwd = req.getParameter("password");
        String url = req.getParameter("url");
        //白名单配置
        String whiteKey = (String) AppConfig.getProperties().get("white.key");
        boolean flag = false;
        if (StringUtils.isNotBlank(whiteKey)){
            String[] whiteList = whiteKey.split(",");
            for (String whiteValue : whiteList) {
                if (url.contains(whiteValue)) {
                    flag = true;
                    break;
                }
            }
        }else{
            flag = true;
        }

        if (flag){
            if (req.getParameter("remember") != null) {
                Cookie cookie = new Cookie("username", name);
                cookie.setMaxAge(216000);
                resp.addCookie(cookie);
            }

            try {
                String token = identityService.login(name, pwd);
                sessionProvider.createSession(token, req, resp);
            } catch (SecurityException e) {
                CtrlUtil.redirectFailed(ra, e.getMessage());
                return "redirect:/login?url=" + url;
            }
            return "redirect:" + url;
        }else{
            return "redirect:/login?url=" + url;
        }



    }

    @RequestMapping(value = "logout")
    public String logout(HttpServletRequest req, HttpServletResponse resp) {
        sessionProvider.destroySession(req, resp);
        String url = req.getParameter("url");
        url = url == null ? "" : "?url=" + url;
        return "redirect:/login" + url;
    }

    @RequestMapping("ajax/userConfig")
    public String userEdit(HttpServletRequest req, HttpServletResponse response, Model model) {
        model.addAttribute("user", userService.getUser(sessionProvider.getSession(req, response).getUserId()));
        return "user-config";
    }

    @RequestMapping("ajax/oldPassCheck")
    @ResponseBody
    public List<Object> pwdCheck(HttpServletRequest req, HttpServletResponse response) {
        User user = userService.getUser(sessionProvider.getSession(req, response).getUserId());
        List<Object> back = new ArrayList<Object>();
        back.add(req.getParameter("fieldId"));
        if (user.isPasswordMatch(req.getParameter("fieldValue"))) {
            back.add(true);
        }
        return back;
    }

    @ModelAttribute("user")
    public User getUser(@RequestParam(value = "userId", required = false) String userId) {
        return userId == null ? new User() : userService.getUser(userId);
    }

    @RequestMapping(value = "ajax/saveUserConfig", method = RequestMethod.POST)
    @ResponseBody
    public String saveUserConfig(@ModelAttribute("user") User user) {
        try {
            String pwd = user.getPassword();
            user.setHashPassword(pwd);
            userService.saveUser(user);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }
}
