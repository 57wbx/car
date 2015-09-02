'use strict';
app.controller('hotWordDetailsController', ['$scope', '$state','$http','hintService','sessionStorageService',
  function($scope, $state,$http,hintService,sessionStorageService) {
	
    $scope.formData = {};
    
    $("form[name=form] input").attr("disabled","disabled");
    $("form[name=form] select").attr("disabled","disabled");
    
    $scope.needCacheArray = ["hotWordListDataTableProperties","hotWordIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("hotWordIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("hotWordIdForDetails");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	var id = $scope.rowIds[0];
    
    initData(id);
    function initData(id){
    	$http({
    		url:"opr/hotWordAction!detailsHotWord.action",
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
    
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
  
}]);