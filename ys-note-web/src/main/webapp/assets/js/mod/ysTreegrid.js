layui.define(['laytpl'],function(exports) {
	var laytpl = layui.laytpl;
	var mod = {
		createEasyTreegrid:function(elem,view,data,parentid,singleSelect){
			var treeGrid = layui.treegrid;
			treeGrid.config.render = function (viewid, data) {
                var view = document.getElementById(viewid).innerHTML;
                return laytpl(view).render(data) || '';
            };
			var myTreeGrid=treeGrid.createNew({
                elem: elem,
                view: view,
                data: data,
                parentid: parentid||'parentId',
                singleSelect: singleSelect==null?true:singleSelect
//                loadRow: function (data) {
//                    setTimeout(function () {
//                        data.children(data.rows);
//                    }, 2000);
//                }
            });
			myTreeGrid.build();
            $('#'+elem).parents('.treegridWrap .layui-btn').on('click', function () {
                switch ($(this).attr('lay-filter')) {
                    case 'expandAll': {
                    	myTreeGrid.expandAll()
                    } break;
                    case 'collapseAll': {
                    	myTreeGrid.collapseAll()
                    } break;
                }
            });
            return myTreeGrid;
		}
	};
	exports('ysTreegrid', mod);
});