app.factory("loginInterceptor",['$state',function($state){
	var interceptor = {
			'request':function(config){
				console.info(config);
				return config ;
			},
			'responseError':function(config){
				console.info("没有登陆或者没有权限",config);
				if(config.data.code==2){
					//没有登陆
					$state.go("access.signin");
				}else{
					$state.go("app.home");
				}
				return config;
			}
	};
	return interceptor ;
}]);