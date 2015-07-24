'use strict';
app.controller('UserEdit', ['$scope', '$http', '$state',  'uiLoad', 'JQ_CONFIG','$timeout',
  function($scope, $http, $state, uiLoad, JQ_CONFIG,$timeout) {
    uiLoad.load(JQ_CONFIG.dataTable);
    $scope.formData = {};
    var id = sessionStorage.getItem("id");
    if($scope.permBtn&&$scope.permBtn.length>0&&$scope.permBtn.indexOf("modify")<0){//判断是否有修改权限
    	$("#modify").remove();
    }
    function loadRole(){
    	var url = app.url.role.api.list;
    	app.utils.getData(url,function callback(dt) {
    		$scope.roleList = dt;
    		loadData();
    	});
    }
    loadRole();
    // 获取要被编辑组织的数据
    function loadData(){
    	$http({
    		url: app.url.user.api.edit,
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
				orgId: dt.orgId,
				orgName:dt.orgName,
				personId:dt.personId,
				personName:dt.personName,
				cell: dt.cell,
				email: dt.email,
				roleId:dt.roleId,
				description: dt.description
    		};
    	});
    }
    // 提交并更新数据
    $scope.submit = function() {
      var url = app.url.user.api.modify;
      app.utils.getData(url, $scope.formData, function(dt) {
    	$("#clickId").next().removeAttr("disabled");
        $state.go('app.user.list');
        $scope.$parent.reload();
      });
    };
    $scope.return = function(){
    	$state.go('app.user.list');
    	$scope.$parent.reload();
	};
    $scope.checkNull = function(){
    	if($scope.formData.roleId&&$scope.formData.number&&$scope.formData.name&&$scope.formData.personName){
    		$(".w100.btn.btn-success").attr("disabled",false);
    	}else{
    		$(".w100.btn.btn-success").attr("disabled",true);
    	}
    };
  }
]);