package com.aaron.activiti.controller;

import com.aaron.activiti.model.*;
import com.aaron.activiti.service.ActivitiService;
import com.aaron.activiti.util.Const;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Aaron
 * @description TODO
 * @date 2019/4/17
 */
@RequestMapping("/activiti")
@Controller
public class ActivitiController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    /**
     * 新建一个空模型
     */
    @RequestMapping(value = "/newModel")
    public void newModel(HttpServletRequest request, HttpServletResponse response) {
        //初始化一个空模型
        Model model = repositoryService.newModel();
        ObjectMapper objectMapper = new ObjectMapper();
        //设置一些默认信息
        String name = "new-process";
        String description = "";
        int revision = 1;
        String key = "process";

        try {
            ObjectNode modelNode = objectMapper.createObjectNode();
            modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);

            model.setName(name);
            model.setKey(key);
            model.setMetaInfo(modelNode.toString());

            repositoryService.saveModel(model);
            String id = model.getId();

            //完善ModelEditorSource
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace",
                    "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            repositoryService.addModelEditorSource(id,editorNode.toString().getBytes("utf-8"));
            //跳转到流程编辑页面
            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建模型
     * 创建模型时相关的有act_re_model和act_ge_bytearray两个。
     * 成功创建模型后可以看到model表中会有一条数据，同时在bytearray表中也会同时生成两条对应的数据。而model表中会存入这两条数据的id，从而产生关联。
     */
    @RequestMapping(value = "/createModel")
    public void create(ActivitiModel activiti,HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<String, String>();
        Model newModel = repositoryService.newModel();
        User user = getAccount(request);
        if (user != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode modelObjectNode = objectMapper.createObjectNode();
                modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME,
                        activiti.getName());
                modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
                modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,
                        StringUtils.defaultString(activiti.getDescription()));
                newModel.setMetaInfo(modelObjectNode.toString());
                newModel.setName(activiti.getName());
                newModel.setKey(StringUtils.defaultString(activiti.getKey()));
                repositoryService.saveModel(newModel);
                ObjectNode editorNode = objectMapper.createObjectNode();
                editorNode.put("id", "canvas");
                editorNode.put("resourceId", "canvas");
                ObjectNode stencilSetNode = objectMapper.createObjectNode();
                stencilSetNode.put("namespace",
                        "http://b3mn.org/stencilset/bpmn2.0#");
                editorNode.put("stencilset", stencilSetNode);
                repositoryService.addModelEditorSource(newModel.getId(),
                        editorNode.toString().getBytes("utf-8"));
                response.sendRedirect(request.getContextPath() +
                        "/modeler.html?modelId="
                        + newModel.getId());
            } catch (Exception e) {
                e.getStackTrace();
            }
            /*map.put("isLogin", "yes");
            map.put("userName", "chenhao");
            map.put("path", "/service/editor?id=");
            map.put("modelId", newModel.getId());
            return map;*/
        }
    }

    /**
     * 模型列表
     */
    @RequestMapping(value = "/modelList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object modelList(HttpServletRequest req) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<ActivitiModel> modelList = new ArrayList<ActivitiModel>();
        User user = getAccount(req);
        if (user != null) {
            try {
                List<Model> modelList1 = repositoryService.createModelQuery()
                        .list();
                if (modelList1 != null && modelList1.size() > 0) {
                    for (Model model : modelList1) {
                        ActivitiModel activitiModel = new ActivitiModel();
                        activitiModel.setId(model.getId());
                        activitiModel.setCreateTime(model.getCreateTime());
                        activitiModel.setDescription(model.getMetaInfo());
                        activitiModel.setKey(model.getKey());
                        activitiModel.setLastUpdateTime(model
                                .getLastUpdateTime());
                        activitiModel.setName(model.getName());
                        activitiModel.setVersion(model.getVersion());
                        modelList.add(activitiModel);
                    }
                }
                map.put("result", "success");
                map.put("data", modelList);
            } catch (Exception e) {
                e.getStackTrace();
            }
            map.put("isLogin", "yes");
        }else {
            map.put("isLogin", "no");
        }
        return map;
    }
    /**
     * 根据模型id部署流程定义
     * 与流程定义相关的有3张表，分别是act_ge_bytearray、act_re_procdef和act_re_deployment。当然了，如果更准确的说，在自定义流程中，流程定义需要用到流程模型相关的数据，也可以说流程定义相关的就有四张表，也包括model表。
     * 根据前端传入的deploymentId部署流程定义，这里还是使用repositoryService进行操作，大致上的过程就是根据deploymentId查询出创建模型时生成的相关文件，然后进行一定的转换后进行部署
     * 成功部署以后，bytearray表中会再次增加两条数据，同时act_re_procdef和act_re_deployment这两张表也都会各自出现一条对应的数据
     */
    @RequestMapping(value = "/deploye", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object deploye(@RequestBody ActivitiModel activitiModel,HttpServletRequest req) {
        Map<String, Object> map = new HashMap<String, Object>();
        String modelId = activitiModel.getId();
        User user = getAccount(req);
        if (user != null) {
            try {
                Model modelData = repositoryService.getModel(modelId);
                ObjectNode modelNode = (ObjectNode) new ObjectMapper()
                        .readTree(repositoryService
                                .getModelEditorSource(modelData.getId()));
                byte[] bpmnBytes = null;
                BpmnModel model = new BpmnJsonConverter()
                        .convertToBpmnModel(modelNode);
                bpmnBytes = new BpmnXMLConverter().convertToXML(model);
                String processName = modelData.getName() + ".bpmn20.xml";
                Deployment deployment = repositoryService.createDeployment()
                        .name(modelData.getName())
                        .addString(processName, new String(bpmnBytes,"UTF-8")).deploy();
//                -Dfile.encoding="UTF-8"
                if (deployment != null && deployment.getId() != null) {
                    map.put("result", "success");
                }
            } catch (Exception e) {
                e.printStackTrace();
                map.put("result", "error");
            }
            map.put("isLogin", "yes");
        }else {
            map.put("isLogin", "no");
        }
        return map;
    }

    /**
     * 流程定义列表
     *
     */
    @RequestMapping(value = "/processList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object processList(HttpServletRequest req) {
        Map<String, Object> map = new HashMap<>();
        User user = getAccount(req);
        if (user != null) {
            List<ProcessModel> processList = new ArrayList<>();
            List<ProcessDefinition> processList1 = repositoryService
                    .createProcessDefinitionQuery().list();
            for (ProcessDefinition pro : processList1) {
                ProcessModel processModel = new ProcessModel();
                processModel.setDeploymentId(pro.getDeploymentId());
                processModel.setId(pro.getId());
                processModel.setKey(pro.getKey());
                processModel.setResourceName(pro.getResourceName());
                processModel.setVersion(pro.getVersion());
                processModel.setName(pro.getName());
                processModel.setDiagramResourceName(pro
                        .getDiagramResourceName());
                processList.add(processModel);
            }
            map.put("isLogin", "yes");
            map.put("userName",user.getFirstName());
            map.put("result", "success");
            map.put("data", processList);
        }else {
            map.put("isLogin", "no");
        }
        return map;
    }


    /**
     * 查询流程节点
     * 获取流程部署时xml文件里的各个节点相关信息，用来辨别当前是哪个节点，下一节点又是什么
     */
    public Iterator<FlowElement> findFlow(String processDefId)
            throws XMLStreamException {
        List<ProcessDefinition> lists = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefId)
                .orderByProcessDefinitionVersion().desc().list();
        ProcessDefinition processDefinition = lists.get(0);
        processDefinition.getCategory();
        String resourceName = processDefinition.getResourceName();
        InputStream inputStream = repositoryService.getResourceAsStream(
                processDefinition.getDeploymentId(), resourceName);
        BpmnXMLConverter converter = new BpmnXMLConverter();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
        BpmnModel bpmnModel = converter.convertToBpmnModel(reader);
        Process process = bpmnModel.getMainProcess();
        Collection<FlowElement> elements = process.getFlowElements();
        Iterator<FlowElement> iterator = elements.iterator();
        return iterator;
    }

    /**
     * 启动流程
     * 成功启动一个流程实例后，会看到(act_ru_execution、act_ru_identitylink、act_ru_task、act_ru_variable以及act_hi_actinst、act_hi_detail、
     * act_hi_indentitylink、act_hi_procinst、act_hi_taskinst、act_hi_varinst)表中都有了数据。
     * 除开variable和varinst中的数据条数是根据对应的流程变量多少来定的，其他都是增加了一条数据
     */
    @RequestMapping(value = "/startProcess", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
   public Object startProcess(@RequestBody ApplyModel applyModel,
                               HttpServletRequest req) throws XMLStreamException {
        Map<String, String> map = new HashMap<>();
        User user = getAccount(req);
        if (user != null) {
            if (applyModel != null) {
                String processKey = applyModel.getKey();
                String processDefId = applyModel.getProDefId();
                // //////////////////////////
                Iterator<FlowElement> iterator = this.findFlow(processDefId);
                Map<String, Object> varables = new HashMap<>();
                int i = 1;
                while (iterator.hasNext()) {
                    FlowElement flowElement = iterator.next();
                    // 申请人
                    if (flowElement.getClass().getSimpleName()
                            .equals("UserTask")
                            && i == 1) {
                        UserTask userTask = (UserTask) flowElement;
                        String assignee = userTask.getAssignee();
                        int index1 = assignee.indexOf("{");
                        int index2 = assignee.indexOf("}");
                        if(index1 > -1 && index2 > -1) {
                            varables.put(assignee.substring(index1 + 1, index2),
                                    applyModel.getProPerson());
                        }else {
                            varables.put(assignee, applyModel.getAppPerson());
                        }
                        varables.put("cause", applyModel.getCause());
                        varables.put("content", applyModel.getContent());
                        varables.put("taskType", applyModel.getName());
                        i++;
                        // 下一个处理人
                    } else if (flowElement.getClass().getSimpleName()
                            .equals("UserTask")
                            && i == 2) {
                        UserTask userTask = (UserTask) flowElement;
                        String assignee = userTask.getAssignee();
                        int index1 = assignee.indexOf("{");
                        int index2 = assignee.indexOf("}");
                        if(index1 > -1 && index2 > -1) {
                            varables.put(assignee.substring(index1 + 1, index2),
                                    applyModel.getProPerson());
                        }else {
                            varables.put(assignee, applyModel.getAppPerson());
                        }
                        break;
                    }
                }
                // ///////////////////////////
                runtimeService.startProcessInstanceByKey(processKey, varables);
                map.put("userName",user.getId());
                map.put("result", "success");
            } else {
                map.put("result", "fail");
            }
            map.put("isLogin", "yes");
        }else{
            map.put("isLogin", "no");
        }

        return map;
    }

    /**
     * 查询个人任务
     * 流程启动以后会同时生成一个流程实例和用户任务，这个用户任务保存在act_ru_task和act_hi_task表中
     */
    @RequestMapping(value = "/findTask", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object findTask(HttpServletRequest req) throws XMLStreamException {
        Map<String, Object> map = new HashMap<>();
        User user = getAccount(req);
        if (user != null) {
            List<TaskModel> taskList = new ArrayList<>();
            String assginee = user.getId();
            List<Task> taskList1 = taskService.createTaskQuery()
                    .taskAssignee(assginee).list();
            if (taskList1 != null && taskList1.size() > 0) {
                for (Task task : taskList1) {
                    TaskModel taskModel = new TaskModel();
                    taskModel.setAssignee(task.getAssignee());
                    taskModel.setCreateTime(task.getCreateTime());
                    taskModel.setId(task.getId());
                    taskModel.setName(task.getName());
                    taskModel.setProcessInstanceId(task.getProcessInstanceId());
                    taskModel.setProcessDefId(task.getProcessDefinitionId());
                    // 获取流程变量
                    Map<String, Object> variables = runtimeService
                            .getVariables(task.getProcessInstanceId());
                    Set<String> keysSet = variables.keySet();
                    Iterator<String> keySet = keysSet.iterator();
                    while (keySet.hasNext()) {
                        String key = keySet.next();
                        if (key.endsWith("cause")) {
                            taskModel.setCause((String) variables.get("cause"));
                        } else if (key.endsWith("content")) {
                            taskModel.setContent((String) variables
                                    .get("content"));
                        } else if (key.endsWith("taskType")) {
                            taskModel.setTaskType((String) variables
                                    .get("taskType"));
                        } else if (!assginee.equals(variables.get(key))) {
                            // 想办法查询是否还有下一个任务节点
                            Iterator<FlowElement> iterator = this.findFlow(task
                                    .getProcessDefinitionId());
                            while (iterator.hasNext()) {
                                FlowElement flowElement = iterator.next();
                                String classNames = flowElement.getClass()
                                        .getSimpleName();
                                if (classNames.equals("UserTask")) {
                                    UserTask userTask = (UserTask) flowElement;
                                    String assginee11 = userTask.getAssignee();
                                    /*String assginee12 = assginee11.substring(
                                            assginee11.indexOf("{") + 1,
                                            assginee11.indexOf("}"));*/
                                    String assginee12 = assginee11;
                                            String assignee13 = (String) variables
                                            .get(assginee12);
                                    if (assginee.equals(assignee13)) {
                                        // 看下下一个节点是什么
                                        iterator.next();
                                        FlowElement flowElement2 = iterator
                                                .next();
                                        String classNames1 = flowElement2
                                                .getClass().getSimpleName();
                                        // 设置下一个任务人
                                        if (!(classNames1.equals("EndEvent"))) {
                                            UserTask userTask2 = (UserTask) flowElement2;
                                            String assginee21 = userTask2
                                                    .getAssignee();
                                            /*String assginee22 = assginee21.substring(
                                                            assginee21.indexOf("{") + 1,
                                                            assginee21.indexOf("}"));*/
                                            String assginee22 = assginee21;
                                            String assignee23 = (String) variables
                                                    .get(assginee22);
                                            taskModel.setNextPerson(assignee23);
                                        }
                                    }


                                }
                            }
                            // //////////
                        }
                    }
                    taskList.add(taskModel);
                }
            }
            map.put("isLogin", "yes");
            map.put("userName",user.getId());
            map.put("result", "success");
            map.put("data", taskList);
        } else {
            map.put("isLogin", "no");
        }
        return map;
    }

    /**
     * 完成个人任务
     * 完成任务使用taskService调用complete方法来完成，一旦正确调用了这个方法，当前任务就会结束，进入到下一个任务，如果当前任务已经是最后一个任务，则整个流程结束。
     * 对于已经结束的任务，act_ru_task中所存在的那条对应数据会被删除，取而代之的是，对应的act_hi_taskinst中的那条数据会增加结束时间。
     * 上边所说的正确调用是指，如果当前任务的下一个任务设有个人任务变量或者组任务变量，那么提交的时候必须有对应的变量数据，否则会抛出异常，完成任务失败。（但是，如果下一个任务没有设置这些，提交时依旧填了流程变量是不会出错的）
     * 下边的例子中，之所以还查询了流程节点的信息，并做了相关的处理，是为了实现针对任意数量任务的流程都能正常运行，否则不需要这么麻烦。
     */
    @RequestMapping(value = "/completeTask", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object completeTask(@RequestBody TaskModel taskModel,
                               HttpServletRequest req) throws XMLStreamException {
        User user = getAccount(req);
        if (user != null) {
            String taskId = taskModel.getId();
            // 1、查task
            Task task = taskService.createTaskQuery().taskId(taskId)
                    .singleResult();
            if(task == null){
                return "未找到任务"+taskId;
            }
            // 2、查variables
            Map<String, Object> variables = runtimeService.getVariables(task
                    .getProcessInstanceId());
            Set<String> keysSet = variables.keySet();
            Iterator<String> keySet = keysSet.iterator();
            Map<String, Object> variables1 = new HashMap<>();
            String assignee = task.getAssignee();
            // 判断之后是否还有任务
            // ////////////////
            while (keySet.hasNext()) {
                String key = keySet.next();
                if (key.equals("cause") || key.equals("content")
                        || key.equals("taskType")) {
                    continue;
                } else if (!(assignee.equals(variables.get(key)))) {
                    // 3、查flowElement
                    Iterator<FlowElement> iterator = this.findFlow(task
                            .getProcessDefinitionId());
                    while (iterator.hasNext()) {
                        FlowElement flowElement = iterator.next();
                        String classNames = flowElement.getClass()
                                .getSimpleName();
                        if (classNames.equals("UserTask")) {
                            UserTask userTask = (UserTask) flowElement;
                            String assginee11 = userTask.getAssignee();
                            /*String assginee12 = assginee11.substring(
                                    assginee11.indexOf("{") + 1,
                                    assginee11.indexOf("}"));*/
                            String assginee12 = assginee11;
                            String assignee13 = (String) variables.get(assginee12);
                            if (assignee.equals(assignee13)) {
                                // 看下下一个节点是什么
                                iterator.next();
                                FlowElement flowElement2 = iterator.next();
                                String classNames1 = flowElement2.getClass()
                                        .getSimpleName();
                                // 设置下一个任务人
                                if (!(classNames1.equals("EndEvent"))) {
                                    UserTask userTask2 = (UserTask) flowElement2;
                                    String assginee21 = userTask2.getAssignee();
                                    /*String assginee22 = assginee21.substring(
                                            assginee21.indexOf("{") + 1,
                                            assginee21.indexOf("}"));*/
                                    String assginee22 = assginee21;
                                    // String assignee23 = (String) variables
                                    // .get(assginee22);
                                    String assignee23 = taskModel
                                            .getNextPerson();
                                    variables1.put(assginee22, assignee23);
                                }
                            }
                        }
                    }
                }
            }
            taskService.complete(taskId, variables1);
        }
        return user.getFirstName()+"已完成任务";
    }

    /**
     * 查询我的历史任务
     * 需要注意的是，历史表中存在并非是单一类型的数据，就拿历史任务表来说，里边既有已经结束的任务，也有还没有结束的任务。
     * 如果要单独查询结束了的任务，就可以调用finished()方法
     */
    @RequestMapping(value = "/hisTask", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object hisTask(HttpServletRequest req) {
        Map<String, Object> map = new HashMap<>();
        User user = getAccount(req);
        if (user != null) {
            String assignee = user.getId();
            List<HistoricTaskInstance> hisTaskList1 = historyService
                    .createHistoricTaskInstanceQuery().taskAssignee(assignee)
                    .finished().list();
            List<HisTaskModel> hisTaskList = new ArrayList<>();
            for (HistoricTaskInstance hisTask : hisTaskList1) {
                HisTaskModel hisModel = new HisTaskModel();
                List<HistoricVariableInstance> hisList = historyService
                        .createHistoricVariableInstanceQuery()
                        .processInstanceId(hisTask.getProcessInstanceId())
                        .list();
                for (HistoricVariableInstance hisVariable : hisList) {
                    String name = hisVariable.getVariableName();
                    if (name.equals("content")) {
                        hisModel.setContent((String) hisVariable.getValue());
                    } else if (name.equals("cause")) {
                        hisModel.setCause((String) hisVariable.getValue());
                    } else if (name.equals("taskType")) {
                        hisModel.setTaskType((String) hisVariable.getValue());
                    }
                }
                hisModel.setAssignee(hisTask.getAssignee());
                hisModel.setEndTime(hisTask.getEndTime());
                hisModel.setId(hisTask.getId());
                hisModel.setName(hisTask.getName());
                hisModel.setProcessInstanceId(hisTask.getProcessInstanceId());
                hisModel.setStartTime(hisTask.getStartTime());
                hisTaskList.add(hisModel);
            }
            map.put("isLogin", "yes");
            map.put("userName",user.getId());
            map.put("result", "success");
            map.put("data", hisTaskList);
        } else {
            map.put("isLogin", "no");
        }
        return map;
    }

    /**
     * 保存模型
     *
     * @param modelId
     * @param name
     * @param description
     * @param json_xml
     * @param svg_xml
     */
    @RequestMapping(value = "/service/model/{modelId}/save", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void saveModel(@PathVariable String modelId
            , String name, String description
            , String json_xml, String svg_xml) {
        try {

            Model model = repositoryService.getModel(modelId);

            ObjectNode modelJson = (ObjectNode) new ObjectMapper().readTree(model.getMetaInfo());

            modelJson.put("ewew", name);
            modelJson.put("43434", description);
            model.setMetaInfo(modelJson.toString());
            model.setName(name);

            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(), json_xml.getBytes(StandardCharsets.UTF_8));

            InputStream svgStream = new ByteArrayInputStream(svg_xml.getBytes(StandardCharsets.UTF_8));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();

        } catch (Exception e) {
            throw new ActivitiException("Error saving model", e);
        }
    }

    /**
     * 验证是否登录
     * @param req
     * @return
     */
    public User getAccount(HttpServletRequest req){
        User user = (User)req.getSession().getAttribute(Const.SESSION_ACCOUNT);
        return user;
    }

    @Autowired
    private ActivitiService activitiService;

    /**
     * 创建复杂工作流
     * @throws IOException
     */
    @RequestMapping(value = "/createProcess")
    public void createProcess() throws IOException {
        activitiService.createProcess();
    }

    /**
     * 创建一个简单的工作流
     * @throws IOException
     */
    @RequestMapping(value = "/createSimpleProcess")
    public void createSimpleProcess() throws IOException {
        activitiService.createSimpleProcess();
    }
    @RequestMapping(value = "/download")
    public void download(String depId,String name) throws IOException {
        InputStream processBpmn = repositoryService.getResourceAsStream(depId, name+".bpmn");
        FileUtils.copyInputStreamToFile(processBpmn,new File("D:\\Test\\activiti\\"+depId+".bpmn"));

    }

    /**
     * 创建节点任务 使用监听设置处理人
     * @param id 任务id标识
     * @param name 任务名称
     * @param taskListenerList 监听的集合,TaskListener实现类的的具体路径例：com.sky.bluesky.activiti.utils.MangerTaskHandlerCandidateUsers
     * @return
     */
    public UserTask createUserTask(String id, String name, List<String> taskListenerList) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);


        List<ActivitiListener> list = new ArrayList<ActivitiListener>();
        for (String taskListener : taskListenerList) {
            ActivitiListener listener = new ActivitiListener();
            listener.setEvent("create");
//Spring配置以变量形式调用无法写入，只能通过继承TaskListener方法，
            listener.setImplementationType("class");
            listener.setImplementation(taskListener);

            list.add(listener);

        }
        userTask.setTaskListeners(list);
        return userTask;
    }
}
