'use strict';
app.controller('autoPartDetailsController', ['$scope','$rootScope', '$state','$http', 'sessionStorageService',
  function($scope, $rootScope,$state,$http,sessionStorageService) {
    
    $scope.treeAPI.hiddenBusTypeTree();
    
    $scope.needCacheArray = ["autoPartListDataTableProperties","autoPartIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds[0]){
		sessionStorageService.setItem("autoPartIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("autoPartIdForDetails");
	}
	
	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
    
    //调用初始化方法
    initData();
    /*
     * 修改页面在加载的时候需要在后台进行数据的读取，然后在前台进行初始化数据
     */
    //初始化数据方法
    function initData(){
    	$http({
    		url:"base/autoPartAction!detailsAutoPartById.action",
    		data:{
    			id : $scope.rowIds[0]
    		},
    		method:"post"
    	}).then(function(resp){
    		var code = resp.data.code 
    		if(code == 1){//代表成功获得数据
    			$scope.formData = resp.data.details ;
    		}else{
    			alert(resp.data.message);
    			$state.go($scope.state.list);
    		}
    	});
    }
    
    
    
    /*
     * 页面方法api 
     */
    
    //提交方法
    $scope.submit = function(){
    	$http({
    		url:"base/autoPartAction!addAutoPart.action",
    		method:"post",
    		data:$scope.formData
    	}).then(function(resp){
    		var code = resp.data.code ;//服务器返回的数据
    		if(code==1){//成功的代码
    			$state.go($scope.state.list);
    		}else{//不成功
    			alert(resp.data.message);
    			return ;
    		}
    	});
    }
    
    //取消方法
    $scope.cancel = function(){
    	$state.go($scope.state.list);
    }
    
    $scope.clearRowIds();
    
}
]);