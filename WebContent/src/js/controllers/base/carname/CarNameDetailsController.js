'use strict';
app.controller('carNameDetailsController', ['$scope', '$state','$modal', '$http','sessionStorageService','FileUploader','previewService','hintService',
  function($scope, $state,$modal, $http, sessionStorageService,FileUploader,previewService,hintService) {
	
    $scope.formData = {};
    
	$scope.needCacheArray = ["carNameListDataTableProperties","carNameIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds[0]){
		sessionStorageService.setItem("carNameIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("carNameIdForDetails");
	}
	
	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
    
	   
	   /**
	    * 初始化数据
	    */
	   initData();
	   function initData(){
		   $http({
			   url:"base/carNameAction!detailsCarName.action",
			   method:"post",
			   data:{
				   id:$scope.rowIds[0]
			   }
		   }).then(function(resp){
			   if(resp.data.code === 1){
				   $scope.formData = resp.data.details ;
				   $scope.formData.updateTime = undefined ;
			   }else{
				   alert(resp.data.message);
			   }
		   });
		   
		   $("form[name=form] input").attr("disabled","disabled");
	   }
    
}]);