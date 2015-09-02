'use strict';
app.controller('carDetailsController', ['$scope', '$state','$http','hintService','sessionStorageService',
  function($scope, $state,$http,hintService,sessionStorageService) {
	
	$("form[name=formDetails] input").attr("disabled","disabled");
	
    $scope.formData = {};
    
    $scope.needCacheArray = ["carListDataTableProperties","carIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("carIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("carIdForDetails");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	var id = $scope.rowIds[0];
    
    initData(id);
    function initData(id){
    	$http({
    		url:"base/carAction!detailsCar.action",
    		method:"post",
    		data:{
    			id:id
    		}
    	}).then(function(resp){
    		if(resp.data.code===1){
    			$scope.formData = resp.data.details ;
    			$scope.formData.updateTime = undefined ;
    		}else{
    			alert(resp.data.message);
    			$state.go($scope.state.list);
    		}
    	});
    }
    
    $scope.submit = function(){
    	$http({
    		url:"base/carAction!addOrUpdateCar.action",
    		method:"post",
    		data:$scope.formData 
    	}).then(function(resp){
    		if(resp.data.code === 1){
    			hintService.hint({title: "成功", content: "保存成功！" });
    			$state.go($scope.state.list);
    		}else{
    			alert(resp.data.message);
    		}
    		$scope.isDoing = false ;
    	});
    }
    
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
  
}]);