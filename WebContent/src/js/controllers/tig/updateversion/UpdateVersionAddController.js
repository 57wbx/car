app.controller("updateVersionAddController",['$scope','$state','$http','checkUniqueService',function($scope,$state,$http,checkUniqueService){
	
//	$scope.formData.fitemID  新增开始的时候需要从服务器中下载下来，以便于子项的操作
	
	$scope.formData = {};
	
	//初始化选中的数据
	$scope.setCanEdit(false);
	$scope.clearRowIds();
	
	
	
	
	/**
	 * 检查内部版本号是否存在
	 */
	$scope.versionCodeIsUnique = true;
	$scope.notUniqueMessage = "已存在";
	$scope.checkVersionCodeIsUnique = function(id,versionCode){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkVersionCodeUnique(id,versionCode).then(function(resp){
			if(resp.data.code==1){
				$scope.versionCodeIsUnique = true;
			}else{
				$scope.versionCodeIsUnique = false;
			}
		});
	}
	$("#versionCode").change(function(e){
		if($scope.form.versionCode.$valid){
			$scope.checkVersionCodeIsUnique($scope.formData.id,$(e.target).val());
		}
	});
	
	/**
	 * 检查外部版本号是否存在
	 */
	$scope.versionNameIsUnique = true;
	$scope.checkVesionNameIsUnique = function(id,versionName){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkVersionNameUnique(id,versionName).then(function(resp){
			if(resp.data.code==1){
				$scope.versionNameIsUnique = true;
			}else{
				$scope.versionNameIsUnique = false;
			}
		});
	}
	$("#versionName").change(function(e){
		if($scope.form.versionName.$valid){
			$scope.checkVesionNameIsUnique($scope.formData.id,$(e.target).val());
		}
	});
	
	
	//初始化时间控件
	$('#uploadTimeStr').focus(
	    		function(){
		    		var optionSet = {
							singleDatePicker : true,
							timePicker : true,
							format : 'YYYY-MM-DD HH:mm'
						};
		    		$('#uploadTimeStr').daterangepicker(optionSet).on('apply.daterangepicker', function(ev){
		    			$scope.formData.uploadTimeStr=$('#uploadTimeStr').val();
		    		});
	    		}
	);
	
	
	
	
	
	/**
	 * 点击保存操作
	 */
	$scope.submit = function(){
		$http({
			url:"tig/updateVersionAction!addUpdateVersion.action",
			method:'post',
			data : $scope.formData
		}).then(function(resp){
			var code = resp.data.code ;
			if(code == 1){//代表保存成功
				$state.go("app.updateversion.list");
			}else{//代表保存失败
				alert("保存失败");
			}
		},function(resp){
			alert("保存出错");
		});
	}
	
	/**
	 * 取消按钮的操作，直接跳转到列表页面上
	 */
	$scope.cancel = function(){
		$state.go("app.updateversion.list");
	}
	
	
	
}]);