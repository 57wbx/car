'use strict';

app.controller('busPackageController', ['$rootScope','$scope','$state','$timeout','$http',function($rootScope, $scope, $state, $timeout,$http) {
//  var url = app.url.org.api.list; // 后台API路径
	
	var url = "base/busTypeAction!listBusType.action";
	var data = null;

	$scope.rowIds = [];//用来保存所选列表的id
	$scope.busTypeTree = {//用来保存现在选中的的业务类型$scope.busTypeTree.selectedTypeCode
			selectedTypeCode:""
	};//用来保存现在选中的
	/**
	 * 父cotroller中保存的API，该API中的方法会在该和子controller中被调用
	 */
	$scope.busPackageAPI = {//用来保存子页面中可以被调用的方法,提供给该controller调用,该对象中的API需要在子controller中进行维护，
			/**
			 * 用来进行对list页面的套餐选择筛选出树所指向的业务类型的数据
			 * 需要在子类controller中进行维护
			 * 该方法在BusPackageListController中初始化。
			 * $scope.busPackageAPI.clickTreeListReload
			 */
			clickTreeListReload:null,
			/**
			 * 主要是在新增和修改页面使用，目的是当选择树的时候，新增或修改页面筛选出特定业务类型下的服务项
			 * 需要在子类controller中进行初始化和维护
			 * $scope.busPackageAPI.clickTreeAddOrUpdateReload
			 */
			clickTreeAddOrUpdateReload:null,
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
	};
	
	
	
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
		$state.go("app.buspackage.add");
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
		$state.go("app.buspackage.details");
	}
	
	/**
	 * 修改方法的按钮
	 */
	$scope.editRow = function(){
		if($scope.editId){
			$scope.clearRowIds();
			$scope.rowIds.push($scope.editId);
		}
		$state.go("app.buspackage.edit");
	}
	/**
	 * 删除方法的按钮
	 */
	$scope.deleteRow = function(){
		 mask.insertBefore(container);
		 container.removeClass('none');
		 doIt = function(){
			 if($scope.rowIds.length>0){
					$http({
						url:'base/busPackageAction!deleteBusPackageByIds.action',
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
    console.info($scope.org_tree);
    if(!$rootScope.item_id){
      $timeout(function(){
        // 默认选中第一个节点
//        $scope.org_tree.select_first_branch();
      }, 200);
    }else{	
    		$scope.org_tree.select_branch($rootScope.busTypeBranch);
    }
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
	  if($scope.busPackageAPI.clickTreeListReload){
		  $scope.busPackageAPI.clickTreeListReload(busTypeCode);
	  }else if($scope.busPackageAPI.clickTreeAddOrUpdateReload){
		  $scope.busPackageAPI.clickTreeAddOrUpdateReload();
	  }
  };

  
 
  
  
  
  $scope.click = function(){};

  var mask = $('<div class="mask"></div>');
  var container = $('#dialog-container');
  var message_body = $("#dialog_containner_message_body");
  var old_message_body = message_body.html();
  var dialog = $('#dialog');
  var hButton = $('#clickId');
  var doIt = function(){};

 

  // 执行操作
  $rootScope.do = function(){
    doIt();
  };

  // 模态框退出
  $rootScope.cancel = function(){
    mask.remove();
    container.addClass('none');
    message_body.html(old_message_body);
  };  

 
  
  /**
   * 以下为API中方法的初始化过程
   */
  /**
	 * 用来切换视图的方法，该cotroller的view还包含了一个子的view，通过该方法切换是否显示业务类型的树
	 */
	//显示或者不显示业务类型的树
	$scope.busPackageAPI.showOrHiddenBusTypeTree = function(){
		if($("#busPackageBusTypeTree").attr("class").indexOf("none")>0){//此时的树是隐藏的
			$scope.busPackageAPI.showBusTypeTree();
		}else{
			$scope.busPackageAPI.hiddenBusTypeTree();
		}
	}
	$scope.busPackageAPI.showBusTypeTree = function(){
		$("#busPackageContent").removeClass("col-md-12");
		$("#busPackageContent").addClass("col-md-10");
		$("#busPackageBusTypeTree").removeClass("none");
	}
	$scope.busPackageAPI.hiddenBusTypeTree = function(){
		$("#busPackageBusTypeTree").addClass("none");
		$("#busPackageContent").removeClass("col-md-10");
		$("#busPackageContent").addClass("col-md-12");
	}
  

  

}]);