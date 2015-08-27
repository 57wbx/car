'use strict';

app.controller('menuDetailsController', ['$http','$rootScope','$scope','$state','sessionStorageService','uiClassService',function($http, $rootScope, $scope, $state,sessionStorageService,uiClassService) {
	
	$scope.needCacheArray = ["carShopIdForDetails","carShopListDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds[0]){
		sessionStorageService.setItem("carShopIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("carShopIdForDetails");
	}
	
	$scope.imgArray = uiClassService.getImgArray();
	$scope.colorArray = uiClassService.getColorArray();
//	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
//		$state.go($scope.state.list);//返回到列表界面
//	}
	$scope.treeAPI.clickTreeListReload = function(id){
		console.info("details中的id",id);
		$scope.formData = {} ;
		$http({
			url:'sys/menuAction!detailsMenu.action',
			method:"post",
			data:{
				id:id
			}
		}).then(function(resp){
			if(resp.data.code){
				$scope.formData = resp.data.details ;
			}else{
				alert(resp.data.message);
			}
		});
	};
    
    $scope.clearRowIds();
}]);