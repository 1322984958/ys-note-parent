package com.gkys.api.sys;

import java.util.List;
import java.util.Map;
import com.gkys.model.sys.SysOrganization;
import com.gkys.common.base.BaseResult;
import com.gkys.common.base.BaseService;

public interface SysOrganizationService extends BaseService<SysOrganization,Long>{

	BaseResult save(SysOrganization sysOrganization);
}