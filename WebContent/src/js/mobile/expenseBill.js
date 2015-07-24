'use strict';
//费用报销申请单
app.controller('ExpenseBillController', function($location,$rootScope, $scope, $state, $timeout) {
	$("#myDiv").parent().css("background-color","white");
	if($rootScope.details){
		$scope.formData = $rootScope.details;
		if($rootScope.userF7Id&&$rootScope.userF7Name){
			$scope.formData.auditor = $rootScope.userF7Id;
			$scope.formData.auditorName = $rootScope.userF7Name;
		}
	}else{
		$scope.formData = {};
		$scope.formData.emergent=1;
		init();
	}
	if(!$rootScope.entrys){
		$rootScope.entrys = new Array();
	}else{
		var len = $scope.entrys.length;
		var fpAmount=0;//发票金额
		var bxAmount=0;//报销金额
		for(var i=0;i<len;i++){
			fpAmount += $scope.entrys[i].invoiceAmount;
			bxAmount += $scope.entrys[i].amount;
		}
		$scope.formData.invoiceAmount=fpAmount;//发票金额
		$scope.formData.amount=bxAmount;//借款金额
		$scope.formData.borrowAmount = $scope.formData.borrowAmount?$scope.formData.borrowAmount:0;
		if(bxAmount-$scope.formData.borrowAmount>0){
			$scope.formData.actAmount = bxAmount-$scope.formData.borrowAmount;//实际报销金额
		}else{
			$scope.formData.actAmount = 0;//实际报销金额
		}
	}
	$scope.entrys = $rootScope.entrys;
	$scope.return = function(){
		$state.go('expenseBillList');
	};
	$scope.submit = function(){//提交
		var width = Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth)
		if(!$scope.formData.title){
			app.utils.showMsg("标题不能为空",115);
			return;
		}
		if(!$scope.formData.amount||$scope.formData.amount==0){
			app.utils.showMsg("申请报销金额不能为空",140);
			return;
		}
		if(!$scope.formData.auditor){
			app.utils.showMsg("审批人不能为空",115);
			return;
		}
		var parentJsonStr = JSON.stringify($scope.formData);
		var entrysJsonStr = JSON.stringify($scope.entrys);
		var url = app.url.expenseBill.api.save;
		app.utils.getData(url,{"parentJsonStr":parentJsonStr,"entrysJsonStr":entrysJsonStr}, function(dt) {
			app.utils.showMsg("保存成功",(width-95)/2);
		});
	};
	$scope.showAuditor = function(){//选择审批人
		$rootScope.details=$scope.formData;
		$state.go('userf7');
	};
	$scope.addDetail = function(){//添加明细
		$rootScope.optType="add";
		$rootScope.details=$scope.formData;
		$state.go('expenseBillEntry');
	};
	$scope.borrowAmountChange = function(){//借款金额改变事件
		if($scope.formData.amount&&$scope.formData.amount>=$scope.formData.borrowAmount){
			$scope.formData.actAmount = $scope.formData.amount-$scope.formData.borrowAmount;
		}else{
			$scope.formData.actAmount = 0;
		}
	};
	function init(){//页面加载初始化信息
		var url = $location.$$absUrl;
		console.log(url);
		var userid="";
		if(url.indexOf("?")>0){
			url = url.substring(url.indexOf("?")+1,url.length);
			var arr = url.split("&");
			if(arr&&arr.length>0){
				userid= arr[0].substring(arr[0].indexOf("=")+1,arr[0].length);
			}
		}
		url = app.url.expenseBill.initData;
		$.ajax({ 
    		type: "post", 
    		url: url,
            cache: false, 
            async: false, //默认是true：异步，false：同步。
            data:{"userid":userid},
            success: function(dt){ 
            	$scope.formData.dept=dt.dept;
            	$scope.formData.createTime=dt.date;
            	$scope.formData.user=dt.user;
            	$scope.formData.title="费用报销-"+dt.user+dt.date.replace(/-/g,"");
            }
    	});
	}
	$scope.editDeatil = function(obj){
		$scope.entrys.pop(obj);
		$rootScope.optType = "edit";
		$rootScope.entry = obj;
		$state.go('expenseBillEntry');
	};
	$scope.delDeatil = function(obj){
		$scope.entrys.pop(obj);
	};
});

app.controller('ExpenseBillEntryController', function($rootScope, $scope, $state, $timeout) {
	$("#myDiv").parent().css("background-color","white");
	$scope.formData = {};
	if(!$scope.feeType){
		app.utils.getData(app.url.expenseBill.feeTypeList, function(dt) {
			$scope.feeType=dt;
			$rootScope.feeType=dt;
			if($scope.optType=="edit"){
				$scope.formData = $scope.entry;
				$("#startDate").val($scope.formData.startDate);
			}
		});
	}else{
		if($scope.optType=="edit"){
			$scope.formData = $scope.entry;
			$("#startDate").val($scope.formData.startDate);
		}
	}
	$scope.return = function(){
		$state.go('expenseBill');
	};
	$scope.submit = function(){
		var width = Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth)
		if(!$scope.formData.feeType){
			app.utils.showMsg("费用项目不能为空",125);
			return;
		}
		var startDate = $("#startDate").val();
		if(!startDate){
			app.utils.showMsg("业务日期不能为空",125);
			return;
		}
		if(startDate>=$scope.details.createTime){
			app.utils.showMsg("业务日期不能晚于当前日期",145);
			return;
		}
		$scope.formData.startDate=startDate;
		$scope.formData.invoiceAmount = $scope.formData.invoiceAmount?$scope.formData.invoiceAmount:0;
//		if(!$scope.formData.invoiceAmount||$scope.formData.invoiceAmount<=0){
//			app.utils.showMsg("发票金额不能为空",(width-125)/2);
//			return;
//		}
		if(!$scope.formData.amount||$scope.formData.amount==0){
			app.utils.showMsg("申请报销金额不能为空",140);
			return;
		}
		$scope.formData.feeTypeName=$("#feeType").find("option:selected").text();
		if(!$rootScope.entrys){
			$rootScope.entrys = new Array();
		}
		$rootScope.entrys.push($scope.formData);
		$state.go('expenseBill');
	};
	$timeout(function(){
		if($scope.formData.feeType){
			$("#feeType").val($scope.formData.feeType);
		}
    }, 500);
});
app.controller('ExpenseBillListController', function($rootScope, $scope, $state, $timeout) {
	$scope.return = function(){};
	$scope.add = function(){
		$state.go('expenseBill');
	};
	$(function(){
		
	});
	function stateBtn(){
		$(".stateBtn").bind("click",function(){
			$(".stateBtn").attr("disabled",true);
			$(".stateBtn").css("background-color","#aaaaaa");
			$(this).css("background-color","white");
			//TODO loadData
			app.utils.showMsg("loading...&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",100);
			$(".stateBtn").attr("disabled",false);
		});
	}
	stateBtn();
});