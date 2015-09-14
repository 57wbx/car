'use strict';

app.controller('carownerController',['$rootScope','$scope','$state','$timeout','$http','sessionStorageService','memberStateService','warnService','hintService',function($rootScope,$scope,$state,$timeout,$http,sessionStorageService,memberStateService,warnService,hintService){
	
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["carownerDataTableProperties","carownerIdForEdit","carownerIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);
	
	$scope.rowIds = [];//用来保存所选列表的id
	
	$scope.selectData = {
			useState:memberStateService.getUseStateObject()
	}
	
	console.info($scope.selectData.useState);
	/**
	 * 模块路由路径统一管理
	 */
	$scope.state = {
			list:"app.carowner.list",
			add:"app.carowner.add",
			edit:"app.carowner.edit",
			details:"app.carowner.details"
	}
	
	/**
	 * 并无实际的意义，但是如果没有用这个方法的话，页面中菜单按钮将不会根据属性的值进行变化
	 * 
	 * 2015.7.15 利用一个隐藏的按钮来解决按钮状态值更新的事件clickId
	 */
//	var hiddenButton = $("#clickId");
	function show(){
		$timeout(function(){
			//无实际作用，更新页面中的按钮菜单样式
		},0);
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
		if(id){
			$scope.editId = id;
		}
		show();
	}
	$scope.setButtonStatus = function(){
		var len = $scope.rowIds.length ;//id的个数
		console.info("父类中的设置按钮的状态");
		if(len==0){
			$scope.isSingle = false ;
			$scope.isMulti = false ;
			$scope.setCanEdit(false);
		}else if(len==1){
			$scope.isSingle = true ;
			$scope.isMulti = false ;
			$scope.setCanEdit(true,$scope.rowIds[0]);
		}else{
			$scope.isSingle = false ;
			$scope.isMulti = true ;
			$scope.setCanEdit(false);
		}
		show();
//		hiddenButton.trigger("click");
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
	   * 改变状态的操作
	   */
	  $scope.setUseState = function(param){
		  //1=正常、2=停用、3=注销4=（黑名单）
		 var useState = memberStateService.getUseState(param);
		  warnService.warn(null,"您确定要将该记录的状态变换为："+useState+" 吗？",function(){
			  return $http({
				  url:"base/memberAction!updateMemberUseState.action",
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
	 * 清空需要操作的id，主要是在busAtomListController中调用
	 */
	$scope.clearRowIds = function(){
		$scope.rowIds = [];
		$scope.setButtonStatus();
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
	

	$scope.message = {
			cell:{
				pattern:"请输入正确的手机号码"
			},
			IDCARDNO:{
				pattern:"请输入正确格式的身份证号"
			}
	}
	//结束方法
}]);