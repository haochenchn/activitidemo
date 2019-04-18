package com.aaron.activiti.model;

/**
 * @author Aaron
 * @description 自定义的申请单实体类
 * @date 2019/4/18
 */
public class ApplyModel {
    /**
     * 流程定义id
     */
    private String proDefId;
    /**
     * 流程定义的key
     */
    private String key;

    private String name;
    /**
     * 申请人
     */
    private String appPerson;
    /**
     * 原因
     */
    private String cause;
    /**
     * 内容
     */
    private String content;
    /**
     * 处理人，即下一个任务节点的受理人
     */
    private String proPerson;


    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }


    public String getAppPerson() {
        return appPerson;
    }


    public void setAppPerson(String appPerson) {
        this.appPerson = appPerson;
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


    public String getProPerson() {
        return proPerson;
    }


    public void setProPerson(String proPerson) {
        this.proPerson = proPerson;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getProDefId() {
        return proDefId;
    }


    public void setProDefId(String proDefId) {
        this.proDefId = proDefId;
    }


    @Override
    public String toString() {
        return "applyModel [proDefId=" + proDefId + ", key=" + key + ", name="
                + name + ", appPerson=" + appPerson + ", cause=" + cause
                + ", content=" + content + ", proPerson=" + proPerson + "]";
    }
}
