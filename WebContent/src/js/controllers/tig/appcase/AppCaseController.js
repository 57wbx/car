'use strict';

app.controller('appCaseController',['$rootScope','$scope','$state','$timeout','$http','sessionStorageService','hintService','roleBtnService',function($rootScope,$scope,$state,$timeout,$http,sessionStorageService,hintService,roleBtnService){
	
	var roleBtnUiClass = "app.appcase.";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);
	
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["appCaseDataTableProperties","appCaseIdForEdit","appCaseIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);
	
	$scope.rowIds = [];//用来保存所选列表的id
	
	/**
	 * 模块路由路径统一管理
	 */
	$scope.state = {
			list:"app.appcase.list",
			add:"app.appcase.add",
			edit:"app.appcase.edit",
			details:"app.appcase.details"
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
	
	/**
	 * 页面弹出框对象
	 */
	var mask = $('<div class="mask"></div>');
	var container = $('#dialog-container');	
	var doIt = function(){};
	 // 执行操作
	  $rootScope.do = function(){
	    doIt();
	  };

	  // 模态框退出
	  $rootScope.cancel = function(){
	    mask.remove();
	    container.addClass('none');
	  };  
	/**
	 * 删除方法的按钮
	 */
	$scope.deleteRow = function(){
		 mask.insertBefore(container);
		 container.removeClass('none');
		 doIt = function(){
			 if($scope.rowIds.length>0){
					$http({
						url:'tig/appCaseAction!deleteAppCaseByIds.action',
						method:'get',
						params:{
							ids:$scope.rowIds
						}
					}).then(function(resp){
						if(resp.data.code==1){//代表成功
							hintService.hint({title: "成功", content: "删除成功！" });
							$state.reload();
						}else{
							alert(resp.data.message);
						}
					});
				}
		 }
	}
	  
	
	//结束方法
}]);