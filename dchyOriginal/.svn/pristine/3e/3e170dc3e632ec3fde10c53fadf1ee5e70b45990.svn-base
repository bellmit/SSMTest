/*
 * Project:  onemap
 * Module:   server
 * File:     CtrlUtil.java
 * Modifier: xyang
 * Modified: 2013-05-14 05:42:32
 *
 * Copyright (c) 2013 Gtmap Ltd. All Rights Reserved.
 *
 * Copying of this document or code and giving it to others and the
 * use or communication of the contents thereof, are forbidden without
 * expressed authority. Offenders are liable to the payment of damages.
 * All rights reserved in the event of the grant of a invention patent or the
 * registration of a utility model, design or code.
 */

package cn.gtmap.onemap.server.web.console;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller Util Class, add result ret to View
 *
 * @author <a href="mailto:rayzy1991@163.com">Ray Zhang</a>
 * @version V1.0, 13-3-29 下午5:08
 */
public class CtrlUtil {

	public static void success(RedirectAttributes ra) {
        ra.addFlashAttribute("ret", true);
    }
	
	public static void success(Model model){
		 model.addAttribute("ret", true);
	}
	
    public static void failed(Model model, String msg) {
        model.addAttribute("msg", msg);
        model.addAttribute("ret", false);
    }

    public static void redirectFailed(RedirectAttributes ra, String msg) {
        ra.addFlashAttribute("ret", false);
        ra.addFlashAttribute("msg", msg);
    }

}
