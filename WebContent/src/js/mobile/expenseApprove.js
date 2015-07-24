'use strict';
//费用报销审批流程
app.controller('ExpenseBillController', function($location,$rootScope, $scope, $state, $timeout) {
	$("#myDiv").parent().css("background-color","white");
	$scope.formData = {};
	$rootScope.id = "";
	function init(){//页面加载初始化信息
		var url = $location.$$absUrl;
		console.log(url);
		var userid="",id="";
		if(url.indexOf("?")>0){
			url = url.substring(url.indexOf("?")+1,url.length);
			var arr = url.split("&");
			if(arr&&arr.length>1){
				userid = arr[0].substring(arr[0].indexOf("=")+1,arr[0].length);
				id = arr[1].substring(arr[1].indexOf("=")+1,arr[1].length);
				console.log(userid+","+id);
			}
		}
		url = app.url.expenseBill.loadExpenseBillById;
		$.ajax({ 
    		type: "post", 
    		url: url,
            cache: false, 
            async: false, //默认是true：异步，false：同步。
            data:{"userid":userid,"id":id},
            success: function(dt){ 
            	$scope.formData = dt;
            	$rootScope.id = dt.id;
            	$scope.entrys = dt.rows;
            }
    	});
	}
	init();
	function bindStyle(){
		$(".stateBtn").bind("mousedown",function(e){
			if(e.target.innerHTML!="更多"){
				$("#btns").slideUp("fast");
			}
			$(".stateBtn").css("background-color","#dddddd");
			$(this).css("background-color","white");
		});
	}
	bindStyle();
	//点击更多
	$scope.showBtns = function(){
		if($("#btns").css("display")=="none"){
			$("#btns").slideDown("fast");
		}else{
			$("#btns").slideUp("fast");
		}
	};
	//同意
	$scope.agree = function(){
		$state.go("expenseApproveAgree");
	};
	//拒绝
	$scope.refuse = function(){
	};
	//审批记录
	$scope.record = function(){
		
	};
	//转交
	$scope.passOn = function(){
	};
	//终止
	$scope.stop = function(){
	};
	//催办
	$scope.reminders = function(){
	};
	
});