/**
 * 用来保存登陆的过的用户，当页面刷新的时候，该对象会在用户按f5时刷新，登陆的时候需要将用户信息保存在cookie或者sessionLocal中。
 * 当页面刷新之后，该对象先从cookie中或其他地方获取用户对象，若果没有对象就为空
 * 
 */
app.factory("loginedUserService",['$location','$cookieStore',function($location,$cookieStore){
//	    	//用户还没有登陆，但是去业务数据
//	    	var cookie = $cookieStore.get('username');
//	    	if(!cookie){
//	    		$location.path("/access/signin");
//	        	return;
//	        }
//	    }
	var user = null;
	
	/**
	 * 获取用户信息
	 */
	var getUser = function(){
		return getUserFromCookie();
	};
	/**
	 * 设置用户，同时保存到cookie中
	 */
	var setUser = function(user1){
		user = user1;
		$cookieStore.put('user',JSON.stringify(user1));
	};
	/**
	 * 判断是否登陆
	 */
	var isLogined = function(){
		if(getUserFromCookie()){
			console.info("账户已登录");
			return true;
		}else{
			console.info("账户未登陆");
			return false;
		}
	}
	/**
	 * 退出登陆
	 */
	var logout = function(){
		user = null;
		$cookieStore.remove("user");
	}
	
	var getUserFromCookie = function(){
		if(user){
			return user;
		}else{
			var cookie = $cookieStore.get('user');
			if(cookie){
				user = JSON.parse(cookie);
				return user;
			}
			return null ;
		}
	};
	
	/**
	 * 返回该用户对象
	 */
	return  {
		getUser : getUser,
		setUser : setUser,
		isLogined : isLogined,
		logout:logout
	};
	
}]);