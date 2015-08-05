app.controller("busAtomDetailsController",["$scope","$state","$http",function($scope,$state,$http){
	
	
	$scope.treeAPI.hiddenBusTypeTree();
	
	$scope.formData = {};
	var id ;//所查看数据的id
	//判断时候有父controller中是否有ids 并获取中第一个id
	if($scope.busAtomIds.length!=0){
		id = $scope.busAtomIds[0];
		$scope.clearBusAtomIds();//清除数据
	}else{
		$state.go($scope.state.list);
	}
	
	
	//从服务端获取数据
	$http({
		url:'base/busAtomAction!detailsBusAtomByFid.action',
		method:'get',
		params:{
			fid : id
		}
	}).then(function(resp){
		var code = resp.data.code ;
		if(code == 1){//代表成功
			$scope.formData =  resp.data.details ;
			$scope.formData.autoPartAllName = resp.data.details.autoPart.partName +"" +
					"_"+resp.data.details.autoPart.brandName+"_"+resp.data.details.autoPart.spec+"_" +
							""+resp.data.details.autoPart.model;
			$scope.formData.itemCode = resp.data.details.busItem.itemCode ;
			$scope.formData.itemName = resp.data.details.busItem.itemName ;
		}
	});
	
	
	/**
	 * 返回的方法
	 */
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
	
	//方法结束
}]);