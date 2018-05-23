package com.gkys.common.base;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gkys.common.util.SpringContextUtil;


/**
 * 实现BaseService抽象类
 * Created by ys on 2017/01/07.
 */
public abstract class BaseServiceImpl<Mapper, T, ID extends Serializable> implements BaseService<T,ID> {
	private final Logger logger=LoggerFactory.getLogger(getClass());
	public Mapper mapper;

	@Override
	public void initMapper() {
		this.mapper = SpringContextUtil.getBean(getMapperClass());
		//logger.debug(">>>>> mapper{}", mapper);
	}

	/**
	 * 获取类泛型class
	 * @return
	 */
	public Class<Mapper> getMapperClass() {
		return (Class<Mapper>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected BaseMapper getBaseMapper(){
		if(this.mapper==null){
			initMapper();
		}
		BaseMapper baseMapper=(BaseMapper<T, ID>)this.mapper;
		return baseMapper;
	}
	
	@Override
	public int insert(T record){
		return getBaseMapper().insert(record);
	}
	@Override
	public int insertSelective(T record){
		return getBaseMapper().insertSelective(record);
	}
	@Override
	public int updateByPrimaryKey(T record){
		return getBaseMapper().updateByPrimaryKey(record);
	}
	@Override
	public int updateByPrimaryKeySelective(T record){
		return getBaseMapper().updateByPrimaryKeySelective(record);
	}
	@Override
	public int updateByExample(T record,Object example){
		return getBaseMapper().updateByExample(record,example);
	}
	@Override
	public int updateByExampleSelective(T record,Object example){
		return getBaseMapper().updateByExampleSelective(record,example);
	}
	@Override
	public int delete(T record){
		return getBaseMapper().delete(record);
	}
	@Override
	public int deleteByPrimaryKey(Object key){
		return getBaseMapper().deleteByPrimaryKey(key);
	}
	@Override
	public int deleteByExample(Object example){
		return getBaseMapper().deleteByExample(example);
	}

	@Override
	public List<T> selectAll(){
		return getBaseMapper().selectAll();
	}
	@Override
	public int selectCount(T record){
		return getBaseMapper().selectCount(record);
	}
	@Override
	public T selectByPrimaryKey(ID key){
		return (T) getBaseMapper().selectByPrimaryKey(key);
	}
	@Override
	public List<T> select(T record){
		return getBaseMapper().select(record);
	}
	@Override
	public T selectOne(T record){
		return (T) getBaseMapper().selectOne(record);
	}

	@Override
	public List<T> selectByExample(Object example){
		return getBaseMapper().selectByExample(example);
	}
	
	@Override
	public T getOneByExample(Object example){
		List<T> list=selectByExample(example);
		if(CollectionUtils.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public int selectCountByExample(Object example){
		return getBaseMapper().selectCountByExample(example);
	}
}