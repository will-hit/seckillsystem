package org.softwareb.entity;

import java.io.Serializable;

public class User implements Serializable{
    private Integer id;

    private String name;

    private String gender;

    private String phone;

    private String mail;

    private String type; // 1: 普通用户  2：管理员

    public User() {
    }

    public User(String name, String gender, String phone, String mail, String type) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.mail = mail;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail == null ? null : mail.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}