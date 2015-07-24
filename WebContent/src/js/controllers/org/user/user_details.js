'use strict';

app.controller('UserDetails', function($http, $rootScope, $scope, $state) {
	var id = sessionStorage.getItem("id");
    $http({
      url: app.url.user.api.edit,
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
      $scope.details = dt.data.editData;
    });
    $scope.return = function(){
    	$state.go('app.user.list');
    	$scope.$parent.reload();
	};
});