'use strict';

app.controller('PositionController', function($rootScope, $scope, $state, $timeout) {
  var allBtn =["add","delete","modify"];// btn id array
  $scope.permBtn = app.utils.rolePerm(app.url.position.api.menuPerm,"003",allBtn);//已有权限项数组
  var url = app.url.org.api.list; // 后台API路径
  var data = null;
  // 从后台获取数据
  app.utils.getData(url, function callback(dt){
    data = dt;
    initData();
    $scope.loading = false;
    $scope.loading_sub = false;
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
    node['data'] = dt.id || '没有数据';
    node['children'] = node['children'] || [];
    node['onSelect'] = item_selected;
    node['FLongNumber'] = dt.FLongNumber;
    node['unitLayer'] = dt.unitLayer;
    if (dt['parent']) {
      setParentNode(node, dt['parent']);  // 若存在父节点，则先构造父节点
    } else {
      node['parent'] = null;
    }
    nodes['id' + dt['id']] = node;
    return node;
  }
  $scope.reload = function(){
	  $timeout(function(){
        $scope.init();
      }, 200);
  };
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
  $scope.tree_data = [];
  $scope.org_tree = {};

  // 初始化数据并生成列表树所需的数据结构
  function initData() {
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
        treeData[i].expanded = true;
        container_a.push(treeData[i]);
      } else{
        container_b.push(treeData[i]);
      }
    }
    treeData = container_a.concat(container_b);
    // 列表树数据传值
    $scope.tree_data = treeData;
      $timeout(function(){
        // 默认选中第一个节点
    	  try {
    		  $scope.init();
    	  } catch (e) {
    	  }
      }, 200);
  }
  var thisBranch;
  // 选择列表树中的一项
  var item_selected = function(branch) {
	  thisBranch=branch;
	  $rootScope.FLongNumber=branch.FLongNumber;
    $rootScope.ids = [];
    $scope.setBtnStatus();
    try {
    	$scope.init();
    } catch (e) {
    }
  };  

  // 选择列表树中的一项
  var tree_handler = function(branch) {
    $state.go('app.position.list');
  };
  $scope.click = function(){};
  
  var status_false = {
    only : true,
    single : true,
    locked : true,
    mutiple : true
  };

  var status = {
    only : false,
    single : true,
    locked : true,
    mutiple : true
  };

  // 添加职位（工具栏按钮）
  $scope.addUnit = function(){
	  if(!$scope.org_tree.get_selected_branch()||!(!thisBranch.unitLayer||thisBranch.unitLayer==3)){
		  mask.insertBefore(container);
		  $("#msgP").html("只能在部门下添加职位!");
		  container.removeClass('none');
		  doIt = function(){
			  $rootScope.cancel();
		  };
	  }else{
		  sessionStorage.setItem("treeId", thisBranch.data);
		  sessionStorage.setItem("treeName", thisBranch.label);
		  setStatus(status_false);
		  $state.go('app.position.add');
	  }
  };

  // 编辑某一职位（工具栏按钮）
  $scope.editIt = function(){
	  sessionStorage.setItem("id", $rootScope.ids[0]);
      setStatus(status_false);
      $state.go('app.position.edit');
  };   

  var mask = $('<div class="mask"></div>');
  var container = $('#dialog-container');
  var dialog = $('#dialog');
  var hButton = $('#clickId');
  var doIt = function(){};

  // 删除某一职位（工具栏按钮）
  $scope.removeIt = function(){
    mask.insertBefore(container);
    $("#msgP").html("你确定要执行该操作吗？");
    container.removeClass('none');
    doIt = function(){
      if($rootScope.ids.length !== 0){
        var url = app.url.position.api.delete;
        app.utils.getData(url, {"ids":$rootScope.ids}, function callback(dt){
          mask.remove();
          container.addClass('none');
          $state.reload('app.position.list');
        });
      }
    };
  };

  // 执行操作
  $rootScope.do = function(){
    doIt();
  };

  // 模态框退出
  $rootScope.cancel = function(){
    mask.remove();
    container.addClass('none');
  };  

  // 不操作返回
  $scope.return = function(){
    $rootScope.ids = [];
    setStatus(status);
    window.history.back();
  };  

  // 查看某一职位详情（工具栏按钮）
  $scope.seeDetails = function(id){
	  sessionStorage.setItem("id",id? id:$rootScope.ids[0]);
	  setStatus(status_false);
	  $state.go('app.position.details');
  };

  // 设置按钮的状态值
  $scope.setBtnStatus = function(){

    if($scope.ids.length === 0){
      $scope.single = true;
      $scope.mutiple = true;
      $scope.only = false;
    }else if($scope.ids.length === 1){
      $scope.only = false;
      $scope.single = false;
      $scope.mutiple = false;
    }else{
      $scope.only = false;
      $scope.single = true;
      $scope.mutiple = false;
    }

    hButton.trigger('click'); // 触发一次点击事件，使所以按钮的状态值生效
  };

  function setStatus(param){
    if(param){
      $scope.only = param.only,
      $scope.single = param.single,
      $scope.locked = param.locked,
      $scope.mutiple = param.mutiple
    }

    //hButton.trigger('click');
  }

});