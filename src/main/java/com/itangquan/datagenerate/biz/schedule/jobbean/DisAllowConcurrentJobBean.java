package com.itangquan.datagenerate.biz.schedule.jobbean;

import cn.hutool.json.JSONUtil;
import com.itangquan.datagenerate.base.util.SpringContextUtil;
import com.itangquan.datagenerate.biz.schedule.entity.ScheduleJob;
import com.itangquan.datagenerate.biz.schedule.service.ScheduleJobService;
import com.itangquan.datagenerate.biz.schedule.util.ScheduleRunnable;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * 非并发定时任务
 *
 * DisallowConcurrentExecution ：不允许并发执行（解释：如果执行间隔是1s，但任务执行时间大于1s，则在该任务执行完成之前不会开启新线程再次执行）
 */
@DisallowConcurrentExecution
@Slf4j
public class DisAllowConcurrentJobBean extends QuartzJobBean {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    ScheduleJobService scheduleJobService = SpringContextUtil.getBean(ScheduleJobService.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get(ScheduleJob.JOB_PARAM_KEY);

        //任务开始时间
        long startTime = System.currentTimeMillis();

        try {
            ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBean(), scheduleJob.getMethod(), scheduleJob.getParams());
            Future<?> future = executorService.submit(task);
            future.get();

            if (scheduleJob.getDelOnSuccess()==ScheduleJob.DelOnSuccess.YES.getValue()){
                scheduleJobService.deleteJob(scheduleJob.getId());
                log.info("任务执行成功，删除任务-->"+JSONUtil.toJsonStr(scheduleJob));
            }
        } catch (Exception e) {
            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;

            log.error("任务执行失败，时间：{} ID：{}" ,times,scheduleJob.getId(), e);

            scheduleJob.setLog(e.getMessage());

            scheduleJobService.save(scheduleJob);
        }
    }
    
}
