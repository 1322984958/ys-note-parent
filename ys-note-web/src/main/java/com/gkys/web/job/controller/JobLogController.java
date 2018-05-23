package com.gkys.web.job.controller;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.gkys.api.job.SysScheduleJobLogService;
import com.gkys.model.job.SysScheduleJobLog;

/**
 * @author cuiP
 * Created by JK on 2017/5/22.
 */
@Controller
@RequestMapping("/admin/job/logger")
public class JobLogController{
	private Logger logger=LoggerFactory.getLogger(getClass());
	
    private static final String BASE_PATH = "admin/job/";

    @Resource
    private SysScheduleJobLogService scheduleJobLogService;

    /**
     * 根据ID分页查询调度任务历史
     *
     * @param pageNum   当前页码
     * @param jobName  用户名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param modelMap
     * @return
     */
    @RequiresPermissions("job:logger")
    @GetMapping("/{jobId}")
    public String list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @PathVariable("jobId") Long jobId,
            String jobName, String startTime, String endTime, ModelMap modelMap) {
        logger.debug("根据ID分页查询调度任务历史参数! pageNum = {}, username = {}, username = {}, startTime = {}, endTime = {}", pageNum, jobName, startTime, endTime);
        PageInfo<SysScheduleJobLog> pageInfo = scheduleJobLogService.findPage(pageNum, pageSize, jobId, jobName, startTime, endTime);
        logger.info("根据ID分页查询调度任务历史结果！ pageInfo = {}", pageInfo);
        modelMap.put("pageInfo", pageInfo);
        modelMap.put("jobId", jobId);
        modelMap.put("jobName", jobName);
        modelMap.put("startTime", startTime);
        modelMap.put("endTime", endTime);
        return BASE_PATH + "job-logger";
    }
}
