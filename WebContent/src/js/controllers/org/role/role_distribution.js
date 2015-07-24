'use strict';

app.controller('RoleAllot', function($http, $rootScope, $scope, $state) {
	
	var roleId = sessionStorage.getItem("id");
	var curStatus = "init", curAsyncCount = 0, asyncForAll = false, goAsync = false;
	var type={ "Y" : "ps", "N" : "ps" };
	var setting = {
		async: {
			enable: true,
			url:app.url.role.loadUser,
			autoParam:["id"],
			otherParam:{"roleId":roleId},
			type: "get"
		},
		check:{enable:true,chkboxType:type},
		data: {simpleData: {enable: true}}
	};
	
	function asyncAll() {
		if (!check()) {
			return;
		}
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		if (asyncForAll) {
		} else {
			asyncNodes(zTree.getNodes());
			if (!goAsync) {
				curStatus = "";
			}
		}
	}
	
	function asyncNodes(nodes) {
		if (!nodes) return;
		curStatus = "async";
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		for (var i=0, l=nodes.length; i<l; i++) {
			if (nodes[i].isParent && nodes[i].zAsync) {
				asyncNodes(nodes[i].children);
			} else {
				goAsync = true;
				zTree.reAsyncChildNodes(nodes[i], "refresh", true);
			}
		}
	}

	function check() {
		if (curAsyncCount > 0) {
			return false;
		}
		return true;
	}
	
	app.utils.getData(app.url.role.loadOrgList, function(dt) {
        $.fn.zTree.init($("#treeDemo"), setting, dt);
        asyncAll();
    });
	
	$scope.submit = function(){
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		var changeArray = zTree.getChangeCheckedNodes();
		var addArray = new Array();
		var delArray = new Array();
		for(var i in changeArray){
			if(changeArray[i].isUser){
				if(changeArray[i].checked){
					addArray.push(changeArray[i].id);
				}else{
					delArray.push(changeArray[i].id);
				}
			}
		}
		if(addArray.length!=0||delArray!=0){
			var addUserIds = addArray.length==0?"":"'"+addArray.join("','")+"'";
			var delUserIds = delArray.length==0?"":"'"+delArray.join("','")+"'";
			app.utils.getData(app.url.role.saveUserRoles,{"roleId":roleId,"addUserIds":addUserIds,"delUserIds":delUserIds}, function(dt) {
				$state.go('app.role.list');
		    	$scope.$parent.reload();
		    });
		}else{
			$state.go("app.role.list");
		}
	};
	
	$scope.return = function(){
    	$state.go('app.role.list');
    	$scope.$parent.reload();
	};
	
	
	
	
	
	
});