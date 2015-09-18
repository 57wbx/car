app.controller("orderPayDetailsController",['$scope','$state','$http','sessionStorageService',function($scope,$state,$http,sessionStorageService){
	
    $scope.needCacheArray = ["orderDataTableProperties","orderIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("orderIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("orderIdForDetails");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	
	$scope.$evalAsync(initDetails);
	/**
	 * 获取详细信息
	 */
	function initDetails(){
		$http({
			url:"opr/orderAction!detailsOrderPay.action",
			method:"post",
			data:{
				id:$scope.rowIds[0]
			}
		}).then(function(resp){
			if(resp.data.code===1){
				$scope.formData = resp.data.details ;
				renderFormData($scope.formData);
			}else{
				alert(resp.data.message);
				$state.go($scope.state.list);
			}
		});
		//清除数据
		$scope.setCanEdit(false);
	}
	
	/**
	 * 订单详情来源于两张表，所以需要对formdata进行一些处理，已达到同样的显示效果
	 */
	function renderFormData(formData){
		if(!formData){
			return ;
		}
		if(formData.payType == 2 ){// 微信支付
			formData.payType  = "微信";
			formData.totalFee = formData.totalFee/100;//总金额
			formData.cashFee = formData.cashFee/100 ;//现金支付金额
			formData.resultCode = formData.resultCode=="SUCCESS"?"支付成功":"支付失败";
		}else if(formData.payType ==1 ){
			formData.payType  = "支付宝";
			formData.resultCode = formData.resultCode=="SUCCESS"?"支付成功":"支付失败";
		}
	}
	
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
	
// 结束controller
}]);