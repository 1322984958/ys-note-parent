package com.gkys.service.job;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gkys.api.job.SysScheduleJobLogService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.mapper.job.SysScheduleJobLogMapper;
import com.gkys.model.job.SysScheduleJobLog;

import tk.mybatis.mapper.entity.Example;

@Transactional
@BaseService
@Service
public class SysScheduleJobLogServiceImpl extends BaseServiceImpl<SysScheduleJobLogMapper,SysScheduleJobLog,Long> implements SysScheduleJobLogService{
	private Logger logger=LoggerFactory.getLogger(SysScheduleJobLogServiceImpl.class);
	
	@Transactional(readOnly = true)
    @Override
    public PageInfo<SysScheduleJobLog> findPage(Integer pageNum, Integer pageSize, Long jobId, String jobName, String startTime, String endTime){
		try{
	        Example example = new Example(SysScheduleJobLog.class);
	        Example.Criteria criteria = example.createCriteria();
	
	        criteria.andEqualTo("jobId", jobId);
	        if(StringUtils.isNotEmpty(jobName)){
	            criteria.andLike("jobName", "%"+jobName+"%");
	        }
	        if(StringUtils.isNotEmpty(startTime)){
	            criteria.andGreaterThanOrEqualTo("createTime", DateFormatUtils.format(DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
	        }
	        if(StringUtils.isNotEmpty(endTime)){
	            criteria.andLessThanOrEqualTo("createTime", DateFormatUtils.format(DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
	        }
	        //倒序
	        example.orderBy("createTime").desc();
	        //分页
	        PageHelper.startPage(pageNum,pageSize);
	        List<SysScheduleJobLog> jobLogList = this.selectByExample(example);
	
	        return new PageInfo<SysScheduleJobLog>(jobLogList);
		}catch(Exception e){
			logger.error("出错：",e);
		}
		return null;
    }
}
