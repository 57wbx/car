'use strict';
app.controller('OrgEdit', ['$scope', '$http', '$state','$timeout', 'sessionStorageService','hintService',
  function($scope, $http, $state,$timeout,sessionStorageService,hintService) {
	
	$scope.treeAPI.reloadListTable = undefined ;
    
    $scope.needCacheArray = ["orgListDataTableProperties","orgIdForEdit"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("orgIdForEdit",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("orgIdForEdit");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	var id = $scope.rowIds[0];
    
    // 获取要被编辑组织的数据
    $http({
      url: "basedata/orgZAction!detailsOrg.action",
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
    	if(dt.data.code==1){
    		$scope.formData = dt.data.details ;
    		$timeout(function(){
    			$scope.treeController.select_branch_byId($scope.formData.pId);
    		},200);
    	}else{
    		alert(dt.data.message);
    	}
    });
    // 提交并更新数据
    $scope.submit = function() {
    	$scope.formData.createTime = undefined ;
    	$scope.formData.FLongNumber = undefined ;
    	$scope.formData.lastModifyTime = undefined ;
    	
    	$scope.formData.parentId = $scope.treeData.orgId ;
    	
    	$http({
    		url:"basedata/orgZAction!updateOrg.action",
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
    };
    $scope.cancel = function(){
    	$state.go($scope.state.list);
	};

  }
]);