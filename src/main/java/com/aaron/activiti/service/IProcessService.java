package com.aaron.activiti.service;

import com.aaron.activiti.model.ApplyModel;
import com.aaron.activiti.model.ProcessModel;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;

public interface IProcessService {
    /**
     * 启动流程（key）
     * @param applyModel
     * @param variables
     * @return
     */
    ProcessInstance startProcessByKey(ApplyModel applyModel, Map<String, Object> variables);

    /**
     * 读取运行中的流程实例
     * @return
     */
    List<ProcessModel> findRunningProcessInstaces();

}
