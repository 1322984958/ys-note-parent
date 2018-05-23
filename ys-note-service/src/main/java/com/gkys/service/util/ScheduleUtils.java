package com.gkys.service.util;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gkys.common.constant.JobStatus;
import com.gkys.common.exception.BaseException;
import com.gkys.model.job.SysScheduleJob;
import com.gkys.service.job.config.AsyncJobFactory;
import com.gkys.service.job.config.SyncJobFactory;

public class ScheduleUtils {
	private static final Logger log = LoggerFactory.getLogger(ScheduleUtils.class);

	/**
	 * 获取触发器key
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public static TriggerKey getTriggerKey(String jobName, String jobGroup) {
		return TriggerKey.triggerKey(jobName, jobGroup);
	}

	/**
	 * 获取jobKey
	 * @param jobName
	 *            the job name
	 * @param jobGroup
	 *            the job group
	 * @return the job key
	 */
	public static JobKey getJobKey(String jobName, String jobGroup) {
		return JobKey.jobKey(jobName, jobGroup);
	}

	/**
	 * 获取表达式触发器
	 * @param scheduler
	 *            the scheduler
	 * @param jobName
	 *            the job name
	 * @param jobGroup
	 *            the job group
	 * @return cron trigger
	 */
	public static CronTrigger getCronTrigger(Scheduler scheduler, String jobName, String jobGroup) {
		try {
			TriggerKey triggerKey = ScheduleUtils.getTriggerKey(jobName, jobGroup);
			return (CronTrigger) scheduler.getTrigger(triggerKey);
		} catch (SchedulerException e) {
			log.error("获取定时任务CronTrigger出现异常", e);
			throw new BaseException("获取定时任务CronTrigger出现异常");
		}
	}

	/**
	 * 创建任务
	 * @param scheduler
	 *            the scheduler
	 * @param scheduleJob
	 *            the schedule job
	 */
	public static void createScheduleJob(Scheduler scheduler, SysScheduleJob scheduleJob) {
		createScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup(), scheduleJob.getCron(),
				scheduleJob.getIsAsync(), scheduleJob);
	}

	/**
	 * 创建定时任务
	 * @param scheduler
	 *            the scheduler
	 * @param jobName
	 *            the job name
	 * @param jobGroup
	 *            the job group
	 * @param cronExpression
	 *            the cron expression
	 * @param isAsync
	 *            the is isAsync
	 * @param param
	 *            the param
	 */
	public static void createScheduleJob(Scheduler scheduler, String jobName, String jobGroup, String cronExpression,
			Boolean isAsync, Object param) {
		try {
			// 同步或异步
			Class<? extends Job> jobClass = (Class<? extends Job>) (isAsync ? AsyncJobFactory.class
					: SyncJobFactory.class);

			// 构建job信息 (任务名，任务组，任务执行类)
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();

			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)
					.withSchedule(scheduleBuilder).build();

			SysScheduleJob scheduleJob = (SysScheduleJob) param;

			// 放入参数，运行时的方法可以获取
			jobDetail.getJobDataMap().put(SysScheduleJob.JOB_PARAM_KEY, scheduleJob);

			scheduler.scheduleJob(jobDetail, trigger);

			// 暂停任务 (如果修改修改任务时，选择先删除再创建，保持原来的任务状态)
			if (JobStatus.PAUSE.getValue() == scheduleJob.getStatus()) {
				pauseJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
			}
		} catch (SchedulerException e) {
			log.error("创建定时任务失败", e);
			throw new BaseException("创建定时任务失败");
		}
	}

	/**
	 * 运行一次任务
	 * 
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void runOnce(Scheduler scheduler, String jobName, String jobGroup) {
		JobKey jobKey = getJobKey(jobName, jobGroup);
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			log.error("运行一次定时任务失败", e);
			throw new BaseException("运行一次定时任务失败");
		}
	}

	/**
	 * 暂停任务
	 * 
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup) {
		JobKey jobKey = getJobKey(jobName, jobGroup);
		try {
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			log.error("暂停定时任务失败", e);
			throw new BaseException("暂停定时任务失败");
		}
	}

	/**
	 * 恢复任务
	 *
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup) {
		JobKey jobKey = getJobKey(jobName, jobGroup);
		try {
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			log.error("暂停定时任务失败", e);
			throw new BaseException("暂停定时任务失败");
		}
	}

	/**
	 * 更新定时任务的cron表达式
	 *
	 * @param scheduler
	 *            the scheduler
	 * @param scheduleJob
	 *            the schedule job
	 */
	public static void updateScheduleJob(Scheduler scheduler, SysScheduleJob scheduleJob) {
		try {
			TriggerKey triggerKey = ScheduleUtils.getTriggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCron());

			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
					.startAt(new Date()).build();
			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			// 忽略状态为PAUSED的任务，解决集群环境中在其他机器设置定时任务为PAUSED状态后，集群环境启动另一台主机时定时任务全被唤醒的bug
			if (!triggerState.name().equalsIgnoreCase("PAUSED")) {
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}

			// 更新放入参数的值，运行时的方法可以获取
			trigger.getJobDataMap().put(SysScheduleJob.JOB_PARAM_KEY, scheduleJob);
		} catch (SchedulerException e) {
			log.error("更新定时任务失败", e);
			throw new BaseException("更新定时任务失败");
		}
	}

	/**
	 * 删除定时任务
	 *
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void deleteScheduleJob(Scheduler scheduler, String jobName, String jobGroup) {
		try {
			scheduler.deleteJob(getJobKey(jobName, jobGroup));
		} catch (SchedulerException e) {
			log.error("删除定时任务失败", e);
			throw new BaseException("删除定时任务失败");
		}
	}
}
