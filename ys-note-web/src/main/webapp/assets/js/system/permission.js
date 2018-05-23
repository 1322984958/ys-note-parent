$(function() {
	layui.use(['ysAjax','ysTreegrid','grid','treegrid'], function(ysAjax,ysTreegrid,grid,treegrid){
		ysAjax.postJson('permission/list',{}, function(code,resultData,message){
			if(code==1){
				ysTreegrid.createEasyTreegrid('tree-grid','treegridView',{ rows: resultData },'parentId',false);
			}else{
				layer.msg(message);
			}
		})
		
    	layui.form.on('submit(btnSubmit)', function(data) {
	        data.field.token = getToken();
	        data.field._method = $("#editForm").attr("method");
	        layer.load(1);
	        ysAjax.postJson('permission/save',data.field, function(code,resultData,message){
	        	if(code==1){
	        		layer.msg(message,{icon: 1});
	                layer.closeAll('page');
	                layui.table.reload('table', {});
	        	}else{
	                layer.msg(message,{icon: 2});
	            }
	        });
	        return false;
    	 })
	});
	
    //渲染表格
    /*layui.table.render({
        elem : '#table',
        url : '../api/permission',
        where: {
            token : getToken()
        },
        page: true,
        cols: [[
            {type:'numbers'},
            {field:'id', sort: true, title: 'ID'},
            {field:'name', sort: true, title: '权限名'},
            {field:'value', sort: true, title: '权限值'},
            {field:'category', sort: true, templet:function(d){ return d.permissionType==0?'菜单':'按钮'; }, title: '类型'},
            {field:'parentName', sort: true, title: '父级'},
            {field:'uri', sort: true, title: '路径'},
            {field:'orderNumber', sort: true,title: '排序号'},
            {field:'createTime', sort: true, templet:function(d){ return layui.util.toDateString(d.createTime); }, title: '创建时间'},
            {align:'center', toolbar: '#barTpl', minWidth: 110, title: '操作'}
        ]]
    });*/
     
    //添加按钮点击事件
    $("#addBtn").click(function(){
        showEditModel(null);
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
     
    //监听状态开关操作
    layui.form.on('switch(statusCB)', function(obj){
        updateStatus(obj);
    });
     
    //搜索按钮点击事件
    $("#searchBtn").click(function(){
        doSearch(table);
    });
});
 
//显示表单弹窗
function showEditModel(data){
    layer.open({
        type: 1,
        title: data==null?"添加权限":"修改权限",
        area: '450px',
        offset: '120px',
        content: $("#addModel").html()
    });
    $("#editForm")[0].reset();
    $("#editForm").attr("method","POST");
    var selectItem = "";
    var type = 0;
    if(data!=null){
        $("#editForm input[name=id]").val(data.id);
        $("#editForm input[name=name]").val(data.name);
        $("#editForm input[name=value]").val(data.value);
        $("#editForm input[name=orders]").val(data.orders);
        $("#editForm").attr("method","PUT");
        selectItem = data.parentId;
        type = data.permissionType;
        if(0==data.permissionType){
            $("#type0").attr("checked","checked");
            $("#type1").removeAttr("checked");
        }else{
            $("#type0").removeAttr("checked");
            $("#type1").attr("checked","checked");
        }
        layui.form.render('radio');
    }
    $("#btnCancel").click(function(){
        layer.closeAll('page');
    });
     
    getParents(selectItem,"1");
}
 
//获取所有父级菜单
function getParents(selectItem,category){
    layer.load(1);
    $.post("../api/permission/parent/"+category,{
        token: getToken()
    }, function(data){
        layui.laytpl(parentsSelect.innerHTML).render(data.data, function(html){
            $("#parent-select").html(html);
            $("#parent-select").val(selectItem);
            layui.form.render('select');
            layer.closeAll('loading');
        });
    },"json");
}
 
//删除
function doDelete(obj){
    layer.confirm('确定要删除吗？', function(index){
        layer.close(index);
        layer.load(1);
        $.ajax({
            url: "api/permission/"+obj.data.permissionId+"?token="+getToken(), 
            type: "DELETE", 
            dataType: "JSON", 
            success: function(data){
                layer.closeAll('loading');
                if(data.code==200){
                    layer.msg(data.msg,{icon: 1});
                    obj.del();
                    parents1 = null;
                    parents2 = null;
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
    $.post("api/permission/status", {
        permissionId: obj.elem.value,
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