'use strict';
app.controller('EmployeeAdd', ['$scope', '$state', 'uiLoad','JQ_CONFIG',
  function($scope, $state, uiLoad, JQ_CONFIG) {
    uiLoad.load(JQ_CONFIG.dataTable);
    $scope.formData = {};
    $scope.formData.orgId=sessionStorage.getItem("orgId");
    $scope.formData.superName = sessionStorage.getItem("orgName");
    $scope.formData.position = sessionStorage.getItem("positionId");
    $scope.formData.positionName = sessionStorage.getItem("positionName");
    
    var postionList = $('#positionUnit');
    var positionChooseBtn = $('#positionChooseBtn');
    var posTable;
    var positionStep,posFirstTime=true;
    // 提交并添加数据
    if($scope.permBtn&&$scope.permBtn.length>0&&$scope.permBtn.indexOf("add")<0){//判断是否有权限
    	$("#add").remove();
    }
    $scope.success = true;
    $scope.submit = function() {
      var url = app.url.employee.api.save;
      app.utils.getData(url, $scope.formData, function(dt) {
        $state.go('app.employee.list');
        $scope.$parent.reload();
      });
    };
    $scope.return = function(){
    	 $state.go('app.employee.list');
    	 $scope.$parent.reload();
    };
    $scope.checkNull = function(){
    	if($.trim($("#number").val())!=""&&$.trim($("#name").val())!=""&&$.trim($("#positionName").val())!=""){
    		$scope.success = false;
    	}else{
    		$scope.success = true;
    	}
    };

//TODO 职位要添加组织过滤
    $scope.positionChoose = function() {
      var url = app.url.employee.getPositionByOrgId;
      positionStep = positionChooseBtn.data('step') || 0;
      if(positionStep === 0){
        if(posFirstTime){
          app.utils.getData(url,{"id":$scope.formData.orgId}, function callback(dt) {
            if(posTable){
            	posTable.fnDestroy();
            }
            initPositionTable(dt);
            postionList.removeClass('none');
            positionChooseBtn.html('取消').data('step', 1);
          });
          posFirstTime = false;
        }else{
          postionList.removeClass('none');
          positionChooseBtn.html('取消').data('step', 1);
        }
      }else{
        postionList.addClass('none');
        positionChooseBtn.html('选择').data('step', 0);
        if(!$scope.formData.positionName){
        	$scope.success = true;
        }else{
        	if($.trim($("#number").val())!=""&&$.trim($("#name").val())!=""){
        		$scope.success = false;
        	}else{
        		$scope.success = true;
        	}
        }
      }
    };
    //职位table
    function initPositionTable(data) {
      posTable = $('#positionList').dataTable({
        "data": data,
        "sAjaxDataProp": "dataList",
        "fnCreatedRow": function(nRow, aData, iDataIndex) {
          $(nRow).attr({'data-id': aData['id'], 'data-name': aData['name']});
        },
        "oLanguage": {
          "sLengthMenu": "每页 _MENU_ 条",
          "sZeroRecords": "没有找到符合条件的数据",
          "sProcessing": "&lt;img src=’./loading.gif’ /&gt;",
          "sInfo": "共 _TOTAL_ 条",
          "sInfoEmpty": "没有记录",
          "sInfoFiltered": "(从 _MAX_ 条记录中过滤)",
          "sSearch": "搜索",
          "oPaginate": {
            "sPrevious": "<",
            "sNext": ">"
          }
        },
        "aoColumns": [{
          "mDataProp": "number"
        }, {
          "mDataProp": "name"
        }]
      });
      posTable.$('tr').dblclick(function(e, settings) {
      }).click(function(e) {
        $scope.formData.position = $(this).data('id');
        $scope.formData.positionName = $(this).data('name');
        var that = $(this), classname = 'rowSelected';
        var siblings = $(this).siblings();
        if(that.hasClass(classname)){
          that.removeClass(classname);
          $scope.formData.positionName="";
          positionChooseBtn.html('取消').data('step', 1);
          $scope.formData.position = null;
          $scope.formData.positionName = null;
        }else{
          that.addClass(classname);
          positionChooseBtn.html('确定').data('step', 2);
        }
        siblings.removeClass(classname);
      });
    }
  }
]);