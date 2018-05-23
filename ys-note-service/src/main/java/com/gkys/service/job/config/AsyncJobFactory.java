package com.gkys.service.job.config;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gkys.model.job.SysScheduleJob;
import com.gkys.service.util.ScheduleExecute;

/**
 * 异步任务工厂(计划任务执行处 无状态)
 * @author cuiP
 */
public class AsyncJobFactory extends QuartzJobBean {
	private static final Logger log = LoggerFactory.getLogger(AsyncJobFactory.class);
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			log.info("AsyncJobFactory execute,执行开始...");
			SysScheduleJob scheduleJob = (SysScheduleJob) context.getMergedJobDataMap().get(SysScheduleJob.JOB_PARAM_KEY);

			//获取spring上下文
			ApplicationContext applicationContext = (ApplicationContext)context.getScheduler().getContext().get("applicationContextKey");

			//执行调度任务
			ScheduleExecute.execute(applicationContext, scheduleJob);
		} catch (SchedulerException e) {
			log.error("AsyncJobFactory execute,执行失败...");
		}
	}
}