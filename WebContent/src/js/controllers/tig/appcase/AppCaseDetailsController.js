'use strict';
app.controller('appCaseDetailsController', ['$rootScope','$scope', '$http', '$state',  'uiLoad', 'JQ_CONFIG','FileUploader','previewService','sessionStorageService','hintService',
  function($rootScope,$scope, $http, $state, uiLoad, JQ_CONFIG,FileUploader,previewService,sessionStorageService,hintService) {
	
	$scope.needCacheArray = ["appCaseDataTableProperties","appCaseIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("appCaseIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("appCaseIdForDetails");
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
      url: "tig/appCaseAction!detailsAppCase.action",
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