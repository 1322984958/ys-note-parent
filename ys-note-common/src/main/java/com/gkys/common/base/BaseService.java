package com.gkys.common.base;

import java.io.Serializable;
import java.util.List;

/**
 * BaseService接口
 * Created by ys on 2017/01/07.
 */
public interface BaseService<T, ID extends Serializable>{
	void initMapper();

	int insert(T record);
	int insertSelective(T record);
	int updateByPrimaryKey(T record);
	int updateByPrimaryKeySelective(T record);
	int updateByExample(T record,Object example);
	int updateByExampleSelective(T record,Object example);
	int delete(T record);
	int deleteByPrimaryKey(Object key);
	int deleteByExample(Object example);

	List<T> selectAll();
	int selectCount(T record);
	T selectByPrimaryKey(ID key);
	List<T> select(T record);
	T selectOne(T record);

	List<T> selectByExample(Object example);
	
	T getOneByExample(Object example);
	
	int selectCountByExample(Object example);

}