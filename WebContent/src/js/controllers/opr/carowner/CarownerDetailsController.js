'use strict';
app.controller('carownerDetailsController', ['$rootScope','$scope', '$http', '$state',  'uiLoad', 'JQ_CONFIG','FileUploader','previewService','sessionStorageService','hintService',
  function($rootScope,$scope, $http, $state, uiLoad, JQ_CONFIG,FileUploader,previewService,sessionStorageService,hintService) {
	
	$scope.needCacheArray = ["carownerDataTableProperties","carownerIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("carownerIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("carownerIdForDetails");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	var id = $scope.rowIds[0];
	
	
	$("form[name=form] input").attr("disabled",true);
	$("form[name=form] select").attr("disabled",true);
	$("form[name=form] textarea").attr("disabled",true);
	
    // 获取要被编辑组织的数据
    $http({
      url: "base/memberAction!detailsCarowner.action",
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
    	if(dt.data.code){
    		$scope.formData = dt.data.details ;
	        $scope.formData.IDCARDURLName = dt.data.details.IDCARDURL?"请点击&nbsp;<a  style='color: blue; text-decoration: underline;'  onClick='$(\"#IDCARDURL\").click();'>预览！</a>":undefined;
	        $scope.formData.birthdayStr = dt.data.details.birthday.substring(0,10) ;//2015-08-12    十位
	        $scope.formData.birthdat = dt.data.details.birthday.substring(0,10);
    	}else{
    		alert(dt.data.message);
    		$state.go($scope.state.list);
    	}
    });
    
    /**
     * 预览图片功能
     */
    var previewImg = $scope.previewImg = function(name){
    	 previewService.preview($scope.formData[name]);
    }
    
    
    $('#birthday').focus(
    		function(){
	    		var optionSet = {
	    				showDropdowns:true,
						singleDatePicker : true,
//						timePicker : true,
						format : 'YYYY-MM-DD'
						
					};
	    		$('#birthday').daterangepicker(optionSet).on('apply.daterangepicker', function(ev){
	    			$scope.formData.birthday=$('#birthday').val();
	    		});
    		}
	);
    
    $scope.submit = function(){
    	$http({
    		url:"base/memberAction!updateWorker.action",
    		method:"post",
    		data:$scope.formData
    	}).then(function(resp){
    		if(resp.data.code==1){
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