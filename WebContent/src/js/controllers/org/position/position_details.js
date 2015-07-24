'use strict';

app.controller('PositionDetails', function($http, $rootScope, $scope, $state) {
	var id = sessionStorage.getItem("id");
    $http({
      url: app.url.position.api.edit,
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
      $scope.details = dt.data.editData;
    });
    $scope.return = function(){
    	$state.go('app.position.list');
    	$scope.$parent.reload();
	};
});