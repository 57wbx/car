'use strict';

app.controller('autoPartController', ['$rootScope','$scope','$state','$timeout','roleBtnService','sessionStorageService',function($rootScope, $scope, $state, $timeout,roleBtnService,sessionStorageService) {
	
	var roleBtnUiClass = "app.autopart.";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);

	$scope.rowIds = [];
	
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["autoPartListDataTableProperties","autoPartIdForEdit","autoPartIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);
	
	/**
	 * 清空需要操作的id，主要是在busAtomListController中调用
	 */
	$scope.clearRowIds = function(){
		$scope.rowIds = [];
		$scope.setBtnStatus();
	}

  $scope.search ={};
	
	$scope.state = {
			add:"app.autopart.add",
			edit:"app.autopart.edit" ,
			details:"app.autopart.details",
			list:"app.autopart.list"
	}


  // 添加组织（工具栏按钮）
  $scope.addAutoPart = function(){
    $rootScope.details = {};
    $state.go($scope.state.add);
  };

  // 编辑某一组织（工具栏按钮）
  $scope.editAutoPart = function(){
	  $state.go($scope.state.edit);
  };   

  var mask = $('<div class="mask"></div>');
  var container = $('#dialog-container');
  var dialog = $('#dialog');
  var hButton = $('#clickId');
  var doIt = function(){};

 
  // 删除某一组织（工具栏按钮）
  $scope.removeAutoPart = function(){
    mask.insertBefore(container);
    container.removeClass('none');
    
    doIt = function(){
      if($rootScope.ids.length !== 0){
    	  
    	  
        app.utils.getData("base/autoPartAction!deleteAutoPartByIds.action", {"ids":$rootScope.ids}, function callback(dt){
          mask.remove();
          container.addClass('none');
          $state.reload('app.autopart.list');
        });
      }
    };
  };

  /// 查看某一组织详情（工具栏按钮）
  $scope.seeDetails = function(id){
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
	  
	  /**
	   * 用户输入框提示信息
	   */
	  $scope.message = {
			  partCode:{
				  pattern:"编号只能为数字、字母和点的组成"
			  },
			  sunitPrice:{
				  pattern:"出厂价格最多只能带两位小数"
			  },
			  yunitPrice:{
				  pattern:"优惠价格最多只能带两位小数"
			  },
			  eunitPrice:{
				  pattern:"市场价格最多只能带两位小数"
			  },
			  stock:{
				  pattern:"库存数只能为整数"
			  }
	  }
	  

}]);