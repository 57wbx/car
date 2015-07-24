'use strict';
app.controller('PositionEdit', ['$scope', '$http', '$state',  'uiLoad', 'JQ_CONFIG',
  function($scope, $http, $state, uiLoad, JQ_CONFIG) {
	var id = sessionStorage.getItem("id");
    if($scope.permBtn&&$scope.permBtn.length>0&&$scope.permBtn.indexOf("modify")<0){//判断是否有修改权限
    	$("#modify").remove();
    }
    // 获取要被编辑组织的数据
    $http({
      url: app.url.position.api.edit,
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
        simpleName: dt.simpleName,
        description:dt.description,
        org:dt.org,
        superName:dt.orgName
      };
    });
    // 提交并更新数据
    $scope.submit = function() {
      var url = app.url.position.api.modify;
      $scope.formData.unitLayer = $("#orgType").val();
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