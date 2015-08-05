app.controller("manageItemImgDetailsController",['$scope','$modalInstance','$http','imgId','content','fileType',function($scope,$modalInstance,$http,imgId,content,fileType){
	
	$scope.formData = {};
	$scope.formData.id = imgId ;
	$scope.formData.content = content ;
	$scope.formData.fileType = fileType ;
	
	
	var submit = function(){
		return $http({
			url:"base/busItemImgAction!saveOrUpdateBusItemImgDetails.action",
			method:"post",
			data:$scope.formData
		});
	}
	
	$scope.ok = function(){
		submit().then(function(resp){
			if(resp.data.code){//成功的操作
				 $modalInstance.close($scope.formData);
			}else{
				alert(resp.data.message);
			}
		});
	}
	
	
	
	$scope.cancel = function() {
	    $modalInstance.dismiss('cancel');
	};
	
	//关闭窗口
}]);