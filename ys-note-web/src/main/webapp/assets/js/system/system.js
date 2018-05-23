$(function() {
	//渲染表格
	layui.table.render({
		elem : '#table',
		url : '../api/system/list',
		method: 'post',
 		where: {
	  		token : getToken()
		},
		page: true,
		cols: [[
			{type:'numbers'},
//			{field:'id', sort: true,width: 165,title: 'id'},
			{field:'category', sort: true,width: 165,title: '类别'},
			{field:'name', sort: true, width: 165,title: '名称'},
			{field:'value', sort: true, width: 165,title: '值'},
			{field:'createTime', sort: true, title: '创建时间',templet:function(d){ return layui.util.toDateString(d.createTime); },width: 100},
			{field:'description', sort: true, width: 165,title: '描述'},
			{field:'state', sort: true, width: 165,title: '状态'},
			{field:'basepath', sort: true, width: 165,title: '根路径'},
			{field:'title', sort: true, width: 165,title: '系统标题'},
			{field:'orders', sort: true, width: 165,title: '排序'},
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
	
	 //自定义验证规则
	//表单提交事件
	layui.form.on('submit(btnSubmit)', function(data) {
		data.field.token = getToken();
		layer.load(1);
		layui.use(['ysAjax'], function(ysAjax){
			ysAjax.postJson('system/save',data.field, function(code,resultData,message){
				if(code==1){
					layer.msg(message,{icon: 1});
					layer.closeAll('page');
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
			showEditModel(data);
		} else if(layEvent === 'del'){ //删除
			doDelete(obj);
		}
	});
	
	//搜索按钮点击事件
	$("#main-content").on("click","#searchBtn",function(){
		doSearch(table);
	});
	
	layui.form.render('select');
});

//显示表单弹窗
function showEditModel(data){
	layer.open({
		type: 1,
		title: data==null?"添加数据":"修改数据",
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
		$("#editForm input[name=category][value='"+data.category+"']").prop("checked",true).siblings().prop("checked",false);
		$("#editForm input[name=name]").val(data.name);
		$("#editForm input[name=value]").val(data.value);
		$("#editForm input[name=description]").val(data.description);
		$("#editForm input[name=state][value='"+data.state+"']").prop("checked",true).siblings().prop("checked",false);
		$("#editForm input[name=basepath]").val(data.basepath);
		$("#editForm input[name=title]").val(data.title);
		$("#editForm input[name=orders]").val(data.orders);
		layui.form.render('radio');
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
			ysAjax.postJson('system/delete/'+obj.data.id, {token:getToken()}, function(code,resultData,message){
				layer.closeAll('loading');
				if(code==1){
					layer.msg(data.message,{icon: 1});
					obj.del();
				}else{
					layer.msg(data.message,{icon: 2});
				}
			})
		})
	});
}

//搜索
function doSearch(table){
	var searchParas={};
	searchParas.category=$("#category").val();
	searchParas.name=$("#name").val();
	searchParas.value=$("#value").val();
	searchParas.state=$("#state").val();
	layui.table.reload('table', {where: searchParas});
}