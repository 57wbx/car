'use strict';
app.controller('appCaseEditController', ['$rootScope','$scope', '$http', '$state',  'uiLoad', 'JQ_CONFIG','FileUploader','previewService','sessionStorageService','hintService',
  function($rootScope,$scope, $http, $state, uiLoad, JQ_CONFIG,FileUploader,previewService,sessionStorageService,hintService) {
	
	$scope.needCacheArray = ["appCaseDataTableProperties","appCaseIdForEdit"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("appCaseIdForEdit",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("appCaseIdForEdit");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	var id = $scope.rowIds[0];
	
	
    // 获取要被编辑组织的数据
    $http({
      url: "tig/appCaseAction!detailsAppCase.action",
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
    	if(dt.data.code){
    		$scope.formData = dt.data.details ;
    	}else{
    		alert(dt.data.message);
    		$state.go($scope.state.list);
    	}
    });
    
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
    
    $scope.submit = function(){
    	$http({
    		url:"tig/appCaseAction!updateAppCase.action",
    		method:"post",
    		data:$scope.formData
    	}).then(function(resp){
    		if(resp.data.code){
    			hintService.hint({title: "成功", content: "修改成功！" });
    			$state.go($scope.state.list);
    		}else{
    			alert(resp.data.message);
    		}
    	});
    }
    
    $scope.cancel = function(){
    	$state.go($scope.state.list);
    }
    

    $scope.clearRowIds();
    
   }
]);