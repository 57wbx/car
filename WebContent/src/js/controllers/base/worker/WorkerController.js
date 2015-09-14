'use strict';

app.controller('workerController',['$rootScope','$scope','$state','$timeout','$http','sessionStorageService','roleBtnService',function($rootScope,$scope,$state,$timeout,$http,sessionStorageService,roleBtnService){
	
	var roleBtnUiClass = "app.worker.";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);
	
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["memberDataTableProperties","memberIdForEdit","memberIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);
	
	$scope.rowIds = [];//用来保存所选列表的id
	
	/**
	 * 模块路由路径统一管理
	 */
	$scope.state = {
			list:"app.worker.list",
			add:"app.worker.add",
			edit:"app.worker.edit",
			details:"app.worker.details"
	}
	
	
	// 设置按钮的状态值
	  $scope.setBtnStatus = function(){
		  console.info("设置按钮",$scope.rowIds);
	    if($scope.rowIds.length === 0){
	      $scope.single = true;
	      $scope.locked = true;
	      $scope.mutiple = true;
	    }else if($scope.rowIds.length === 1){
	      $scope.single = false;
	      $scope.locked = false;
	      $scope.mutiple = false;
	    }else{
	      $scope.single = true;
	      $scope.locked = true;
	      $scope.mutiple = false;
	    }

	    $scope.$evalAsync();
//	    if(){
//	    	hButton.trigger('click'); // 触发一次点击事件，使所以按钮的状态值生效
//	    }
	  };
	
	/**
	 * 清空需要操作的id，主要是在busAtomListController中调用
	 */
	$scope.clearRowIds = function(){
		$scope.rowIds = [];
		$scope.setBtnStatus();
	}
	
	/**
	 * 新增按钮的方法
	 */
	$scope.addRow = function(){
		$state.go($scope.state.add);
	}
	
	/**
	 * 管理图片的方法
	 */
	$scope.manageImg = function(){
		if($scope.editId){
			$scope.clearRowIds();
			$scope.rowIds.push($scope.editId);
		}
		$state.go($scope.state.manageimg);
	}
	
	/**
	 * 查看按钮的方法
	 */
	$scope.seeDetails = function(id){
		if(id!=null&&id!=""){
			$scope.clearRowIds();
			$scope.rowIds.push(id);
		}else if($scope.editId){
			$scope.rowIds.push($scope.editId);
		}
		$state.go($scope.state.details);
	}
	
	/**
	 * 修改方法的按钮
	 */
	$scope.editRow = function(){
		if($scope.editId){
			$scope.clearRowIds();
			$scope.rowIds.push($scope.editId);
		}
		$state.go($scope.state.edit);
	}
	
	
	//结束方法
}]);