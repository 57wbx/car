'use strict';
app.controller('OrgAdd', ['$rootScope','$scope', '$state', 'uiLoad','JQ_CONFIG',
  function($rootScope,$scope, $state, uiLoad, JQ_CONFIG) {
    $scope.formData = {};
    $scope.formData.parent = sessionStorage.getItem("treeId");
    $scope.formData.superName = sessionStorage.getItem("treeName");
    if($scope.permBtn.length>0&&$scope.permBtn.indexOf("add")<0){//判断是否有添加权限
    	$("#add").remove();
    }
    /*
	$scope.onClick = function (e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		$("#superName").val(zTree.getSelectedNodes()[0].name);
		$("#menuContent").slideUp("fast");
		$scope.formData.parent = zTree.getSelectedNodes()[0].id;
        $scope.viewData.superName = zTree.getSelectedNodes()[0].name;
        $scope.checkNull();
        $("body").unbind("mousedown", onBodyDown);
	};
	function hideMenu() {
		$("#menuContent").slideUp("fast");
		$("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		if (!( event.target.id == "superName" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
			hideMenu();
		}
	}
	$scope.showMenu = function () {
		var obj = $("#superName");
		$("#menuContent").css("width",obj.css("width"));
		obj.offset();
		$("#menuContent").slideDown("fast");
		$("body").bind("mousedown", onBodyDown);
	};
	var setting = {
		view: {dblClickExpand: false,selectedMulti: false,showIcon: false},
		data: {simpleData: {enable: true}},
		callback: {onDblClick: $scope.onClick}
	};
	app.utils.getData(app.url.org.loadOrgList, function(dt) {
        $.fn.zTree.init($("#treeDemo"), setting, dt);
    });
    */
    // 提交并添加数据
    $scope.submit = function() {
      var url = app.url.org.api.save;
      //$scope.formData.unitLayer = $("#orgType").val();
      app.utils.getData(url, $scope.formData, function(dt) {
    	$("#clickId").next().removeAttr("disabled");
        $state.go('app.org.list');
        $scope.$parent.reload();
      });
    };
    $scope.return = function(){
    	$state.go('app.org.list');
    	$scope.$parent.return();
	};
  }
]);