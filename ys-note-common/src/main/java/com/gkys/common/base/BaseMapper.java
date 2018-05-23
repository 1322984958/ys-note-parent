package com.gkys.common.base;

import tk.mybatis.mapper.common.ExampleMapper;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/24.
 */
public interface BaseMapper<T, ID extends Serializable> extends tk.mybatis.mapper.common.BaseMapper<T>,ExampleMapper<T>{
}
