'use strict';
app.controller('autoPartAddController', ['$scope', '$state','$http', 'uiLoad','JQ_CONFIG','checkUniqueService'
  ,function($scope, $state,$http ,uiLoad, JQ_CONFIG,checkUniqueService) {
    uiLoad.load(JQ_CONFIG.dataTable);
    
    $scope.treeAPI.hiddenBusTypeTree();
    
    $scope.formData = {};
    
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
    			$state.go("app.autopart.list");
    		}else{//不成功
    			alert(resp.data.message);
    			return ;
    		}
    	});
    }
    
    //取消方法
    $scope.cancel = function(){
    	$state.go("app.autopart.list");
    }
    
    
    
}
]);