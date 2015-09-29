'use strict';

app.controller('UserController', ['$rootScope','$scope','$state','$timeout','$http','roleBtnService','modelDataCacheService','utilService','sessionStorageService',function($rootScope, $scope, $state, $timeout,$http,roleBtnService,modelDataCacheService,utilService,sessionStorageService) {
	
	var roleBtnUiClass = "app.user.";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);
	
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["userListDataTableProperties","userIdForEdit","userIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);
	
	
	var stateName = "user";
	$scope.state = {
			list:"app."+stateName+".list",
			add:"app."+stateName+".add",
			edit:"app."+stateName+".edit",
			details:"app."+stateName+".details"
	};
	
	$scope.rowIds = [];
	/**
	 * 清空需要操作的id，主要是在busAtomListController中调用
	 */
	$scope.clearRowIds = function(){
		$scope.rowIds = [];
		$scope.setBtnStatus();
	}
	
	/**
	 * 组织树的形成
	 */
	$scope.treeAPI = {};
	$scope.treeData = [];
	$scope.selectData = {};
	$scope.treeController = {};
	modelDataCacheService.orgHasDeptAndCarShopTreeDataService().then(function(orgData){
		$scope.loading = false ;
		$scope.treeData = utilService.wrapData(orgData,"id","pId","name",function(obj,node){
			node.FLongNumber = obj.FLongNumber ;
			if(obj.unitLayer == 1){
				var unitLayer = "集团";
			}else if(obj.unitLayer == 2){
				var unitLayer = "子公司";
			}else if(obj.unitLayer == 3){
				var unitLayer = "部门";
			}else{
				var unitLayer = "门店";
			}
			node.label = obj.name + "(" + unitLayer + ")";
			node.unitLayer = obj.unitLayer ;
			node.carShopId = obj.carShopId ;
			node.onSelect = function(branch){
				if(branch.FLongNumber){
					$scope.selectData.FLongNumber = branch.FLongNumber ;
					$scope.selectData.carShopId = undefined ;
				}else{
					$scope.selectData.carShopId = branch.id ;
					$scope.selectData.FLongNumber = undefined ;
				}
				$scope.selectData.orgName = branch.label ;
				$scope.selectData.orgId = branch.id ;
				$scope.selectData.unitLayer = branch.unitLayer ;
				
				if($scope.treeAPI.reloadListTable){
					$scope.treeAPI.reloadListTable();
				}
			}
		}) ;
	});
	
	//选择一个默认的节点
	select_first();
	function select_first(){
		$timeout(function(){
			$scope.treeController.select_first_branch();
			$scope.treeController.expand_all();
			if(!$scope.selectData.orgName){
				select_first();
			}
		},30);
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
	 };
	 
	 $scope.add = function(){
		 $state.go($scope.state.add);
		 $scope.clearRowIds();
	 };
	 
	 $scope.editIt = function(){
		 $state.go($scope.state.edit);
	 }
	 
	/*
	 *公共的api 或者储存数据用 
	 */
	$scope.API = {
			isListPage : true,
			checkCodeUnique:checkCodeUnique
	};
	function checkCodeUnique(id,code,fn){
		$http({
			url:"basedata/userAction!checkUserCodeUnique.action",
			method:"POST",
			data:{
				id:id,
				number:code
			}
		}).then(function(resp){
			if(fn){
				fn(resp);
			}
		});
	}
	//是否是列表界面
	$scope.$watch("API.isListPage",function(val){
		 var view_content = $("#view_content");
		 if(val){
			 view_content.addClass("col-sm-9");
			 view_content.removeClass("col-sm-12");
		 }else{
			 view_content.addClass("col-sm-12");
			 view_content.removeClass("col-sm-9");
		 }
	 });
	 
	 //输入提示
	 $scope.message = {
			 password:{
				 pattern:"密码只能由数字和字母组成"
			 },
			 number:{
				 custom_pattern:"该账号名已经存在"
			 }
	 }

}]);