package org.softwareb.entity;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private Long id;

    private Integer pid;

    private Integer uid;

    private String money;

    private Date createTime;

    private Date payTime;

    private String status;

    private String receiverAddress;

    private String receiverPhone;

    private String receiverName;

    private String transactonId;

    public Order() {
    }

    public Order(Long id, Integer pid, Integer uid, String money,
                 Date createTime, Date payTime, String status,
                 String receiverAddress, String receiverPhone,
                 String receiverName, String transactonId) {
        this.id = id;
        this.pid = pid;
        this.uid = uid;
        this.money = money;
        this.createTime = createTime;
        this.payTime = payTime;
        this.status = status;
        this.receiverAddress = receiverAddress;
        this.receiverPhone = receiverPhone;
        this.receiverName = receiverName;
        this.transactonId = transactonId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money == null ? null : money.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress == null ? null : receiverAddress.trim();
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    public String getTransactonId() {
        return transactonId;
    }

    public void setTransactonId(String transactonId) {
        this.transactonId = transactonId == null ? null : transactonId.trim();
    }
}