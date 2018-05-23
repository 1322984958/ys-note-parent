$(function() {
	//渲染表格
	layui.table.render({
		elem : '#table',
		url : '../api/activity/model/list',
		method: 'post',
 		where: {
	  		token : getToken()
		},
		page: true,
		cols: [[
			{type:'numbers'},
			{type: 'checkbox'},
			{field:'id', sort: true, title: 'ID'},
			{field:'key', sort: true, title: 'KEY'},
			{field:'name', sort: true, title: '名称'},
			{field:'version', sort: false, title: '版本号'},
			{field:'createTime', sort: true, templet:function(d){ return layui.util.toDateString(d.createTime); },width: 165,title: '创建时间'},
			{field:'updateTime', sort: true, templet:function(d){ return layui.util.toDateString(d.createTime); },width: 165,title: '最后更新时间'},
			{field:'version', sort: false, title: '元数据'},
			{align:'center', toolbar: '#barTpl', minWidth: 300, title: '操作'}
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
		showEditModel(null);
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
 
		if(layEvent === 'edit'){ //修改
			window.open('../modeler.html?modelId='+data.id);
//			showEditModel(data);
		} else if(layEvent === 'del'){ //删除
			doDelete(obj);
		} else if(layEvent === 'deploy'){ //部署
			doDeploy(data.id);
		} else if(layEvent === 'exportBpmn'){
			doExport(data.id, 'bpmn');
		} else if(layEvent === 'exportJson'){
			doExport(data.id, 'json');
		}
	});
	
	//搜索按钮点击事件
	$("#main-content").on("click","#searchBtn",function(){
		doSearch(table);
	});
});

function doDeploy(modelId){
	layui.use(['ysAjax'], function(ysAjax){
		ysAjax.postJson('activity/model/deploy/'+modelId, {}, function(code,resultData,message){
			if(code==1){
				layer.msg(message,{icon: 1});
				layer.closeAll('page');
				layui.table.reload('table', {});
			}else{
				layer.msg(message,{icon: 2});
			}
		})
	})
}
function doExport(modelId,type){
	window.open('../api/activity/model/export/'+modelId+'/'+type+'?token='+getToken());
}

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

//删除
function doDelete(obj){
	layer.confirm('确定要删除吗？', function(index){
		layer.close(index);
		layer.load(1);
		layui.use(['ysAjax'], function(ysAjax){
			ysAjax.postJson('activity/model/delete/'+obj.data.id, {}, function(code,resultData,message){
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