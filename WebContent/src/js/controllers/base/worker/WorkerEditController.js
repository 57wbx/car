'use strict';
app.controller('workerEditController', ['$rootScope','$scope', '$http', '$state',  'uiLoad', 'JQ_CONFIG','FileUploader','previewService','sessionStorageService','hintService',
  function($rootScope,$scope, $http, $state, uiLoad, JQ_CONFIG,FileUploader,previewService,sessionStorageService,hintService) {
	
	$scope.needCacheArray = ["memberDataTableProperties","memberIdForEdit"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds&&$scope.rowIds[0]){
		sessionStorageService.setItem("memberIdForEdit",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("memberIdForEdit");
	}
	
	if(!$scope.rowIds||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	var id = $scope.rowIds[0];
	
	
    // 获取要被编辑组织的数据
    $http({
      url: "base/memberAction!detailsWorker.action",
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
    	if(dt.data.code){
    		$scope.formData = dt.data.details ;
    		$scope.formData.MCARDURLName = dt.data.details.MCARDURL?"请点击&nbsp;<a  style='color: blue; text-decoration: underline;'  onClick='$(\"#MCARDURL\").click();'>预览！</a>":undefined;
	        $scope.formData.IDCARDURLName = dt.data.details.IDCARDURL?"请点击&nbsp;<a  style='color: blue; text-decoration: underline;'  onClick='$(\"#IDCARDURL\").click();'>预览！</a>":undefined;
	        $scope.formData.birthdayStr = dt.data.details.birthday.substring(0,10) ;//2015-08-12    十位
	        $scope.formData.birthday = dt.data.details.birthday.substring(0,10);
    	}else{
    		alert(dt.data.message);
    		$state.go($scope.state.list);
    	}
    });
    
    
    var uploader = $scope.uploader = new FileUploader({
        url: 'common/fileUploadAction!uploadOnePictrue.action',
        alias:"files",
        autoUpload:false,
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
       		  console.info(item);
       		  alert("请选择图片文件!");
       	  }else{
       		  //清除之前的有的，每个单一文件只能有一个
       		 if(uploader.queue&&uploader.queue.length>0){
          	   for(var i=0;i<uploader.queue.length;i++){
          		   if(options.name==uploader.queue[i].name){
          			   uploader.removeFromQueue(i);
          		   }
          	   }
             }
       	  }
          return isImage;
         }
     });
    /**
     * 预览图片功能
     */
    var previewImg = $scope.previewImg = function(name){
    	 previewService.preview($scope.formData[name]);
    }
    /**
     * 删除一张图片，删除图片只能删除图片formdata中的记录，目前后台不做任何操作
     */
    $scope.deleteImg = function(name){
    	$scope.formData[name] = undefined;
    	$scope.formData[name+"Name"] = undefined;
    }
    
    
    /**
     * 上传一张图片，根据图片的name来上传指定的图片
     */
    $scope.uploadImg = function(alias){
    	if(uploader.queue&&uploader.queue.length>0){
    		for(var i=0;i<uploader.queue.length;i++){
    			if(uploader.queue[i].name==alias){
    				//禁用上传按钮
    				buttonClick(uploader.queue[i].name);
    				//上传
    				uploader.uploadItem(i);
    				return ;
    			}
    		}
    	}else{
    		alert("请选择图片文件!");
    	}
    }
    /**
     * 当成功上传一个文件的时候调用
     */
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
    	if(response.code){//成功上传
    		var name = fileItem.name;
    		$scope.formData[name] = response.url;
    		$scope.formData[name+"Name"] = response.name;
    		console.info($scope.formData);
    	}else{
    		alert(response.message);
    	}
    };
    
    /**
     * 当上传按钮被点击的时候，将该按钮变为disblead，并改变其中的html
     */
    var buttonClick = function(name){
    	var buttonName = "#"+name+"Button";
    	$(buttonName).attr("disabled","disabled");
    	$(buttonName).html("上传中...");
    }
    
    
    $('#birthday').focus(
    		function(){
	    		var optionSet = {
	    				showDropdowns:true,
						singleDatePicker : true,
//						timePicker : true,
						format : 'YYYY-MM-DD'
						
					};
	    		$('#birthday').daterangepicker(optionSet).on('apply.daterangepicker', function(ev){
	    			$scope.formData.birthday=$('#birthday').val();
	    		});
    		}
	);
    
    $scope.submit = function(){
    	$http({
    		url:"base/memberAction!updateWorker.action",
    		method:"post",
    		data:$scope.formData
    	}).then(function(resp){
    		if(resp.data.code==1){
    			hintService.hint({title: "成功", content: "修改成功！" });
    			$state.go($scope.state.list);
    		}else{
    			alert(resp.data.message);
    		}
    	});
    }
    
    $scope.cancel = function(){
    	$state.go($scope.state.list);
    }
    

    $scope.clearRowIds();
    
   }
]);