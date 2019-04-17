package com.aaron.activiti.controller;

import com.aaron.activiti.model.ActivitiModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Aaron
 * @description TODO
 * @date 2019/4/17
 */
@RequestMapping("/activiti")
@Controller
public class activitiController {
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 创建模型
     * 创建模型时相关的有act_re_model和act_ge_bytearray两个。
     * 成功创建模型后可以看到model表中会有一条数据，同时在bytearray表中也会同时生成两条对应的数据。而model表中会存入这两条数据的id，从而产生关联。
     */
    @RequestMapping(value = "/createModel")
    public void create(ActivitiModel activiti,HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<String, String>();
        Model newModel = repositoryService.newModel();
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

    /**
     * 模型列表
     */
    @RequestMapping(value = "/modelList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object modelList() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<ActivitiModel> modelList = new ArrayList<ActivitiModel>();
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

        return map;
    }
    /**
     * 根据模型id部署流程定义
     * 与流程定义相关的有3张表，分别是act_ge_bytearray、act_re_procdef和act_re_deployment。当然了，如果更准确的说，在我的自定义流程中，流程定义需要用到流程模型相关的数据，也可以说流程定义相关的就有四张表，也包括model表。
     * 根据前端传入的deploymentId部署流程定义，这里还是使用repositoryService进行操作，大致上的过程就是根据deploymentId查询出创建模型时生成的相关文件，然后进行一定的转换后进行部署
     */
    @RequestMapping(value = "/deploye", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object deploye(@RequestBody ActivitiModel activitiModel) {
        Map<String, Object> map = new HashMap<String, Object>();
        String modelId = activitiModel.getId();
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
                    .addString(processName, new String(bpmnBytes)).deploy();
            if (deployment != null && deployment.getId() != null) {
                map.put("result", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
