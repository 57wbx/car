app.controller("orderUpdateStateModelController",['$scope','$http','$modalInstance','orderStateService','id','orderState',function($scope,$http,$modalInstance,orderStateService,id,orderState){
	$scope.formData = {} ;
	$scope.state = orderStateService.getOrderState(orderState) ;
	
	$scope.submit = function(){
		$http({
			url:"opr/orderAction!updateOrderState.action",
			method:"POST",
			data:{
				id:id,
				orderState:orderState,
				trackMemo:$scope.formData.trackMemo
			}
		}).then(function(resp){
			if(resp.data.code == 1){
				$modalInstance.close();
			}else{
				alert(resp.data.message);
			}
			$scope.isDoing = false ;
		});
	}
	
	$scope.cancel = function() {
	    $modalInstance.dismiss('cancel');
	};
	
//结束controller	
}]);