'use strict';
app.controller('autoPartEditController', ['$scope','$rootScope', '$state','$http','checkUniqueService','sessionStorageService',
  function($scope, $rootScope,$state,$http ,checkUniqueService,sessionStorageService) {
    
    $scope.treeAPI.hiddenBusTypeTree()
    
	$scope.needCacheArray = ["autoPartListDataTableProperties","autoPartIdForEdit"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds[0]){
		sessionStorageService.setItem("autoPartIdForEdit",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("autoPartIdForEdit");
	}
	
	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
    
    /**
	 * 检测所需要保存的partCode是否唯一
	 */
	$scope.partCodeIsUnique = true;
	$scope.notUniqueMessage = "已存在";
	$scope.checkPartCodeIsUnique = function(id,partCode){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkAutoPartCodeUnique(id,partCode).then(function(resp){
			if(resp.data.code==1){
				$scope.partCodeIsUnique = true;
			}else{
				$scope.partCodeIsUnique = false;
			}
		});
	}
	$("#partCode").change(function(e){
		if($scope.form.partCode.$invalid){
			$("#partCode").css("border-color","red");
		}else{
			$("#partCode").removeAttr("style");
		}
		if($scope.form.partCode.$valid){
			$scope.checkPartCodeIsUnique($scope.formData.id,($scope.formData.busTypeCode || '')+$(e.target).val());
		}
	});
    
    
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
    
//结束controller
}]);