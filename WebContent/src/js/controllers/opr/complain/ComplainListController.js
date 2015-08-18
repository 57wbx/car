'use strict';

app.controller('complainListController',['$scope','$state','$timeout','$http','$modal','sessionStorageService','dataTableSearchService','orderStateService','complainStateService',
                                      function($scope,$state,$timeout,$http,$modal,sessionStorageService,dataTableSearchService,orderStateService,complainStateService){
	
	$scope.search = {};
	$scope.cache = {};//用来缓存选中的数据，因为在弹框事件需要该数据
	
	var isF5 = true ;
	$scope.complainDataTableProperties = null;
	$scope.needCacheArray = ["complainDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.complainDataTableProperties  = sessionStorageService.getItemObj("complainDataTableProperties");
	
	$scope.setComplainDataTableProperties = function(obj){
		$scope.complainDataTableProperties = obj;
		sessionStorageService.setItem("complainDataTableProperties",obj);
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
	
	$scope.$evalAsync(initDataTable);
	
	var complainTable ;
	function initDataTable(){
		
		complainTable = $("#complainTable").on('preXhr.dt', function ( e, settings, data ){
		if(isF5){
			isF5 = false ;
			var oldData = sessionStorageService.getItemObj("complainDataTableProperties"); 
			if(oldData){
				angular.copy(oldData,data);
				$scope.search.dealState = data.dealState;
				$scope.search.objType = data.objType ;
				$scope.search.objName = data.objName ;
			}
		}else{
			data.dealState = $scope.search.dealState;
			data.objType = $scope.search.objType;
			data.objName = $scope.search.objName;
			$scope.setComplainDataTableProperties(data);
		}
		
	}).DataTable({
		"sAjaxSource":"opr/complainAction!listComplain.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
    	 "sServerMethod": "POST",
    	"pageLength":10,
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
          },
    	  "aoColumns": [{
            "mDataProp": "objType",
            "render":function(param){
            	return complainStateService.getObjType(param);
            }
          }, {
            "mDataProp": "objName",
          }, {
            "mDataProp": "complainTime",
          }, {
        	  "mDataProp": "complainReason",
          }, {
        	  "render":function(data,type,row){
                	if(row.complainUser){
                		return row.complainUser.mySign;
                	}else{
                		return "";
                	}
                }
          }, {
        	  "mDataProp": "dealState",
        	  "render":function(param){
        		  return complainStateService.getDealStateWithStyle(param);
        	  }
          }, {
        	  "mDataProp": "dealSuggestion"
          }, {
        	  "mDataProp": "dealResult"
          }, {
        	  "mDataProp": "dealTime"
          }, {
        	  "render":function(data,type,row){
              	if(row.dealUser){
              		return row.dealUser.name;
              	}else{
              		return "";
              	}
              }
          },{
        	  "render":function(){
        		  return "<button class='btn btn-default'>处理</button>";
        	  }
          }],
          "fnCreatedRow": function(nRow, aData, iDataIndex){
        	  $(nRow).attr("data-id",aData['id']);
	            $(nRow).find("button").click(function(e){
	            	showModal(nRow,aData);
	            });
	            $(nRow).find("a").click(function(e){
	            	showModal(nRow,aData);
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
          },
          "initComplete":function(settings,json){
          	if( $scope.complainDataTableProperties){
          		var pageIndex = $scope.complainDataTableProperties.iDisplayStart/$scope.complainDataTableProperties.iDisplayLength;
          		complainTable.page(pageIndex).draw(false);
          	}
          	initSearchDiv(settings,json);
          }
	});
	}//结束初始化方法
	
	//初始化搜索框
	var initSearchDiv = function(settings,json){
		dataTableSearchService.initSearch([
													{
														   formDataName:'search.dealState',
														   label:"处理状态",
														   options:[{label:"全部"},{
															   value:0,
															   label:"未处理"
														   	},{
														   		value:1,
														   		label:"已处理"
														   	}]
													},{
														   formDataName:'search.objType',
														   label:"对象类型",//1=门店、2=技工、3=（服务）、4=套餐，5=其他
														   options:[{label:"全部"},{
															   value:1,
															   label:"门店"
														   	},{
														   		value:2,
														   		label:"技工"
														   	},{
														   		value:3,
														   		label:"服务"
														   	},{
														   		value:4,
														   		label:"套餐"
														   	},{
														   		value:5,
														   		label:"其他"
														   	}]
													},
              {formDataName:'search.objName',placeholder:'投诉名称'},
		],$scope,settings,complainTable);
	}
	
	
	/**
	 * 弹窗事件
	 */
	var showModal = function(nRow,aData){
		var modalInstance = $modal.open({
   	     templateUrl: 'src/tpl/opr/complain/deal_complain.html',
   	     size: 'lg',
   	     backdrop:true,
   	     controller:"dealComplainController",
   	     resolve: {
   	    	id: function () {
       	           return  aData['id'];
       	         }
   	     }
   	   });
		/**
		 * 弹窗关闭事件
		 */
		modalInstance.result.then(function (name) {
			complainTable.ajax.reload();
    	});
	}
	
//结束	
}]);