'use strict';

app.controller('orderController',['$rootScope','$scope','$state','$timeout','$http','sessionStorageService','previewService','roleBtnService','orderStateService','warnService','hintService',
                                  function($rootScope,$scope,$state,$timeout,$http,sessionStorageService,previewService,roleBtnService,orderStateService,warnService,hintService){
	
	$scope.rowIds = [];//用来保存所选列表的id
	
	var roleBtnUiClass = "app.order.";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);
	
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["orderDataTableProperties","orderIdForEdit","orderIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);
	
	/**
	 * 模块路由路径统一管理
	 */
	$scope.state = {
			list:"app.order.list",
			add:"app.order.add",
			edit:"app.order.edit",
			details:"app.order.paydetails"
	}
	
	function show(){
		$scope.$evalAsync();
	}
	/**
	 * 设置按钮的状态按钮 其中包括三个属性 当选择0个 、1个、多个
	 * 0个 都不显示
	 * 1个显示修改和查看
	 * 多个显示删除
	 * canEdit 变量是在该页面中显示是否可以修改、详细信息的按钮
	 * 
	 */
	$scope.canEdit = false;
	$scope.setCanEdit = function(booleanValue,id){
		$scope.canEdit = booleanValue ;
		$scope.clearRowIds();
		if(id){
			 $scope.rowIds.push(id);
		}
		show();
	}
	
	$scope.setState = function(stateCode){
		warnService.warn("操作提示","您确定要将该订单记录更改为 "+orderStateService.getOrderState(stateCode)+" 状态吗？",function(){ return setOrderState($scope.rowIds[0],stateCode);},function(resp){
			  if(resp.data.code===1){
				  hintService.hint({title: "成功", content: "更改成功！" });
				  if($scope.API.reloadTable){
					  $scope.API.reloadTable();
				  }
			  }else{
				  alert(resp.data.message);
			  }
		  });
	}
	
	/**
	 * 请求服务，将一条记录的订单状态改变为指定的状态
	 */
	function setOrderState(id,stateCode){
		return $http({
			url:"opr/orderAction!updateOrderState.action",
			method:"post",
			data:{
				id:id,
				orderState:stateCode
			}
		}) ;
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
		if($scope.API.canSeeDetails){
			if(!$scope.API.canSeeDetails($scope.rowIds[0])){
				alert("必须选择已经支付完成的订单");
				return ;
			}
		}
		$state.go($scope.state.details);
	}
	
	/**
	 * 清除缓存数据
	 */
	$scope.clearRowIds = function(){
		$scope.rowIds = [];
	}
	
	$scope.API = {};
	$scope.distributionTask = function(){
		$scope.API.distributionTask();
	}
		
	
	//结束方法
}]);