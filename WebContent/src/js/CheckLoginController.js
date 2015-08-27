app.controller("checkLoginController",['$scope','$state','loginedUserService',function($scope,$state,loginedUserService){
	if(!loginedUserService.isLogined()){
		//没有登陆
		$state.go("access.signin");
	}
}]);