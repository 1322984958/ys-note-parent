package com.gkys.web.activity.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gkys.common.base.BaseController;
import com.gkys.common.base.BaseResult;

@Controller
@RestController
@RequestMapping(value = "api/activity/process")
public class ProcessController extends BaseController {
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
	@Autowired
	private HistoryService historyService;
	@Autowired
	ProcessEngineFactoryBean processEngine;
	@Autowired
	ProcessEngineConfiguration processEngineConfiguration;

	/**
	 * 流程定义列表
	 * @param page
	 * @param limit
	 * @return
	 */
	@PostMapping("list")
	public BaseResult list(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer limit) {
		List<Map<String,Object>> objects = new ArrayList<Map<String,Object>>();
		Page myPage=PageHelper.startPage(page, limit);
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(page, limit);
        for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("id", processDefinition.getId());
            map.put("deploymentId", deployment.getId());
            map.put("name", processDefinition.getName());
            map.put("key", processDefinition.getKey());
            map.put("version", processDefinition.getVersion());
            map.put("resourceName", processDefinition.getResourceName());
            map.put("diagramResourceName", processDefinition.getDiagramResourceName());
            map.put("deploymentTime", deployment.getDeploymentTime());
            objects.add(map);
        }
        BaseResult baseResult=BaseResult.success(objects);
        baseResult.setTotal(processDefinitionQuery.count());
		return baseResult;
	}

	/**
	 * 运行中的流程列表
	 * @param page
	 * @param limit
	 * @return
	 */
	@PostMapping("running/list")
	public BaseResult runningList(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer limit) {
		List<Map<String,Object>> result=new ArrayList<>();
		Page myPage=PageHelper.startPage(page, limit);
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery().orderByProcessInstanceId().desc();//.processDefinitionKey("lear").active()
		List<ProcessInstance> list = query.listPage(myPage.getStartRow(), myPage.getEndRow());
		for (ProcessInstance processInstance : list) {
		    String businessKey = processInstance.getBusinessKey();
		    String processDefinitionId=processInstance.getProcessDefinitionId();
		    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		    Map<String,Object> map=new HashMap<>();
		    map.put("processDefinitionId", processDefinition.getId());
		    map.put("processInstanceId", processInstance.getId());
		    map.put("name", processDefinition.getName());
		    map.put("businessKey", businessKey);
		    map.put("isSuspended", processInstance.isSuspended());
		    map.put("category", processDefinition.getCategory());
		    // 设置当前任务信息
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().orderByTaskCreateTime().desc().listPage(0, 1);
            if(CollectionUtils.isNotEmpty(tasks)){
            	map.put("task", tasks.get(0).getName());
            }
		    result.add(map);
		}
		return BaseResult.success(result);
	}
	
	/**
     * 挂起、激活流程实例
     */
    @RequestMapping(value = "update/{state}/{processInstanceId}")
    public BaseResult updateState(@PathVariable("state") String state, @PathVariable("processInstanceId") String processInstanceId) {
        if (state.equals("active")) {
        	runtimeService.activateProcessInstanceById(processInstanceId);
        	return BaseResult.success("已激活ID为[" + processInstanceId + "]的流程实例。");
        } else if (state.equals("suspend")) {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            return BaseResult.success("已挂起ID为[" + processInstanceId + "]的流程实例。");
        }
        return BaseResult.success();
    }
	
	/**
	 * 删除流程定义
	 * @param deploymentId
	 * @return
	 */
	@PostMapping(value = "delete/processDefinition/{deploymentId}")
	public BaseResult deleteProcessDefinition(@PathVariable("deploymentId") String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
		return BaseResult.success();
	}
	
	/**
	 * 删除流程实例
	 * @param deploymentId
	 * @return
	 */
	@PostMapping(value = "delete/processInstance/{processInstanceId}")
	public BaseResult deleteProcessInstance(@PathVariable("processInstanceId") String processInstanceId) {
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
		if(pi==null){//流程已结束
			historyService.deleteHistoricProcessInstance(processInstanceId); 
		}else{
			runtimeService.deleteProcessInstance(processInstanceId,""); 
			historyService.deleteHistoricProcessInstance(processInstanceId);
		}
		return BaseResult.success();
	}
	
	@PostMapping(value = "convertToModel/{processDefinitionId}")
	public BaseResult convertToModel(@PathVariable("processDefinitionId") String processDefinitionId)
            throws UnsupportedEncodingException, XMLStreamException {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        com.fasterxml.jackson.databind.node.ObjectNode modelNode = converter.convertToJson(bpmnModel);
        Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getResourceName());
        modelData.setCategory(processDefinition.getDeploymentId());

        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());

        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
        return BaseResult.success();
    }
	
	/**
     * 读取带跟踪的图片
     */
    @RequestMapping(value = "trace/auto/{executionId}")
    public void readResource(@PathVariable("executionId") String executionId, HttpServletResponse response)
            throws Exception {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);

        // 使用spring注入引擎请使用下面的这行代码
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
    
    /**
     * 读取资源，通过部署ID
     * @param processDefinitionId 流程定义
     * @param resourceType        资源类型(xml|image)
     * @throws Exception
     */
    @RequestMapping(value = "resource/read")
    public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType) throws Exception {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String resourceName = "";
        if (resourceType.equals("image")) {
        	response.setContentType("image/jpeg");
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
        	response.setContentType("application/xml");
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
}
