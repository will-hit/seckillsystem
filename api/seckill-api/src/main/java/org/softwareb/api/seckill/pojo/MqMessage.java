package org.softwareb.api.seckill.pojo;

import java.io.Serializable;

public class MqMessage implements Serializable {
    Integer uid;

    Integer pid;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public MqMessage() {
    }

    public MqMessage(Integer uid, Integer pid) {
        this.uid = uid;
        this.pid = pid;
    }
}
