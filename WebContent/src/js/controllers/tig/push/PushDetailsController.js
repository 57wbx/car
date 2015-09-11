'use strict';
app.controller('pushDetailsController', ['$rootScope','$scope', '$http', '$state','sessionStorageService','hintService',
  function($rootScope,$scope, $http, $state,sessionStorageService,hintService) {
	
	$scope.needCacheArray = ["pushListDataTableProperties","pushIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("pushIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("pushIdForDetails");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	var id = $scope.rowIds[0];
	
	
	$("form[name=form] input").attr("disabled",true);
	$("form[name=form] select").attr("disabled",true);
	$("form[name=form] textarea").attr("disabled",true);
	
    // 获取要被编辑组织的数据
    $http({
      url: "tig/pushMessageAction!detailsPushMessage.action",
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
    	if(dt.data.code){
    		$scope.formData = dt.data.details ;
    	}else{
    		alert(dt.data.message);
    		$state.go($scope.state.list);
    	}
    });
    
    $scope.cancel = function(){
    	$state.go($scope.state.list);
    }
    

    $scope.clearRowIds();
    
   }
]);