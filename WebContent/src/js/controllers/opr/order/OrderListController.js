'use strict';

app.controller('orderListController',['$scope','$state','$timeout','$http','$modal','sessionStorageService','dataTableSearchService','orderStateService',
                                      function($scope,$state,$timeout,$http,$modal,sessionStorageService,dataTableSearchService,orderStateService){
	
	$scope.search = {};
	$scope.cache = {};//用来缓存选中的数据，因为在弹框事件需要该数据
	
	var isF5 = true ;
	$scope.orderDataTableProperties = null;
	$scope.needCacheArray = ["orderDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.orderDataTableProperties  = sessionStorageService.getItemObj("orderDataTableProperties");
	
	$scope.setOrderDataTableProperties = function(obj){
		$scope.orderDataTableProperties = obj;
		sessionStorageService.setItem("orderDataTableProperties",obj);
	}
	
	//单击表格中的一行应该调用这个方法
	function clickTr(e,inp){
	      var evt = e || window.event;
	      //evt.preventDefault();
	      evt.stopPropagation();
		
		var target = $(inp.parents("tr")['0']);
		var id = target.data('id');//获取这一表格行的id
		//执行选择效果
		if(target.find("input")['0'].checked){
			target.find("input")['0'].checked = true;
			//将id保存在上级scope中的
			if($scope.rowIds.indexOf(id)==-1){
				$scope.rowIds.push(id);
			}
		}else{
			target.find("input")['0'].checked = false ;
			//移除指定的id
			var index = $scope.rowIds.indexOf(id);
			if(index !== -1 ) $scope.rowIds.splice(index, 1);
		}
		//标题的选择框
		var inputsCount = target.parents("table").find("tbody input").length;
		var checkCount = target.parents("table").find("tbody input:checked").length;
		
		if(inputsCount == checkCount){
			target.parents(".dataTables_scroll").find(" thead .i-checks input").prop("checked",true);//.dataTables_scroll 是datatable插件自动生成的css
		}else{
			target.parents(".dataTables_scroll").find(" thead .i-checks input").prop("checked",false);//.dataTables_scroll 是datatable插件自动生成的css
		}
		$scope.setButtonStatus();//设置按钮状态
		console.info("-------------------------------点击之后的数据");
		console.info($scope.rowIds);
	}
	
	
	/**
	 * 点击一行的时候，显示子项信息
	 */
	function showBusAtoms(target){
		var id = target.data('id');//获取这一表格行的id
		//点击一行变色的方法
		if ( target.hasClass('selected') ) {
			target.removeClass('selected');
			target.find("td").css("background-color","white");
			getBusAtoms("");//设置空id，这样从后台获取的数据为空
			$scope.setCanEdit(false);//设置不可以查看和修改
			$scope.cache.rowData = null ;
        }else {
        	busItemTable.$('tr.selected').find("td").css("background-color","white");
        	busItemTable.$('tr.selected').removeClass('selected');
        	target.addClass('selected');
        	target.find("td").css("background-color","#b0bed9");
        	getBusAtoms(id);
        	$scope.setCanEdit(true,id);//设置可以查看和修改
        }
	}
	
	$scope.$evalAsync(initDataTable);
	
	var busItemTable ;
	function initDataTable(){
		
	busItemTable = $("#orderTable").on('preXhr.dt', function ( e, settings, data ){
		if(isF5){
			isF5 = false ;
			var oldData = sessionStorageService.getItemObj("orderDataTableProperties"); 
			if(oldData){
				angular.copy(oldData,data);
//				data = oldData ;
//				$scope.search.itemCode = data.itemCode ;
				$scope.search.id = data.id;
				$scope.search.payOrderCode = data.payOrderCode ;
				$scope.search.userName = data.userName ;
				$scope.search.workerName = data.workerName ;
			}
		}else{
//			data.itemCode = $scope.search.itemCode ;
			data.id = $scope.search.id;
			data.payOrderCode = $scope.search.payOrderCode;
			data.userName = $scope.search.userName;
			data.workerName = $scope.search.workerName;
			$scope.setOrderDataTableProperties(data);
		}
		
	}).DataTable({
		"sAjaxSource":"opr/orderAction!listOrderByLoginCarShop.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
    	 "sServerMethod": "POST",
    	"pageLength":5,
    	"ordering":false,
    	"dom": '<"top">rt<"bottom"ip><"clear">',
    	"oLanguage": {
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
          },scrollX:true,
    	  "aoColumns": [{
            "orderable": false,
            "render": function(param){
              return '<label class="i-checks"><input type="checkbox"><i></i></label>';
            }
          }, {
            "mDataProp": "id",
          }, {
            "mDataProp": "payOrderCode",
          }, {
            "mDataProp": "orderName",
          }, {
            "render":function(data,type,row){
            	if(row.tigUsers){
            		return row.tigUsers.mySign;
            	}else{
            		return "";
            	}
            }
          }, {
        	  "render":function(data,type,row){
              	if(row.worker){
              		return row.worker.name;
              	}else{
              		return "";
              	}
              }
          }, {
            "mDataProp": "orderTime",
          }, {
            "mDataProp": "expectTime",
          }, {
                "mDataProp": "serviceType",
                "render":function(param){
                	return orderStateService.getServiceType(param);
                }
            }, {
              "mDataProp": "orderState",
              "render":function(param){
            	  return orderStateService.getOrderState(param);
              }
            }, {
              "mDataProp": "orderNum",
            }, {
              "mDataProp": "totalAmount",
            }, {
              "mDataProp": "payType",
              "render":function(param){
            	  return orderStateService.getPayType(param);
              }
            }, {
              "mDataProp": "payState",
              "render":function(param){
            	  return orderStateService.getPayState(param);
              }
            }, {
              "mDataProp": "takeFare",
            }, {
              "mDataProp": "sendFare",
            }, {
              "mDataProp": "endTime",
            }, {
              "mDataProp": "dealType",
              "render":function(param){
            	  return orderStateService.getDealType(param);
              }
            }],
          "fnCreatedRow": function(nRow, aData, iDataIndex){
        	  $(nRow).attr("data-id",aData['id']);
//        	  $(nRow).dblclick(function(){
//        		  $scope.setCanEdit(true,$(this).data('id'));
//        		  $scope.seeDetails($(this).data('id'));//调用上级controller中的查看方法
//        	  });
        	  $(nRow).click(function(e){
        		  if(e.target.nodeName=="TD"||e.target.nodeName=="td"){
        			  showBusAtoms($(this));
        			  $scope.cache.rowData = aData ;
        		  }
        	  });
        	  $(nRow).find("input").click(function(e){
        		  clickTr(e,$(this));
        	  });
          },
          "drawCallback":function(setting){
        	  $scope.clearRowIds();//首先清空上级controller中的ids
        	  //清空服务子项的内容
//        	  getBusAtoms("");//清空服务子项的内容
        	  $(this).parents(".dataTables_scroll").find("thead .i-checks input").prop("checked",false);
        	  //设置标题全选按钮事件
        	  $(this).parents(".dataTables_scroll").find("thead .i-checks input").off().click(function(){
        		  var inputs = $(setting.nScrollBody).find("tbody input");
        		  if($(setting.nScrollHead).find(".i-checks input").prop("checked")){
        			  inputs.prop("checked",true);
        			  $scope.clearRowIds();
        			  for(var i=0;i<inputs.length;i++){
        				  $scope.rowIds.push($(inputs[i]).parents("tr").data('id'));
        			  }
        			  $scope.setButtonStatus();//设置按钮状态
        		  }else{
        			  inputs.prop("checked",false);
        			  $scope.clearRowIds();//调用上级controller中的清空ids的方法
        		  }
        	  });
        	  
//        	  $('#busItemTable tbody').on( 'click', 'tr', function () {
//        		  console.info("-------------");
//        		  console.info($(this));
//        	        if ( $(this).hasClass('selected') ) {
//        	            $(this).removeClass('selected');
//        	            $(this).find("td").css("background-color","white");
//        	        }
//        	        else {
//        	        	busItemTable.$('tr.selected').find("td").css("background-color","white");
//        	            $(this).addClass('selected');
//        	            $(this).find("td").css("background-color","#b0bed9");
//        	        }
//        	    } );
          },
          "initComplete":function(settings,json){
          	if( $scope.shopItemDataTableProperties){
          		var pageIndex = $scope.shopItemDataTableProperties.iDisplayStart/$scope.shopItemDataTableProperties.iDisplayLength;
          		busItemTable.page(pageIndex).draw(false);
          	}
          	initSearchDiv(settings,json);
          }
	});
	}//结束初始化方法
	
	//初始化搜索框
	var initSearchDiv = function(settings,json){
		dataTableSearchService.initSearch([
              {formDataName:'search.id',placeholder:'订单编号'},
              {formDataName:'search.payOrderCode',placeholder:'订单编号(第三方支付)'},
			  {formDataName:'search.userName',placeholder:'客户名称'},
			  {formDataName:'search.workerName',placeholder:'师傅名称'}
		],$scope,settings,busItemTable);
	}
	
	
	$scope.API.distributionTask = function(){
		if(	$scope.cache.rowData && !$scope.cache.rowData.worker){
			showModal($scope.cache.rowData);
		}else{
			alert("该记录已经分配师傅了！");
		}
	}
	/**
	 * 弹窗事件
	 */
	var showModal = function(aData){
		var modalInstance = $modal.open({
   	     templateUrl: 'src/tpl/opr/order/choose_worker.html',
   	     size: 'lg',
   	     backdrop:true,
   	     controller:"chooseWorkerController",
   	     resolve: {
   	    	orderId: function () {
       	           return  aData['id'];
       	         }
   	     }
   	   });
		/**
		 * 弹窗关闭事件
		 */
		modalInstance.result.then(function (name) {
			busItemTable.ajax.reload();
    	});
	}
	
	/**
	 * 显示子项信息列表
	 */
	var busAtomTable = $("#orderTrackTable").DataTable({
		"info":false,
		"lengthChange":false,
		"paging":false,
		"searching":false,
		"ordering":false,
		"pageLength":20,
		"sAjaxSource":"shop/shopAtomAction!getShopAtomsByItemId.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
    	"oLanguage": {
            "sLengthMenu": "每页 _MENU_ 条",
            "sZeroRecords": "该订单还没有订单跟踪信息",
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
          },scrollX:true,
    	  "aoColumns": [ {
            "mDataProp": "orderState",
            "render":function(param){
            	return orderStateService.getOrderState(param);
            }
          },{
        	"mDataProp":"operatorName"  
          },{
        	"mDataProp":"operTime"  
          },{
        	"mDataProp":"exReason"  
          }]
	});
	
	/**
	 * 更具服务记录获取子项信息的方法
	 * 
	 */
	function getBusAtoms(busItemId){
		var url = "opr/orderAction!getOrderTrackByOrder.action?id="+busItemId ;
		busAtomTable.ajax.url(url).load();
	}
	
	
	
//结束	
}]);