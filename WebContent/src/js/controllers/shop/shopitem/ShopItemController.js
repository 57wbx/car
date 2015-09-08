'use strict';

/**
 * 该模块用的权限有 新增：ADD	、修改：UPDATE、删除：DELETE、详细信息：DETAILS、图片管理：MANAGEIMG、推送：PUSH
 * $scope.btn = {"ADD":true,"UPDATE":true,"DELETE":true,"DETAILS":true,"MANAGEIMG":true,"PUSH":true}
 */
app.controller('shopItemController',['$rootScope','$scope','$state','$timeout','$http','sessionStorageService','previewService','warnService','hintService','roleBtnService',function($rootScope,$scope,$state,$timeout,$http,sessionStorageService,previewService,warnService,hintService,roleBtnService){
	
	var roleBtnUiClass = "app.shopitem.";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);
	
	$scope.rowIds = [];//用来保存所选列表的id
	
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["shopItemDataTableProperties","shopItemIdForImg","shopItemIdForEdit","shopItemIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);
	
	/**
	 * 模块路由路径统一管理
	 */
	$scope.state = {
			list:"app.shopitem.list",
			add:"app.shopitem.add",
			edit:"app.shopitem.edit",
			details:"app.shopitem.details",
			manageimg:"app.shopitem.manageimg"
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
		$scope.treeAPI.hiddenBusTypeTree();
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
		$scope.treeAPI.hiddenBusTypeTree();
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
	
//	/**
//	 * 删除方法的按钮
//	 */
//	$scope.deleteRow = function(){
//		 mask.insertBefore(container);
//		 container.removeClass('none');
//		 doIt = function(){
//			 if($scope.rowIds.length>0){
//					$http({
//						url:'shop/shopItemAction!deleteShopItemByIds.action',
//						method:'get',
//						params:{
//							ids:$scope.rowIds
//						}
//					}).then(function(resp){
//						if(resp.data.code==1){//代表成功
//							$state.reload();
//						}else{
//							alert("删除失败");
//						}
//					});
//				}
//		 }
//	}
	/**
	 * 推送方法的按钮
	 */
	$scope.pushRow = function(){
		if($scope.editId){
//			$scope.clearRowIds();
//			$scope.rowIds.push($scope.editId);
		}
		warnService.warn("操作提示","您确定要推送这一条服务信息吗？该操作是不可更改的！",function(){return push($scope.editId)},function(resp){
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
			url:"shop/shopItemAction!pushShopItem.action",
			method:"post",
			data:{
				fid:id
			}
		});
	}
	
	function deleteMethod(ids){
		if(ids.length>0){
			return $http({
				url:'shop/shopItemAction!deleteShopItemByIds.action',
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
		warnService.warn("操作提示","您确定要删除这些服务信息吗？",function(){return deleteMethod($scope.rowIds)},function(resp){
			  if(resp.data.code===1){
				  hintService.hint({title: "成功", content: "删除成功！" });
				  $state.reload();
			  }else{
				  alert(resp.data.message);
			  }
		  });
	}
	 /**
     * 显示原始图片的方法
     */
	$scope.API = {};
    $scope.API.showImg = function(item){
   	 previewService.preview(item);
    }
    $scope.API.deleteImg = function(urlObject){
    	urlObject.atomPhotoUrl = undefined;
    }
	
	
	//结束方法
}]);