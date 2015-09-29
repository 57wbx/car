'use strict';

app.controller('OrgDetails', ['$http','$scope','$state','$timeout','sessionStorageService',
                              function($http, $scope, $state,$timeout,sessionStorageService) {
	disabledForm();
	
	$scope.needCacheArray = ["orgListDataTableProperties","orgIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("orgIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("orgIdForDetails");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	var id = $scope.rowIds[0];
	
	$http({
		url:"basedata/orgZAction!detailsOrg.action",
		method:"POST",
		data:{
			id:$scope.rowIds[0]
		}
	}).then(function(resp){
		if(resp.data.code == 1){
			$scope.formData = resp.data.details ;
			$timeout(function(){
    			$scope.treeController.select_branch_byId($scope.formData.pId);
    		},200);
		}else{
			$state.go($scope.state.list);
		}
	});
	
	function disabledForm(){
		$("form[name=form] input").attr("disabled",true);
		$("form[name=form] select").attr("disabled",true);
	}

    $scope.cancel = function(){
    	$state.go($scope.state.list);
	};
}]);