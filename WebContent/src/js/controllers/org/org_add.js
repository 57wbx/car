'use strict';
app.controller('OrgAdd', ['$rootScope','$scope', '$state','$http','hintService',
  function($rootScope,$scope, $state,$http,hintService) {
	
	$scope.clearRowIds();
	
    $scope.formData = {};
    
    defaultFormData();
    
    
    $scope.cancel = function(){
    	$state.go($scope.state.list);
	};
	
	/**
	 * 保存
	 */
	$scope.submit = function(){
		//上级组织
		$scope.formData.parentId = $scope.treeData.orgId ;
		$http({
			url:"basedata/orgZAction!saveOrg.action",
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
	
	
	function defaultFormData(){
		$scope.formData.unitLayer = 2 ;
	}

//结束controller
}]);