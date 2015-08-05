'use strict';

/**
 * 该模块是根据前面的模块的摸索整理的出来的自己的一个写法，后面的模块以该模块为模板，但是会修改部分方法或者属性名称。
 */
app.controller('busAtomController',['$rootScope','$scope','$state','$timeout','$http',function($rootScope,$scope,$state,$timeout,$http){
	
	$scope.busAtomIds = [];
	
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
//	hiddenButton.click = function(){};
	
	
	
	/**
	 * 设置按钮的状态按钮 其中包括三个属性 当选择0个 、1个、多个
	 * 0个 都不显示
	 * 1个显示修改和查看
	 * 多个显示删除
	 */
	$scope.setButtonStatus = function(){
		var len = $scope.busAtomIds.length ;//id的个数
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
	$scope.clearBusAtomIds = function(){
		$scope.busAtomIds = [];
		$scope.setButtonStatus();
	}
	
	/**
	 * 新增按钮的方法
	 */
	$scope.addBusAtom = function(){
		$state.go("app.busatom.add");
		$scope.treeAPI.hiddenBusTypeTree();
	}
	
	/**
	 * 查看按钮的方法
	 */
	$scope.seeDetails = function(id){
		if(id!=null&&id!=""){
			$scope.clearBusAtomIds();
			$scope.busAtomIds.push(id);
		}
		$state.go("app.busatom.details");
		$scope.treeAPI.hiddenBusTypeTree();
	}
	
	/**
	 * 修改方法的按钮
	 */
	$scope.editBusAtom = function(){
		$state.go("app.busatom.edit");
		$scope.treeAPI.hiddenBusTypeTree();
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
	$scope.removeBusAtom = function(){
		 mask.insertBefore(container);
		 container.removeClass('none');
		 doIt = function(){
			 if($scope.busAtomIds.length>0){
					$http({
						url:'base/busAtomAction!deleteBusAtomByIds.action',
						method:'get',
						params:{
							ids:$scope.busAtomIds
						}
					}).then(function(resp){
						if(resp.data.code==1){//代表成功
							$state.reload();
						}else{
							alert("删除失败");
						}
					});
				}
		 }
		
	}
	
	
	//结束方法
}]);