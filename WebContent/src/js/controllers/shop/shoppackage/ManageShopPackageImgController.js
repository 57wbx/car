'use strict';

app.controller('manageShopPackageImgController',['$rootScope','$scope','$state','$timeout','$http','$modal','FileUploader','previewService','hintService','sessionStorageService','utilService',
                                      function($rootScope,$scope,$state,$timeout,$http,$modal,FileUploader,previewService,hintService,sessionStorageService,utilService){
	//用来缓存服务项id
	var itemId ;
	$scope.imgs = [];

	$scope.treeAPI.hiddenBusTypeTree();
	
	if($scope.rowIds[0]){
		itemId = $scope.rowIds[0];
		sessionStorageService.setItem("shopPackageIdForImg",$scope.rowIds[0]);
	}else{
		itemId = sessionStorageService.getItemStr("shopPackageIdForImg");
		$scope.rowIds[0] = itemId ;
	}
	
	/**
	 * 返回所有的图片列表
	 */
	$http({
		url:"shop/shopPackageAction!listShopPackageImgByShopPackage.action",
		method:"get",
		params:{
			fid:itemId
		}
	}).then(function(resp){
		if(resp.data.code==1){
			$scope.imgs = resp.data.images;
		}
	});
	/**
	 * 返回套餐项的名称
	 */
	$http({
		url:"shop/shopPackageAction!detailsShopPackage.action",
		method:"params",
		params:{
			fid:itemId
		}
	}).then(function(resp){
		if(resp.data.code){
			$scope.packageName = resp.data.details.packageName ;
		}
	});
	
	var seleceImg = $scope.seleceImg = function(){
		$("#addImage").click();
	}
	
	 var uploader = $scope.uploader = new FileUploader({
	        url: 'shop/shopPackageImgAction!addShopPackageImg.action',
	        alias:"files",
	        autoUpload:true,
	        removeAfterUpload:true,
	        formData:[{
	        	shopPackageId:itemId
	        }]
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
	  * 返回值
	  */
	 uploader.onSuccessItem = function(fileItem, response, status, headers) {
         if(response.code==1){
        	 hintService.hint({title: "成功", content: "上传成功！" });
        	 $scope.imgs.push({
        		//http://{{item.serverIp}}:{{item.port}}/{{item.filePath}}
        		 id:response.id,
        		 serverIp:response.serverIp,
        		 port:response.port,
        		 filePath:response.filePath
        	 });
         }else{
        	 alert(response.message);
         }
     };
     
     
     $scope.deleteImg = function(item){
    	 $http({
    		 url:"shop/shopPackageImgAction!deleteShopPackageImgById.action",
    		 method:"get",
    		 params:{
    			 id:item.id
    		 }
    	 }).then(function(resp){
    		 if(resp.data.code==1){
    			 hintService.hint({title: "成功", content: "删除图片成功！" });
    			 var index ;
    			 for(var i=0;i<$scope.imgs.length;i++){
    				 if($scope.imgs[i].id==resp.data.id){
    					 index = i;
    					 break;
    				 }
    			 }
    			 if(index||index===0){
    				 $scope.imgs.splice(index,1);
    			 }
    		 }else{
    			 //删除不成功的情况下
    			 alert(resp.data.message);
    		 }
    	 });
     }
     
     
     /**
      * 显示原始图片的方法
      */
     $scope.showImg = function(item){
    	 previewService.preview("http://"+item.serverIp+":"+item.port+"/"+item.filePath);
     }
     
     
     $scope.addOrUpdateDetails = function(item){
    	 showModal(item);
     }
     
     $scope.back = function(){
    	 $state.go($scope.state.list);
     }
     
     
     /**
		 * 弹窗事件
		 */
		var showModal = function(item){
			var modalInstance = $modal.open({
    	     templateUrl: 'src/tpl/shop/shoppackage/manage_shopitemimg_model.html',
    	     size: 'lg',
    	     backdrop:true,
    	     controller:"manageShopPackageImgDetailsController",
    	     resolve: {
    	    	 imgId:function(){
    	    		 return item.id;
    	    	 },
    	    	 content:function(){
    	    		 return item.content;
    	    	 }
    	     }
    	   });
			/**
			 * 弹窗关闭事件
			 */
			modalInstance.result.then(function (formData) {
				var item = utilService.getObjectFromArray("id",formData.id,$scope.imgs);
				if(item){
					item.content = formData.content;
				}
			});
		}
     
     
	//结束方法
}]);