package com.aaron.activiti.model;

import java.util.Date;

/**
 * @author Aaron
 * @description 自定义的任务实体类
 * @date 2019/4/18
 */
public class TaskModel {
    private String id;
    private String name;
    private String processInstanceId;
    private String assignee;
    private Date createTime;
    private String nextPerson;
    private String cause;
    private String content;
    private String taskType;
    private String processKey;
    private String processDefId;

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

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNextPerson() {
        return nextPerson;
    }

    public void setNextPerson(String nextPerson) {
        this.nextPerson = nextPerson;
    }

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

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getProcessDefId() {
        return processDefId;
    }

    public void setProcessDefId(String processDefId) {
        this.processDefId = processDefId;
    }

    @Override
    public String toString() {
        return "TaskModel [id=" + id + ", name=" + name
                + ", processInstanceId=" + processInstanceId + ", assignee="
                + assignee + ", createTime=" + createTime + ", nextPerson="
                + nextPerson + ", cause=" + cause + ", content=" + content
                + ", taskType=" + taskType + ", processKey=" + processKey
                + ", processDefId=" + processDefId + "]";
    }
}
