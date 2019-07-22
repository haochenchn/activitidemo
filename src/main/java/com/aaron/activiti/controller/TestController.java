package com.aaron.activiti.controller;

import com.aaron.activiti.model.TaskModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLStreamException;
import java.util.*;

/**
 * @author Aaron
 * @description TODO
 * @date 2019/4/24
 */
public class TestController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private FormService formService;

    /**
     * @throws XMLStreamException
     *             启动流程
     *
     * @author：tuzongxun
     * @Title: startProcess
     * @param @return
     * @return Object
     * @date Mar 17, 2016 2:06:34 PM
     * @throws
     */
    /*@RequestMapping(value = "/getStartFormAndStartProcess.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object startProcess1(HttpServletRequest req)
            throws XMLStreamException {
        Map<String, String[]> formMap = req.getParameterMap();
        String deploymentId = formMap.get("deploymentId")[0];
        // 拿到第一个data_1设置申请人
        String person1 = (String) formMap.get("data_1")[0];
        Map<String, String> map = new HashMap<String, String>();
//        boolean isLogin = this.isLogin(req);
        if (true) {
            if (deploymentId != null) {
                HttpSession session = req.getSession();
                String assginee = (String) session.getAttribute("userName");
                ProcessDefinition pd = repositoryService
                        .createProcessDefinitionQuery()
                        .deploymentId(deploymentId).singleResult();
                String processDefinitionId = pd.getId();
                Map<String, String> formProperties = new HashMap<String, String>();
                Iterator<FlowElement> iterator1 = this
                        .findFlow(processDefinitionId);
                // 取第一个节点，开始节点的行号
                String row = null;
                while (iterator1.hasNext()) {
                    FlowElement flowElement = iterator1.next();
                    row = flowElement.getXmlRowNumber() + "";
                    break;
                }

                // 从request中读取参数然后转换
                Set<Map.Entry<String, String[]>> entrySet = formMap.entrySet();
                for (Map.Entry<String, String[]> entry : entrySet) {
                    String key = entry.getKey();
                    String value = entry.getValue()[0];
                    if (!key.equals("deploymentId")) {
                        String keyString = key + row;
                        formProperties.put(keyString, value);
                    }
                }
                formProperties.put("deploymentId", deploymentId);
                Iterator<FlowElement> iterator = this.findFlow(pd.getId());
                int i = 1;
                while (iterator.hasNext()) {
                    FlowElement flowElement = iterator.next(); // 申请人
                    if (flowElement.getClass().getSimpleName()
                            .equals("UserTask")
                            && i == 1) {
                        UserTask userTask = (UserTask) flowElement;
                        String assignee = userTask.getAssignee();
                        int index1 = assignee.indexOf("{");
                        int index2 = assignee.indexOf("}");
                        formProperties
                                .put(assignee.substring(index1 + 1, index2),
                                        person1);
                        break;
                    }
                }
                identityService.setAuthenticatedUserId(assginee);
                ProcessInstance processInstance = formService
                        .submitStartFormData(processDefinitionId,
                                formProperties);
                map.put("userName",
                        (String) req.getSession().getAttribute("userName"));
                map.put("isLogin", "yes");
                map.put("result", "success");
            } else {
                map.put("result", "fail");
            }
        } else {
            map.put("isLogin", "no");
        }
        return map;
    }

    *//**
     * @throwsXMLStreamException
     *             查询我申请未提交的任务
     *
     * @author：tuzongxun
     * @Title: findTask
     * @param@return
     * @return Object
     * @date Mar 17, 20162:44:11 PM
     * @throws
     *//*
    @RequestMapping(value = "/findFirstTask.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object findFirstTask(HttpServletRequest req)
            throws XMLStreamException {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean isLogin = this.isLogin(req);
        if (isLogin) {
            List<TaskModel> taskList = new ArrayList<TaskModel>();
            HttpSession session = req.getSession();
            String assginee = (String) session.getAttribute("userName");
            List<Task> taskList1 = taskService.createTaskQuery()
                    .taskAssignee(assginee).list();
            if (taskList1 != null && taskList1.size() > 0) {
                for (Task task : taskList1) {
                    TaskModel taskModel = new TaskModel();
                    // 获取部署名
                    String processdefintionId = task.getProcessDefinitionId();
                    ProcessDefinition processDefinition = repositoryService
                            .createProcessDefinitionQuery()
                            .processDefinitionId(processdefintionId)
                            .singleResult();

                    // 根据taskname和节点判断是否是第一个
                    String taskName = task.getName();
                    Iterator<FlowElement> iterator = this
                            .findFlow(processdefintionId);
                    String row0 = null;
                    String eleName0 = null;
                    while (iterator.hasNext()) {
                        FlowElement flowElement0 = iterator.next();
                        // 下一个节点
                        FlowElement flowElement = iterator.next();
                        String eleName = flowElement.getName();
                        if (taskName.equals(eleName)) {
                            row0 = flowElement0.getXmlRowNumber() + "";
                            eleName0 = flowElement0.getClass().getSimpleName();
                            break;
                        }
                    }

                    // 提交申请时
                    if (eleName0.equals("StartEvent")) {
                        ///////////////////////////
                        // 获取流程变量
                        Map<String, Object> variables = runtimeService
                                .getVariables(task.getProcessInstanceId());
                        Set<String> keysSet = variables.keySet();
                        Iterator<String> keySet = keysSet.iterator();
                        Map<String, String> formData = new HashMap<String,String>();
                        taskModel.setLastForm(this
                                .getStartForm1((String) variables
                                        .get("deploymentId")));

                        taskModel.setAssignee(task.getAssignee());
                        taskModel.setCreateTime(task.getCreateTime());
                        taskModel.setId(task.getId());
                        taskModel.setName(task.getName());
                        taskModel.setProcessInstanceId(task
                                .getProcessInstanceId());
                        taskModel
                                .setProcessDefId(task.getProcessDefinitionId());
                        taskModel.setFormKey(task.getFormKey());
                        String deploymentId = processDefinition
                                .getDeploymentId();
                        Deployment deployment = repositoryService
                                .createDeploymentQuery()
                                .deploymentId(deploymentId).singleResult();
                        String deploymentName = deployment.getName();
                        taskModel.setDeploymentName(deploymentName);
                        while (keySet.hasNext()) {
                            String key = keySet.next();
                            String value = (String) variables.get(key);
                            if (key.contains(row0)) {
                                formData.put(key, value);
                            }
                        }
                        taskModel.setFormData(formData);
                        taskList.add(taskModel);
                    }

                }
            }
            map.put("isLogin", "yes");
            map.put("userName",
                    (String) req.getSession().getAttribute("userName"));
            map.put("result", "success");
            map.put("data", taskList);
        } else {
            map.put("isLogin", "no");
        }
        return map;
    }

    *//**
     * @throwsXMLStreamException
     *             完成个人任务
     *
     * @author：tuzongxun
     * @Title: completeTask
     * @param@return
     * @return Object
     * @date Mar 17, 20164:55:31 PM
     * @throws
     *//*
    @RequestMapping(value = "/completeTask.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object completeTask(HttpServletRequest req)
            throws XMLStreamException {
        Map<String, String[]> formMap = req.getParameterMap();
        String taskId = (String) formMap.get("taskId")[0];
        boolean isLogin = this.isLogin(req);
        if (isLogin) {
            if (taskId != null) {
                // 根据taskName和流程节点中的名字判断当前节点之后是否还有任务
                Task task = taskService.createTaskQuery().taskId(taskId)
                        .singleResult();
                String taskName = task.getName();
                Iterator<FlowElement> flows = this.findFlow(task
                        .getProcessDefinitionId());
                String row0 = null;
                Map<String, Object> formProperties = new HashMap<String, Object>();
                while (flows.hasNext()) {
                    FlowElement flowElement = flows.next();
                    // 找到当前节点，查询出下下一个节点是否是结束节点，如果不是则需要设置下一个任务人，否则直接保存相关流程变量结束
                    // 同时要查处当前任务的行数用来设置流程变量名称
                    if (taskName.equals(flowElement.getName())) {
                        // 设置行号
                        row0 = flowElement.getXmlRowNumber() + "";
                        FlowElement flowElement3 = flows.next();
                        System.out.println("SequenceFlow".equals(flowElement3
                                .getClass().getSimpleName()));
                        if ("SequenceFlow".equals(flowElement3.getClass()
                                .getSimpleName())) {
                            SequenceFlow seq = (SequenceFlow) flowElement3;
                            System.out.println(seq.getConditionExpression()
                                    + "," + seq.getName());
                        }
                        FlowElement flowElement2 = flows.next();
                        // 当前任务不是最后一个任务,流程没有结束,需要设置下一个处理人
                        if (flowElement2 != null
                                && !("EndEvent".equals(flowElement2.getClass()
                                .getSimpleName()) && !("SequenceFlow"
                                .equals(flowElement2.getClass()
                                        .getSimpleName())))) {
                            UserTask userTask = (UserTask) flowElement2;
                            // 获取userTask中的
                            String assignee = userTask.getAssignee();
                            int index1 = assignee.indexOf("{");
                            int index2 = assignee.indexOf("}");
                            String person1 = (String) formMap.get("data_1")[0];
                            formProperties.put(
                                    assignee.substring(index1 + 1, index2),
                                    person1);
                        }
                        break;
                    }
                }
                // 从request中读取参数然后转换0
                Set<Entry<String, String[]>> entrySet = formMap.entrySet();
                for (Entry<String, String[]> entry : entrySet) {
                    String key = entry.getKey() + row0;
                    String value = entry.getValue()[0];
                    if (!key.equals("taskId")) {
                        formProperties.put(key, value);
                    }
                }
                taskService.complete(taskId, formProperties);
                ;
            }
        }
        return null;
    }*/
}
