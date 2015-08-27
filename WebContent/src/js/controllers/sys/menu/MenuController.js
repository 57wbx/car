'use strict';

app.controller('menuController', ['$rootScope','$scope','$state','$timeout','$location','$http','sessionStorageService','hintService','warnService','carShopStateService',function($rootScope, $scope, $state, $timeout,$location,$http,sessionStorageService,hintService,warnService,carShopStateService) {
	
  var url = app.url.org.api.list; // 后台API路径
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
			list:"app.carshop.list",
			add:"app.carshop.add",
			edit:"app.carshop.edit",
			details:"app.carshop.details"
	}
  
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["carShopListDataTableProperties","carShopIdForEdit","carShopIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);

  $scope.click = function(){};

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

  var mask = $('<div class="mask"></div>');
  var container = $('#dialog-container');
  var dialog = $('#dialog');
  var hButton = $('#clickId');
  var doIt = function(){};

  // 删除请假记录（工具栏按钮）
  $scope.removeIt = function(){
    mask.insertBefore(container);
    container.removeClass('none');
    doIt = function(){
      if($scope.rowIds.length !== 0){
    	  $http({
    		  url:"base/carShopAction!deleteCarShop.action",
    		  method:"post",
    		  data:{
    			  ids:$scope.rowIds
    		  }
    	  }).then(function(resp){
    		  mask.remove();
    		  container.addClass('none');
    		  if(resp.data.code){
    			  hintService.hint({title: "成功", content: "删除成功！" });
    			  $state.reload($scope.state.list);
    		  }else{
    			  alert(resp.data.message);
    		  }
    	  });
      }
    };
  };

  // 执行操作
  $rootScope.do = function(){
    doIt();
  };

  // 模态框退出
  $rootScope.cancel = function(){
    mask.remove();
    container.addClass('none');
  };  

  // 不操作返回
  $scope.return = function(){
    $scope.rowIds = [];
    setStatus(status);
    window.history.back();
  };  

  // 查看某一组织详情（工具栏按钮）
  $scope.seeDetails = function(id){
    setStatus(status_false);
    if(id){
    	$scope.rowIds.push(id);
    }
    $state.go($scope.state.details);
//    console.info($scope.ids);
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
//    if(){
//    	hButton.trigger('click'); // 触发一次点击事件，使所以按钮的状态值生效
//    }
  };

  function setStatus(param){
    if(param){
      $scope.single = param.single,
      $scope.locked = param.locked,
      $scope.mutiple = param.mutiple
    }
  }
  
  /**
   * 改变状态的操作
   */
  $scope.setUseState = function(param){
	  //1=正常、2=停用、3=注销（黑名单）
	 var useState = carShopStateService.getUseState(param);
	  warnService.warn(null,"您确定要将该记录的状态变换为："+useState+" 吗？",function(){
		  return $http({
			  url:"base/carShopAction!updateUseStateByIds.action",
		  	  method:"post",
		  	  data:{
		  		  ids :$scope.rowIds,
		  		  useState : param 
		  	  }
		  });
	  },function(resp){
		  if(resp.data.code){
			  hintService.hint({title: "成功", content: "状态更改成功！" });
			  $state.reload($scope.state.list);
		  }else{
			  alert(resp.data.message);
		  }
	  });
  }
  
}]);