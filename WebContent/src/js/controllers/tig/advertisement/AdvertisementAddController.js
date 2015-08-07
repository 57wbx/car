app.controller("advertisementAddController",['$scope','$state','$http','checkUniqueService','sessionStorageService','FileUploader','hintService',function($scope,$state,$http,checkUniqueService,sessionStorageService,FileUploader,hintService){
	
//	$scope.formData.fitemID  新增开始的时候需要从服务器中下载下来，以便于子项的操作
	
	$scope.formData = {};
	
	//初始化选中的数据
	$scope.setCanEdit(false);
	$scope.clearRowIds();
	
	sessionStorageService.clearNoCacheItem();
	
	
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
	        	$scope.formData.serverIP = response.host ;
	        	$scope.formData.port = response.port ;
	        	$scope.formData.filepath = response.resourcesPath;
	        	$scope.formData.filename = response.name;
	        	
	         }else{
	        	 alert(response.message)
	         }
	     };
	
	/**
	 * 检查内部版本号是否存在
	 */
	$scope.versionCodeIsUnique = true;
	$scope.notUniqueMessage = "已存在";
	$scope.checkVersionCodeIsUnique = function(id,versionCode){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkVersionCodeUnique(id,versionCode).then(function(resp){
			if(resp.data.code==1){
				$scope.versionCodeIsUnique = true;
			}else{
				$scope.versionCodeIsUnique = false;
			}
		});
	}
	$("#versionCode").change(function(e){
		if($scope.form.versionCode.$valid){
			$scope.checkVersionCodeIsUnique($scope.formData.id,$(e.target).val());
		}
	});
	
	/**
	 * 检查外部版本号是否存在
	 */
	$scope.versionNameIsUnique = true;
	$scope.checkVesionNameIsUnique = function(id,versionName){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkVersionNameUnique(id,versionName).then(function(resp){
			if(resp.data.code==1){
				$scope.versionNameIsUnique = true;
			}else{
				$scope.versionNameIsUnique = false;
			}
		});
	}
	$("#versionName").change(function(e){
		if($scope.form.versionName.$valid){
			$scope.checkVesionNameIsUnique($scope.formData.id,$(e.target).val());
		}
	});
	
	
	//初始化时间控件
	$('#uploadTimeStr').focus(
	    		function(){
		    		var optionSet = {
							singleDatePicker : true,
							timePicker : true,
							format : 'YYYY-MM-DD HH:mm'
						};
		    		$('#uploadTimeStr').daterangepicker(optionSet).on('apply.daterangepicker', function(ev){
		    			$scope.formData.uploadTimeStr=$('#uploadTimeStr').val();
		    		});
	    		}
	);
	
	
	
	
	
	/**
	 * 点击保存操作
	 */
	$scope.submit = function(){
		$http({
			url:"tig/advertisementAction!addAdvertisement.action",
			method:'post',
			data : $scope.formData
		}).then(function(resp){
			var code = resp.data.code ;
			if(code == 1){//代表保存成功
				$state.go($scope.state.list);
			}else{//代表保存失败
				alert(resp.data.message);
			}
		})
	}	
	/**
	 * 取消按钮的操作，直接跳转到列表页面上
	 */
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
	
	
	
}]);