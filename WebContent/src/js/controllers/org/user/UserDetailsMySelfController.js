app.controller("userDetailsMySelfController",['$scope','$state','$http','$modal','loginedUserService','hintService',function($scope,$state,$http,$modal,loginedUserService,hintService){
	if(loginedUserService.getUser()){
		var id = loginedUserService.getUser().id;
	}else{
		$state.go("access.signin");
	}
	$http({
		url:"basedata/userAction!getDataById.action",
		method:"post",
		data:{
			id:id
		}
	}).then(function(resp){
		$scope.details = resp.data.editData ;
	});

	/**
	 * 修改密码
	 */
	$scope.updatePassWord = function(){
		updatePassWordModel();
	}
	
	function updatePassWordModel(){
		var modalInstance = $modal.open({
   	     templateUrl: 'src/tpl/org/user/user_updatepassword_model.html',
   	     size: 'lg',
   	     backdrop:true,
   	     controller:"userUpdatePassWordController"
   	   });
		/**
		 * 弹窗关闭事件
		 */
		modalInstance.result.then(function () {
			hintService.hint({title: "成功", content: "保存成功！" });
    	});
	}
	
	$scope.cancel = function(){
		$state.go("app.home");
	}
	
}]);