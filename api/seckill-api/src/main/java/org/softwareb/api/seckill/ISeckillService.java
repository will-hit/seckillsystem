package org.softwareb.api.seckill;


import org.softwareb.common.pojo.ResultBean;
import org.softwareb.entity.Order;

public interface ISeckillService {
    ResultBean addTask(Order order);

    ResultBean checkStatus(int uid, int pid);

    ResultBean queryWaitPaidOrderByUid(Integer uid);
}
