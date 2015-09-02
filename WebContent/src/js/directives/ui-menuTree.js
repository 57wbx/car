/**
 * 该树是业务类型的树，主要作用是供list界面筛选出编码规范服务业务类型的所有记录
 * 
 */
app.directive("menuTree",['$http','utils','modelDataCacheService',function($http,utils,modelDataCacheService){
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
						   scope.menuTree = {}; 
						   scope.treeAPI = {};
							var data = null;
							 // 从后台获取数据
							modelDataCacheService.menuDataService().then(function(dt){
							 	data = dt;
							 	/*
							 	 * 添加全选的记录
							 	 */
							    initData();
							    scope.loading = false;
							    scope.loading_sub = false;
							});
							  var nodes = {};
							  var treeData = [];

							  // 构造节点
							  function setNode(dt) {
								    if (!nodes['id' + dt['id']]) {
								      var node = {};
								    } else {
								      return nodes['id' + dt['id']];
								    }
								    node['label'] = dt.name || '没有名字';
								    node['data'] = dt.id || '';
								    node['level'] = dt.level ;
								    node['children'] = node['children'] || [];
								    node['parent'] = dt.parentId;
								    
								    node['onSelect'] = item_selected;
								    if (dt['parentId']) {
								      setParentNode(node, dt['parentId']);  // 若存在父节点，则先构造父节点
								    } else {
								      node['parent'] = null;
								    }
								    nodes['id' + dt['id']] = node;
								    return node;
							  }

							  // 构造父节点
							  function setParentNode(node, id) {
								    var len = data.length;
								    for (var i = 0; i < len; i++) {
								      if (data[i]['id'] === id) {
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
									  var id = branch.data ;
									  scope.menuTree.selectedMenuId = id ;
									  scope.menuTree.selectedLevel = branch.level ;
									  console.info("选择菜单的等级: "+scope.menuTree.selectedLevel);
									  console.info("选择的菜单id："+scope.menuTree.selectedMenuId);
									  if(scope.treeAPI.clickTreeListReload){
										  scope.treeAPI.clickTreeListReload(id);
									  }
							  };
				        // 连接函数 
				} 
			}
		 }
	}
}]);