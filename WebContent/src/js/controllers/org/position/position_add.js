'use strict';
app.controller('PositionAdd', ['$scope', '$state', 'uiLoad','JQ_CONFIG',
  function($scope, $state, uiLoad, JQ_CONFIG) {
    $scope.formData = {};
    $scope.formData.org = sessionStorage.getItem("treeId");
    $scope.formData.superName = sessionStorage.getItem("treeName");
    if($scope.permBtn&&$scope.permBtn.length>0&&$scope.permBtn.indexOf("add")<0){//判断是否有修改权限
    	$("#add").remove();
    }
    // 提交并添加数据
    $scope.submit = function() {
      var url = app.url.position.api.save;
      app.utils.getData(url, $scope.formData, function(dt) {
    	$state.go('app.position.list');
    	$scope.$parent.reload();
      });
    };
    $scope.return = function(){
    	$state.go('app.position.list');
    	$scope.$parent.reload();
	};
    
  }
]);