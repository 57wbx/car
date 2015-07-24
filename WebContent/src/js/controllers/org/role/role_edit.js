'use strict';
app.controller('RoleEdit', ['$scope', '$http', '$state',  'uiLoad',
  function($scope, $http, $state, uiLoad) {
	var id = sessionStorage.getItem("id");
	if($scope.permBtn&&$scope.permBtn.length>0&&$scope.permBtn.indexOf("modify")<0){//判断是否有权限
    	$("#modify").remove();
    }
    // 获取要被编辑组织的数据
    $http({
      url: app.url.role.api.edit,
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
      dt = dt.data.editData;
      $scope.formData = {
        id: dt.id,
        number: dt.number,
        name: dt.name,
        orgId:dt.orgId,
        superName:dt.orgName,
        description:dt.description
      };
    });
    // 提交并更新数据
    $scope.submit = function() {
      var url = app.url.role.api.modify;
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