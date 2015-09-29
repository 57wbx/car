'use strict';

app.controller('carShopDetailsController', ['$http','$rootScope','$scope','$state','sessionStorageService',function($http, $rootScope, $scope, $state,sessionStorageService) {
	
	$scope.API.isListPage = false ;
	
	$scope.needCacheArray = ["carShopIdForDetails","carShopListDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds[0]){
		sessionStorageService.setItem("carShopIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("carShopIdForDetails");
	}
	
	
	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
    $http({
      url: "base/carShopAction!detailsCarShopById.action",
      data: {
        id: $scope.rowIds[0]
      },
      method: 'POST'
    }).then(function(dt) {
      $scope.details = dt.data.details;
    });
    
    $scope.clearRowIds();
}]);