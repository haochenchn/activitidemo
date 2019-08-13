package com.aaron.activiti.model;

import java.util.Date;

/**
 * @author Aaron
 * @description 自定义的流程定义实体类
 * @date 2019/4/18
 */
public class ProcessModel {
    private String id;
    private String deploymentId;
    private String key;
    private String resourceName;
    private int version;
    private String name;
    private String diagramResourceName;
    private String appPerson;
    private Date startTime;
    private Date endTime;
    private String procInstanceId;
    private boolean suspended;
    private String currentTaskName;
    private String currentTaskId;
    private Date curretTaskTime;
    private String currentAssign;

    public String getName() {
        return name;
    }

    public String getProcInstanceId() {
        return procInstanceId;
    }

    public void setProcInstanceId(String procInstanceId) {
        this.procInstanceId = procInstanceId;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDiagramResourceName() {
        return diagramResourceName;
    }


    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getDeploymentId() {
        return deploymentId;
    }


    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }


    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public String getResourceName() {
        return resourceName;
    }


    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }


    public int getVersion() {
        return version;
    }


    public void setVersion(int version) {
        this.version = version;
    }

    public String getAppPerson() {
        return appPerson;
    }

    public void setAppPerson(String appPerson) {
        this.appPerson = appPerson;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public String getCurrentTaskName() {
        return currentTaskName;
    }

    public void setCurrentTaskName(String currentTaskName) {
        this.currentTaskName = currentTaskName;
    }

    public String getCurrentTaskId() {
        return currentTaskId;
    }

    public void setCurrentTaskId(String currentTaskId) {
        this.currentTaskId = currentTaskId;
    }

    public Date getCurretTaskTime() {
        return curretTaskTime;
    }

    public void setCurretTaskTime(Date curretTaskTime) {
        this.curretTaskTime = curretTaskTime;
    }

    public String getCurrentAssign() {
        return currentAssign;
    }

    public void setCurrentAssign(String currentAssign) {
        this.currentAssign = currentAssign;
    }
}
