package com.aaron.activiti.service;

import com.aaron.activiti.model.TaskModel;

import java.util.List;

/**
 * @author Aaron
 * @description TODO
 * @date 2019/8/25
 */
public interface IMyTaskService {

    List<TaskModel> findTodoTasks(String userId);
}
