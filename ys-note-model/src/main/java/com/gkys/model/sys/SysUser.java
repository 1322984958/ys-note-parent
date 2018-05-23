package com.gkys.model.sys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.alibaba.fastjson.annotation.JSONField;

public class SysUser implements Serializable{
	/** id **/
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "version")
	private Integer version;
	/** 类别 **/
	@Column(name = "category")
	private String category;
	/** 所属系统表id **/
	@Column(name = "system_id")
	private Long systemId;
	/** 用户名 **/
	@NotNull(message="用户名不能为空")
	@Size(min = 2, max = 10)
	@Column(name = "username")
	private String username;
	/** 密码 **/
	@JSONField(serialize=false)
	@Column(name = "password")
	private String password;
	/** 真名 **/
	@Column(name = "name")
	private String name;
	/****/
	@Email(message="email地址无效！")
	@Column(name = "email")
	private String email;
	/****/
	@Column(name = "telephone")
	private String telephone;
	/****/
	@Column(name = "qq")
	private String qq;
	/****/
	@JSONField(serialize=false)
	@Column(name = "salt")
	private String salt;
	/** 状态 **/
	@Column(name = "state")
	private String state;
	/** 创建时间 **/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createTime;
	/****/
	@JSONField(serialize=false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modify_time")
	private Date lastModifyTime;
	/****/
	@Column(name = "description")
	private String description;
	/** 头像 **/
	@Column(name = "portrait_img")
	private String portraitImg;
	/** 删除标记 **/
	@Column(name = "delete_mark")
	private Integer deleteMark=0;
	/****/
	@Column(name = "pro_id")
	private String proId;
	/****/
	@Column(name = "city_id")
	private String cityId;
	/****/
	@Column(name = "area_id")
	private String areaId;
	/** 城市中文名 **/
	@Column(name = "pro_city_name")
	private String proCityName;
	/** 详细位置 **/
	@Column(name = "location")
	private String location;
	/** 排序 **/
	@Column(name = "orders")
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

}