'use strict';

app.controller('EmployeeDetails', function($http, $rootScope, $scope, $state) {
	
	var id = sessionStorage.getItem("id");
	if(!id){
		$scope.return();
	}
    $http({
      url: app.url.employee.api.edit,
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
    	$scope.details = dt.data.editData;
    	$scope.details.gender = DataRender.Gender($scope.details.gender);
    });
    $scope.return = function(){
    	$state.go('app.employee.list');
    	$scope.$parent.reload();
	};
});