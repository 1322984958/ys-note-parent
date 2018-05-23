package com.gkys.web.job.listener;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gkys.api.job.SysScheduleJobService;

public class ScheduleJobListener {
	private static final Logger log = LoggerFactory.getLogger(ScheduleJobListener.class);
    @Autowired
    private SysScheduleJobService scheduleJobService;

    /**
     * 项目启动时初始化
     */
    @PostConstruct
    public void init() {
        if (log.isInfoEnabled()) {
            log.info("初始化定时任务...,开始");
        }
        scheduleJobService.initScheduleJob();

        if (log.isInfoEnabled()) {
            log.info("初始化定时任务...,完成");
        }
    }
}
