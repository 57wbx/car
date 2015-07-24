'use strict';

app.controller('OrgDetails', function($http, $rootScope, $scope, $state) {
	var id = sessionStorage.getItem("id");
    $http({
      url: app.url.org.api.edit,
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
      $scope.details = dt.data.editData;
    });
    $scope.return = function(){
    	$state.go('app.org.list');
    	$scope.$parent.return();
	};
});