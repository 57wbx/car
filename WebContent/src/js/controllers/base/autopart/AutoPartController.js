'use strict';

app.controller('autoPartController', ['$rootScope','$scope','$state','$timeout','roleBtnService',function($rootScope, $scope, $state, $timeout,roleBtnService) {
	
	var roleBtnUiClass = "app.autopart.";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);

  $scope.click = function(){};

  var status_false = {
    only : false,
    single : true,
    locked : true,
    mutiple : true
  };

  var status = {
    only : false,
    single : true,
    locked : true,
    mutiple : true
  };

  // 添加组织（工具栏按钮）
  $scope.addAutoPart = function(){
    $rootScope.details = {};
    setStatus(status_false);
    $state.go('app.autopart.add');
  };

  // 编辑某一组织（工具栏按钮）
  $scope.editAutoPart = function(){
	  setStatus(status_false);
	  $state.go('app.autopart.edit');
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
    $rootScope.ids = [];
    setStatus(status);
    window.history.back();
  };  

  // 查看某一组织详情（工具栏按钮）
  $scope.seeDetails = function(id){
    setStatus(status_false);
    $state.go('app.autopart.details');
  };

  // 设置按钮的状态值
  $scope.setBtnStatus = function(){
    if($scope.ids.length === 0){
      $scope.only = false;
      $scope.single = true;
      $scope.locked = true;
      $scope.mutiple = true;
    }else if($scope.ids.length === 1){
      $scope.only = false;
      $scope.single = false;
      $scope.locked = false;
      $scope.mutiple = false;
    }else{
      $scope.only = false;
      $scope.single = true;
      $scope.locked = true;
      $scope.mutiple = false;
    }
    hButton.trigger('click'); // 触发一次点击事件，使所以按钮的状态值生效
  };

  function setStatus(param){
    if(param){
      $scope.only = param.only,
      $scope.single = param.single,
      $scope.locked = param.locked,
      $scope.mutiple = param.mutiple
    }
  }

}]);