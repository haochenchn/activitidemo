package com.aaron.activiti.controller;

import com.aaron.activiti.model.TaskModel;
import com.aaron.activiti.service.IMyTaskService;
import com.aaron.activiti.util.Const;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Aaron
 * @description TODO
 * @date 2019/8/25
 */
@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private IMyTaskService myTaskService;

    @Autowired
    private TaskService taskService;
    /**
     * 读取运行中的流程实例
     *
     * @return
     */
    @RequestMapping(value = "/list/todoTasks")
    public String todoTasks(HttpServletRequest request, Model model){
        Object attribute = request.getSession().getAttribute(Const.SESSION_ACCOUNT);
        if (attribute != null){
            List<TaskModel> taskModels = myTaskService.findTodoTasks(((User)attribute).getId());
            model.addAttribute("taskModels", taskModels);
            return "task-list";
        }
        return "error";
    }

    /**
     * 签收任务
     */
    @RequestMapping(value = "/claim/{id}")
    public String claim(@PathVariable("id") String taskId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Object attribute = request.getSession().getAttribute(Const.SESSION_ACCOUNT);
        if (attribute == null){
            return "";
        }
        taskService.claim(taskId, ((User)attribute).getId());
        redirectAttributes.addFlashAttribute("message", "任务已签收");
        return "redirect:/task/list/todoTasks";
    }

    /**
     * 完成任务
     */
    @RequestMapping(value = "complete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public String complete(@PathVariable("id") String taskId, RedirectAttributes redirectAttributes) {
        try {
            taskService.complete(taskId, null);
            redirectAttributes.addFlashAttribute("message", "任务已办理");
            return "redirect:/task/list/todoTasks";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "任务办理失败");
            return "redirect:/task/list/todoTasks";
        }
    }
}
