package org.softwareb.seckillservice.service;


import com.alibaba.dubbo.config.annotation.Service;
import org.softwareb.api.seckill.ISeckillService;
import org.softwareb.common.pojo.ResultBean;
import org.softwareb.entity.Order;
import org.softwareb.entity.Product;
import org.softwareb.mapper.OrderMapper;
import org.softwareb.seckillservice.task.MultiThreadCreateOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.softwareb.common.constant.RedisKeyConstant.*;

@Service
@Component
public class SeckillService implements ISeckillService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MultiThreadCreateOrder multiThreadCreateOrder;

    // order中需要包含 uid pid createTime phone address
    @Override
    public ResultBean addTask(Order order) {
        StringBuilder redisKeys = new StringBuilder(USERQUEUESTATUS);
        redisKeys.append(":").append(order.getUid()).append(":").append(order.getPid());
        // 判断能不能进行下单
        // TODO try-catch捕获异常
        Integer o = (Integer)redisTemplate.opsForValue().get(redisKeys.toString());
        if (o == null || o.intValue() == 0){
            redisTemplate.boundListOps(GOODSWAITQUEUE).leftPush(order);
            // 设置两小时过期时间
            redisTemplate.opsForValue().set(redisKeys.toString(), 1, 2, TimeUnit.HOURS);
            //异步下单操作
            multiThreadCreateOrder.createOrder();
            return ResultBean.success("正在抢单");
        }else {
            return ResultBean.error("不可重复抢单", 500);
        }
    }

    @Override
    public ResultBean checkStatus(int uid, int pid) {
        StringBuilder redisKey = new StringBuilder(USERQUEUESTATUS);
        redisKey.append(":").append(uid).append(":").append(pid);
        Integer o = (Integer)redisTemplate.opsForValue().get(redisKey.toString());
        System.out.println("userqueuestatus: " + o);
        if (o == null || o.intValue() == 0){
            return ResultBean.error("下单失败！", 500);
        }else if (o.intValue() == 1){
            return ResultBean.error("排队中", 501);
        }else if (o.intValue() == 2){
            return ResultBean.success(null);
        }else{
            return ResultBean.error("未知错误！", 502);
        }

    }

    @Override
    public ResultBean queryWaitPaidOrderByUid(Integer uid) {
        StringBuilder IdList = new StringBuilder(USERPIDLIST);
        IdList.append(":").append(uid);
        List Ids = (List)redisTemplate.opsForValue().get(IdList.toString());
        List<Order> list = new ArrayList<>();
        if (Ids == null){
            return ResultBean.error("没有待支付订单", 501);
        }
        for (Object pid: Ids){
            StringBuilder orderkey = new StringBuilder(SECKILLORDER);
            orderkey.append(":").append((Integer)pid);
            Order order = (Order)redisTemplate.boundHashOps(orderkey.toString()).get(uid);
            list.add(order);
        }
        return ResultBean.success(list);
    }

    @Override
    public ResultBean queryAllProduct() {
        List<Product> products = redisTemplate.boundHashOps(SECKILLGOODS).values();
        if (products == null || products.size() == 0)return ResultBean.success(null);
        List<Product> res = new ArrayList<>();
        for (Product product : products) {
            StringBuilder redisKey = new StringBuilder(SECKILLGOODS);
            redisKey.append(product.getId());
            Long size = redisTemplate.opsForList().size(redisKey.toString());
            System.out.println("list size: " + size);
            product.setStockCount(size.intValue());
            res.add(product);
        }
        return ResultBean.success(res);
    }

    @Override
    public ResultBean payOrderSuccess(Integer pid, Integer uid) {
        StringBuilder orderkey = new StringBuilder(SECKILLORDER);
        orderkey.append(":").append(pid);
        Order order = (Order)redisTemplate.boundHashOps(orderkey.toString()).get(uid);
        if (order == null)return ResultBean.error("未找到对应订单！订单支付超时或者用户不存在！", 501);
        order.setPayTime(new Date());
        order.setStatus("2");
        int i = orderMapper.insertSelective(order);
        System.out.println("更新状态:" + i);
        if (i == 1){
            redisTemplate.boundHashOps(orderkey.toString()).delete(uid);
            // 删除待支付订单的商品Id
            StringBuilder IdList = new StringBuilder(USERPIDLIST);
            IdList.append(":").append(order.getUid());
            List Ids = (List)redisTemplate.opsForValue().get(IdList.toString());
            if (Ids != null){
                Ids.remove(order.getPid());
                redisTemplate.opsForValue().set(IdList.toString(), Ids);
            }
            // 删除排队状态
            StringBuilder userQueueStatus = new StringBuilder(USERQUEUESTATUS);
            userQueueStatus.append(":").append(uid).append(":").append(pid);
            redisTemplate.delete(userQueueStatus.toString());
            return ResultBean.success(null);
        }
        else
            return ResultBean.error("由于网络原因 添加失败", 500);
    }

}
