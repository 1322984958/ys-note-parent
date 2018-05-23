//提示：模块也可以依赖其它模块，如：layui.define('layer', callback);
layui.define(function(exports) {
	var mod = {
		getJson : function(url, params, callback) {
			this.ajax(url, {method:'get'}, params, callback);
		},
		postJson : function(url, params, callback) {
			this.ajax(url, {method:'post'}, params, callback);
		},
		deleteJson : function(url, params, callback) {
			this.ajax(url+"?token="+getToken(), {method: 'DELETE',type: "DELETE"}, params, callback);
		},
		ajax : function(url, config, params, callback) {
			if(config['method']!='DELETE'){
				params.token=getToken();
			}
			var defaultConfig = {
				method : 'post',
				statusCode : {
					404 : function() {
						alert('网络异常');
					}
				},
				success : function(data, textStatus, jqXHR) {
					if (!data.match("^\{(.+:.+,*){1,}\}$")) {
						callback(-1, "系统错误！", "系统错误！");
					} else {
						var jsonData = $.parseJSON(data);
						if (jsonData.code == 1) {
							callback(jsonData.code, jsonData.data,jsonData.message);
						} else {
							callback(jsonData.code, jsonData.data,jsonData.message);
						}
					}
				}
			}
			$.extend(true, defaultConfig, config);
			defaultConfig.url = baseApiUrl + url;
			defaultConfig.data = params;
			try {
				$.ajax(defaultConfig);
			} catch (e) {
				console.debug(e);
			}
		}
	};
	exports('ysAjax', mod);
});