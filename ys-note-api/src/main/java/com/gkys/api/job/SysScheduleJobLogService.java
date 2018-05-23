package com.gkys.api.job;

import com.github.pagehelper.PageInfo;
import com.gkys.common.base.BaseService;
import com.gkys.model.job.SysScheduleJobLog;

/**
 *
 * Created by cuip on 2017/5/20.
 */
public interface SysScheduleJobLogService extends BaseService<SysScheduleJobLog,Long> {
    /**
     * 根据ID分页查询调度任务历史
     * @param pageNum
     * @param pageSize
     * @param jobName
     * @param startTime
     * @param endTime
     * @return
     */
    PageInfo<SysScheduleJobLog> findPage(Integer pageNum, Integer pageSize, Long jobId, String jobName, String startTime, String endTime);
}
