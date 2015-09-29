app.controller('UserAdd', ['$scope', '$state','$http','hintService',
  function($scope, $state,$http,hintService) {
	
	$scope.API.isListPage = false ;
	
    function loadRole(){
    	var url = app.url.role.api.list;
    	app.utils.getData(url,function callback(dt) {
    		$scope.roleList = dt;
    	});
    }
    loadRole();
    // 提交并添加数据
    $scope.submit = function() {
    	if($scope.selectData.unitLayer == 1 || $scope.selectData.unitLayer == 2 || $scope.selectData.unitLayer == 3){//判断新增的用户是什么组织下面的，进行数据处理
    		//部门下面
    		$scope.formData.orgId = $scope.selectData.orgId ;
    	}else{
    		$scope.formData.carShopId = $scope.selectData.carShopId ;
    	}
    	$http({
    		url:"basedata/userAction!saveUser.action",
    		method:"POST",
    		data:$scope.formData 
    	}).then(function(resp){
    		if(resp.data.code == 1){
    			hintService.hint({title: "成功", content: "保存成功！" });
    			$state.go($scope.state.list);
    		}else{
    			alert(resp.data.message);
    		}
    		$scope.isDoing = false ;
    	});
    };
    
    $scope.$watch("formData.number",function(val){
    	$scope.API.checkCodeUnique(null,val,unquieNumber);
    });
    function unquieNumber(resp){
    	if(resp.data.code == 1){
    		//不重复
    		$scope.form.number.$setValidity("custom_pattern",true);
    	}else{
    		//重复
    		$scope.form.number.$setValidity("custom_pattern",false);
    	}
    }
    
    $scope.cancel = function(){
    	$state.go($scope.state.list);
	};
	
}]);