$(function() {
	doGetOrganization();
	layui.form.render('radio');
	getFormToken();
	layui.use(['ysAjax'], function(ysAjax){
		layui.form.on('submit(btnSubmit)', function(data) {
		    data.field.token = getToken();
		    data.field.tokenForm = $.cookie('tokenForm');
		    var treeObj = $.fn.zTree.getZTreeObj("organization");
		    data.field.parentId = treeObj.getSelectedNodes()[0].pId;
		    layer.load(1);
		    ysAjax.postJson('organization/save', data.field, function(code,resultData,message){
		    	if(code==1){
		    		$.extend(treeObj.getSelectedNodes()[0], resultData);
		    		treeObj.updateNode(treeObj.getSelectedNodes()[0]);
		    		layer.msg(message,{icon: 1});
		            layer.closeAll();
		            getFormToken();
		    	}else{
		            layer.msg(message,{icon: 2});
		        }
		    });
		    return false;
		})
	})
});

var setting = {
	view : {
		addHoverDom : addHoverDom,
		removeHoverDom : removeHoverDom,
		selectedMulti : false
	},
	check : {
		enable : false
	},
	data : {
		simpleData : {enable : true, pidKey: "parentId"},
		key : { 
	    	checked : "checked",
	    	children : "childrens"
	    }
	},
	edit : {
		enable : true,
		showRenameBtn: false,
		renameTitle: '重命名',
		removeTitle: '删除'
	},
	callback: {
		onClick: doClick,
		onNodeCreated: doNodeCreated,
		beforeRemove: doBeforeRemove
	}
};

function doGetOrganization(){
	layui.use(['ysAjax'], function(ysAjax){
		ysAjax.postJson('organization/tree',{}, function(code,resultData,message){
			if(code==1){
				$.fn.zTree.init($("#organization"), setting, resultData);  
				layer.closeAll('loading');
			}else{
				layer.msg(message);
			}
		});
	})
}

function doClick(event, treeId, treeNode, clickFlag) {
	$("#editForm input[name='id']").val(treeNode.id);
	$("#editForm input[name='name']").val(treeNode.name);
	if(treeNode.otherInfo){
		$("#editForm input[name='value']").val(treeNode.otherInfo.value);
		$("#editForm textarea[name='description']").val(treeNode.otherInfo.description);
		$("#editForm input[name='state'][value='"+treeNode.otherInfo.state+"']").prop("checked",true).siblings().prop("checked",false);
		$("#editForm input[name='orders']").val(treeNode.otherInfo.orders);
	}else{
		$("#editForm input[name='value']").val('');
		$("#editForm textarea[name='description']").val('');
		$("#editForm input[name='state'][value='1']").prop("checked",true).siblings().prop("checked",false);
		$("#editForm input[name='orders']").val(1);
	}
	layui.form.render('radio');
	console.debug(treeNode);
}

var newCount = 1;
function addHoverDom(treeId, treeNode) {
    var sObj = $("#" + treeNode.tId + "_span");
    if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0){
    	return;
    }
    var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
        + "' title='增加节点' onfocus='this.blur();'></span>";
    sObj.after(addStr);
    var btn = $("#addBtn_"+treeNode.tId);
    if (btn) {
    	btn.bind("click", function(){
    		$("#editForm input[name='id']").val('');
        	$("#editForm input[name='name']").val('');
        	$("#editForm input[name='value']").val('');
        	$("#editForm textarea[name='description']").val('');
        	$("#editForm input[name='state'][value='1']").prop("checked",true).siblings().prop("checked",false);
        	$("#editForm input[name='orders']").val('1');
        	layui.form.render('radio');
    		var zTree = $.fn.zTree.getZTreeObj("organization");
    		zTree.addNodes(treeNode, {id: undefined, pId:treeNode.id, name:"请输入名称"}); // 'newNode' + newCount++
    		return false;
    	});
    }
};
function removeHoverDom(treeId, treeNode) {
	$("#addBtn_"+treeNode.tId).unbind().remove();
}

function doBeforeRemove(treeId, treeNode){
	layer.confirm('确定要删除吗？', function(index){
		layer.close(index);
		layer.load(1);
	    layui.use(['ysAjax'], function(ysAjax){
			ysAjax.deleteJson('organization/delete/'+treeNode.id,{}, function(code,resultData,message){
				if(code==1){
					var treeObj = $.fn.zTree.getZTreeObj("organization");
					treeObj.removeNode(treeObj.getSelectedNodes()[0],false);
				}else{
					layer.msg(message);
				}
				layer.closeAll('loading');
			});
		})
	});
	return false;
};

function doNodeCreated(event, treeId, treeNode){
	$('#'+treeNode.tId+' a').click();
}