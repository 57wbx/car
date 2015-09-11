app.controller("userUpdatePassWordController",['$scope','$modalInstance','$http','loginedUserService',function($scope,$modalInstance,$http,loginedUserService){
	
	$scope.message = {
			newPassWord:{
				pattern:"密码只能为数字或者字母",
				custom_pattern:"新密码不一致"
			}
	}
	
	$scope.change = function(){
		if($scope.formData.newSecondPassWord == $scope.formData.newPassWord){
			$scope.form.newSecondPassWord.$setValidity("custom_pattern",true);
			$scope.form.newPassWord.$setValidity("custom_pattern",true);
		}else{
			$scope.form.newSecondPassWord.$setValidity("custom_pattern",false);
			$scope.form.newPassWord.$setValidity("custom_pattern",false);
		}
	}
	
	if(loginedUserService.getUser()){
		var id = loginedUserService.getUser().id;
	}
	
	$scope.cancel = function() {
	    $modalInstance.dismiss('cancel');
	};
	
	$scope.ok = function() {
		submit().then(function(resp){
			if(resp.data.code==1){
				 $modalInstance.close();
			}else{
				alert(resp.data.message);
			}
		}) 
	 };
	 
	 function submit(){
		 if(id){
			 return $http({
				 url:"basedata/userAction!updateUserPassWord.action",
				 method:"post",
				 data:{
					 password:$scope.formData.newSecondPassWord,
					 oldPassWord:$scope.formData.oldPassWord
				 } 
			 });
		 }
	 }
	 
}]);