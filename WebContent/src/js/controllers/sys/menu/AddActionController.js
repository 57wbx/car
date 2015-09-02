app.controller("addActionController",['$scope','$modalInstance','$http','$timeout','checkUniqueService',"permItemId","aData",function($scope,$modalInstance,$http,$timeout,checkUniqueService,permItemId,aData){
	
	console.info("permItemId",permItemId);
	if(aData){
		$scope.formData = aData ;
	}
	
//	if(permItemId){
//		$http({
//			url:"sys/permItemAction!detailsPermItem.action",
//			method:"post",
//			data:{
//				id:permItemId 
//			}
//		}).then(function(resp){
//			if(resp.data.code==1){
//				$scope.formData = resp.data.details ;
//			}
//		});
//	}
	
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