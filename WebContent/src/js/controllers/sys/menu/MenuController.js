'use strict';

app.controller('menuController', ['$rootScope','$scope','$state','$timeout','$location','$http','sessionStorageService','hintService','warnService','carShopStateService',function($rootScope, $scope, $state, $timeout,$location,$http,sessionStorageService,hintService,warnService,carShopStateService) {
	
  $scope.search ={};
  
  /**
	 * 模块路由路径统一管理
	 */
	$scope.state = {
			list:"app.menu.list",
			add:"app.menu.add",
			edit:"app.menu.edit",
			details:"app.menu.details"
	}
  
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["menuListDataTableProperties","menuIdForEdit","menuIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);

	
	/**
	 * 新增二级菜单
	 */
	$scope.addSecondMenu = function(){
		console.info($scope.menuTree.selectedLevel);
		if($scope.menuTree.selectedLevel==1 || $scope.menuTree.selectedLevel===undefined || !$scope.menuTree.selectedMenuId){
			alert("请选择需要新增的一级菜单");
			return ;
		}
		if($scope.treeAPI&&$scope.treeAPI.newMenu){
			console.info("新增二级菜单：父菜单id为：",$scope.menuTree.selectedMenuId);
			$scope.treeAPI.newMenu($scope.menuTree.selectedMenuId);
		}
	}
	/**
	 * 新增一级菜单
	 */
	$scope.addFirstMenu = function(){
		if($scope.treeAPI&&$scope.treeAPI.newMenu){
			$scope.treeAPI.newMenu(null);
		}
		
	}


  
}]);