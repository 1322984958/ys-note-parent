package com.gkys.api.job;

import com.github.pagehelper.PageInfo;
import com.gkys.common.base.BaseService;
import com.gkys.model.job.SysScheduleJob;

/**
 * @author cuiP
 * Created by JK on 2017/5/4.
 */
public interface SysScheduleJobService extends BaseService<SysScheduleJob,Long> {

    /**
     * 初始化定时任务
     */
    void initScheduleJob();

    /**
     * 分页查询任务调度列表
     * @param pageNum
     * @param pageSize
     * @param jobName
     * @param startTime
     * @param endTime
     * @return
     */
    PageInfo<SysScheduleJob> findPage(Integer pageNum, Integer pageSize, String jobName, String startTime, String endTime);
    /**
     * 保存定时任务
     */
    void saveScheduleJob(SysScheduleJob scheduleJob);

    /**
     * 更新定时任务
     */
    void updateScheduleJob(SysScheduleJob scheduleJob);

    /**
     * 删除定时任务
     */
    void deleteScheduleJob(Long jobIds);

    /**
     * 暂停运行调度任务
     */
    void pauseJob(Long jobId);

    /**
     * 恢复运行调度任务
     */
    void resumeJob(Long jobId);

    /**
     * 运行一次调度任务
     */
    void runOnce(Long jobId);

    /**
     * 根据任务名称和任务分组查询任务
     * @param jobName
     * @param jobGroup
     * @return
     */
    SysScheduleJob findByJobNameAndJobGroup(String jobName, String jobGroup);
}
