'use strict';

app.controller('carShopController', ['$rootScope','$scope','$state','$timeout','$location','$http','sessionStorageService','hintService','warnService','carShopStateService',function($rootScope, $scope, $state, $timeout,$location,$http,sessionStorageService,hintService,warnService,carShopStateService) {
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

  /**
	 * 推送方法的按钮
	 */
	$scope.pushRow = function(){
		warnService.warn("操作提示","您确定要推送这一条商铺信息吗？该操作是不可更改的！",function(){return push($scope.rowIds[0])},function(resp){
			  if(resp.data.code===1){
				  hintService.hint({title: "成功", content: "推送成功！" });
			  }else{
				  alert(resp.data.message);
			  }
		  });
	}
	
	/**
	 * 推送的http
	 */
	function push(id){
		return $http({
			url:"base/carShopAction!pushCarShop.action",
			method:"post",
			data:{
				id:id
			}
		});
	}
	
	function deleteMethod(ids){
		if(ids.length>0){
			return $http({
				url:'base/carShopAction!deleteCarShop.action',
				method:'post',
				data:{
					ids:ids
				}
			});
		}else{
			alert("请选择需要删除的数据");
		}
	}
	
	/**
	 * 删除方法的按钮
	 */
	$scope.deleteRow = function(){
		if(!$scope.rowIds||$scope.rowIds.length<=0){
			alert("请选择需要删除的数据");
			return ;
		}
		warnService.warn("操作提示","您确定要删除这些商铺信息吗？",function(){return deleteMethod($scope.rowIds)},function(resp){
			  if(resp.data.code===1){
				  hintService.hint({title: "成功", content: "删除成功！" });
				  $state.reload();
			  }else{
				  alert(resp.data.message);
			  }
		  });
	}
	  
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
		  if(resp.data.code==1){
			  hintService.hint({title: "成功", content: "状态更改成功！" });
			  $state.reload($scope.state.list);
		  }else{
			  alert(resp.data.message);
		  }
	  });
  }
  
  /**
   *  表单输入 错误提示
   */
  $scope.message = {
		  IDCARDNO:{
			  pattern:"请输入有效的身份证号"
		  },
		  shopCode:{
			  pattern:"只能由数字和字母组成"
		  },
		  busCard:{
			  pattern:"只能由数字和字母组成"
		  },
		  legalCELL:{
			  pattern:"请输入正确的手机号码"
		  },
		  telephone:{
			  pattern:"请输入正确的电话号码"
		  },
		  qq:{
			  pattern:"请输入正确的数字qq号吗"
		  },
		  bankCardNo:{
			  pattern:"必须为16或19位的正确银行卡号"
		  },
		  employeeNum:{
			  pattern:"员工数必须为整数"
		  }
  }
  
}]);