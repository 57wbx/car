'use strict';

/**
 * 该模块用的权限有 新增：ADD	、修改：UPDATE、删除：DELETE、详细信息：DETAILS
 * $scope.btn = {"ADD":true,"UPDATE":true,"DELETE":true,"DETAILS":true}
 */
app.controller('carNameController', ['$rootScope','$scope','$state','$timeout','$location','$http','sessionStorageService','hintService','warnService','roleBtnService','previewService',function($rootScope, $scope, $state, $timeout,$location,$http,sessionStorageService,hintService,warnService,roleBtnService,previewService) {
	
	var roleBtnUiClass = "app.carname.";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);
	
    var data = null;
    $scope.rowIds = [];
  
	/**
	 * 清空需要操作的id，主要是在busAtomListController中调用
	 */
	$scope.clearRowIds = function(){
		$scope.rowIds = [];
		$scope.setBtnStatus();
	}

  $scope.search ={};
  
  /**
	 * 模块路由路径统一管理
	 */
	$scope.state = {
			list:"app.carname.list",
			add:"app.carname.add",
			edit:"app.carname.edit",
			details:"app.carname.details"
	}
  
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["carNameListDataTableProperties","carNameIdForEdit","carNameIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);


  var status_false = {
    single : true,
    locked : true,
    mutiple : true
  };

  var status = {
    single : true,
    locked : true,
    mutiple : true
  };

  // 添加组织（工具栏按钮）
  $scope.addUnit = function(){
    if($scope.rowIds.length === 0){
      $rootScope.details = {};
    }
    setStatus(status_false);
    $state.go($scope.state.add);
  };

  // 编辑某一组织（工具栏按钮）
  $scope.editIt = function(){
//    if($rootScope.obj['id']){
//      $rootScope.details = $rootScope.obj;
      setStatus(status_false);
      $state.go($scope.state.edit);
//    }
  };   

  
  function deleteCarNames(ids){
	  if(ids && ids.length>0){
    	  return $http({
    		  url:"base/carNameAction!deleteCarNameByIds.action",
    		  method:"post",
    		  data:{
    			  ids:ids
    		  }
    	  });
	  }else{
		  alert("请选择需要删除的记录！");
	  }
  }
  // 删除请假记录（工具栏按钮）
  $scope.removeIt = function(){
	  if($scope.rowIds&&$scope.rowIds.length>0){
		  warnService.warn("操作提示","您确定要删除这些车品牌数据吗？",function(){return deleteCarNames($scope.rowIds)},function(resp){
			  if(resp.data.code===1){
				  hintService.hint({title: "成功", content: "删除成功！" });
				  $state.reload();
			  }else{
				  alert(resp.data.message);
			  }
		  });
	  }else{
		  alert("请选择需要删除的记录！");
	  }
  };

  // 模态框退出
  $rootScope.cancel = function(){
    mask.remove();
    container.addClass('none');
  };  

  // 查看某一组织详情（工具栏按钮）
  $scope.seeDetails = function(id){
    setStatus(status_false);
    if(id){
    	$scope.rowIds.push(id);
    }
    $state.go($scope.state.details);
  };

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

  function setStatus(param){
    if(param){
      $scope.single = param.single,
      $scope.locked = param.locked,
      $scope.mutiple = param.mutiple
    }
  }
  
  
	 /**
   * 显示原始图片的方法
   */
	$scope.API = {};
  $scope.API.showImg = function(item){
 	 previewService.preview(item);
  }
  $scope.API.deleteImg = function(urlObject){
  	urlObject.photoUrl = undefined;
  }
  
  $scope.cancel = function(){
	  $state.go($scope.state.list);
  }
  
}]);