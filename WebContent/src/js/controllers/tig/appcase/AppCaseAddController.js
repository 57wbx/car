app.controller("appCaseAddController",['$scope','$state','$http','checkUniqueService','FileUploader','previewService','hintService','sessionStorageService',function($scope,$state,$http,checkUniqueService,FileUploader,previewService,hintService,sessionStorageService){
	
	
	$scope.formData = {};
	
	//初始化选中的数据
	$scope.setCanEdit(false);
	$scope.clearRowIds();
	
	$scope.needCacheArray = ["appCaseDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	
    $('#uploadTime').focus(
    		function(){
	    		var optionSet = {
	    				showDropdowns:true,
						singleDatePicker : true,
						timePicker : true,
						format : 'YYYY-MM-DD hh:mm:ss'
						
					};
	    		$('#uploadTime').daterangepicker(optionSet).on('apply.daterangepicker', function(ev){
	    			$scope.formData.uploadTime=$('#uploadTime').val();
	    		});
    		}
	);
	

	
	/**
	 * 点击保存操作
	 */
	$scope.submit = function(){
		$http({
			url:"tig/appCaseAction!addAppCase.action",
			method:'post',
			data : $scope.formData
		}).then(function(resp){
			var code = resp.data.code ;
			if(code){//代表保存成功
				hintService.hint({title: "成功", content: "保存成功！" });
				$state.go($scope.state.list);
			}else{//代表保存失败
				alert(resp.data.message);
			}
		});
	}
	
	/**
	 * 取消按钮的操作，直接跳转到列表页面上
	 */
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
}]);