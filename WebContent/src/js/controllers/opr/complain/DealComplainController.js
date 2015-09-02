app.controller("dealComplainController",['$scope','$modalInstance','$timeout','$http','dataTableSearchService','hintService','id',function($scope,$modalInstance,$timeout,$http,dataTableSearchService,hintService,id){
	
	$scope.hasIsBlackList = false;
	
	$timeout(initData,20);
	
	/**
	 * 初始化数据
	 */
	function initData(){
		$http({
			url:"opr/complainAction!detailsDealComplainDetailsWithIsBlackList.action",
			method:"post",
			data:{
				id:id
			}
		}).then(function(resp){
			if(resp.data.code==1){
				$scope.formData = resp.data.details ;
				//1=门店、2=技工
				
				console.info("objType",$scope.formData.objType);
				if($scope.formData.objType==1 || $scope.formData.objType==2){
					$scope.hasIsBlackList = true ;
				}else{
					$scope.hasIsBlackList = false ;
				}
			}else{
				alert(resp.data.message);
				$scope.cancel();
			}
		});
	}
	
	
	/**
	 * 处理投诉信息
	 */
	$scope.sumbit = function(){
		$(".modal-content button").attr("disabled","true");
		$scope.formData.id = id ;
		$http({
			url:"opr/complainAction!addOrUpdateDealComplain.action",
			method:"post",
			data:$scope.formData
		}).then(function(resp){
			if(resp.data.code==1){
				//成功
				hintService.hint({title: "成功", content: "处理成功！" });
				$modalInstance.close();
			}else{
				$(".modal-content button").removeAttr("disabled");
				alert(resp.data.message);
			}
		});
	}
	
	
	$scope.cancel = function() {
		    $modalInstance.dismiss('cancel');
	};
}]);