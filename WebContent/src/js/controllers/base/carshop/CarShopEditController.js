'use strict';
app.controller('carShopEditController', ['$rootScope','$scope', '$http', '$state','$modal','hintService','FileUploader','previewService','sessionStorageService',
  function($rootScope,$scope, $http, $state, $modal,hintService,FileUploader,previewService,sessionStorageService) {
	
	$scope.API.isListPage = false ;
	
	$scope.needCacheArray = ["carShopListDataTableProperties","carShopIdForEdit"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("carShopIdForEdit",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("carShopIdForEdit");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	var id = $scope.rowIds[0];
	
	
    // 获取要被编辑组织的数据
    $http({
      url: "base/carShopAction!detailsCarShopById.action",
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
      dt = dt.data.details;
      $scope.formData = dt;//将ajax的数据反写到页面上
      $scope.formData.orgId = dt.org.id;
      $scope.formData.orgName = dt.org.name;
      $scope.formData.org = undefined;
      
      $scope.formData.IDCARDURLName = dt.IDCARDURL?"请点击&nbsp;<a style='color: blue; text-decoration: underline;'  onClick='$(\"#IDCARDURL\").click();'>预览！</a>":undefined;
      $scope.formData.bankCardUrlName = dt.bankCardUrl ?"请点击&nbsp;<a  style='color: blue; text-decoration: underline;'  onClick='$(\"#bankCardUrl\").click();'>预览！</a>":undefined;
      $scope.formData.busCardUrlName = dt.busCardUrl?"请点击&nbsp;<a  style='color: blue; text-decoration: underline;'  onClick='$(\"#busCardUrl\").click();'>预览！</a>":undefined;
      $scope.formData.photoUrlName = dt.photoUrl?"请点击&nbsp;<a  style='color: blue; text-decoration: underline;'  onClick='$(\"#photoUrl\").click();'>预览！</a>":undefined;
      
      initFormData(dt);//配置其他需要手动加工的数据
    });
    
    /**
     * 特殊的属性需要在这里配置一下
     */
    function initFormData(data){
    	var busStartTimeH = data.busStartTime.split(":")[0];
    	var busStartTimeM = data.busStartTime.split(":")[1];
    	var busEndTimeH = data.busEndTime.split(":")[0];
    	var busEndTimeM = data.busEndTime.split(":")[1];
    	$scope.formData.busStartTimeH = busStartTimeH ;
    	$scope.formData.busStartTimeM = busStartTimeM ;
    	$scope.formData.busEndTimeH = busEndTimeH ;
    	$scope.formData.busEndTimeM = busEndTimeM ;
    	$scope.formData.registerDate = data.registerDate.substr(0,10);
    	initFormDateControl(data);
    }
    
    /**
     * 初始话日期控件
     */
    function initFormDateControl(data){
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
    	
    	if(data.registerDate){
    		$('#registerDate').val(data.registerDate.substr(0,10));
    	}
    }
    
    
 // 提交并更新数据
    $scope.submit = function() {
      
      //提交数据的时候，将坐标地址更新到scope中
      $scope.formData.mapX=$('#mapX').val();
      $scope.formData.mapY=$('#mapY').val();
      
      //提交数据时需要将开始营业时间和结束营业时间进行合并处理
      
      $scope.formData.busStartTime = $scope.formData.busStartTimeH + ":" + $scope.formData.busStartTimeM;
      $scope.formData.busEndTime = $scope.formData.busEndTimeH + ":" + $scope.formData.busEndTimeM;
      
      $http({
 		 url:"base/carShopAction!editCarShop.action",
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
	    		 return $scope.formData.mapX;
	    	 },
	    	 mapY:function(){
	    		 return $scope.formData.mapY ;
	    	 }
	     }
   	   });
		/**
		 * 弹窗关闭事件
		 */
		modalInstance.result.then(function (obj) {
			console.info(obj);
			$scope.formData.mapX = obj.lng ;
			$scope.formData.mapY = obj.lat ;
    	});
	}

    $scope.clearRowIds();
    
}]);