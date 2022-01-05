/*
 * Project:  onemap
 * Module:   server
 * File:     TaskController.java
 * Modifier: xyang
 * Modified: 2013-05-10 02:30:37
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

import cn.gtmap.onemap.server.service.TaskManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-5-2
 */
@Controller()
@RequestMapping("/console/task")
public class TaskController {
    @Autowired
    private TaskManager taskManager;

    @RequestMapping("index")
    public String index(Model model) {
        model.addAttribute("tasks", taskManager.getTasks());
        return "/console/task/index";
    }

    @RequestMapping(value = "stop", method = RequestMethod.GET)
    public String stop(@RequestParam(value = "id") String id) {
        taskManager.stopTask(id);
        return "redirect:/console/task/index";
    }
}
