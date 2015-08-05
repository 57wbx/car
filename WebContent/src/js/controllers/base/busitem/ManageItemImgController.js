'use strict';

app.controller('manageItemImgController',['$rootScope','$scope','$state','$timeout','$http','$modal','FileUploader','previewService','hintService','sessionStorageService','utilService',
                                      function($rootScope,$scope,$state,$timeout,$http,$modal,FileUploader,previewService,hintService,sessionStorageService,utilService){
	//用来缓存服务项id
	var itemId ;

	$scope.busItemAPI.hiddenBusTypeTree();
	
	if($scope.rowIds[0]){
		itemId = $scope.rowIds[0];
		sessionStorageService.setItem("busItemId",$scope.rowIds[0]);
	}else{
		itemId = sessionStorageService.getItemStr("busItemId");
		$scope.rowIds[0] = itemId ;
	}
	
	$http({
		url:"base/busItemAction!listItemImgByBusItem.action",
		method:"get",
		params:{
			fid:itemId
		}
	}).then(function(resp){
		if(resp.data.code==1){
			$scope.imgs = resp.data.images;
		}
	});
	
	var seleceImg = $scope.seleceImg = function(){
		$("#addImage").click();
	}
	
	 var uploader = $scope.uploader = new FileUploader({
	        url: 'base/busItemImgAction!addBusItemImg.action',
	        alias:"files",
	        autoUpload:true,
	        removeAfterUpload:true,
	        formData:[{
	        	itemId:itemId
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
         console.info('onSuccessItem', fileItem, response, status, headers);
         // response = {imgPath: "http://120.25.149.142:8048/group1/M00/00/04/eBmVjlW7RIKELGqbAAAAALC8Z0Q272.jpg", code: 1}
         hintService.hint({title: "成功", content: "上传成功！" });
         if(response.code==1){
        	 $scope.imgs.push({
        		//http://{{item.serverIp}}:{{item.port}}/{{item.filePath}}
        		 serverIp:response.serverIp,
        		 port:response.port,
        		 filePath:response.filePath
        	 });
         }
     };
     
     
     $scope.deleteImg = function(item){
    	 $http({
    		 url:"base/busItemImgAction!deleteItemImgById.action",
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
    	 $state.go("^.list");
     }
     
     
     /**
		 * 弹窗事件
		 */
		var showModal = function(item){
			var modalInstance = $modal.open({
    	     templateUrl: 'src/tpl/base/busitem/manage_itemimg_model.html',
    	     size: 'lg',
    	     backdrop:true,
    	     controller:"manageItemImgDetailsController",
    	     resolve: {
    	    	 imgId:function(){
    	    		 return item.id;
    	    	 },
    	    	 content:function(){
    	    		 return item.content;
    	    	 },
    	    	 fileType:function(){
    	    		 return item.fileType;
    	    	 }
    	     }
    	   });
			/**
			 * 弹窗关闭事件
			 */
			modalInstance.result.then(function (formData) {
				var item = utilService.getObjectFromArray("id",formData.id,$scope.imgs);
				if(item){
					item.fileType = formData.fileType;
					item.content = formData.content;
				}
			});
		}
     
     
	//结束方法
}]);