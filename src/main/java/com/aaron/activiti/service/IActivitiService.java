package com.aaron.activiti.service;

import java.io.IOException;

/**
 * 工作流业务接口
 */
public interface IActivitiService {
    void createProcess() throws IOException;
    void createSimpleProcess() throws IOException;
}
