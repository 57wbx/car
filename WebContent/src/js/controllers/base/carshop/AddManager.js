app.controller("addManager",['$scope','$modalInstance','$http','$timeout','carShopId','orgId','userId','checkUniqueService',function($scope,$modalInstance,$http,$timeout,carShopId,orgId,userId,checkUniqueService){
	
	var url = app.url.role.api.list;
	app.utils.getData(url,function callback(dt) {
		$scope.roleList = dt;
	});
	
	$scope.formData = {};//初始化form表单的内容
	
	if(userId){
		$http({
			url:"basedata/userAction!getDataById.action",
			method:"get",
			params:{
				id:userId
			}
		}).then(function(resp){
			$scope.formData.name = resp.data.editData.name;
			$scope.formData.id =  resp.data.editData.id;
			$scope.formData.roleId =  resp.data.editData.roleId;
			$scope.formData.number =  resp.data.editData.number;
			$scope.formData.password =  resp.data.editData.password;
		});
	}
	
	/**
	 * 检测所需要保存的number   userCode是否唯一
	 */
	$scope.isUnique = true;
	$scope.notUniqueMessage = "已存在";
	$scope.checkUserCodeUnique = function(id,number){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkUserCodeUnique(id,number).then(function(resp){
			if(resp.data.code==1){
				$scope.isUnique = true;
			}else{
				$scope.isUnique = false;
			}
		});
		
		}
	
	$timeout(function(){
		$("#number").change(function(e){
			if($scope.addManagerForm.number.$invalid){
				$("#number").css("border-color","red");
			}else{
				$("#number").removeAttr("style");
			}
			if($scope.addManagerForm.number.$valid){
				$scope.checkUserCodeUnique($scope.formData.id,$(e.target).val());
			}
		});	
	},100);
	
	
	
	
	
	var submit = function(){
		$scope.formData.carShopId = carShopId ;
		$scope.formData.orgId = orgId;
		if($scope.formData.id){
			return $http({
				url:"basedata/userAction!updateCarShopUser.action",
				method:"post",
				data:$scope.formData
			});
		}else{
			return $http({
				url:"basedata/userAction!saveCarShopUesr.action",
				method:"post",
				data:$scope.formData
			});
		}
	}
	
	
	 $scope.ok = function() {
		submit().then(function(resp){
			if(resp.data.code==1){
				 $modalInstance.close($scope.formData.name);
			}else{
				alert("保存失败");
			}
		}) 
	 };

	$scope.cancel = function() {
		    $modalInstance.dismiss('cancel');
	};
	
	//
}]);