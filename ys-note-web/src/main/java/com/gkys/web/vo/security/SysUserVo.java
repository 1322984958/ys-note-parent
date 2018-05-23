package com.gkys.web.vo.security;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.annotation.JSONField;

public class SysUserVo {
	private Long id;
	private String category;
	private String systemId;
	private SysSystemVo sysSystemVo;
	private String username;
	/** 密码 **/
	@JSONField(serialize = false)
	private String password;
	/** 真名 **/
	private String name;
	/****/
	private String email;
	/****/
	private String telephone;
	/****/
	private String qq;
	/****/
	@JSONField(serialize = false)
	private String salt;
	/** 状态 **/
	private String state;
	/** 创建时间 **/
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	/****/
	@JSONField(serialize = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifyTime;
	/****/
	private String description;
	/** 头像 **/
	private String portraitImg;
	/** 删除标记 **/
	private Integer deleteMark = 0;
	/****/
	private String proId;
	/****/
	private String cityId;
	/****/
	private String areaId;
	/** 城市中文名 **/
	private String proCityName;
	/** 详细位置 **/
	private String location;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getPortraitImg() {
		return portraitImg;
	}

	public void setPortraitImg(String portraitImg) {
		this.portraitImg = portraitImg;
	}

	public Integer getDeleteMark() {
		return deleteMark;
	}

	public void setDeleteMark(Integer deleteMark) {
		this.deleteMark = deleteMark;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getProCityName() {
		return proCityName;
	}

	public void setProCityName(String proCityName) {
		this.proCityName = proCityName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public SysSystemVo getSysSystemVo() {
		return sysSystemVo;
	}

	public void setSysSystemVo(SysSystemVo sysSystemVo) {
		this.sysSystemVo = sysSystemVo;
	}

}
