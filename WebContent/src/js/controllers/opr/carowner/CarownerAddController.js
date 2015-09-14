app.controller("carownerAddController",['$scope','$state','$http','checkUniqueService','FileUploader','previewService','hintService',function($scope,$state,$http,checkUniqueService,FileUploader,previewService,hintService){
	
	
	$scope.formData = {};
	
	//初始化选中的数据
	$scope.clearRowIds();
	
	
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
    $scope.previewImg = function(name){
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
	

	
	/**
	 * 点击保存操作
	 */
	$scope.submit = function(){
		$http({
			url:"base/memberAction!addCarowner.action",
			method:'post',
			data : $scope.formData
		}).then(function(resp){
			var code = resp.data.code ;
			if(code){//代表保存成功
				hintService.hint({title: "成功", content: "保存成功！" });
				$state.go($scope.state.list);
			}else{//代表保存失败
				alert(resp.data.message);
			}
			$scope.isDoing = false ;
		});
	}
	
	/**
	 * 取消按钮的操作，直接跳转到列表页面上
	 */
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
}]);