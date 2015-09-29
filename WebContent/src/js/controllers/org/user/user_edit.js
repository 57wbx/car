'use strict';
app.controller('UserEdit', ['$scope', '$http', '$state','sessionStorageService','hintService',
  function($scope, $http, $state,sessionStorageService,hintService) {
	
	$scope.API.isListPage = false ;
	
	$scope.needCacheArray = ["userListDataTableProperties","userIdForEdit"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("userIdForEdit",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("userIdForEdit");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	var id = $scope.rowIds[0];
	
	$scope.formData = {};
	
	 function loadRole(){
	    	var url = app.url.role.api.list;
	    	app.utils.getData(url,function callback(dt) {
	    		$scope.roleList = dt;
	    	});
	    }
	loadRole();
	
	$http({
		url:"basedata/userAction!detailsUser.action",
		method:"POST",
		data:{
			id:id
		}
	}).then(function(resp){
		if(resp.data.code==1){
			$scope.formData = resp.data.details ;
			$scope.formData.password = 123456;
			checkUnquie();
		}else{
			alert($scope.data.message);
			$state.go($scope.state.list);
		}
	});
	
	
	function checkUnquie(){
		$scope.$watch("formData.number",function(val){
			if($scope.formData){
				$scope.API.checkCodeUnique(id,val,unquieNumber);
			}
	    });
	}
    function unquieNumber(resp){
    	if(resp.data.code == 1){
    		//不重复
    		$scope.form.number.$setValidity("custom_pattern",true);
    	}else{
    		//重复
    		$scope.form.number.$setValidity("custom_pattern",false);
    	}
    }
    
    $scope.$watch("formData.updatePassWord",function(val){
		if(!val){
			$scope.formData.password = 123456;
		}
    });
    
    //修改
    $scope.submit = function(){
    	if(!$scope.formData.updatePassWord == 1){
    		$scope.formData.password = undefined ;
    	}
    	$http({
    		url:"basedata/userAction!updateUser.action",
    		method:"POST",
    		data:$scope.formData
    	}).then(function(resp){
    		if(resp.data.code==1){
    			hintService.hint({title: "成功", content: "保存成功！" });
    			$state.go($scope.state.list);
    		}else{
    			alert(resp.data.message);
    		}
    		$scope.isDoing = false ;
    	});
    }
	
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
	
	$scope.clearRowIds();
	
}]);