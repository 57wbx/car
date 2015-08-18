app.controller("dealComplainController",['$scope','$modalInstance','$timeout','$http','dataTableSearchService','hintService','id',function($scope,$modalInstance,$timeout,$http,dataTableSearchService,hintService,id){
	
	$timeout(initData,20);
	
	/**
	 * 初始化数据
	 */
	function initData(){
		$http({
			url:"opr/complainAction!detailsDealComplainDetails.action",
			method:"post",
			data:{
				id:id
			}
		}).then(function(resp){
			if(resp.data.code){
				$scope.formData = resp.data.details ;
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
			if(resp.data.code){
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