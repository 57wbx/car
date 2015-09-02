'use strict';
app.controller('carNameEditController', ['$scope', '$state','$modal', '$http','sessionStorageService','FileUploader','previewService','hintService',
  function($scope, $state,$modal, $http, sessionStorageService,FileUploader,previewService,hintService) {
	
    $scope.formData = {};
    $scope.viewData = {};
    
	$scope.needCacheArray = ["carNameListDataTableProperties","carNameIdForEdit"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds[0]){
		sessionStorageService.setItem("carNameIdForEdit",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("carNameIdForEdit");
	}
	
	console.info("------------需要修改的id为："+$scope.rowIds[0]);
	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
    
	/**
	 * 初始化文件上传控件
	 */
	 var uploader = $scope.uploader = new FileUploader({
	        url: 'common/fileUploadAction!uploadOnePictrue.action',
	        alias:"files",
	        autoUpload:true,
	        removeAfterUpload:true
	    });
	
	 /**
	  * 过滤器
	  */
	 uploader.filters.push({
          name: 'imageFilter',
          fn: function(item /*{File|FileLikeObject}*/, options) {
        	  var isImage = false;
        	  isImage = item.type.indexOf("image")>=0;
        	  if(!isImage){
        		  alert("请选择图片文件!");
        	  }
              return isImage;
          }
      });
	  /**
	   * 上传开始之前
	   */
	 uploader.onBeforeUploadItem = function(item) {
		  $scope.uploading = true ;
      };
	  /**
		  * 返回值
		  */
      uploader.onSuccessItem = function(fileItem, response, status, headers) {
			 $scope.uploading = false ;
	         if(response.code==1){
	        	 hintService.hint({title: "成功", content: "上传成功！" });
	        	 //上传成功的返回数据：{"name":"13293618_1200x1000_0.jpg","code":1,"url":"http://120.25.149.142:8048/group1/M00/00/0C/eBmVjlXCvHCERq5wAAAAAKmTBbk746.jpg"}
	        	$scope.formData.photoUrl = response.url ;
	         }else{
	        	 alert(response.message)
	         }
	   };
	   /**
	    * 保存操作
	    */
	   $scope.submit = function(){
		   $http({
			   url:"base/carNameAction!updateCarName.action",
			   method:"post",
			   data:$scope.formData
		   }).then(function(resp){
			   if(resp.data.code === 1){
				   hintService.hint({title: "成功", content: "修改成功！" });
				   $state.go($scope.state.list)
			   }else{
				   alert(resp.data.message);
			   }
			   $scope.isDoing = false ;
		   });
	   }
	   
	   /**
	    * 初始化数据
	    */
	   initData();
	   function initData(){
		   $http({
			   url:"base/carNameAction!detailsCarName.action",
			   method:"post",
			   data:{
				   id:$scope.rowIds[0]
			   }
		   }).then(function(resp){
			   if(resp.data.code === 1){
				   $scope.formData = resp.data.details ;
				   $scope.formData.updateTime = undefined ;
			   }else{
				   alert(resp.data.message);
			   }
		   });
	   }
    
}]);