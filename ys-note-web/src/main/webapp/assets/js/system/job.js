$(function() {
	//渲染表格
	layui.table.render({
		elem : '#table',
		url : '../api/job/list',
		method: 'post',
 		where: {
	  		token : getToken()
		},
		page: true,
		cols: [[
			{type:'numbers'},
			{field:'id', sort: true, title: 'ID'},
			{field:'jobName', sort: true, title: '任务名称'},
			{field:'jobGroup', sort: true, title: '任务分组'},
			{field:'cron', sort: false, title: '执行计划'},
			{field:'isLocal', sort: true, width: 80, title: '调度方式',templet: function(d){
					return d.isLocal == '1'? '本地' : '远程';
				}
			},
			{field:'isAsync', sort: true, templet: '#isAsyncTpl',width: 80, title: '是否异步'},
			{field:'createTime', sort: true, templet:function(d){ return layui.util.toDateString(d.createTime); }, width: 165,title: '创建时间'},
			{field:'描述', sort: false, title: '描述'},
			{field:'state', sort: true, templet: '#stateTpl',width: 80, title: '状态'},
			{align:'center', toolbar: '#barTpl', minWidth: 280, title: '操作'}
    	]],
    	response: {
    		statusName: 'code',
    		statusCode: 1,
    		countName:'total',
    		dataName:'data'
    	}
	});
	
	//添加按钮点击事件
	$("#addBtn").click(function(){
		showEditModel(null);
	});
	
	layui.use(['ysAjax'], function(ysAjax){
		layui.form.on('submit(btnSubmit)', function(data){
			data.field.token = getToken();
			data.field.tokenForm = $.cookie('tokenForm');
			var url = 'job/create';
			if(data.field.id){
				url = 'job/update'
			}
			layer.load(1);
			ysAjax.postJson(url, data.field, function(data){
				layer.closeAll('loading');
				if(data.code==1){
					layer.msg(data.message,{icon: 1});
					layer.closeAll('page');
					layui.table.reload('table', {});
					getFormToken();
				}else{
					layer.msg(data.message,{icon: 2});
				}
			}, "JSON");
			return false;
		});
	});
	
	//工具条点击事件
	layui.table.on('tool(table)', function(obj){
		var data = obj.data;
		var layEvent = obj.event;
 
		if(layEvent === 'edit'){ //修改
			showEditModel(data);
		} else if(layEvent === 'del'){ //删除
			doDelete(obj);
		} else if(layEvent == 'pause'){
			doPause(data.id);
		} else if(layEvent == 'resume'){
			doResume(data.id);
		} else if(layEvent == 'runOnce'){
			doRunOnce(data.id);
		}
	});
	
	//监听状态开关操作
	layui.form.on('switch(statusCB)', function(obj){
		updateStatus(obj);
	});
	
	//搜索按钮点击事件
	$("#searchBtn").click(function(){
		doSearch();
	});
	var laydate = layui.laydate;
	laydate.render({ 
		elem: '#startTime',
		type: 'datetime'
	});
	laydate.render({ 
		elem: '#endTime',
		type: 'datetime'
	});

});

//显示表单弹窗
function showEditModel(data){
	getFormToken();
	layer.open({
		type: 1,
		title: data==null?"添加任务":"修改任务",
		area: '600px',
		offset: '120px',
		content: $("#addModel").html()
	});
	$("#editForm")[0].reset();
	$("#editForm").attr("method","POST");
	if(data!=null){
		$("#editForm input[name=id]").val(data.id);
		$("#editForm input[name=jobName]").val(data.jobName);
		$("#editForm textarea[name=remarks]").val(data.remarks);
	}
	$("#btnCancel").click(function(){
		layer.closeAll('page');
	});
    layui.form.on('radio(isLocal)', function(data){
		if(data.value=='1'){
			$('.local').show();
			$('.remote').hide();
		}else{
			$('.local').hide();
			$('.remote').show();
		}
    });
}

function doPause(id){
	layer.load(1);
	layui.use(['ysAjax'], function(ysAjax){
		ysAjax.postJson('job/pause/'+id, {},function(code,resultData,message){
			if(code==1){
				layer.closeAll('loading');
				layer.msg(resultData,{icon: 1});
			}else{
				layer.msg(resultData,{icon: 2});
			}
		})
	})
}

function doResume(id){
	layer.load(1);
	layui.use(['ysAjax'], function(ysAjax){
		ysAjax.postJson('job/resume/'+id, {},function(code,resultData,message){
			if(code==1){
				layer.closeAll('loading');
				layer.msg(resultData, {icon: 1});
			}else{
				layer.msg(resultData, {icon: 2});
			}
		})
	})
}

function doRunOnce(id){
	layer.load(1);
	layui.use(['ysAjax'], function(ysAjax){
		ysAjax.postJson('job/run/'+id, {},function(code,resultData,message){
			if(code==1){
				layer.closeAll('loading');
				layer.msg(resultData, {icon: 1});
			}else{
				layer.msg(resultData, {icon: 2});
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
			ysAjax.deleteJson('job/delete/'+obj.data.id, {},function(code,resultData,message){
				layer.closeAll('loading');
				if(code==1){
					layer.msg(resultData,{icon: 1});
					obj.del();
				}else{
					layer.msg(resultData,{icon: 2});
				}
			})
		})
	});
}

//更改状态
function updateStatus(obj){
	layer.load(1);
	var newStatus = obj.elem.checked?0:1;
	$.post("api/job/status", {
		roleId: obj.elem.value,
		status: newStatus,
		token: getToken()
	}, function(data){
		layer.closeAll('loading');
		if(data.code==200){
			layui.table.reload('table', {});
		}else{
			layer.msg(data.msg,{icon: 2});
			layui.table.reload('table', {});
		}
	});
}

//搜索
function doSearch(){
	var jobName = $("#jobName").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	layui.table.reload('table', {where: {jobName: jobName,startTime: startTime,endTime: endTime}});
}