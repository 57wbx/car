'use strict';

app.controller('carShopDetailsController', function($http, $rootScope, $scope, $state) {
	var id = $scope.$parent.ids[0];
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