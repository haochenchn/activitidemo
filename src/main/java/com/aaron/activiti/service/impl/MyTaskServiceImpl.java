package com.aaron.activiti.service.impl;

import com.aaron.activiti.model.TaskModel;
import com.aaron.activiti.service.IMyTaskService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron
 * @description TODO
 * @date 2019/8/25
 */
@Service
public class MyTaskServiceImpl implements IMyTaskService {
    @Autowired
    private TaskService taskService;

    @Override
    public List<TaskModel> findTodoTasks(String userId) {
        List<TaskModel> taskModels = new ArrayList<>();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
        for (Task task: taskList){
            TaskModel taskModel = new TaskModel();
            taskModel.setId(task.getId());
            taskModel.setProcessInstanceId(task.getProcessInstanceId());
            taskModel.setProcessDefId(task.getProcessDefinitionId());
            taskModel.setName(task.getName());
            taskModel.setCreateTime(task.getCreateTime());
            if(task.getAssignee() != null){
                taskModel.setAssignee(task.getAssignee());
            }
            taskModels.add(taskModel);

        }
        return taskModels;
    }
}
