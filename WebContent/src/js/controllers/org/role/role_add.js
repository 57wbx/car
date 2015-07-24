'use strict';
app.controller('RoleAdd', ['$scope', '$state', 'uiLoad','JQ_CONFIG',
  function($scope, $state, uiLoad, JQ_CONFIG) {
    $scope.formData = {};
    $scope.formData.orgId = sessionStorage.getItem("treeId");
    $scope.formData.superName = sessionStorage.getItem("treeName");
    if($scope.permBtn&&$scope.permBtn.length>0&&$scope.permBtn.indexOf("add")<0){//判断是否有权限
    	$("#add").remove();
    }
    // 提交并添加数据
    $scope.submit = function() {
      var url = app.url.role.api.save;
      app.utils.getData(url, $scope.formData, function(dt) {
        $state.go('app.role.list');
        $scope.$parent.reload();
      });
    };
    $scope.return = function(){
    	$state.go('app.role.list');
    	$scope.$parent.reload();
	};
    $scope.checkNull = function(){
    	if($scope.formData.number&&$scope.formData.name){
    		$(".w100.btn.btn-success").attr("disabled",false);
    	}else{
    		$(".w100.btn.btn-success").attr("disabled",true);
    	}
    };
  }
]);