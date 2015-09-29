'use strict';

app.controller('OrgController', ['$rootScope','$scope','$state','$timeout','roleBtnService','modelDataCacheService','utilService','sessionStorageService',function($rootScope, $scope, $state, $timeout,roleBtnService,modelDataCacheService,utilService,sessionStorageService) {
	
	var roleBtnUiClass = "app.org.";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);
	
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["orgListDataTableProperties","orgIdForEdit","orgIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);
	
	$scope.rowIds = [];
	  
	/**
	 * 清空需要操作的id，主要是在busAtomListController中调用
	 */
	$scope.clearRowIds = function(){
		$scope.rowIds = [];
		$scope.setBtnStatus();
	}
	
	/**
	 * 路由地址
	 */
	$scope.state = {
			list:"app.org.list",
			add:"app.org.add",
			edit:"app.org.edit",
			details:"app.org.details"
	}
	
	/**
	 * 树的相关API
	 */
	$scope.treeAPI = {
			reloadListTable:undefined
	}
	$scope.treeData = {
			orgName:undefined,
			orgId:undefined
	}
	
	/**
	 * 组织树的形成
	 */
	$scope.treeData = [];
	$scope.treeController = {};
	modelDataCacheService.orgHasDeptTreeDataService().then(function(orgData){
		$scope.loading = false ;
		$scope.treeData = utilService.wrapData(orgData,"id","pId","name",function(obj,node){
			node.FLongNumber = obj.FLongNumber ;
			if(obj.unitLayer == 1){
				var unitLayer = "集团";
			}else if(obj.unitLayer == 2){
				var unitLayer = "子公司";
			}else if(obj.unitLayer == 3){
				var unitLayer = "部门";
			}
			node.label = obj.name + "(" + unitLayer + ")";
			node.onSelect = function(branch){
				$scope.FLongNumber = branch.FLongNumber ;
				$scope.treeData.orgName = branch.label ;
				$scope.treeData.orgId = branch.id ;
				
				if($scope.treeAPI.reloadListTable){
					$scope.treeAPI.reloadListTable();
				}
			}
		}) ;
	});
	
	//选择一个默认的节点
	$timeout(function(){
		$scope.treeController.select_first_branch();
		$scope.treeController.expand_all();
	},300);
	
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
	 };
	 
	 $scope.addUnit = function(){
		 $state.go($scope.state.add);
	 }
	 
	 $scope.editIt = function(){
		 $state.go($scope.state.edit);
	 }
	 
	 $scope.seeDetails = function(){
		 $state.go($scope.state.details);
	 }
	 
//结束controller
}]);