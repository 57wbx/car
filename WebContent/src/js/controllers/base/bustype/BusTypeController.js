'use strict';

app.controller('busTypeController', function($rootScope, $scope, $state, $timeout) {
//  var url = app.url.org.api.list; // 后台API路径
	
	var url = "base/busTypeAction!listBusType.action";
	var data = null;
	
	//三个按钮的控制规则
	$scope.addSign = true ;
	$scope.editSign = true ;
	$scope.deleteSign = false ;

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
    console.info($scope.org_tree);
    if(!$rootScope.item_id){
      $timeout(function(){
        // 默认选中第一个节点
        $scope.org_tree.select_first_branch();
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
    $state.go('app.bustype.edit');
    //每次点击将初始化修改表单中的数据
    $rootScope.initBusTypeEditControllerDataObject.editable = false;
    $rootScope.initBusTypeEditControllerDataObject.initDetails();
    //通过判断是否有子节点 来确实是否可以进行删除操作
    if(branch.children[0]==null){
    	$scope.deleteSign = true ;
    }else{
    	$scope.deleteSign = false ;
    }
    
  };

  
  $scope.addBrotherBusType = function(){
	  //新增的时候将上级菜单加入到页面中
	  $rootScope.initBusTypeEditControllerDataObject.addBusType($rootScope.busTypePatentId);
	  $rootScope.initBusTypeEditControllerDataObject.editable = true;
	  $rootScope.initBusTypeEditControllerDataObject.isNew = true;
	  
  }
  
  $scope.addChildBusType = function(){
	  $rootScope.initBusTypeEditControllerDataObject.addBusType($rootScope.item_id);
	  $rootScope.initBusTypeEditControllerDataObject.editable = true;
	  $rootScope.initBusTypeEditControllerDataObject.isNew = true;
  }
  
  $scope.editBusType = function(){
	  $rootScope.initBusTypeEditControllerDataObject.editBusType("修改详细信息");
	  $rootScope.initBusTypeEditControllerDataObject.editable = true;
	  $rootScope.initBusTypeEditControllerDataObject.isNew = false;
  }
  $scope.deleteBusType = function(){
	  mask.insertBefore(container);
	  message_body.html("您确认要删除该记录吗？");
	  container.removeClass('none');
	  doIt = function(){
		  
		  
		  $rootScope.initBusTypeEditControllerDataObject.deleteBusType();
		  
		  container.addClass('none');
		  message_body.html(old_message_body);//将提示语变换为原来的语句
	  }
  }
  
  
  
  $scope.click = function(){};

  var status_false = {
    single : true,
    locked : true,
    mutiple : true
  };

  var status = {
    single : true,
    locked : true,
    mutiple : true
  };

  // 添加组织（工具栏按钮）
  $scope.addUnit = function(){
    if($rootScope.ids.length === 0){
      $rootScope.details = {};
    }
    setStatus(status_false);
    $state.go('app.carshop.add');
  };

  // 编辑某一组织（工具栏按钮）
  $scope.editIt = function(){
    if($rootScope.obj['id']){
      $rootScope.details = $rootScope.obj;
      setStatus(status_false);
      $state.go('app.carshop.edit');
    }
  };   

  var mask = $('<div class="mask"></div>');
  var container = $('#dialog-container');
  var message_body = $("#dialog_containner_message_body");
  var old_message_body = message_body.html();
  var dialog = $('#dialog');
  var hButton = $('#clickId');
  var doIt = function(){};

  // 删除请假记录（工具栏按钮）
  $scope.removeIt = function(){
    mask.insertBefore(container);
    container.removeClass('none');
    doIt = function(){
      if($rootScope.ids.length !== 0){
        var url = "base/carShopAction!deleteCarShop.action";
        app.utils.getData(url, {"ids":$rootScope.ids}, function callback(dt){
          mask.remove();
          container.addClass('none');
          $state.reload('app.carshop.list');
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
    message_body.html(old_message_body);
  };  

  // 不操作返回
  $scope.return = function(){
    $rootScope.ids = [];
    setStatus(status);
    window.history.back();
  };  

  // 查看某一组织详情（工具栏按钮）
  $scope.seeDetails = function(id){
    setStatus(status_false);
    $rootScope.ids=[];
    $rootScope.ids.push(id);
    $state.go('app.carshop.details');
//    console.info($scope.ids);
  };

  // 设置按钮的状态值
  $scope.setBtnStatus = function(){

    if($scope.ids.length === 0){
      $scope.single = true;
      $scope.locked = true;
      $scope.mutiple = true;
    }else if($scope.ids.length === 1){
      $scope.single = false;
      $scope.locked = false;
      $scope.mutiple = false;
    }else{
      $scope.single = true;
      $scope.locked = true;
      $scope.mutiple = false;
    }


    hButton.trigger('click'); // 触发一次点击事件，使所以按钮的状态值生效
  };

  function setStatus(param){
    if(param){
      $scope.single = param.single,
      $scope.locked = param.locked,
      $scope.mutiple = param.mutiple
    }
  }
});