package com.aaron.activiti.model;

import java.util.Date;
/**
 * @author Aaron
 * @description 自定义的任务实体类
 * @date 2019/4/18
 */
public class HisTaskModel {
    private String id;
    private String name;
    private String assignee;
    private String processInstanceId;
    private Date startTime;
    private Date endTime;
    private String cause;
    private String content;
    private String taskType;

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
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

    @Override
    public String toString() {
        return "HisTaskModel [id=" + id + ", name=" + name + ", assignee="
                + assignee + ", processInstanceId=" + processInstanceId
                + ", startTime=" + startTime + ", endTime=" + endTime
                + ", cause=" + cause + ", content=" + content + ", taskType="
                + taskType + "]";
    }
}
