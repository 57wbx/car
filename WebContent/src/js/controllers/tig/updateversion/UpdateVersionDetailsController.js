app.controller("updateVersionDetailsController",['$scope','$state','$http','checkUniqueService',function($scope,$state,$http,checkUniqueService){
	
//	$scope.formData.fitemID  新增开始的时候需要从服务器中下载下来，以便于子项的操作
	
	$scope.formData = {};
	
	console.info($scope.form);
	
	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
		$state.go("app.updateversion.list");//返回到列表界面
	}
	
	/**
	 * 初始化信息
	 */
	$http({
		url:"tig/updateVersionAction!detailsUpdateVersion.action",
		method:"get",
		params:{
			id:$scope.rowIds[0]
		}
	}).then(function(resp){
		var code = resp.data.code ;
		if(code == 1){
			$scope.formData = resp.data.updateVersion;
			$scope.formData.uploadTimeStr = resp.data.updateVersion.uploadTime ;
		}else{
			$state.go("app.updateversion.list");
		}
	});
	
	
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
	
	
	
	
	function setAllDisabled(){
		$("form[name=form] input").attr("disabled",true);
		$("form[name=form] select").attr("disabled","disabled");
		$("form[name=form] textarea").attr("disabled","disabled");
	}
	setAllDisabled();
	
	/**
	 * 取消按钮的操作，直接跳转到列表页面上
	 */
	$scope.cancel = function(){
		$state.go("app.updateversion.list");
	}
	
	
	
}]);