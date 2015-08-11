/**
 * 该树是业务类型的树，主要作用是供list界面筛选出编码规范服务业务类型的所有记录
 * 
 */
app.directive("busTypeTree",['$http','utils','modelDataCacheService',function($http,utils,modelDataCacheService){
	return {
		restrict:"A",
		transclude:true,
		template:'<div class="panel panel-default">'+
				'<div class="panel-body">'+
				'<div class="wrapper-md">'+
				'<div class="b-a r-2x" ng-init="loading=true">'+
				'<div ng-if="loading" class="loading">'+
					'<i class="glyphicon glyphicon-repeat"></i>'+
				'</div>'+
				'<span ng-if="doing_async">...加载中...</span>'+
				'<div ng-transclude></div>'+
			'</div></div></div>',
			compile: function(tEle, tAttrs, transcludeFn){
				   return {
					   pre: function(scope, iElem, iAttrs){
						   
						   scope.busTypeTree = {//用来保存现在选中的的业务类型$scope.busTypeTree.selectedTypeCode
									selectedTypeCode:""
							};//用来保存现在选中的
						   scope.treeAPI = scope.treeAPI || {
								   /**
									 * 用来进行对list页面的套餐选择筛选出树所指向的业务类型的数据
									 * 需要在子类controller中进行维护
									 * 该方法在BusPackageListController中初始化。
									 * $scope.treeAPI.clickTreeListReload
									 */
									clickTreeListReload:null,
									/**
									 * 主要是在新增和修改页面使用，目的是当选择树的时候，新增或修改页面筛选出特定业务类型下的服务项
									 * 需要在子类controller中进行初始化和维护
									 * $scope.treeAPI.clickTreeAddOrUpdateReload
									 */
									clickTreeAddOrUpdateReload:null,
									/**
									 * 显示或者隐藏树
									 * 在该controller中已经初始化
									 * $scope.treeAPI.showOrHiddenBusTypeTree
									 */
									showOrHiddenBusTypeTree:null,
									/**
									 * 显示树
									 * 在该controller中已经初始化
									 * $scope.treeAPI.showBusTypeTree
									 */
									showBusTypeTree:null,
									/**
									 * 隐藏树
									 * 在该controller中已经初始化
									 */
									hiddenBusTypeTree:null
						   };
						   
						   var url = "base/busTypeAction!listBusType.action";
							var data = null;
							 // 从后台获取数据
							
							modelDataCacheService.BusTypeTreeDataService().then(function(dt){
							 	data = dt;
							 	/*
							 	 * 添加全选的记录
							 	 */
							 	data.push({
							 		busTypeCode:"",
							 		busTypeName:"所有类型",
							 		parentId:"",
							 		simpleName:"所有类型",
							 	});
							    initData();
							    scope.loading = false;
							    scope.loading_sub = false;
							});
							
							/**
							 * 0: Object
busTypeCode: "BP"
busTypeName: "钣金油漆"
isLeaf: 2
isShow: 1
memo: "一级目录"
parentId: ""
simpleName: "钣喷"
sortCode: "06"
useState: 1
							 */
							
//							  initTree();

//							  function initTree(){
//								  utils.getData(url, function callback(dt){
//									    data = dt;
//									    initData();
//									    scope.loading = false;
//									    scope.loading_sub = false;
//									  });
//							  }
							  
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
							    node['data'] = dt.busTypeCode || '';
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
							  scope.tree_data = [];
							  scope.org_tree = {};

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

							    // 列表树数据传值
							    scope.tree_data = treeData;
							  }

							  // 选择列表树中的一项
							  var item_selected = function(branch) {
							    
							    tree_handler(branch);
							  };  

							  
							  
							  // 选择列表树中的一项
							  var tree_handler = function(branch) {
								  var busTypeCode = branch.data ;
								  scope.busTypeTree.selectedTypeCode = busTypeCode ;
								  console.info("业务类型id："+scope.busTypeTree.selectedTypeCode);
								  if(scope.treeAPI.clickTreeListReload){
									  scope.treeAPI.clickTreeListReload(busTypeCode);
								  }else if(scope.treeAPI.clickTreeAddOrUpdateReload){
									  scope.treeAPI.clickTreeAddOrUpdateReload();
								  }
							  };

							  /**
							   * 以下为API中方法的初始化过程
							   */
							  /**
								 * 用来切换视图的方法，该cotroller的view还包含了一个子的view，通过该方法切换是否显示业务类型的树
								 */
								//显示或者不显示业务类型的树
							  scope.treeAPI.showOrHiddenBusTypeTree = function(){
									if($("#modelBusTypeTree").attr("class").indexOf("none")>0){//此时的树是隐藏的
										scope.treeAPI.showBusTypeTree();
									}else{
										scope.treeAPI.hiddenBusTypeTree();
									}
								}
							  scope.treeAPI.showBusTypeTree = function(){
									$("#modelContent").removeClass("col-md-12");
									$("#modelContent").addClass("col-md-10");
									$("#modelBusTypeTree").removeClass("none");
								}
							  scope.treeAPI.hiddenBusTypeTree = function(){
									$("#modelBusTypeTree").addClass("none");
									$("#modelContent").removeClass("col-md-10");
									$("#modelContent").addClass("col-md-12");
								}
//							  
						   
			              }
				   };
				        // 连接函数 
				} 
			
			//
	}
}]);