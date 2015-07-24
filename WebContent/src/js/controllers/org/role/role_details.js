'use strict';

app.controller('RoleDetails', function($http, $rootScope, $scope, $state) {
	var id = sessionStorage.getItem("id");
    $http({
      url: app.url.role.api.edit,
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
      $scope.details = dt.data.editData;
    });
    $scope.return = function(){
    	$state.go('app.role.list');
    	$scope.$parent.reload();
	};
});