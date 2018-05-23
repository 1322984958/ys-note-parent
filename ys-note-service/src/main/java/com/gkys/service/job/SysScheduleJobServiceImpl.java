package com.gkys.service.job;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gkys.api.job.SysScheduleJobService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.common.constant.JobStatus;
import com.gkys.common.util.SelectUtil;
import com.gkys.common.util.SpringContextUtil;
import com.gkys.mapper.job.SysScheduleJobMapper;
import com.gkys.model.job.SysScheduleJob;
import com.gkys.service.util.ScheduleUtils;

import tk.mybatis.mapper.entity.Example;

@Transactional
@BaseService
@Service
public class SysScheduleJobServiceImpl extends BaseServiceImpl<SysScheduleJobMapper, SysScheduleJob, Long>
		implements SysScheduleJobService {
	@Autowired
//    @Qualifier("schedulerFactoryBean")
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private SysScheduleJobMapper scheduleJobMapper;

	@Override
	public void initScheduleJob() {
		List<SysScheduleJob> scheduleJobList = scheduleJobMapper.selectAll();

		if (CollectionUtils.isEmpty(scheduleJobList)) {
			return;
		}

		for (SysScheduleJob scheduleJob : scheduleJobList) {
			CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(getSchedulerFactoryBean().getScheduler(),
					scheduleJob.getJobName(), scheduleJob.getJobGroup());

			// 不存在，创建一个
			if (cronTrigger == null) {
				ScheduleUtils.createScheduleJob(getSchedulerFactoryBean().getScheduler(), scheduleJob);
			} else {
				// 已存在，那么更新相应的定时设置
				ScheduleUtils.updateScheduleJob(getSchedulerFactoryBean().getScheduler(), scheduleJob);
			}
		}
	}

	@Override
	public PageInfo<SysScheduleJob> findPage(Integer pageNum, Integer pageSize, String jobName, String startTime,
			String endTime) {
		Example example = new Example(SysScheduleJob.class);
		Example.Criteria criteria = example.createCriteria();
		SelectUtil.addParameterIfNotNull(criteria, SelectUtil.LIKE, "jobName", jobName);
		SelectUtil.addParameterEqualToBetweenIfNotNull(criteria, "createTime", startTime, endTime);
		// 倒序
		example.orderBy("createTime").desc();
		// 分页
		PageHelper.startPage(pageNum, pageSize);
		List<SysScheduleJob> jobList = this.selectByExample(example);

		return new PageInfo<SysScheduleJob>(jobList);
	}

	@Override
	public void saveScheduleJob(SysScheduleJob scheduleJob) {
		scheduleJob.setStatus(JobStatus.NORMAL.getValue());
		// 创建调度任务
		ScheduleUtils.createScheduleJob(getSchedulerFactoryBean().getScheduler(), scheduleJob);

		// 保存到数据库
//		User user = ShiroUtils.getUserEntity();

		scheduleJob.setId(null);
		scheduleJob.setStatus(1);
//		scheduleJob.setCreateBy(user.getId());
		if (scheduleJob.getIsLocal()) {
			scheduleJob.setRemoteUrl(null);
			scheduleJob.setRemoteRequestMethod(null);
		} else {
			scheduleJob.setBeanClass(null);
			scheduleJob.setMethodName(null);
			scheduleJob.setRemoteRequestMethod("POST"); // 默认只支持post
		}
		super.insertSelective(scheduleJob);
	}

	@Override
	public void updateScheduleJob(SysScheduleJob scheduleJob) {

		// 根据ID获取修改前的任务记录
		SysScheduleJob record = super.selectByPrimaryKey(scheduleJob.getId());

		// 参数赋值
		scheduleJob.setRemoteRequestMethod(record.getRemoteRequestMethod());
		scheduleJob.setStatus(record.getStatus());
		scheduleJob.setCreatorId(record.getCreatorId());
		scheduleJob.setModifierId(record.getModifierId());
		scheduleJob.setCreateTime(record.getCreateTime());
//		scheduleJob.setModifyTime(record.getModifyTime());
		if (scheduleJob.getIsLocal()) {
			scheduleJob.setIsLocal(true);
			scheduleJob.setBeanClass(scheduleJob.getBeanClass());
			scheduleJob.setMethodName(scheduleJob.getMethodName());
			scheduleJob.setRemoteUrl(null);
			scheduleJob.setRemoteRequestMethod(null);
		} else {
			scheduleJob.setIsLocal(false);
			scheduleJob.setRemoteUrl(scheduleJob.getRemoteUrl());
			scheduleJob.setRemoteRequestMethod("POST"); // 默认只支持post
			scheduleJob.setBeanClass(null);
			scheduleJob.setMethodName(null);
		}

		// 因为Quartz只能更新cron表达式，当更改了cron表达式以外的属性时，执行的逻辑是：先删除旧的再创建新的。注:equals排除了cron属性
		if (!scheduleJob.equals(record)) {
			// 删除旧的任务
			ScheduleUtils.deleteScheduleJob(getSchedulerFactoryBean().getScheduler(), record.getJobName(),
					record.getJobGroup());
			// 创建新的任务,保持原来任务的状态
			scheduleJob.setStatus(record.getStatus());
			ScheduleUtils.createScheduleJob(getSchedulerFactoryBean().getScheduler(), scheduleJob);
		} else {
			// 当cron表达式和原来不一致才做更新
			if (!scheduleJob.getCron().equals(record.getCron())) {
				// 更新调度任务
				ScheduleUtils.updateScheduleJob(getSchedulerFactoryBean().getScheduler(), scheduleJob);
			}
		}

		// 更新数据库
//		User user = ShiroUtils.getUserEntity();
//		scheduleJob.setModifyBy(user.getId());
		super.updateByPrimaryKey(scheduleJob);
	}

	@Override
	public void deleteScheduleJob(Long jobId) {
		SysScheduleJob scheduleJob = super.selectByPrimaryKey(jobId);
		// 删除运行的任务
		ScheduleUtils.deleteScheduleJob(getSchedulerFactoryBean().getScheduler(), scheduleJob.getJobName(),
				scheduleJob.getJobGroup());
		// 删除数据
		super.deleteByPrimaryKey(jobId);
	}

	@Override
	public void pauseJob(Long jobId) {
		SysScheduleJob scheduleJob = super.selectByPrimaryKey(jobId);
		// 暂停正在运行的调度任务
		ScheduleUtils.pauseJob(getSchedulerFactoryBean().getScheduler(), scheduleJob.getJobName(),
				scheduleJob.getJobGroup());
		// 更新数据库状态为 禁用 0
		SysScheduleJob model = new SysScheduleJob();
		model.setId(jobId);
		model.setStatus(0);
		super.updateByPrimaryKeySelective(model);
	}

	@Override
	public void resumeJob(Long jobId) {
		SysScheduleJob scheduleJob = super.selectByPrimaryKey(jobId);
		// 恢复处于暂停中的调度任务
		ScheduleUtils.resumeJob(getSchedulerFactoryBean().getScheduler(), scheduleJob.getJobName(),
				scheduleJob.getJobGroup());
		// 更新数据库状态 启用 1
		SysScheduleJob model = new SysScheduleJob();
		model.setId(jobId);
		model.setStatus(1);
		super.updateByPrimaryKeySelective(model);
	}

	@Override
	public void runOnce(Long jobId) {
		SysScheduleJob scheduleJob = super.selectByPrimaryKey(jobId);
		// 运行一次
		ScheduleUtils.runOnce(getSchedulerFactoryBean().getScheduler(), scheduleJob.getJobName(), scheduleJob.getJobGroup());
	}

	@Transactional(readOnly = true)
	@Override
	public SysScheduleJob findByJobNameAndJobGroup(String jobName, String jobGroup) {
		SysScheduleJob scheduleJob = new SysScheduleJob();
		scheduleJob.setJobName(jobName);
		scheduleJob.setJobGroup(jobGroup);
		return super.selectOne(scheduleJob);
	}

	public SchedulerFactoryBean getSchedulerFactoryBean() {
		if(schedulerFactoryBean==null){
			schedulerFactoryBean=SpringContextUtil.getBean(SchedulerFactoryBean.class);
		}
		return schedulerFactoryBean;
	}
}
