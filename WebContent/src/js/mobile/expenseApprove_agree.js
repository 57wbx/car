'use strict';
//费用报销审批流程
app.controller('ExpenseApproveAgreeController', function($location,$rootScope, $scope, $state, $timeout) {
	$("#myDiv").parent().css("background-color","white");
	$scope.formData = {};
	$scope.formData.billId = $scope.id;
	console.log($scope.formData.billId);
	
	$scope.isEnd = function(){
		
	};
	//下一步处理人
	$scope.showAuditor = function(){ 
		$rootScope.details=$scope.formData;
		$state.go('userf7');
	};
});