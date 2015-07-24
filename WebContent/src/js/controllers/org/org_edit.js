'use strict';
app.controller('OrgEdit', ['$scope', '$http', '$state',  'uiLoad', 'JQ_CONFIG',
  function($scope, $http, $state, uiLoad, JQ_CONFIG) {
    var id = sessionStorage.getItem("id");
    if($scope.permBtn&&$scope.permBtn.length>0&&$scope.permBtn.indexOf("modify")<0){//判断是否有修改权限
    	$("#modify").remove();
    }
    // 获取要被编辑组织的数据
    $http({
      url: app.url.org.api.edit,
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
      dt = dt.data.editData;
      $scope.formData = {
        id: dt.id,
        code: dt.code,
        name: dt.name,
        simpleName: dt.simpleName,
        phoneNumber: dt.phoneNumber,
        fax: dt.fax,
        adminAddress: dt.adminAddress,
        parent:dt.parent,
        unitLayer:dt.unitLayer,
        superName:dt.parentName
      };

    });
    // 提交并更新数据
    $scope.submit = function() {
      var url = app.url.org.api.modify;
      app.utils.getData(url, $scope.formData, function(dt) {
    	  $state.go('app.org.list'); 
    	  $scope.$parent.reload();
      });
    };
    $scope.return = function(){
    	$state.go('app.org.list');
    	$scope.$parent.return();
	};

  }
]);