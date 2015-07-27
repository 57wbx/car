'use strict';
app.controller('EmployeeEdit', ['$scope', '$http', '$state',  'uiLoad', 'JQ_CONFIG',
  function($scope, $http, $state, uiLoad, JQ_CONFIG) {
    uiLoad.load(JQ_CONFIG.dataTable);
    var id = sessionStorage.getItem("id");
    var postionList = $('#positionUnit');
    var positionChooseBtn = $('#positionChooseBtn');
    var posTable ,positionStep,posFirstTime=true;
    if($scope.permBtn&&$scope.permBtn.length>0&&$scope.permBtn.indexOf("modify")<0){//判断是否有权限
    	$("#modify").remove();
    }
    // 获取要被编辑组织的数据
    $http({
      url: app.url.employee.api.edit,
      data: {
        id: id
      },
      method: 'POST'
    }).then(function(dt) {
      dt = dt.data.editData;
      $scope.formData = {
        id: dt.id,
        number: dt.number,
        name: dt.name,
        gender:dt.gender,
        state:dt.state,
        orgId: dt.orgId,
        superName:dt.orgName,
        position:dt.positionId,
        positionName:dt.positionName,
        cell: dt.cell,
        address: dt.address,
        description: dt.description
      };
    });
    // 提交并更新数据
    $scope.submit = function() {
      var url = app.url.employee.api.modify;
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
    	if($.trim($("#number").val())!=""&&$.trim($("#name").val())!=""){
    		$(".w100.btn.btn-success").attr("disabled",false);
    	}else{
    		$(".w100.btn.btn-success").attr("disabled",true);
    	}
    };

	$scope.positionChoose = function() {
	    var url = app.url.employee.getPositionByOrgId;
	    positionStep = positionChooseBtn.data('step') || 0;
	  if(positionStep === 0){
        if(posFirstTime){
          $scope.orgId = $scope.formData.orgId;
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
      }
	  if(!$scope.formData.positionName){
        $(".w100.btn.btn-success").attr("disabled",true);
      }else{
        if($.trim($("#number").val())!=""&&$.trim($("#name").val())!=""){
        	$(".w100.btn.btn-success").attr("disabled",false);
        }else{
        	$(".w100.btn.btn-success").attr("disabled",true);
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
        //$scope.seeDetails($(this).data('id'));
      }).click(function(e) {
        $scope.formData.position = $(this).data('id');
        $scope.formData.positionName = $(this).data('name');

        var that = $(this), classname = 'rowSelected';
        var siblings = $(this).siblings();

        var chooseBtn = $('#positionChooseBtn');
        if(that.hasClass(classname)){
          that.removeClass(classname);
          $('#positionName').val('');
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