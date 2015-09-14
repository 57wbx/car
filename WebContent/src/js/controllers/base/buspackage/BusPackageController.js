'use strict';

app.controller('busPackageController', ['$rootScope','$scope','$state','$timeout','$http','warnService','hintService','sessionStorageService','roleBtnService',function($rootScope, $scope, $state, $timeout,$http,warnService,hintService,sessionStorageService,roleBtnService) {
//  var url = app.url.org.api.list; // 后台API路径
	
	var roleBtnUiClass = "app.buspackage.";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);
	
	var url = "base/busTypeAction!listBusType.action";
	var data = null;
	
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["busPackageDataTableProperties","busPackageIdForImg","busPackageIdForEdit","busPackageIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);

	$scope.rowIds = [];//用来保存所选列表的id
	
	/**
	 * 模块路由路径统一管理
	 */
	$scope.state = {
			list:"app.buspackage.list",
			add:"app.buspackage.add",
			edit:"app.buspackage.edit",
			details:"app.buspackage.details",
			manageimg:"app.buspackage.manageimg"
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
	/**
	 * 清空需要操作的id，主要是在busAtomListController中调用
	 */
	$scope.clearRowIds = function(){
		$scope.rowIds = [];
		$scope.setButtonStatus();
	}
	$scope.setButtonStatus = function(){
		var len = $scope.rowIds.length ;//id的个数
		if(len==0){
			$scope.isSingle = false ;
			$scope.isMulti = false ;
		}else if(len==1){
			$scope.isSingle = true ;
			$scope.isMulti = false ;
		}else{
			$scope.isSingle = false ;
			$scope.isMulti = true ;
		}
		show();
//		hiddenButton.trigger("click");
	}
	
	/**
	 * 新增按钮的方法
	 */
	$scope.addRow = function(){
		$state.go($scope.state.add);
	}
	
	/**
	 * 查看按钮的方法
	 */
	$scope.seeDetails = function(id){
		$scope.clearRowIds();
		if(id!=null&&id!=""){
			$scope.rowIds.push(id);
		}else if($scope.editId){
			$scope.rowIds.push($scope.editId);
		}
		$state.go($scope.state.details);
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
		$scope.treeAPI.hiddenBusTypeTree();
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
	
	/**
	 * 推送方法的按钮
	 */
	$scope.pushRow = function(){
		if($scope.editId){
//			$scope.clearRowIds();
//			$scope.rowIds.push($scope.editId);
		}
		warnService.warn("操作提示","您确定要推送这一条套餐信息吗？该操作是不可更改的！",function(){return push($scope.editId)},function(resp){
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
			url:"base/busPackageAction!pushBusPackage.action",
			method:"post",
			data:{
				fid:id
			}
		});
	}
	
	/**
	 * 删除的method
	 */
	function deleteMethod(ids){
		if(ids.length>0){
			return $http({
				url:'base/busPackageAction!deleteBusPackageByIds.action',
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
		warnService.warn("操作提示","您确定要删除这些套餐信息吗？",function(){return deleteMethod($scope.rowIds)},function(resp){
			  if(resp.data.code===1){
				  hintService.hint({title: "成功", content: "删除成功！" });
				  $state.reload();
			  }else{
				  alert(resp.data.message);
			  }
		  });
	}
	
}]);