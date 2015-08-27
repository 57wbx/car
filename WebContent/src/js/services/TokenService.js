/**
 * 该服务主要是提供token使用 ，确保表单提交的正确性，保证不重复提交表单
 * @author zw
 */
app.factory("tokenService",['$http',function($http){
	/**
	 * 为保存的token  默认为不检查check
	 */
	var token = null;
	 /**
	  * 清空token
	  */
	var clearToken = function(){
		token = null;
	}
	/**
	 * 获取当前token，不从服务端获取
	 */
	var getToken = function(){
		return token;
	}
	/**
	 * 获取最新的token，需要从服务端获取token
	 */
	var getNewToken = function(){
		flushToken();
	}
	/**
	 * 
	 */
	
	/**
	 * 从服务端flush最新的token
	 *  服务内部使用不对外开放
	 */
	function flushToken(){
		$http({
			url:"token/tokenAction!flushToken.action",
			method:"get"
		}).then(function(resp){
			if(resp.data.code==1){
				token = resp.data.token ;
			}else{
				token = null;
			}
		});
	}
	
	return {
		getNewToken:getNewToken,
		getToken:getToken
	}
	
}]);