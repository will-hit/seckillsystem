package org.softwareb.common.pojo;

import java.io.Serializable;

/***
 * 用来描述返回给客户端的结果
 * @param
 */
public class ResultBean implements Serializable {
    private Integer statusCode;
    private String msg;
    private Object data;

    public ResultBean(){

    }

    public ResultBean(Object data,Integer statusCode){
        this.data = data;
        this.statusCode = statusCode;
    }

    public static ResultBean success(Object data){
        ResultBean resultBean = new ResultBean();
        resultBean.setStatusCode(200);
        resultBean.setData(data);
        return resultBean;
    }

    public static ResultBean error(String msg, Integer statusCode){
        ResultBean resultBean = new ResultBean();
        resultBean.setStatusCode(500);
        resultBean.setMsg(msg);
        return resultBean;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
