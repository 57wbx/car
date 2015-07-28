'use strict';

app.controller('carShopDetailsController', function($http, $rootScope, $scope, $state) {
	
	if(!$rootScope.ids||!$rootScope.ids[0]){
		$state.go("app.carshop.list");
		return;
	}
	var id = $rootScope.ids[0];
    $http({
      url: "base/carShopAction!detailsCarShopById.action",
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
      $scope.details = dt.data.details;
    });
});