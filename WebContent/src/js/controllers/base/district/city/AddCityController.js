/**
*新增或者修改城市的controller
*/
app.controller("addCityController",['$scope','$modalInstance','$http','$timeout',"provinceName","provinceId","cityId","isDetails",
                                    function($scope,$modalInstance,$http,$timeout,provinceName,provinceId,cityId,isDetails){
	
	$scope.formData = {};
	$scope.formData.provinceName = provinceName;
	$scope.formData.provinceId = provinceId ;
	
	var submitUrl ;//新增操作和修改操作提交的地址一样，后台通过cityid来判断到底是新增还是修改
	
	if(cityId){//为修改操作
		/**
		 * 从服务器获取数据
		 */
		$http({
			url:"base/baseCityAction!detailsBaseCity.action",
			method:"get",
			params:{
				id:cityId
			}
		}).then(function(resp){
			if(resp.data.code==1){
				$scope.formData = resp.data.details;
				$scope.formData.provinceId = provinceId ;
				$scope.formData.provinceName = provinceName;
				$scope.formData.baseProvince = undefined;
				$scope.formData.baseAreas = undefined ;
			}
		});
	}
	
	if(isDetails){
		$timeout(function(){
			$("#addCityForm input").attr("disabled","disabled");
			$("#addCityForm select").attr("disabled","disabled");
			$("#addCityForm textarea").attr("disabled","disabled");
			$("#citySubmit").addClass("none");
		},30);
		//所有的将不能修改 隐藏确定按钮
	}
	
	
	/**
	 * 新增或者修改操作
	 */
	var submit = function(){
		return $http({
			url:"base/baseCityAction!addOrUpdateCity.action",
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