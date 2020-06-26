package org.softwareb.api.user.pojo;

import java.io.Serializable;

public class UserVO implements Serializable {

    private String username;

    private String password;

    private String name;

    private String gender;

    private String phone;

    private String mail;

    private String type;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public UserVO() {
    }

    public UserVO(String name, String gender, String phone, String mail,
                  String type, String username, String password) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.mail = mail;
        this.type = type;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone=" + phone +
                ", mail='" + mail + '\'' +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
