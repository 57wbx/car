'use strict';
app.controller('hotWordEditController', ['$scope', '$state','$http','hintService','sessionStorageService',
  function($scope, $state,$http,hintService,sessionStorageService) {
	
    $scope.formData = {};
    
    $scope.needCacheArray = ["hotWordListDataTableProperties","hotWordIdForEdit"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("hotWordIdForEdit",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("hotWordIdForEdit");
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
    
    $scope.submit = function(){
    	$http({
    		url:"opr/hotWordAction!updateHotWord.action",
    		method:"post",
    		data:$scope.formData 
    	}).then(function(resp){
    		if(resp.data.code === 1){
    			hintService.hint({title: "成功", content: "修改成功！" });
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