var refreshNav = true;
$(function() {
	getToken();
	initUserInfo();  //获取用户信息
	initNav();  //获取导航栏
	//路由注册
	Q.reg('home',function(){
		load('../home');
	}).reg('system',function(path){
		load('system/'+path);
	}).reg('activity',function(path){
		load('activity/'+path);
	}).init({
		index: '../home'
	});
	
	//点击导航切换页面时不刷新导航,其他方式切换页面要刷新导航
	layui.element.on('nav(index-nav)', function(elem){
		refreshNav = false;
		if(document.body.clientWidth<=750){
			switchNav(true);
		}
	});
	//修改密码表单提交事件
	layui.form.on('submit(pswSubmit)', function(data){
		data.field.token = getToken();
		data.field._method = $("#pswForm").attr("method");
		layer.load(1);
		$.post("api/user/psw", data.field, function(data){
			if(data.code==200){
				layer.msg(data.msg,{icon: 1});
				setTimeout(function() {
					loginOut();
				}, 1500);
			}else{
				layer.closeAll('loading');
				layer.msg(data.msg,{icon: 2});
			}
		}, "JSON");
		return false;
	});
	layui.form.verify({
		  psw2: function(value, item){
			var newPsw1 = $("#pswForm input[name=newPsw]").val();
		    if(value!=newPsw1){
		      return '两次输入密码不一致';
		    }
		  },
		  pass: [
		    /^[\S]{6,12}$/
		    ,'密码必须6到12位，且不能出现空格'
		  ]
	});
});

//异步加载子页面
function load(path) {
	if(refreshNav){
		activeNav(path);
	}
	refreshNav = true;
	$("#main-content").off("click").load(path +".html",function(){
		layui.element.render('breadcrumb');
		layui.form.render('select');
	});
}

//获取左侧导航栏
function initNav(){
	var indexNavStr = sessionStorage.getItem("index-nav");
	var indexNav = JSON.parse(indexNavStr);
	if(indexNav==null){
		$.get("../api/menu", {
			token : getToken()
		}, function (data) {
			if(1==data.code){
				sessionStorage.setItem("index-nav",JSON.stringify(data.data.menus));
				initNav();
			}else if(401==data.code){
				layer.msg(data.message,{icon: 2});
				setTimeout(function() {
					loginOut();
				}, 1500);
			}else{
				layer.msg("获取导航失败，请刷新页面",{icon: 2});
			}
		},"json");
	}else{
		layui.laytpl(sideNav.innerHTML).render(indexNav, function(html){
			$("#index-nav").html(html);
			layui.element.render('nav', 'index-nav');
		});
	}
}

//获取用户信息
function initUserInfo(){
	try {
		var user = getCurrentUser();
		//$("#userHead").attr("src", user.);
		$("#userNickName").text(user.username);
	} catch (e) {
		console.log(e.message);
	}
}

//退出登录
function loginOut(){
	layer.load(1);
	$.ajax({
		url: "../api/loginOut?token="+getToken(), 
		dataType: "JSON", 
		success: function(data){
			localStorage.removeItem("token");
			localStorage.removeItem("user");
			sessionStorage.removeItem("index-nav");
			location.replace("login.html");
		}
	});
}

//个人信息
function myInfo(){
	var user = getCurrentUser();
	var content = '<ul class="site-dir" style="padding:25px 35px 8px 35px;"><li>账号：'+user.userAccount+'</li><li>用户名：'+user.userNickname+'</li>';
	content += '<li>手机号：'+user.mobilePhone+'</li><li>性别：'+user.sex+'</li><li>角色：'+user.roleName+'</li></ul>';
	layer.open({
		type: 1,
		title: '个人信息',
		area: '350px',
		offset: '120px',
		content: content,
		btn: ['关闭'],
		btnAlign: 'c'
	});
}

//显示表单弹窗
function updatePsw(){
	layer.open({
		type: 1,
		title: "修改密码",
		area: '400px',
		offset: '120px',
		content: $("#pswModel").html()
	});
	$("#pswForm")[0].reset();
	$("#pswCancel").click(function(){
		layer.closeAll('page');
	});
}