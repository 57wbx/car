//所有的增删改查操作都在该页面上进行处理
'use strict';
app.controller('busTypeEditController', ['$rootScope','$scope', '$http', '$state',  'uiLoad', 'JQ_CONFIG',
 function($rootScope,$scope, $http, $state, uiLoad, JQ_CONFIG ) {
	
	$rootScope.initBusTypeEditControllerDataObject = {
		initDetails : initFormData,
		editBusType : editBusType,
		addBusType :add ,
		deleteBusType : deleteBusType,
		editable : false ,
		isNew : false //是否新增
	}
    // 获取要被编辑组织的数据
  
	$scope.editable = false ;
    
    
    function initFormData(){
    	  $http({
    	      url: "base/busTypeAction!detailsBusTypeByBusTypeCode.action",
    	      data: {
    	    	  "busTypeCode": $rootScope.item_id
    	      },
    	      method: 'POST'
    	    }).then(function(dt) {
    	      dt = dt.data.details;
    	      $scope.formData = dt;//将ajax的数据反写到页面上
    	      $scope.formData.oldBusTypeCode = dt.busTypeCode ;
    	      if(dt.parentId!=""){
    	    	  $scope.formData.newBusTypeCode = dt.busTypeCode.split(dt.parentId)[1];
    	      }else{
    	    	  $scope.formData.newBusTypeCode = dt.busTypeCode;
    	      }
    	      if(!$rootScope.initBusTypeEditControllerDataObject.editable){
    	    	  details();
    	      }
    	    });
    }
    
    
    
    function details(){
    	$("#busTypeEdit input").attr("disabled","disabled");
  	  	$("#busTypeEdit select").attr("disabled","disabled");
  	    $("#confirmButton").addClass("none");
	  	$("#cancelButton").addClass("none");
	  	$("#busTypeLabel").html("详细信息");
	  	
	  	$rootScope.initBusTypeEditControllerDataObject.editable = false ;
    }
   
    function edit(label){
    	
    	$("#busTypeEdit input").removeAttr("disabled","disabled");
  	  	$("#busTypeEdit select").removeAttr("disabled","disabled");
  	  	$("#confirmButton").removeClass("none");
  	  	$("#cancelButton").removeClass("none");
  	    $("#busTypeLabel").html(label);
  	    
  	    
  	  $rootScope.initBusTypeEditControllerDataObject.editable = true ;
    }
    
    function editBusType(){
    	$rootScope.initBusTypeEditControllerDataObject.editable = true ;
    	if(!$scope.formData.busTypeCode){
    		initFormData();
    	}
    	edit("修改业务信息");
    }
    
    /**
     * 新增bustype的方法  在bustypeController中被调用
     */
    function add(parentId){
    	$scope.formData = {
    			parentId:parentId
    	};
    	edit("新增业务类型");
    }
    
    function deleteBusType(){
    	$http({
  	      url: "base/busTypeAction!deleteBusTypeByBusTypeCode.action",
  	      data: {
  	    	  "busTypeCode": $rootScope.item_id
  	      },
  	      method: 'POST'
  	    }).then(function(dt) {
  	    	$rootScope.item_id = null ;
  	    	 $state.reload("app.bustype.edit");  
  	    });
    	
    }
    
    
    
    $scope.cancel = function(){
    	if($scope.formData.oldBusTypeCode){
    		$rootScope.initBusTypeEditControllerDataObject.editable = false ;
    		initFormData();
    	}else{
    		
    		$state.reload("app.bustype.edit");
    	}
    }
    
 // 提交并更新数据
    $scope.submit = function() {
      var url = "base/busTypeAction!saveOrUpdateBusType.action";
      //对数据做处理 将数据的编号进行处理
      if($scope.formData.parentId==null||$scope.formData.parentId==""){
    	  $scope.formData.busTypeCode = ""+$scope.formData.newBusTypeCode;
      }else{
    	  $scope.formData.busTypeCode = $scope.formData.parentId+""+$scope.formData.newBusTypeCode;
      }
      
      app.utils.getData(url, $scope.formData, function(dt) {
    	  if(dt==0){//主键冲突
    		  alert("该编号已经被使用，请重新设置编号");
    		  return ;
    	  }
    	  $state.reload("app.bustype.edit");
      });
    };
    
    
    
    
    
    
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