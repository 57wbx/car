app.controller('AbnTestController', function($scope, $http, $timeout) {
  var url = app.url.orgUnits;
  var data = null,
    failed = false;
  $http.post(url).then(function(response) {
    if (response.data.dataList && response.data.dataList.length !== 0) {
      data = response.data.dataList;
      initData();
      initTable();
    } else {
      failed = true;
    }
  }, function(x) {
    failed = true;
    console.error('服务器错误');
  });
  if (failed) return;

  var nodes = {};
  var treeData = [];

  function setNode(dt){
    if(!nodes['id' + dt['id']]){
      var node = {};
    }else{
      return nodes['id' + dt['id']];
    }
    node['label'] = dt.name || '没有名字';
    node['data'] = '没有数据';
    node['children'] = node['children'] || [];
    node['onSelect'] = apple_selected;
    if(dt['parent']){
      setParentNode(node, dt['parent']);
    }else{
      node['parent'] = null;
    }
    nodes['id' + dt['id']] = node;
    return node;
  }

  function setParentNode(node, id){
    var len = data.length;
    for(var i=0; i<len; i++){
      if(data[i]['id'] === id){
        var parentNode = setNode(data[i]);
        parentNode['children'].push(node);
      }
    }
  }

  function initData(){
    var len = data.length;
    for(var i=0; i<len; i++){
      var node = setNode(data[i]);
      if(node['parent'] === null){
        treeData.push(node);
      }
    }
  }

  var apple_selected, tree, treedata_avm, treedata_geography;
  $scope.my_tree_handler = function(branch) {
    var _ref;
    $scope.output = "You selected: " + branch.label;
    if ((_ref = branch.data) != null ? _ref.description : void 0) {
      return $scope.output += '(' + branch.data.description + ')';
    }
  };
  apple_selected = function(branch) {
    return $scope.output = "APPLE! : " + branch.label;
  };
  treedata_avm = [{
    label: '大辰科技',
    children: [{
      label: '董事会',
      data: {
        description: "man's best friend"
      }
    }, {
      label: '市场部',
      data: {
        description: "Felis catus"
      }
    }, {
      label: '行政部',
      data: {
        description: "hungry, hungry"
      }
    }, {
      label: '研发部',
      children: ['研发1部', '研发2部', '研发3部']
    }]
  }, {
    label: '浩瀚科技',
    data: {
      definition: "一段描述文字。",
      data_can_contain_anything: true
    },
    onSelect: function(branch) {
      return $scope.output = "浩瀚科技: " + branch.data.definition;
    },
    children: [{
      label: '企发部'
    }, {
      label: '研发部',
      children: [{
        label: '研发1部',
        onSelect: apple_selected
      }, {
        label: '研发2部',
        onSelect: apple_selected
      }, {
        label: '研发3部',
        onSelect: apple_selected
      }]
    }]
  }];

  treedata_avm = treeData;

  $scope.my_data = treedata_avm;
  $scope.try_changing_the_tree_data = function() {
    if ($scope.my_data === treedata_avm) {
      return $scope.my_data = treedata_geography;
    } else {
      return $scope.my_data = treedata_avm;
    }
  };
  $scope.my_tree = tree = {};
  $scope.try_async_load = function() {
    $scope.my_data = [];
    $scope.doing_async = true;
    return $timeout(function() {
      if (Math.random() < 0.5) {
        $scope.my_data = treedata_avm;
      } else {
        $scope.my_data = treedata_geography;
      }
      $scope.doing_async = false;
      return tree.expand_all();
    }, 1000);
  };

  function initTable() {
    $('#orgList').dataTable({
      "data": data,
      //"sAjaxSource": data,
      "sAjaxDataProp": "dataList",
      "aoColumns": [{
        "mDataProp": "code"
      }, {
        "mDataProp": "name"
      }, {
        "mDataProp": "longName"
      }, {
        "mDataProp": "address"
      }, {
        "mDataProp": "parent"
      }, {
        "mDataProp": "fax"
      }, {
        "mDataProp": "phoneNumber"
      }, {
        "mDataProp": "onDutyTime"
      }, {
        "mDataProp": "location"
      }, {
        "mDataProp": "zipCode"
      }, {
        "mDataProp": "createTime"
      }, {
        "mDataProp": "createUser"
      }, {
        "mDataProp": "lastModifyTime"
      }]
    });
  }
  return $scope.try_adding_a_branch = function() {
    var b;
    b = tree.get_selected_branch();
    return tree.add_branch(b, {
      label: 'New Branch',
      data: {
        something: 42,
        "else": 43
      }
    });
  };
});