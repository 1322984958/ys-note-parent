package com.gkys.web.vo.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class SysOrganizationVo {
	/** id **/
	private Long id;
	/** 类别 **/
	private String category;
	/** 名称 **/
	private String name;
	/** 值 **/
	private String value;
	/** 父级id **/
	private Long parentId;
	/** 创建时间 **/
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	/****/
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifyTime;
	/****/
	@Column(name = "description")
	private String description;
	/** 状态 **/
	private String state;
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

}