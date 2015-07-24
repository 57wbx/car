'use strict';

app.controller('UserController', function($rootScope, $scope, $state, $timeout) {
  var allBtn =["add","delete","modify"];//btn id array
  $scope.permBtn = app.utils.rolePerm(app.url.user.api.menuPerm,"004",allBtn);//已有权限项数组
  var url = app.url.org.api.list; // 后台API路径
  var data = null;
  app.utils.getData(url, function callback(dt){
    data = dt;
    initData();
    $scope.loading = false;
    $scope.loading_sub = false;
  });
  $scope.reload = function(){
	  $timeout(function(){
        $scope.init();
      }, 200);
  };
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
    node['FLongNumber'] =dt.FLongNumber;
    node['unitLayer'] = dt.unitLayer;
    if (dt['parent']) {
      setParentNode(node, dt['parent']);  // 若存在父节点，则先构造父节点
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
    console.log(treeData);
    // 列表树数据传值
    $scope.tree_data = treeData;
	$timeout(function(){
		// 默认选中第一个节点
		//$scope.org_tree.select_first_branch();
		try {
			$scope.init();
		} catch (e) {
		}
	}, 200);
  }

  var thisBranch;
  // 选择列表树中的一项
  var item_selected = function(branch) {
	  thisBranch = branch;
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
    $state.go('app.user.list');
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
  // 添加用户（工具栏按钮）
  $scope.addUnit = function(){
	  if(!thisBranch||!thisBranch.unitLayer||thisBranch.unitLayer!=3){
		  mask.insertBefore(container);
		  $("#msgP").html("只能在部门下添加用户!");
		  container.removeClass('none');
		  doIt = function(){
			  $rootScope.cancel();
		  };
		  return;
	  }
	  sessionStorage.setItem("FLongNumber", thisBranch.FLongNumber);
	  sessionStorage.setItem("treeId", thisBranch.data);
	  sessionStorage.setItem("treeName", thisBranch.label);
	  setStatus(status_false);
	  $state.go('app.user.add');
  };

  // 编辑某一用户（工具栏按钮）
  $scope.editIt = function(){
	  sessionStorage.setItem("id", $rootScope.ids[0]);
      setStatus(status_false);
      $state.go('app.user.edit');
  };   

  var mask = $('<div class="mask"></div>');
  var container = $('#dialog-container');
  var dialog = $('#dialog');
  var hButton = $('#clickId');
  var doIt = function(){};

  // 冻结、解冻某一用户（工具栏按钮）
  $scope.freeze = function(){
    mask.insertBefore(container);
    $("#msgP").html("你确定要执行该操作吗？");
    container.removeClass('none');
    doIt = function(){
      var isFreezeUrl = $scope.obj.locked?app.url.org.api.unfreeze:app.url.org.api.freeze
      app.utils.getData(isFreezeUrl, {id: $rootScope.ids[0]}, function callback(dt){
        mask.addClass('none');
        container.addClass('none');
        $rootScope.ids = [];
        $scope.init();
        $scope.setBtnStatus();
      });
    };
  };

  // 删除某一用户（工具栏按钮）
  $scope.removeIt = function(){
    mask.insertBefore(container);
    $("#msgP").html("你确定要执行该操作吗？");
    container.removeClass('none');
    doIt = function(){
      if($rootScope.ids.length !== 0){
        var url = app.url.user.api.delete;
        app.utils.getData(url, {"ids":$rootScope.ids}, function callback(dt){
          mask.remove();
          container.addClass('none');
          $state.reload('app.user.list');
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

  // 查看某一用户详情（工具栏按钮）
  $scope.seeDetails = function(id){
	  sessionStorage.setItem("id",id? id:$rootScope.ids[0]);
	  setStatus(status_false);
	  $state.go('app.user.details');
  };

  // 设置按钮的状态值
  $scope.setBtnStatus = function(){
    if($scope.ids.length === 0){
      $scope.single = true;
      $scope.locked = true;
      $scope.mutiple = true;
      $scope.only = false;
    }else if($scope.ids.length === 1){
      $scope.only = false;
      $scope.single = false;
      $scope.locked = false;
      $scope.mutiple = false;
    }else{
      $scope.only = false;
      $scope.single = true;
      $scope.locked = true;
      $scope.mutiple = false;
    }

    if($scope.obj&&!$scope.obj.locked){
      if(!$scope.single) {
        $('button .fa-lock').next('span').html('冻结');
      }
    } else {
      if(!$scope.single){
        $('button .fa-lock').next('span').html('解冻');
      }
    }

/*    status = {
      only : $scope.only,
      single : $scope.single,
      locked : $scope.locked,
      mutiple : $scope.mutiple
    };*/

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