package com.gkys.model.job;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

public class SysScheduleJob {
	/** 任务调度的参数key */
	@Transient
	public static final String JOB_PARAM_KEY = "jobParam";
	@Id
	private Long id;
	@Column(name = "version")
	private Integer version;
	/*** 任务名称 ***/
	private String jobName;
	/*** 任务分组 ***/
	private String jobGroup;
	/*** 执行计划 ***/
	private String cron;
	/*** 调度方式 false 远程 true 本地 ***/
	private Boolean isLocal;
	/*** 远程请求方式 只支持POST ***/
	private String remoteRequestMethod;
	/*** 远程执行url ***/
	private String remoteUrl;
	/*** 执行类名称 包名+类名 ***/
	private String beanClass;
	/*** 执行方法名称 ***/
	private String methodName;
	/*** 参数 ***/
	private String params;
	/*** 是否异步 0否 1是 ***/
	private Boolean isAsync;
	/*** 任务状态 0禁用 1启用 ***/
	private Integer status;
	/*** 描述 ***/
	private String remarks;
	/*** 创建者 ***/
	private Long creatorId;
	/*** 修改者 ***/
	private Long modifierId;
	/** 创建时间 **/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createTime;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public Boolean getIsLocal() {
		return isLocal;
	}

	public void setIsLocal(Boolean isLocal) {
		this.isLocal = isLocal;
	}

	public String getRemoteRequestMethod() {
		return remoteRequestMethod;
	}

	public void setRemoteRequestMethod(String remoteRequestMethod) {
		this.remoteRequestMethod = remoteRequestMethod;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public String getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(String beanClass) {
		this.beanClass = beanClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Boolean getIsAsync() {
		return isAsync;
	}

	public void setIsAsync(Boolean isAsync) {
		this.isAsync = isAsync;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getModifierId() {
		return modifierId;
	}

	public void setModifierId(Long modifierId) {
		this.modifierId = modifierId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
}
