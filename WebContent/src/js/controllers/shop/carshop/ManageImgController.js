'use strict';

app.controller('manageImgController',['$rootScope','$scope','$state','$timeout','$http','FileUploader','previewService','hintService',
                                      function($rootScope,$scope,$state,$timeout,$http,FileUploader,previewService,hintService){

	$http({
		url:"base/carShopAction!listCarShopImgByLoginUser.action"
	}).then(function(resp){
		if(resp.data.code==1){
			$scope.imgs = resp.data.images;
		}
	});
	
	var seleceImg = $scope.seleceImg = function(){
		$("#addImage").click();
	}
	
	 var uploader = $scope.uploader = new FileUploader({
	        url: 'base/carShopAction!saveCarShopImg.action',
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
    		 url:"base/carShopAction!deleteCarShopImgById.action",
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
     
     
	//结束方法
}]);