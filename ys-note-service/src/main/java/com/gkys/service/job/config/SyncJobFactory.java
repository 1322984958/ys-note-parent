package com.gkys.service.job.config;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gkys.model.job.SysScheduleJob;
import com.gkys.service.util.ScheduleExecute;

/**
 * 同步任务工厂(若一个方法一次执行不完下次轮转时则等待改方法执行完后才执行下一次操作)
 * @author cuiP
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SyncJobFactory extends QuartzJobBean {
	private static final Logger log = LoggerFactory.getLogger(SyncJobFactory.class);
	@Override
	public void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			log.info("SyncJobFactory execute,执行开始...");
			//获取任务记录
			SysScheduleJob scheduleJob = (SysScheduleJob) context.getMergedJobDataMap().get(SysScheduleJob.JOB_PARAM_KEY);
			//获取spring上下文
			ApplicationContext applicationContext = (ApplicationContext)context.getScheduler().getContext().get("applicationContextKey");

			//执行调度任务
			ScheduleExecute.execute(applicationContext, scheduleJob);
		} catch (SchedulerException e) {
			log.error("SyncJobFactory execute,执行失败...");
		}
	}

}