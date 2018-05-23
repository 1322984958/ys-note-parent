$(function() {
	//渲染表格
	layui.table.render({
		elem : '#table',
		url : '../api/activity/process/list',
		method: 'post',
 		where: {
	  		token : getToken()
		},
		page: true,
		cols: [[
			{type:'numbers'},
			{type: 'checkbox'},
			{field:'id', sort: true, title: 'id'},
			{field:'deploymentId', sort: true, title: '部署id'},
			{field:'name', sort: true, title: '名称'},
			{field:'key', sort: false, title: 'KEY'},
			{field:'version', sort: false, title: '版本号'},
			{field:'XML', sort: false, title: 'XML', templet:function(d){
				return '<a href="javascript:void(0)" target="_blank" onclick="openResource(\''+d.id+'\', \'xml\')">'+d.resourceName+'</a>';
			}},
			{field:'XML', sort: false, title: '图片', templet:function(d){
				return '<a href="javascript:void(0)" target="_blank" onclick="openResource(\''+d.id+'\', \'image\')">'+d.diagramResourceName+'</a>';
			}},
			{field:'deploymentTime', sort: false, title: '部署时间', templet:function(d){ return layui.util.toDateString(d.createTime); },width: 165},
			{align:'center', toolbar: '#barTpl', minWidth: 120, title: '操作'}
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
			ysAjax.postJson('activity/task/start', {processDefinitionKey: 'asrfawe'}, function(code,resultData,message){
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
 
		if(layEvent === 'convertToModel'){ //转为model
			doConvertToModel(obj);
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

//转为model
function doConvertToModel(obj){
	layui.use(['ysAjax'], function(ysAjax){
		ysAjax.postJson('activity/process/convertToModel/'+obj.data.id, {}, function(code,resultData,message){
			if(code==1){
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
			ysAjax.postJson('activity/process/delete/processDefinition/'+obj.data.deploymentId, {}, function(code,resultData,message){
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

function openResource(processDefinitionId, resourceType){
	window.open(baseApiUrl+'activity/process/resource/read?processDefinitionId='+processDefinitionId+'&resourceType='+resourceType+'&token='+getToken());
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