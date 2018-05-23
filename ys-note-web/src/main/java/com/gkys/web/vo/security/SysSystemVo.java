package com.gkys.web.vo.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.annotation.JSONField;

public class SysSystemVo {
	/** id **/
	private Long id;
	/** 类别(background,weixin,app) **/
	private String category;
	/** 名称 **/
	private String name;
	/** 值 **/
	private String value;
	/** 创建时间 **/
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	/****/
	@JSONField(serialize = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifyTime;
	/****/
	private String description;
	/** 状态 **/
	private String state;
	/** 根目录 **/
	private String basepath;
	/** 系统标题 **/
	private String title;
	/** 排序 **/
	private Integer orders;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBasepath() {
		return basepath;
	}
	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
	}
	
}