app.controller("mapModelForAddController",["$scope","$timeout","$modalInstance",'mapX','mapY',function($scope,$timeout,$modalInstance,mapX,mapY){
	
	$scope.formData = {} ;
	if(mapX){
		$scope.formData.lng = mapX ;
	}
	if(mapY){
		$scope.formData.lat = mapY ;
	}
	
	$scope.refreshData = function(){
		$scope.formData.lng = $("#model_mapX").val();
		$scope.formData.lat = $("#model_mapY").val();
		$scope.$evalAsync();
	}
	
	$scope.submit = function(){
		 $modalInstance.close($scope.formData);
	}
	
	$scope.cancel = function() {
	    $modalInstance.dismiss('cancel');
	};
	
}]);