'use strict';
app.controller('carShopEditController', ['$scope', '$http', '$state',  'uiLoad', 'JQ_CONFIG',
  function($scope, $http, $state, uiLoad, JQ_CONFIG) {
    var id = $scope.$parent.ids[0];
    console.info(id);
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

      
      initFormData(dt);//配置其他需要手动加工的数据
    });
    
    /**
     * 特殊的属性需要在这里配置一下
     */
    function initFormData(data){
    	var busStartTimeH = data.busStartTime.split(":")[0];
    	var busStartTimeM = data.busStartTime.split(":")[1];
    	var busEndTimeH = data.busEndTime.split(":")[1];
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
        $state.go('app.carshop.list');
      });
    };
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    $scope.openMapButton = function(){
  		if($scope.isOpen){
  			$scope.isOpen = false ;
  			$scope.mapMessage = "打开地图";
  		}else{
  			$scope.isOpen = true ;
  			$scope.mapMessage = "关闭地图";
  		}
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