/**
 * 获取用户按钮级权限的service
 * @auth zw
 */
app.factory("roleBtnService",['$q','$http',function($q,$http){
		return {
			getRoleBtnService:getRoleBtnService
		};
		function getRoleBtnService(uiClass,scope){
//			var deferred = $q.defer();
			$http({
				url:"sys/permItemAction!listRoleBtn.action",
				method:"post",
				data:{
					uiClass:uiClass
				}
			}).then(function(resp){
				if(resp.data.code===1 && resp.data.data ){
					scope.btns = resp.data.data ;
					console.info("用户在"+uiClass+"下拥有的按钮权限有：",scope.btns);
				}
//				deferred.resolve(resp);
			});
//			return deferred.promise;
		}
		//结束service
}]);