app.controller("pushAddController",['$scope','$state','$http','hintService','sessionStorageService',function($scope,$state,$http,hintService,sessionStorageService){
	
	
	$scope.formData = {};
	$scope.formData.fdeviceType = 9;//默认发送全部
	$scope.formData.sendNow = 0;//默认立即发送
	$scope.formData.expiresDay = 0;//默认消息保存0天
	$scope.formData.expiresHour= 5;//默认消息保存5个小时
	var flushTime = new Date();
	//2007-08-15 15:09:00
	$scope.formData.sendTimeStr = flushTime.getFullYear()+"-"+(flushTime.getMonth()+1)+"-"+flushTime.getDate()+" "+flushTime.getHours()+":"+(flushTime.getMinutes()+2)+":"+flushTime.getSeconds();//默认消息保存5个小时
	
	$scope.submit = function(){
		$scope.formData.fexpiresTime = $scope.formData.expiresDay * 24 * 3600 + $scope.formData.expiresHour * 3600 ;
		if($scope.formData.sendNow==1){
			$scope.formData.fsendTimeStr = $scope.formData.sendTimeStr ;
		}
		if($scope.formData.fdeviceType == 4){//当为ios用户时，没有标题信息
			$scope.formData.ftitle = undefined ;
		}
		$http({
			url:"tig/pushMessageAction!addPushMessage.action",
			method:"post",
			data:$scope.formData
		}).then(function(resp){
			if(resp.data.code===1){
				 hintService.hint({title: "成功", content: "保存成功！" });
				 $state.go($scope.state.list);
			}else{
				alert(resp.data.message);
			}
			$scope.isDoing =false ;
		});
	}
	/**
	 * 取消按钮的操作，直接跳转到列表页面上
	 */
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
	
	
	//验证规则
	$scope.message = {
	}
	
}]);