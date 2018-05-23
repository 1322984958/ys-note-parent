//var getTokenInterval=setInterval("getToken",60000);

var sideNavExpand = true; // 导航栏是否展开
var baseApiUrl = "/wc-management-web/api/"
if(location.href.indexOf('wc-management-web/views/index.html')==-1){
	location.replace('/wc-management-web/views/index.html');
}
$(function() {
	layui.config({
		base : '../assets/js/mod/' // 假设这是你存放拓展模块的根目录
	}).extend({ // 设定模块别名
		ysAjax : 'ysAjax',
		grid : '../../libs/layui/extend/grid',
		treegrid:'../../libs/layui/extend/treeGrid'
	});
	var bo = true;
	if (localStorage.getItem("token") != null) {
		bo = parseInt(localStorage.getItem("token").split(";")[1]) < new Date()
				.getTime();
	}
	if (getCurrentUser() == null || bo) {
		location.replace("login.html");
	}
	// 切换导航栏按钮点击事件
	$("#switchNav").click(function() {
		var sideNavExpand = !$('body').hasClass('nav-mini');
		switchNav(!sideNavExpand);
	});
	// 手机遮罩层点击事件
	$('.site-mobile-shade').click(function() {
		switchNav(true);
	});
});

// 获取当前token
function getToken() {
	try {
		$.ajax({
			url : '../api/getToken',
			data : {
				token : localStorage.getItem("token")
			},
//			statusCode : {
//				404 : function() {
//					alert('网络异常');
//				}
//			},
			success : function(data, textStatus, jqXHR) {
				if (!data.match("^\{(.+:.+,*){1,}\}$")) {
					location.replace("login.html");
				} else {
					var jsonData = $.parseJSON(data);
					if (jsonData.code == 1) {
						localStorage.setItem("token", jsonData.data.token);
					}
				}
			},
			error : function(data, textStatus, jqXHR) {
				console.info('系统错误');
			}
		});
	} catch (e) {
		console.debug(e);
	}
	return localStorage.getItem("token");
}

//获取当前formToken
function getFormToken() {
	try {
		$.ajax({
			url : '../api/getFormToken',
			data : {
				token : localStorage.getItem("token")
			},
			statusCode : {
				404 : function() {
					alert('网络异常');
				}
			},
			dataType: 'json',
			success : function(data) {
				if(data.code=='1'){
					$.cookie('tokenForm',data.data);
				}
			},
			error : function(data, textStatus, jqXHR) {
				console.info('系统错误');
			}
		});
	} catch (e) {
		console.debug(e);
	}
	return $.cookie('tokenForm');
}

// 获取当前登录的user
function getCurrentUser() {
	return JSON.parse(localStorage.getItem("user"));
}

// 设置选中导航栏
function activeNav(path_name) {
	$(".layui-side ul.layui-nav li.layui-nav-item .layui-nav-child dd")
			.removeClass("layui-this");
	$(".layui-side ul.layui-nav li.layui-nav-item").removeClass(
			"layui-nav-itemed");
	var $a = $(".layui-side ul.layui-nav>li.layui-nav-item>.layui-nav-child>dd>a[href='#!"
			+ path_name + "']");
	$a.parent("dd").addClass("layui-this");
	$a.parent("dd").parent("dl.layui-nav-child").parent("li.layui-nav-item")
			.addClass("layui-nav-itemed");
	layui.element.render('nav', 'index-nav');
}

// 折叠显示导航栏
function switchNav(expand) {
	var sideNavExpand = !$('body').hasClass('nav-mini');
	if (expand == sideNavExpand) {
		return;
	}
	if (!expand) {
		// $('.layui-side .layui-nav
		// .layui-nav-item.layui-nav-itemed').removeClass('layui-nav-itemed');
		$('body').addClass('nav-mini');
	} else {
		$('body').removeClass('nav-mini');
	}
	$('.nav-mini .layui-side .layui-nav .layui-nav-item').hover(function() {
		var tipText = $(this).find('span').text();
		if ($('body').hasClass('nav-mini') && document.body.clientWidth > 750) {
			layer.tips(tipText, this);
		}
	}, function() {
		layer.closeAll('tips');
	});
}

// 导航栏展开
function openNavItem() {
	if ($('body').hasClass('nav-mini') && document.body.clientWidth > 750) {
		switchNav(true);
	}
}