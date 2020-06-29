package org.softwareb.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.softwareb.api.seckill.ISeckillService;
import org.softwareb.common.pojo.ResultBean;
import org.softwareb.entity.Order;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.softwareb.common.constant.RedisKeyConstant.SECKILLORDER;

@RestController
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
    public ResultBean addOrderAsync(Order order){
        System.out.println(order);
        ResultBean resultBean = seckillService.addTask(order);
        return resultBean;
    }

    @RequestMapping(value = "/checkStatus")
    public ResultBean checkStatus(int uid, int pid){
        System.out.println("uid:" + uid + " " + "pid:" + pid);
        ResultBean resultBean = seckillService.checkStatus(uid, pid);
        return resultBean;
    }

    @RequestMapping(value = "/waitPaidOrder")
    public ResultBean queryWaitPaidOrder(int uid){
        System.out.println(uid);
        seckillService.queryWaitPaidOrderByUid(uid);
    }
}
