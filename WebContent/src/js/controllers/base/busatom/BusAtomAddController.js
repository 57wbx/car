app.controller("busAtomAddController",['$scope','$state','$http','checkUniqueService',function($scope,$state,$http,checkUniqueService){
	
	$scope.treeAPI.hiddenBusTypeTree();
	
	$scope.formData = {};
	//初始化选中的数据
	$scope.clearBusAtomIds();
	
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
			$scope.checkAtomCodeIsUnique(null,($scope.formData.busTypeCode || '')+$(e.target).val());
		}
	});
	
	/**
	 * 初始化配件选择列表
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
	 * 初始话服务项目列表
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
		        	  $(nRow).attr("data-id",aData['fid']);
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
	}
	/**
	 * 点击保存操作
	 */
	$scope.submit = function(){
		$scope.formData.atomCode = ($scope.formData.busTypeCode || '')+$scope.formData.atomCode;
		$http({
			url:"base/busAtomAction!addBusAtom.action",
			method:'post',
			data : $scope.formData
		}).then(function(resp){
			var code = resp.data.code ;
			if(code == 1){//代表保存成功
				$state.go("app.busatom.list");
			}else{//代表保存失败
				alert("保存失败");
			}
		},function(resp){
			alert("保存出错");
		});
	}
	
	/**
	 * 取消按钮的操作，直接跳转到列表页面上
	 */
	$scope.cancel = function(){
		$state.go("app.busatom.list");
	}
	
	
}]);