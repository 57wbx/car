'use strict';
app.controller('UserAdd', ['$scope', '$state', 'uiLoad','JQ_CONFIG',
  function($scope, $state, uiLoad, JQ_CONFIG) {
    uiLoad.load(JQ_CONFIG.dataTable);
    $scope.formData = {};
    $scope.formData.orgId = sessionStorage.getItem("treeId");
    $scope.formData.superName = sessionStorage.getItem("treeName");
    var personList = $('#personUnit');
    var personChooseBtn = $('#personChooseBtn');
    var personTable;
    var personStep,personFirstTime=true;
    if($scope.permBtn&&$scope.permBtn.length>0&&$scope.permBtn.indexOf("add")<0){//判断是否有修改权限
    	$("#add").remove();
    }
    function loadRole(){
    	var url = app.url.role.api.list;
    	app.utils.getData(url,function callback(dt) {
    		$scope.roleList = dt;
    	});
    }
    loadRole();
    // 提交并添加数据
    $scope.submit = function() {
      var url = app.url.user.api.save;
      app.utils.getData(url, $scope.formData, function(dt) {
        $state.go('app.user.list');
        $scope.$parent.reload();
      });
    };
    $scope.return = function(){
    	$state.go('app.user.list');
    	$scope.$parent.reload();
	};
    $scope.checkNull = function(){
    	if($scope.formData.roleId&&$scope.formData.number&&$scope.formData.name&&$scope.formData.personName){
    		$(".w100.btn.btn-success").attr("disabled",false);
    	}else{
    		$(".w100.btn.btn-success").attr("disabled",true);
    	}
    };
    $scope.personChoose = function() {
      var url = app.url.employee.loadNoneUserPersonList;
      var data = null;
      personStep = personChooseBtn.data('step') || 0;
      if(personStep === 0){
        if(personFirstTime){
          app.utils.getData(url,{"FLongNumber":sessionStorage.getItem("FLongNumber")},function callback(dt) {
            data = dt;
            if(personTable){
            	personTable.fnDestroy();
            }
            initPsersonTable(data);
            personList.removeClass('none');
            personChooseBtn.html('取消').data('step', 1);
          });
          personFirstTime = false;
        }else{
          personList.removeClass('none');
          personChooseBtn.html('取消').data('step', 1);
        }
      }else{
        personList.addClass('none');
        personChooseBtn.html('选择').data('step', 0);
        if(!$scope.formData.personName){
        	$(".w100.btn.btn-success").attr("disabled",true);
        }else{
        	if($scope.formData.number&&$scope.formData.name&&$scope.formData.roleId){
        		$(".w100.btn.btn-success").attr("disabled",false);
        	}else{
        		$(".w100.btn.btn-success").attr("disabled",true);
        	}
        }
      }
    };

    //职员table
    function initPsersonTable(data) {
	  personTable = $('#personList').dataTable({
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
	  personTable.$('tr').dblclick(function(e, settings) {
        //$scope.seeDetails($(this).data('id'));
      }).click(function(e) {
        $scope.formData.personId = $(this).data('id');
        $scope.formData.personName = $(this).data('name');
        var that = $(this), classname = 'rowSelected';
        var siblings = $(this).siblings();
        if(that.hasClass(classname)){
          that.removeClass(classname);
          $('#personName').val('');
          personChooseBtn.html('取消').data('step', 1);
          $scope.formData.personId = null;
          $scope.formData.personName = null;
        }else{
          that.addClass(classname);
          $('#personName').val($scope.formData.personName);
          personChooseBtn.html('确定').data('step', 2);
        }
        siblings.removeClass(classname);
      });
    }
  }
]);