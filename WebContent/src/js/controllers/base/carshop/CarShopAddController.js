app.controller('carShopAddController', ['$scope', '$state','$modal','$http', 'FileUploader','previewService','hintService',
  function($scope, $state,$modal,$http, FileUploader,previewService,hintService) {
	
	$scope.API.isListPage = false ;
	
    $scope.formData = {};
    $scope.viewData = {};
    
    
    // 提交并添加数据
    $scope.submit = function() {
    	
      //提交数据的时候，将坐标地址更新到scope中
      $scope.formData.mapX=$('#mapX').val();
      $scope.formData.mapY=$('#mapY').val();
      
      //提交数据时需要将开始营业时间和结束营业时间进行合并处理
      
      $scope.formData.busStartTime = $scope.formData.busStartTimeH + ":" + $scope.formData.busStartTimeM;
      $scope.formData.busEndTime = $scope.formData.busEndTimeH + ":" + $scope.formData.busEndTimeM;
      
	  $http({
		 url:"base/carShopAction!saveCarShop.action",
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
    
    $('#registerDate').focus(
    		function(){
	    		var optionSet = {
						singleDatePicker : true,
						format : 'YYYY-MM-DD'
					};
	    		$('#registerDate').daterangepicker(optionSet).on('apply.daterangepicker', function(ev){
	    			$scope.formData.registerDate=$('#registerDate').val();
	    		});
    		}
	);
    
  	$scope.openMapButton = function(){
  		showModal();
  	}
  	
  	/**
	 * 弹窗事件
	 */
	var showModal = function(){
		var modalInstance = $modal.open({
   	     templateUrl: 'src/tpl/base/carshop/map_model_add.html',
   	     size: 'lg',
   	     backdrop:true,
   	     controller:"mapModelForAddController",
   	     resolve: {
	    	 mapX:function(){
	    		 return null;
	    	 },
	    	 mapY:function(){
	    		 return null;
	    	 }
	     }
   	   });
		/**
		 * 弹窗关闭事件
		 */
		modalInstance.result.then(function (obj) {
			$scope.formData.mapX = obj.lng ;
			$scope.formData.mapY = obj.lat ;
    	});
	}
    
	/**
	 * 初始化新增数据
	 */
	defaultFormData();
	function defaultFormData(){
		$scope.formData.shopType = 0 ;
		$scope.formData.auditState = 0;
		var date = new Date();
		$scope.formData.registerDate = date.getFullYear()+"-"+date.getMonth()+"-"+date.getDate() ;
		$scope.formData.busStartTimeH = "09" ;
		$scope.formData.busStartTimeM = "00" ;
		$scope.formData.busEndTimeH = "23" ;
		$scope.formData.busEndTimeM = "59" ;
	}
  
}]);