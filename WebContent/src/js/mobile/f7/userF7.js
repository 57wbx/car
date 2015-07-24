'use strict';

app.controller('UserF7Controller', function($rootScope, $scope, $state, $timeout) {
	$scope.init = function(){
		var url = app.url.f7.userf7;
		var condition = $("#condition").val();
		app.utils.getData(url,{"name":condition,"number":condition}, function(dt) {
			$scope.userData=dt;
	    });
	};
	$scope.init();
	$scope.submit = function(){
		$state.go('expenseBill');
	};
	$scope.return = function(){
		$state.go('expenseBill');
	};
	$scope.queryUser = function(){
		$scope.init();
	};
	$scope.updateSelect = function(obj){
		$(".list-group-item.ng-binding.ng-scope").css("background-color","#FFFFFF");
		$rootScope.userF7Id = obj.user.id;
		$rootScope.userF7Name = obj.user.name;
		$("#liId"+obj.$index).css("background-color","#abb9d3");
	};
	
	
});
