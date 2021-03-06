package org.softwareb.common.constant;

public interface RedisKeyConstant {
    String SECKILLGOODS = "SecKillGoods";
    String USERQUEUESTATUS = "UserQueueStatus"; // 用于前端读取用户状态 redis中 0:失败 1：排队中 2:成功
    String GOODSWAITQUEUE = "WaitingQueue"; // 等下下单的队列
    String SECKILLORDER = "SecKillOrder"; // 用于查询订单状态
    String USERPIDLIST = "UserPidList"; // 用于查找待支付订单商品ID
}
