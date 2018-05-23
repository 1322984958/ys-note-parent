package com.gkys.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gkys.api.sys.SysSystemService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.mapper.sys.SysSystemMapper;
import com.gkys.model.sys.SysSystem;

@Service
@BaseService
@Transactional
public class SysSystemServiceImpl extends BaseServiceImpl<SysSystemMapper,SysSystem,Long> implements SysSystemService{
}