package com.gkys.web.job.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.gkys.api.job.SysScheduleJobService;
import com.gkys.api.sys.SysUserService;
import com.gkys.common.annotation.FormToken;
import com.gkys.common.base.BaseResult;
import com.gkys.model.job.SysScheduleJob;
import com.gkys.model.sys.SysUser;

/**
 * @author cuiP
 * Created by JK on 2017/5/5.
 */
@Controller
@RequestMapping("/api/job/")
public class JobController{
	private Logger logger=LoggerFactory.getLogger(getClass());
	private static final Integer FAILURE = -1;
	private static final Integer SUCCESS = 1;

    @Resource
    private SysScheduleJobService scheduleJobService;
    @Resource
    private SysUserService sysUserService;

    /**
     * 分页查询调度任务列表
     * @param pageNum   当前页码
     * @param jobName  用户名
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param modelMap
     * @return
     */
    @RequiresPermissions("job:list")
    @PostMapping("list")
    @ResponseBody
    public BaseResult list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            String jobName, String startTime, String endTime, ModelMap modelMap) {
        logger.debug("分页查询调度任务列表参数! pageNum = {}, username = {}, username = {}, startTime = {}, endTime = {}", pageNum, jobName, startTime, endTime);
        PageInfo<SysScheduleJob> pageInfo = scheduleJobService.findPage(pageNum, pageSize, jobName, startTime, endTime);
        logger.info("分页查询调度任务列表结果！ pageInfo = {}", pageInfo);
        BaseResult baseResult=BaseResult.success(pageInfo.getList());
        baseResult.setTotal(pageInfo.getTotal());
        return baseResult;
    }

    /**
     * 添加调度任务
     *
     * @param scheduleJob
     * @return
     */
    @FormToken(needRemoveToken = true)
//    @Operationlogger(value = "添加调度任务")
    @RequiresPermissions("job:create")
    @ResponseBody
    @PostMapping("create")
    public BaseResult createJob(HttpServletRequest request, SysScheduleJob scheduleJob) {
        logger.debug("添加调度任务参数! scheduleJob = {}", scheduleJob);
        //判断任务是否已经存在相同的jobName和jobGroup
        SysScheduleJob record = scheduleJobService.findByJobNameAndJobGroup(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        if (null != record) {
        	return BaseResult.fail("该调度任务已经被注册!");
        }
        scheduleJob.setRemoteUrl(request.getParameter("remoteUrl"));
        scheduleJobService.saveScheduleJob(scheduleJob);

        logger.info("添加调度任务成功! jobId = {}", scheduleJob.getId());
        return BaseResult.success();
    }

    /**
     * 更新调度任务信息
     * @param id
     * @param scheduleJob
     * @return
     */
    @FormToken(needRemoveToken = true)
//    @Operationlogger(value = "编辑调度任务")
    @RequiresPermissions("job:update")
    @ResponseBody
    @PostMapping(value = "edit/{id}")
    public BaseResult updateJob(HttpServletRequest request, @PathVariable("id") Long id, SysScheduleJob scheduleJob){
        logger.debug("编辑调度任务参数! id= {}, scheduleJob = {}", id, scheduleJob);
        if(null == id){
        	return BaseResult.fail("ID不能为空!");
        }
        scheduleJob.setId(id);
        scheduleJob.setRemoteUrl(request.getParameter("remoteUrl"));
        scheduleJobService.updateScheduleJob(scheduleJob);
        logger.info("编辑调度任务成功! id= {}, scheduleJob = {}", id, scheduleJob);
        return BaseResult.success();
    }


    /**
     * 根据主键ID删除调度任务
     * @param id
     * @return
     */
//    @Operationlogger(value = "删除调度任务")
//    @RequiresPermissions("job:delete")
    @RequiresRoles("superAdmin")
    @ResponseBody
    @DeleteMapping(value = "delete/{id}")
    public BaseResult delete(@PathVariable("id") Long id) {
        logger.debug("删除调度任务! id = {}", id);
        if (null == id) {
            logger.info("删除调度任务不存在! id = {}", id);
            return BaseResult.fail("删除调度任务不存在!");
        }
        scheduleJobService.deleteScheduleJob(id);

        logger.info("删除调度任务成功! id = {}", id);
        return BaseResult.success("删除成功!");
    }

    /**
     * 暂停调度任务
     * @param id
     * @return
     */
//    @Operationlogger(value = "暂停调度任务")
//    @RequiresPermissions("job:pause")
    @RequiresRoles("superAdmin")
    @PostMapping(value = "/pause/{id}")
    @ResponseBody
    public BaseResult pauseJob(@PathVariable("id") Long id) {
        logger.debug("暂停调度任务! id = {}", id);

        if (null == id) {
            logger.info("暂停调度任务不存在! id = {}", id);
            return BaseResult.fail("暂停调度任务不存在!");
        }

        scheduleJobService.pauseJob(id);

        logger.info("暂停调度任务成功! id = {}", id);
        return BaseResult.success("暂停成功!");
    }

    /**
     * 恢复调度任务
     * @param id
     * @return
     */
//    @Operationlogger(value = "恢复调度任务")
//    @RequiresPermissions("job:resume")
    @RequiresRoles("superAdmin")
    @ResponseBody
    @PostMapping(value = "/resume/{id}")
    public BaseResult resumeJob(@PathVariable("id") Long id) {
        logger.debug("恢复调度任务! id = {}", id);

        if (null == id) {
            logger.info("恢复调度任务不存在! id = {}", id);
            return BaseResult.fail("恢复调度任务不存在!");
        }

        scheduleJobService.resumeJob(id);

        logger.info("恢复调度任务成功! id = {}", id);
        return BaseResult.success("恢复成功!");
    }

    /**
     * 运行一次调度任务
     * @param id
     * @return
     */
//    @Operationlogger(value = "运行一次调度任务")
//    @RequiresPermissions("job:run")
    @RequiresRoles("superAdmin")
    @ResponseBody
    @PostMapping(value = "/run/{id}")
    public ResponseEntity<String> runOnce(@PathVariable("id") Long id) {
        logger.debug("运行一次调度任务! id = {}", id);
        if (null == id) {
            logger.info("运行一次调度任务不存在! id = {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("运行一次调度任务不存在!");
        }
        scheduleJobService.runOnce(id);

        logger.info("运行一次调度任务成功! id = {}", id);
        return ResponseEntity.ok("运行成功!");
    }

    /**
     * 查看调度任务详情信息
     * @param id
     * @param modelMap
     * @return
     */
    @RequiresPermissions("job:view")
    @GetMapping("view/{id}")
    public BaseResult view(@PathVariable("id")Long id, ModelMap modelMap){
        SysScheduleJob model = scheduleJobService.selectByPrimaryKey(id);
        if(null != model){
            //创建者
        	SysUser userCreate = sysUserService.selectByPrimaryKey(model.getCreatorId());
//            if(null != userCreate){
//                model.setCreateByName(userCreate.getUsername());
//            }
        }
        return BaseResult.success(model);
    }

    /**
     * 检验任务是否存在
     * @param jobName
     * @param jobGroup
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/isExist")
    public Boolean isExist(Long id, String jobName, String jobGroup) throws Exception {
        boolean flag = true;
        logger.debug("检验任务是否存在参数! id= {}, jobName= {}, jobGroup= {}", id, jobName, jobGroup);
        if(StringUtils.isNotEmpty(jobName) && StringUtils.isNotEmpty(jobGroup)){
            SysScheduleJob record = scheduleJobService.findByJobNameAndJobGroup(jobName, jobGroup);
            if (null != record && !record.getId().equals(id)) {
                flag = false;
            }
        }
        logger.info("检验任务是否存在结果! flag = {}", flag);
        return flag;
    }

}
