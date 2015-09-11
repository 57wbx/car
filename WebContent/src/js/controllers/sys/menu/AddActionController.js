app.controller("addActionController",['$scope','$modalInstance','$http','$timeout','checkUniqueService',"permItemId","aData",function($scope,$modalInstance,$http,$timeout,checkUniqueService,permItemId,aData){
	
	$scope.formData = {};
	if(aData){
		$scope.formData = aData ;
	}else{
		$scope.formData.fType = 0;
		$scope.formData.useState = 0;
	}
	
	 $scope.ok = function() {
		 	$scope.formData.useState = parseInt($scope.formData.useState);
		 	$scope.formData.fType = parseInt($scope.formData.fType);
		 	$modalInstance.close($scope.formData);
	 };

	$scope.cancel = function() {
		    $modalInstance.dismiss('cancel');
	};
	
	//
}]);