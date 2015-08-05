app.controller("busAtomEditController",["$scope","$state","$http",'checkUniqueService',function($scope,$state,$http,checkUniqueService){
	
	$scope.treeAPI.hiddenBusTypeTree();
	
	$scope.formData = {};
	var id ;//所查看数据的id
	//判断时候有父controller中是否有ids 并获取中第一个id
	if($scope.busAtomIds.length!=0){
		id = $scope.busAtomIds[0];
		$scope.clearBusAtomIds();//清除数据
	}else{
		$state.go("app.busatom.list");
	}
	
	//从服务端获取数据
	$http({
		url:'base/busAtomAction!detailsBusAtomByFid.action',
		method:'get',
		params:{
			fid : id
		}
	}).then(function(resp){
		var code = resp.data.code ;
		if(code == 1){//代表成功
			$scope.formData =  resp.data.details ;
			$scope.formData.autoPartAllName = resp.data.details.autoPart.partName +"" +
					"_"+resp.data.details.autoPart.brandName+"_"+resp.data.details.autoPart.spec+"_" +
							""+resp.data.details.autoPart.model;
			$scope.formData.chooseAutoPartName='选择';
			$scope.formData.chooseAutoPartButton=false;
			$scope.formData.chooseBusItemName='选择';
			$scope.formData.chooseBusItemButton=false;
			$scope.formData.itemCode = resp.data.details.busItem.itemCode ;
			$scope.formData.itemName = resp.data.details.busItem.itemName ;
			$scope.formData.fItemId = resp.data.details.busItem.fid ;//用来在服务端进行更行操作的字段
			$scope.formData.autoPartId = resp.data.details.autoPart.id;
			$scope.formData.autoPart = undefined;
			$scope.formData.busItem = undefined;
			$scope.formData.updateTime = undefined ;
		}
	});
	
	
	
	/**
	 * 检测所需要保存的atomcode是否唯一
	 */
	$scope.atomCodeIsUnique = true;
	$scope.notUniqueMessage = "已存在";
	$scope.checkAtomCodeIsUnique = function(fid,atomCode){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkBusAtomCodeUnique(fid,atomCode).then(function(resp){
			if(resp.data.code==1){
				$scope.atomCodeIsUnique = true;
			}else{
				$scope.atomCodeIsUnique = false;
			}
		});
	}
	$("#atomCode").change(function(e){
		if($scope.form.atomCode.$invalid){
			$("#atomCode").css("border-color","red");
		}else{
			$("#atomCode").removeAttr("style");
		}
		if($scope.form.atomCode.$valid){
			console.info($scope.formData);
			$scope.checkAtomCodeIsUnique($scope.formData.fid,($scope.formData.busTypeCode || '')+$(e.target).val());
		}
	});
	
	
	/**
	 * 选择按钮的方法
	 */
	$scope.choose = function(){
		if(!$scope.formData.chooseAutoPartButton){
			$scope.formData.chooseAutoPartButton = true;
			if(!autoPartChooseTable){
				initAutoPartChooseTable();
			}
			$("#autoPartChooseTablePanel").removeClass("none");
			$scope.formData.chooseAutoPartName = "关闭选择框";
		}else{
			$scope.formData.chooseAutoPartButton = false ;
			$("#autoPartChooseTablePanel").addClass("none");
			$scope.formData.chooseAutoPartName = "选择";
		}
	}
	
	$scope.chooseItem = function(){
		if(!$scope.formData.chooseBusItemButton){
			$scope.formData.chooseBusItemButton = true;
			if(!busItemChooseTable){
				initBusItemChooseTable();
			}
			$("#busItemChooseTablePanel").removeClass("none");
			$scope.formData.chooseBusItemName = "关闭选择框";
		}else{
			$scope.formData.chooseBusItemButton = false ;
			$("#busItemChooseTablePanel").addClass("none");
			$scope.formData.chooseBusItemName = "选择";
		}
	}
	/**
	 * 初始化配件列表方法
	 */
	var autoPartChooseTable ;
	function initAutoPartChooseTable(){
		autoPartChooseTable = $("#autoPartChooseTable").dataTable({
			"bServerSide":true,
			"sAjaxSource":"base/autoPartAction!listAutoPart.action",
			"sAjaxDataProp":"data",
			"aoColumns": [{
		    	"mDataProp":"partName"  
		      },{
		    	"mDataProp":"brandName"  
		      },{
		    	"mDataProp":"spec"  
		      },{
		    	"mDataProp":"model"  
		      },{
		    	"mDataProp":"eunitPrice"  
		      },{
		    	"mDataProp":"isActivity",
		    	"render":function(param){
		        	//0=不参加（默认），1=参加
		        	switch(param){
		        	case 0:
		        		return "不参加";break;
		        	case 1:
		        		return "参加";break;
		        	default:
		        		return "";break;
		        	}}
		      }],"oLanguage": {
	              "sLengthMenu": "每页 _MENU_ 条",
	              "sZeroRecords": "没有找到符合条件的数据",
	              "sProcessing": "&lt;img src=’./loading.gif’ /&gt;",
	              "sInfo": "当前第 _START_ - _END_ 条，共 _TOTAL_ 条",
	              "sInfoEmpty": "没有记录",
	              "sInfoFiltered": "(从 _MAX_ 条记录中过滤)",
	              "sSearch": "搜索",
	              "oPaginate": {
	                "sFirst": "<<",
	                "sPrevious": "<",
	                "sNext": ">",
	                "sLast": ">>"
	              }
	            }, "fnCreatedRow": function(nRow, aData, iDataIndex){
	        	  $(nRow).attr("data-id",aData['id']);
	        	  $(nRow).click(function(){
	        		  clickTr(aData);
	        	  });
	          }
		});
	}
	
	/**
	 * 初始化服务项列表方法
	 */
	var busItemChooseTable ;
	function initBusItemChooseTable(){
		busItemChooseTable = $("#busItemChooseTable").dataTable({
			"bServerSide":true,
			"sAjaxSource":"base/busItemAction!listBusItem.action",
			"sAjaxDataProp":"data",
			"aoColumns": [{
		    	"mDataProp":"itemCode"  
		      },{
		    	"mDataProp":"itemName"  
		      },{
		    	"mDataProp":"actualPrice"  
		      },{
		    	"mDataProp":"useState"  ,
		    	"render":function(param){
		    		//0=初始化、1=待审核、2=发布（审核通过）、3=停止/下架、4=强制下架（违规）
		    		switch(param){
		        	case 0:
		        		return "初始化";break;
		        	case 1:
		        		return "待审核";break;
		        	case 2:
		        		return "发布（审核通过）";break;
		        	case 3:
		        		return "停止/下架";break;
		        	case 4:
		        		return "强制下架（违规）";break;
		        	default:
		        		return "";break;
		        	}
		    	}
		      },{
		    	"mDataProp":"isActivity",
		    	"render":function(param){
		        	//0=不参加（默认），1=参加
		        	switch(param){
		        	case 0:
		        		return "不参加";break;
		        	case 1:
		        		return "参加";break;
		        	default:
		        		return "";break;
		        	}}
		      }],"oLanguage": {
	              "sLengthMenu": "每页 _MENU_ 条",
	              "sZeroRecords": "没有找到符合条件的数据",
	              "sProcessing": "&lt;img src=’./loading.gif’ /&gt;",
	              "sInfo": "当前第 _START_ - _END_ 条，共 _TOTAL_ 条",
	              "sInfoEmpty": "没有记录",
	              "sInfoFiltered": "(从 _MAX_ 条记录中过滤)",
	              "sSearch": "搜索",
	              "oPaginate": {
	                "sFirst": "<<",
	                "sPrevious": "<",
	                "sNext": ">",
	                "sLast": ">>"
	              }
	            },"fnCreatedRow": function(nRow, aData, iDataIndex){
		        	  $(nRow).attr("data-id",aData['itemCode']);
		        	  $(nRow).click(function(){
		        		  clickBusItemTr(aData);
		        	  });
		          }
		});
	}
	
	/**
	 * 点击一行触发的事件
	 */
	function clickTr(aData){
		var id = aData['id'];
		$scope.formData.autoPartId = id;
		$scope.formData.autoPartAllName = aData['partName']+"+"+aData['brandName']+"+"+aData['spec']+"+"+aData['model'] ;
		$("#autoPartAllName").val(aData['partName']+"+"+aData['brandName']+"+"+aData['spec']+"+"+aData['model']);
	}
	/**
	 * 点击业务选择列表触发的事件
	 */
	function clickBusItemTr(aData){
		$("#itemCode").val(aData['itemCode']);
		$("#itemName").val(aData['itemName']);
		$scope.formData.itemCode = aData['itemCode'];
		$scope.formData.itemName = aData['itemName'];
		$scope.formData.fItemId = aData['fid'];
		console.info($scope.formData.itemCode);
	}
	
	
	/**
	 * 保存方法
	 */
	$scope.submit = function(){
		$http({
			url:"base/busAtomAction!savaBusAtom.action",
			method:"post",
			data:$scope.formData
		}).then(function(resp){
			var code = resp.data.code ;
			if(code == 1){//成功
				$state.go("app.busatom.list");
			}else{
				alert("修改失败");
			}
		});
	}
	
	/**
	 * 取消方法
	 */
	$scope.cancel = function(){
		$state.go("app.busatom.list");
	}
	
	
	//方法结束
}]);