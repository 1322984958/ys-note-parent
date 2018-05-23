package com.gkys.common.vo;

import java.util.List;
import java.util.Map;

import com.gkys.common.util.ModelToStringUtil;

public class TreeNodeVo<T> {
	private T id;
	private T parentId;
	private String name;
	private Boolean open;
	private Boolean checked;
	private Map<String,Object> otherInfo;
	private List<TreeNodeVo> childrens;

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public T getParentId() {
		return parentId;
	}

	public void setParentId(T parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public List<TreeNodeVo> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<TreeNodeVo> childrens) {
		this.childrens = childrens;
	}

	@Override
	public String toString() {
		return ModelToStringUtil.toString(getClass(), this);
	}

	public Map<String,Object> getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(Map<String,Object> otherInfo) {
		this.otherInfo = otherInfo;
	}
}
