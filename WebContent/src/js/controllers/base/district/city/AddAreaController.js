/**
*新增或者修改城市的controller
*/
app.controller("addAreaController",['$scope','$modalInstance','$http','$timeout',"parentName","parentId","parentIsCity","areaId",'isDetails',
                                    function($scope,$modalInstance,$http,$timeout,parentName,parentId,parentIsCity,areaId,isDetails){
	
	$scope.formData = {};
	$scope.formData.parentName = parentName;
	$scope.formData.parentId = parentId ;
	
	var submitUrl ;//新增操作和修改操作提交的地址一样，后台通过cityid来判断到底是新增还是修改
	if(parentIsCity){
		//该地域是属于城市下面的地域
		submitUrl = "base/baseAreaAction!addOrUpdateBaseAreaWithCityId.action";
	}else{
		//该地域是属于地域下面的地域
		submitUrl = "base/baseAreaAction!addOrUpdateBaseAreaWithAreaId.action";
	}
	
	if(areaId){//为修改操作
		/**
		 * 从服务器获取数据
		 */
		$http({
			url:"base/baseAreaAction!detailsBaseArea.action",
			method:"get",
			params:{
				id:areaId
			}
		}).then(function(resp){
			if(resp.data.code==1){
				$scope.formData = resp.data.details;
				$scope.formData.parentName = parentName;
				$scope.formData.parentId = parentId ;
				$scope.formData.baseAreas = undefined;
				$scope.formData.baseCity = undefined;
			}
		});
	}
	
	if(isDetails){
		$timeout(function(){
			$("#addAreaForm input").attr("disabled","disabled");
			$("#areaSubmit").addClass("none");
		},30);
		//所有的将不能修改 隐藏确定按钮
	}
	
	
	/**
	 * 新增或者修改操作
	 */
	var submit = function(){
		return $http({
			url:submitUrl,
			method:"post",
			data:$scope.formData
		})
	}
	
	
	/**
	 * 确定操作
	 */
	$scope.ok = function(){
		submit().then(function(resp){
			if(resp.data.code==1){
				$modalInstance.close();
			}else{
				alert("保存失败");
			}
		});
	}
	
	/**
	 * 关闭取消操作
	 */
	$scope.cancel = function(){
		$modalInstance.dismiss('cancel');
	}
	
	
	//结束controller
}]);