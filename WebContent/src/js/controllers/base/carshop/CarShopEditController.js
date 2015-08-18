'use strict';
app.controller('carShopEditController', ['$rootScope','$scope', '$http', '$state','$modal',  'uiLoad', 'JQ_CONFIG','FileUploader','previewService','sessionStorageService',
  function($rootScope,$scope, $http, $state, $modal,uiLoad, JQ_CONFIG,FileUploader,previewService,sessionStorageService) {
	
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
      
      $scope.formData.IDCARDURLName = dt.IDCARDURL?"请点击&nbsp;<a href='#' style='color: blue; text-decoration: underline;'  onClick='$(\"#IDCARDURL\").click();'>预览！</a>":undefined;
      $scope.formData.bankCardUrlName = dt.bankCardUrl ?"请点击&nbsp;<a href='#' style='color: blue; text-decoration: underline;'  onClick='$(\"#bankCardUrl\").click();'>预览！</a>":undefined;
      $scope.formData.busCardUrlName = dt.busCardUrl?"请点击&nbsp;<a href='#' style='color: blue; text-decoration: underline;'  onClick='$(\"#busCardUrl\").click();'>预览！</a>":undefined;
      $scope.formData.photoUrlName = dt.photoUrl?"请点击&nbsp;<a href='#' style='color: blue; text-decoration: underline;'  onClick='$(\"#photoUrl\").click();'>预览！</a>":undefined;
      
      
      initFormData(dt);//配置其他需要手动加工的数据
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
    
    
    
    /**
     * 特殊的属性需要在这里配置一下
     */
    function initFormData(data){
    	var busStartTimeH = data.busStartTime.split(":")[0];
    	var busStartTimeM = data.busStartTime.split(":")[1];
    	var busEndTimeH = data.busEndTime.split(":")[0];
    	var busEndTimeM = data.busEndTime.split(":")[1];
    	$("#busStartTimeH").val(busStartTimeH);
    	$("#busStartTimeM").val(busStartTimeM);
    	$("#busEndTimeH").val(busEndTimeH);
    	$("#busEndTimeM").val(busEndTimeM);
    	
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
    						timePicker : true,
    						format : 'YYYY-MM-DD HH:mm'
    					};
    	    		$('#registerDate').daterangepicker(optionSet).on('apply.daterangepicker', function(ev){
    	    			$scope.formData.registerDate=$('#registerDate').val();
    	    		});
        		}
    	);
    	
    	$('#registerDate').val(data.registerDate);
    }
    
    
 // 提交并更新数据
    $scope.submit = function() {
      var url = "base/carShopAction!editCarShop.action";
      
      //提交数据的时候，将坐标地址更新到scope中
      $scope.formData.mapX=$('#mapX').val();
      $scope.formData.mapY=$('#mapY').val();
      
      //提交数据时需要将开始营业时间和结束营业时间进行合并处理
      
      $scope.formData.busStartTime = $("#busStartTimeH").val()+":"+$("#busStartTimeM").val();
      $scope.formData.busEndTime = $("#busEndTimeH").val()+":"+$("#busEndTimeM").val();

      
      app.utils.getData(url, $scope.formData, function(dt) {
        $state.go($scope.state.list);
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
    
    
    $scope.checkNull = function(){
    	if($.trim($("#number").val())!=""&&$.trim($("#name").val())!=""&&$.trim($("#superName").val())!=""){
    		$(".w100.btn.btn-success").attr("disabled",false);
    	}else{
    		$(".w100.btn.btn-success").attr("disabled",true);
    	}
    };
    $scope.choose = function() {
      var url = app.url.org.api.list;
      var data = null;
      step = chooseBtn.data('step') || 0;
      if(step === 0){
        if(firstTime){
          app.utils.getData(url, function callback(dt) {
            data = dt;
            initTable(data);
            superList.removeClass('none');
            chooseBtn.html('取消').data('step', 1);
          });
          firstTime = false;
        }else{
          superList.removeClass('none');
          chooseBtn.html('取消').data('step', 1);
        }
      }else{
        superList.addClass('none');
        chooseBtn.html('选择').data('step', 0);
        if($("#superName").val()==''){
        	$(".w100.btn.btn-success").attr("disabled",true);
        }else{
        	if($.trim($("#number").val())!=""&&$.trim($("#name").val())!=""){
        		$(".w100.btn.btn-success").attr("disabled",false);
        	}else{
        		$(".w100.btn.btn-success").attr("disabled",true);
        	}
        }
      }
    };

    $scope.clearRowIds();
    
    function initTable(data) {
      var dTable = $('#orgAddList').dataTable({
        "data": data,
        "sAjaxDataProp": "dataList",
        "fnCreatedRow": function(nRow, aData, iDataIndex) {
          $(nRow).attr({'data-id': aData['id'], 'data-name': aData['name']});
        },
        "aoColumns": [{
          "mDataProp": "code"
        }, {
          "mDataProp": "name"
        }]
      });
      dTable.$('tr').dblclick(function(e, settings) {
        //$scope.seeDetails($(this).data('id'));
      }).click(function(e) {
        $scope.formData.org = $(this).data('id');
        $scope.viewData.orgName = $(this).data('name');

        var that = $(this), classname = 'rowSelected';
        var siblings = $(this).siblings();

        if(that.hasClass(classname)){
          that.removeClass(classname);
          $('#superName').val('');
          chooseBtn.html('取消').data('step', 1);
          $scope.formData.org = null;
          $scope.viewData.orgName = null;
        }else{
          that.addClass(classname);
          $('#superName').val($scope.viewData.orgName);
          chooseBtn.html('确定').data('step', 2);
        }
        
        siblings.removeClass(classname);
      });
    }
  }
]);