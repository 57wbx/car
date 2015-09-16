'use strict';
app.controller('autoPartAddController', ['$scope', '$state','$http','checkUniqueService'
  ,function($scope, $state,$http ,checkUniqueService) {
    
    $scope.treeAPI.hiddenBusTypeTree();
    
    $scope.formData = {};
    
    defaultFormData();
    /*
     * 页面方法api 
     */
    
    /**
	 * 检测所需要保存的partCode是否唯一
	 */
	$scope.partCodeIsUnique = true;
	$scope.notUniqueMessage = "已存在";
	$scope.checkPartCodeIsUnique = function(id,partCode){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkAutoPartCodeUnique(id,partCode).then(function(resp){
			if(resp.data.code==1){
				$scope.partCodeIsUnique = true;
			}else{
				$scope.partCodeIsUnique = false;
			}
		});
	}
	$("#partCode").change(function(e){
		if($scope.form.partCode.$invalid){
			$("#partCode").css("border-color","red");
		}else{
			$("#partCode").removeAttr("style");
		}
		if($scope.form.partCode.$valid){
			$scope.checkPartCodeIsUnique(null,($scope.formData.busTypeCode || '')+$(e.target).val());
		}
	});
	
    
    //提交方法
    $scope.submit = function(){
    	$scope.formData.partCode = ($scope.formData.busTypeCode || "") +$scope.formData.partCode ;
    	$http({
    		url:"base/autoPartAction!addAutoPart.action",
    		method:"post",
    		data:$scope.formData
    	}).then(function(resp){
    		var code = resp.data.code ;//服务器返回的数据
    		if(code==1){//成功的代码
    			$state.go($scope.state.list);
    		}else{//不成功
    			alert(resp.data.message);
    			return ;
    		}
    	});
    }
    
    //取消方法
    $scope.cancel = function(){
    	$state.go($scope.state.list);
    }
    
    
    /**
     * 数据新增的默认值
     */
    function defaultFormData(){
    	$scope.formData.sunitPrice = 0;
    	$scope.formData.yunitPrice = 0;
    	$scope.formData.eunitPrice = 0;
    	$scope.formData.useState = 0;
    	$scope.formData.isActivity = 0;
    	$scope.formData.stock = 0;
    }
    
}]);