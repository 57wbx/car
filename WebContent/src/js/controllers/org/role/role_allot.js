'use strict';

app.controller('RoleAllot', function($http, $rootScope, $scope, $state) {
	$scope.details = {};
	var id = sessionStorage.getItem("id");
	$scope.details.name = sessionStorage.getItem("name");
	$scope.details.number = sessionStorage.getItem("number");
	$scope.details.orgName = sessionStorage.getItem("orgName");
	if($scope.permBtn&&$scope.permBtn.length>0&&$scope.permBtn.indexOf("allot")<0){//判断是否有权限
    	$("#allot").remove();
    }
    var setting = {
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		view:{
			showIcon: false
		}
	};
    $http({
    	url: app.url.permission.api.list,
    	data: {
    		id: id
    	},
    	method: 'POST'
    }).then(function(dt) {
    	$.fn.zTree.init($("#treeDemo"), setting, dt.data);
    	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
    	zTree.setting.check.chkboxType =  { "Y" : "ps", "N" : "ps" };
    });
    $scope.return = function(){
    	$state.go('app.role.list');
    	$scope.$parent.reload();
	};
    $scope.submit = function() {
    	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		var checkCount = zTree.getCheckedNodes(true);//选中的
		var nocheckCount = zTree.getCheckedNodes(false);//没有选中的
		var changeCount = zTree.getChangeCheckedNodes();//改变的数据
		var addItem = new Array();
		var delItem = new Array();
		if(changeCount==0){
			$state.go('app.role.list');
		}else{
			for(var i=0,size=changeCount.length;i<size;i++){
				var obj = changeCount[i];
				if(obj.checked==1&&obj.leaf==1){
					addItem.push("'"+obj.id+"'");
				}else if(obj.leaf==1){
					delItem.push("'"+obj.id+"'");
				}
			}
		}
		var url = app.url.permission.api.save;
        app.utils.getData(url,{"id":id,"addMenuStr":addItem.join(","),"delMenuStr":delItem.join(",")}, function(dt) {
          $state.go('app.role.list');
          $scope.$parent.reload();
        });
	};
});