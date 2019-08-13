package com.aaron.activiti.service.impl;

import com.aaron.activiti.model.ApplyModel;
import com.aaron.activiti.model.ProcessModel;
import com.aaron.activiti.service.IProcessService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Aaron
 * @description TODO
 * @date 2019/8/8
 */
@Service("processService")
public class ProcessServiceImpl implements IProcessService {
    Logger logger = Logger.getLogger(getClass());
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Override
    public ProcessInstance startProcessByKey(ApplyModel applyModel, Map<String, Object> variables) {
        if(StringUtils.isEmpty(applyModel.getKey())){
            logger.error("流程key为空，无法发起");
            return null;
        }else if (StringUtils.isEmpty(applyModel.getAppPerson())){
            logger.error("请设置流程发起人");
            return null;
        }
        identityService.setAuthenticatedUserId(applyModel.getAppPerson());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(applyModel.getKey(), applyModel.getBusinessKey(), variables);
        return processInstance;
    }

    @Override
    public List<ProcessModel> findRunningProcessInstaces() {
        List<ProcessModel> processModels = new ArrayList<>();
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().active().orderByProcessDefinitionId().desc().list();
        for (ProcessInstance processInstance: list){
            ProcessModel procModel = new ProcessModel();
            procModel.setId(processInstance.getProcessDefinitionId());
            procModel.setDeploymentId(processInstance.getDeploymentId());
            procModel.setVersion(processInstance.getProcessDefinitionVersion());
            procModel.setProcInstanceId(processInstance.getProcessInstanceId());
            procModel.setSuspended(processInstance.isSuspended());
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
            procModel.setAppPerson(historicProcessInstance.getStartUserId());
            procModel.setStartTime(historicProcessInstance.getStartTime());
            procModel.setEndTime(historicProcessInstance.getEndTime());
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).active().orderByTaskCreateTime().desc().listPage(0, 1);
            procModel.setCurrentTaskId(tasks.get(0).getId());
            procModel.setCurrentTaskName(tasks.get(0).getName());
            procModel.setCurrentAssign(tasks.get(0).getAssignee());
            procModel.setCurretTaskTime(tasks.get(0).getCreateTime());
            processModels.add(procModel);
        }
        return processModels;
    }

}
