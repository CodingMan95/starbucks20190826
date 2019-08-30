package com.eim.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.eim.entity.ActivityInfo;
import com.eim.entity.ActivityOrder;
import com.eim.service.ActivityInfoService;
import com.eim.service.ActivityOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class TimerJob {

    private static final Logger logger = LoggerFactory.getLogger(TimerJob.class);

    @Autowired
    private ActivityOrderService activityOrderService;
    @Autowired
    private ActivityInfoService activityInfoService;

    private boolean isOpen = true;

    /**
     * 每天晚上0点查询一次
     * 活动时间超过当前时间则活动结束
     */
    //@Scheduled(cron = "0/10 * * * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    public void sendMessageJob() throws Exception {
        if (isOpen) {
            logger.info("-----开始执行定时任务:每晚0点更新一次活动状态");

            long nowTime = new Date().getTime();
            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

            /**
             * 活动时间超过当前时间则活动结束
             */
            List<ActivityInfo> list = activityInfoService.list(new QueryWrapper<ActivityInfo>().select("active_id", "active_time").eq("status", true));
            for (ActivityInfo activityInfo : list) {
                //取出时间把列表中的最大时间
                String[] split = activityInfo.getActiveTime().split(",");
                String de_time = split[split.length - 1];

                //计算活动结束时间离现在时间差
                Date d2 = df1.parse(de_time);
                Calendar c = Calendar.getInstance();
                c.setTime(d2);
                c.add(Calendar.DAY_OF_MONTH, 1);

                long diff1 = c.getTime().getTime() - nowTime;
                if (diff1 <= 0) {
                    activityInfoService.updateStatus(activityInfo.getActiveId(), false);
                }
            }

            /**
             * 用户预约时间大于当前时间则活动结束
             */
            List<ActivityOrder> activityOrders = activityOrderService.list(new QueryWrapper<ActivityOrder>().eq("status", 1).select("order_id", "activity_time"));
            for (ActivityOrder order : activityOrders) {

                Date activeTime = df1.parse(order.getActivityTime());
                Calendar c = Calendar.getInstance();
                c.setTime(activeTime);
                c.add(Calendar.DAY_OF_MONTH, 1);

                long diff1 = c.getTime().getTime() - nowTime;
                if (diff1 <= 0) {
                    activityOrderService.update(new UpdateWrapper<ActivityOrder>().set("status", 0).eq("order_id", order.getOrderId()));
                }

            }
        }
    }
}
