package org.softwareb.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.softwareb.api.seckill.ISeckillService;
import org.softwareb.api.seckill.pojo.MqMessage;
import org.softwareb.common.pojo.ResultBean;
import org.softwareb.entity.Order;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@CrossOrigin
@RequestMapping("/seckill")
public class SeckillController {

    @Reference
    private ISeckillService seckillService;

//    @RequestMapping(value="/addOrder", produces = "application/json;charset=UTF-8")
//    public ResultBean addOrderAsync(@RequestBody Order order){
//        System.out.println(order);
//        ResultBean resultBean = seckillService.addTask(order);
//        return resultBean;
//    }

    @RequestMapping(value="/addOrder")
    public ResultBean addOrderAsync(@RequestBody Order order){
        System.out.println(order);
        order.setCreateTime(new Date());
        ResultBean resultBean = seckillService.addTask(order);
        return resultBean;
    }

    @RequestMapping(value = "/checkStatus")
    public ResultBean checkStatus(@RequestBody MqMessage message){
        System.out.println("uid:" + message.getUid() + " " + "pid:" + message.getPid());
        ResultBean resultBean = seckillService.checkStatus(message.getUid() , message.getPid());
        return resultBean;
    }

    @RequestMapping(value = "/waitPaidOrder")
    public ResultBean queryWaitPaidOrder(Integer uid){
        System.out.println(uid);
        ResultBean resultBean = seckillService.queryWaitPaidOrderByUid(uid);
        return resultBean;
    }

    @RequestMapping(value = "/queryAllProduct")
    public ResultBean queryAllProduct(){
        ResultBean resultBean = seckillService.queryAllProduct();
        return resultBean;
    }

    @RequestMapping(value = "/paySuccess")
    public ResultBean pay(@RequestBody MqMessage message){
        System.out.println(message);
        if (message.getPid() == null || message.getUid() == null)
            return ResultBean.error("订单号错误！", 501);
        ResultBean resultBean = seckillService.payOrderSuccess(message.getPid(), message.getUid());
        return resultBean;
    }

}
