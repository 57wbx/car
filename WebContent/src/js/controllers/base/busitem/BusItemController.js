'use strict';

app.controller('busItemController',['$rootScope','$scope','$state','$timeout','$http','sessionStorageService',function($rootScope,$scope,$state,$timeout,$http,sessionStorageService){
	
	/**
	 * 在session中不能清除的内容，应该包含子缓存对象
	 */
	$scope.session = {};
	$scope.session.cacheArray = ["busItemIdForImg"];
	sessionStorageService.clearNoCacheItem($scope.session.cacheArray);
	
	$scope.rowIds = [];//用来保存所选列表的id
	
	$scope.busTypeTree = {//用来保存现在选中的的业务类型$scope.busTypeTree.selectedTypeCode
			selectedTypeCode:""
	};//用来保存现在选中的
	/**
	 * 用来保存该模块中一系列的操作方法
	 */
	$scope.busItemAPI = {
			/**
			 * 用来进行对list页面的服务选择筛选出树所指向的业务类型的数据
			 * 需要在子类listcontroller中进行维护
			 * 该方法在BusItemListController中初始化。
			 * $scope.busItemAPI.clickTreeListReload
			 */
			clickTreeListReload:null,
			/**
			 * 显示或者隐藏树
			 * 在该controller中已经初始化
			 * $scope.busPackageAPI.showOrHiddenBusTypeTree
			 */
			showOrHiddenBusTypeTree:null,
			/**
			 * 显示树
			 * 在该controller中已经初始化
			 * $scope.busPackageAPI.showBusTypeTree
			 */
			showBusTypeTree:null,
			/**
			 * 隐藏树
			 * 在该controller中已经初始化
			 */
			hiddenBusTypeTree:null
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
		$state.go("app.busitem.add");
		$scope.busItemAPI.hiddenBusTypeTree();
	}
	
	/**
	 * 管理图片的方法
	 */
	$scope.manageImg = function(){
		if($scope.editId){
			$scope.clearRowIds();
			$scope.rowIds.push($scope.editId);
		}
		$state.go("app.busitem.manageimg");
		$scope.busItemAPI.hiddenBusTypeTree();
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
		$state.go("app.busitem.details");
		$scope.busItemAPI.hiddenBusTypeTree();
	}
	
	/**
	 * 修改方法的按钮
	 */
	$scope.editRow = function(){
		if($scope.editId){
			$scope.clearRowIds();
			$scope.rowIds.push($scope.editId);
		}
		$state.go("app.busitem.edit");
		$scope.busItemAPI.hiddenBusTypeTree();
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
						url:'base/busItemAction!deleteBusItemByIds.action',
						method:'get',
						params:{
							ids:$scope.rowIds
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
	
	/**
	 * 树的相关操作
	 */
	var url = "base/busTypeAction!listBusType.action";
	var data = null;
	 // 从后台获取数据
	  initTree();

	  function initTree(){
		  app.utils.getData(url, function callback(dt){
			    data = dt;
			    initData();
			    $scope.loading = false;
			    $scope.loading_sub = false;
			  });
	  }
	  
	  var nodes = {};
	  var treeData = [];

	  // 构造节点
	  function setNode(dt) {
	    if (!nodes['busTypeCode' + dt['busTypeCode']]) {
	      var node = {};
	    } else {
	      return nodes['busTypeCode' + dt['busTypeCode']];
	    }
	    node['label'] = dt.simpleName || '没有名字';
	    node['data'] = dt.busTypeCode || '没有数据';
	    node['children'] = node['children'] || [];
	    node['parent'] = dt.parentId;
	    
	    node['onSelect'] = item_selected;
	    if (dt['parentId']) {
	      setParentNode(node, dt['parentId']);  // 若存在父节点，则先构造父节点
	    } else {
	      node['parent'] = null;
	    }
	    nodes['busTypeCode' + dt['busTypeCode']] = node;
	    return node;
	  }

	  // 构造父节点
	  function setParentNode(node, id) {
	    var len = data.length;
	    for (var i = 0; i < len; i++) {
	      if (data[i]['busTypeCode'] === id) {
	        var parentNode = setNode(data[i]);
	        parentNode['children'].push(node);
	      }
	    }
	  }

	  // 列表树数据
	  $scope.tree_data = [];
	  $scope.org_tree = {};

	  // 初始化数据并生成列表树所需的数据结构
	  function initData() {
	    // data = formatData(data);
	    var len = data.length;
	    for (var i = 0; i < len; i++) {
	      var node = setNode(data[i]);
	      if (node['parent'] === null) {
	        treeData.push(node);
	      }
	    }
	    var container_a = [], container_b = [], ln = treeData.length;
	    for(var i=0; i<ln; i++){
	      if(treeData[i].children.length !== 0){
	        treeData[i].expanded = false;
	        container_a.push(treeData[i]);
	      } else{
	        container_b.push(treeData[i]);
	      }
	    }
	    treeData = container_a.concat(container_b);
	    console.log(treeData);

	    // 列表树数据传值
	    $scope.tree_data = treeData;
	   
	  }

	  // 选择列表树中的一项
	  var item_selected = function(branch) {
	    $rootScope.item_id = branch.data;
	    $rootScope.busTypePatentId = branch.parent;
	    $rootScope.busTypeBranch = branch ;
	    $rootScope.ids = [];
	    try {
	    	$scope.init();
	    } catch (e) {
	    }
	    
	    
	    console.log(branch);
	    console.log($rootScope.busTypePatentId);
	    
	    
	    tree_handler(branch);
	  };  

	  
	  
	  // 选择列表树中的一项
	  var tree_handler = function(branch) {
		  var busTypeCode = branch.data ;
		  $scope.busTypeTree.selectedTypeCode = busTypeCode ;
		  if($scope.busItemAPI.clickTreeListReload){
			  $scope.busItemAPI.clickTreeListReload(busTypeCode);
		  }else if($scope.busItemAPI.clickTreeAddOrUpdateReload){
			  $scope.busItemAPI.clickTreeAddOrUpdateReload();
		  }
	  };

	  
	  /**
	   * 以下为API中方法的初始化过程
	   */
	  /**
		 * 用来切换视图的方法，该cotroller的view还包含了一个子的view，通过该方法切换是否显示业务类型的树
		 */
		//显示或者不显示业务类型的树
		$scope.busItemAPI.showOrHiddenBusTypeTree = function(){
			if($("#modelBusTypeTree").attr("class").indexOf("none")>0){//此时的树是隐藏的
				$scope.busPackageAPI.showBusTypeTree();
			}else{
				$scope.busPackageAPI.hiddenBusTypeTree();
			}
		}
		$scope.busItemAPI.showBusTypeTree = function(){
			$("#modelContent").removeClass("col-md-12");
			$("#modelContent").addClass("col-md-10");
			$("#modelBusTypeTree").removeClass("none");
		}
		$scope.busItemAPI.hiddenBusTypeTree = function(){
			$("#modelBusTypeTree").addClass("none");
			$("#modelContent").removeClass("col-md-10");
			$("#modelContent").addClass("col-md-12");
		}
	  
	
	//结束方法
}]);