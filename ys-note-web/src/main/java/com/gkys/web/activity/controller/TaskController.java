package com.gkys.web.activity.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gkys.common.base.BaseController;
import com.gkys.common.base.BaseResult;
import com.gkys.model.sys.SysUser;
import com.gkys.web.util.UserUtil;

@Controller
@RestController
@RequestMapping(value = "api/activity/task")
public class TaskController extends BaseController {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private TaskService taskService;

	/**
	 * 任务列表
	 * @param page
	 * @param limit
	 * @return
	 */
	@PostMapping("start")
	public BaseResult startWorkflow(String processDefinitionKey){
		SysUser sysUser=UserUtil.getUserFromSession(session);
		ProcessInstance processInstance = null;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(String.valueOf(sysUser.getId()));
            Map<String, Object> variables = new HashMap<String, Object>();
            String businessKey="100";
            processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
            String processInstanceId = processInstance.getId();
            logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[]{"leave", businessKey, processInstanceId, variables});
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
		return BaseResult.success();
	}

}
