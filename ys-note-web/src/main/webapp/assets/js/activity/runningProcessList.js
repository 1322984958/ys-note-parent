$(function() {
	//渲染表格
	layui.table.render({
		elem : '#table',
		url : '../api/activity/process/running/list',
		method: 'post',
 		where: {
	  		token : getToken()
		},
		page: true,
		cols: [[
			{type:'numbers'},
			{type: 'checkbox'},
			{field:'processDefinitionId', sort: true, title: '流程定义id'},
			{field:'processInstanceId', sort: true, title: '流程实例id'},
			{field:'businessKey', sort: true, title: '业务主键'},
			{field:'task', sort: false, title: '当前任务'},
			{field:'isSuspended', sort: true, title: '是否挂起', templet: function(d){
				return d.isSuspended?'是':'否';
			}},
			{align:'center', toolbar: '#barTpl', minWidth: 200, title: '操作'}
    	]],
    	response: {
    		statusName: 'code',
    		statusCode: 1,
    		countName:'total',
    		dataName:'data'
    	}
	});
	
	//添加按钮点击事件
	$("#main-content").on("click","#addBtn",function(){
		//showEditModel(null);
		layui.use(['ysAjax'], function(ysAjax){
			ysAjax.postJson('activity/task/start', {processDefinitionKey: 'one'}, function(code,resultData,message){
				if(code==1){
					layer.msg(message,{icon: 1});
					layui.table.reload('table', {});
				}else{
					layer.msg(message,{icon: 2});
				}
			})
		})
	});
	
	 //自定义验证规则  
	layui.form.verify({
	})
	//表单提交事件
	layui.form.on('submit(btnSubmit)', function(data) {
		data.field.token = getToken();
		data.field.tokenForm = $.cookie('tokenForm');
		layer.load(1);
		layui.use(['ysAjax'], function(ysAjax){
			ysAjax.postJson('activity/model/save', data.field, function(code,resultData,message){
				if(code==1){
					layer.msg(message,{icon: 1});
					layer.closeAll();
					layui.table.reload('table', {});
				}else{
					layer.msg(message,{icon: 2});
				}
			})
		})
		return false;
	});
	
	//工具条点击事件
	layui.table.on('tool(table)', function(obj){
		var data = obj.data;
		var layEvent = obj.event;
 
		if(layEvent === 'active'){ //激活
			doActive(obj);
		} else if(layEvent === 'suspend'){ //挂起
			doSuspend(obj);
		} else if(layEvent === 'del'){ //删除
			doDelete(obj);
		}
	});
	
	//搜索按钮点击事件
	$("#main-content").on("click","#searchBtn",function(){
		doSearch(table);
	});
});

//显示表单弹窗
function showEditModel(data){
	getFormToken();
	layer.open({
		type: 1,
		title: data==null?"添加模型":"修改模型",
		area: '600px',
		offset: '120px',
		fixed: false,
		content: $("#addModel").html()
	});
	$("#editForm")[0].reset();
	$("#editForm").attr("method","POST");
	
	var selectItem = "";
	if(data!=null){
		$("#editForm input[name=id]").val(data.id);
		$("#editForm input[name=name]").val(data.name);
		$("#editForm input[name=key]").val(data.key);
		$("#editForm textarea[name=description]").val(data.description);
	}
	$("#btnCancel").click(function(){
		layer.closeAll('page');
	});
}

function doActive(obj){
	layui.use(['ysAjax'], function(ysAjax){
		ysAjax.postJson('activity/process/update/active/'+obj.data.processInstanceId, {}, function(code,resultData,message){
			if(code==1){
				layui.table.reload('table', {});
				layer.msg(message,{icon: 1});
			}else{
				layer.msg(message,{icon: 2});
			}
		})
	})
}

function doSuspend(obj){
	layui.use(['ysAjax'], function(ysAjax){
		ysAjax.postJson('activity/process/update/suspend/'+obj.data.processInstanceId, {}, function(code,resultData,message){
			if(code==1){
				layui.table.reload('table', {});
				layer.msg(message,{icon: 1});
			}else{
				layer.msg(message,{icon: 2});
			}
		})
	})
}
//删除
function doDelete(obj){
	layer.confirm('确定要删除吗？', function(index){
		layer.close(index);
		layer.load(1);
		layui.use(['ysAjax'], function(ysAjax){
			ysAjax.postJson('activity/process/delete/processInstance/'+obj.data.processInstanceId, {}, function(code,resultData,message){
				if(code==1){
					layer.msg(message,{icon: 1});
					layer.closeAll('loading');
					obj.del();
				}else{
					layer.msg(message,{icon: 2});
				}
			})
		})
	});
}

//搜索
function doSearch(table){
	var key = $("#searchKey").val();
	var value = $("#searchValue").val();
	if (value=='') {
		key = '';
	}
	layui.table.reload('table', {where: {searchKey: key,searchValue: value}});
}