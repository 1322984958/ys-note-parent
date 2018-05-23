$(function() {
	//渲染表格
	layui.table.render({
		elem : '#table',
		url : '../api/user/list',
		method: 'post',
 		where: {
	  		token : getToken()
		},
		page: true,
		cols: [[
			{type:'numbers'},
			{type: 'checkbox'},
			{field:'id', sort: true, title: 'ID'},
			{field:'systemName', sort: true, title: '系统名'},
			{field:'category', sort: true, title: '类别'},
			{field:'username', sort: true, title: '用户名'},
			{field:'name', sort: true, title: '名字'},
			{field:'email', sort: false, title: 'email'},
			{field:'telephone', sort: true, title: '手机号'},
			{field:'description', sort: true, title: '描述'},
			{field:'roleName', sort: true,title: '角色'},
			{field:'state', sort: true, templet: '#statusTpl',width: 80, title: '状态'},
			{field:'createTime', sort: true, templet:function(d){ return layui.util.toDateString(d.createTime); },width: 165,title: '创建时间'},
			{align:'center', toolbar: '#barTpl', minWidth: 180, title: '操作'}
    	]],
    	done: function(res, curr, count){
    		console.debug(res);
    		console.debug(curr);
    	},
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
	
	$('#main-content').on("click","#assign-roles-btn",function(){
		var userIds=[];
		$.each(layui.table.checkStatus('table').data,function(o){
			userIds.push(o.id);
		})
		if(userIds.length==0){
			layer.msg('请选择用户',{time:800,icon: 7});
			return;
		}
		assignRoles();
	});
	
	$("#main-content").on("click","#search-form #down",function(){
		layui.laytpl(searchDown.innerHTML).render({}, function(html){
			$("#search-form").html(html);
			layui.form.render('select');
		});
	});
	$("#main-content").on("click","#search-form #up",function(){
		layui.laytpl(searchUp.innerHTML).render({}, function(html){
			$("#search-form").html(html);
			layui.form.render('select');
		});
	});
	
	layui.laytpl(searchUp.innerHTML).render({}, function(html){
		$("#search-form").html(html);
		layui.form.render('select');
	});
	
	 //自定义验证规则  
	layui.form.verify({
		myPassword: [/^$|^[a-zA-Z0-9_!@#$%^&*?]{6,16}$/, '密码为6-16位，可包含字母数字以及_!@#$%^&*?'],
		myEmail: [/^$|^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/, '邮箱格式不对']  
	})
	//表单提交事件
	layui.form.on('submit(btnSubmit)', function(data) {
		data.field.token = getToken();
		data.field._method = $("#editForm").attr("method");
		data.field.tokenForm = $.cookie('tokenForm');
		layer.load(1);
		$.post("../api/user/save", data.field, function(data){
			layer.closeAll('loading');
			if(data.code==1){
				layer.msg(data.message,{icon: 1});
				layer.closeAll('page');
				layui.table.reload('table', {});
			}else{
				layer.msg(data.message,{icon: 2});
			}
		}, "JSON");
		return false;
	});
	
	//工具条点击事件
	layui.table.on('tool(table)', function(obj){
		var data = obj.data;
		var layEvent = obj.event;
 
		if(layEvent === 'edit'){ //修改
			showEditModel(data);
		} else if(layEvent === 'del'){ //删除
			doDelete(obj);
		} else if(layEvent === 'reset'){ //重置密码
			doReSet(obj.data.userId);
		}
	});
	
	//监听状态开关操作
	layui.form.on('switch(statusCB)', function(obj){
		updateStatus(obj);
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
		title: data==null?"添加用户":"修改用户",
		area: '600px',
		offset: '120px',
		fixed: false,
		content: $("#addModel").html()
	});
	$("#editForm")[0].reset();
	$("#editForm").attr("method","POST");
	
	layui.use(['treeSelect'],function () {
        layui.treeSelect({
	        elem: "#organization-select",
	        data: '../api/organization/tree',//可以是treedata，也可以是 获取treedata的URL地址
	        method: "POST"
	    });
    });
	var selectItem = "";
	if(data!=null){
		$("#editForm input[name=id]").val(data.id);
		$("#editForm input[name=systemCategory]").val(data.systemCategory);
		$("#editForm input[name=category][value='"+data.category+"']").prop("checked",true).siblings().prop("checked",false);
		$("#editForm input[name=username]").val(data.username);
		$("#editForm input[name=name]").val(data.name);
		$("#editForm input[name=sex]").val(data.sex);
		$("#editForm input[name=email]").val(data.email);
		$("#editForm input[name=telephone]").val(data.telephone);
		selectItem = data.roleId;
		if('男'==data.sex){
			$("#sexMan").attr("checked","checked");
			$("#sexWoman").removeAttr("checked");
		}else{
			$("#sexWoman").attr("checked","checked");
			$("#sexMan").removeAttr("checked");
		}
		layui.form.render('radio');
	}
	$("#btnCancel").click(function(){
		layer.closeAll('page');
	});
}

//获取所有角色
var roles = null;
function assignRoles(selectItem){
	if(roles!=null) {
		layer.open({
			type: 1,
			title: "分配角色",
			area: '600px',
			offset: '120px',
			fixed: false,
			content: $('#rolesSelect').html(),
			success:function(h,index){
				layui.laytpl($('#role-select').html()).render(roles, function(html){
					$('#role-select').html(html);
				})
			}
		});
		layui.use(['ysMultiSelect'], function(ysMultiSelect){
			ysMultiSelect('#role-select')
		})
		layer.closeAll('loading');
	}else{
		layer.load(1);
		layui.use(['ysAjax'], function(ysAjax){
			ysAjax.postJson('role/list',{token: getToken()}, function(code,resultData,message){
				if(code==1){
					roles = resultData;
					assignRoles(selectItem);
				}else{
					layer.msg(message);
				}
			})
		})
	}
}
//表单提交事件
layui.form.on('submit(assignRolesSubmit)', function(data) {
	data.field.token = getToken();
	delete data.field.roleId;
	data.field.roleIds=$('#role-select').val();
	if(data.field.roleIds.length==0){
		layer.msg('请选择角色',{time:800,icon: 7});
		return false;
	}
	var userIds=[];
	$.each(layui.table.checkStatus('table').data,function(i,o){
		userIds.push(o.id);
	})
	data.field.userIds=userIds;
	layer.load(1);
	$.post("../api/user/assignRoles", data.field, function(data){
		layer.closeAll('loading');
		if(data.code==1){
			layer.msg(data.message,{icon: 1});
			layer.closeAll('page');
			layui.table.reload('table', {});
		}else{
			layer.msg(data.message,{icon: 2});
		}
	}, "JSON");
	return false;
});

//删除
function doDelete(obj){
	layer.confirm('确定要删除吗？', function(index){
		layer.close(index);
		layer.load(1);
		$.ajax({
			url: "api/user/"+obj.data.userId+"?token="+getToken(), 
			type: "DELETE", 
			dataType: "JSON", 
			success: function(data){
				layer.closeAll('loading');
				if(data.code==200){
					layer.msg(data.msg,{icon: 1});
					obj.del();
				}else{
					layer.msg(data.msg,{icon: 2});
				}
			}
		});
	});
}

//更改状态
function updateStatus(obj){
	layer.load(1);
	var newStatus = obj.elem.checked?0:1;
	$.post("api/user/status", {
		userId: obj.elem.value,
		status: newStatus,
		_method: "PUT",
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
function doSearch(table){
	var key = $("#searchKey").val();
	var value = $("#searchValue").val();
	if (value=='') {
		key = '';
	}
	layui.table.reload('table', {where: {searchKey: key,searchValue: value}});
}

//删除
function doReSet(userId){
	layer.confirm('确定要重置密码吗？', function(index){
		layer.close(index);
		layer.load(1);
		$.post("api/user/psw/"+userId, {
			token: getToken(), 
			_method: "PUT"
		}, function(data){
			layer.closeAll('loading');
			if(data.code==200){
				layer.msg(data.msg,{icon: 1});
			}else{
				layer.msg(data.msg,{icon: 2});
			}
		},"JSON");
	});
}