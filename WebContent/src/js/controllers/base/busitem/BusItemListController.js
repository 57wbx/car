'use strict';

app.controller('busItemListController',['$scope','$state','$timeout','$http','dataTableSearchService','sessionStorageService',function($scope,$state,$timeout,$http,dataTableSearchService,sessionStorageService){
	
	$scope.search = {};
	
	var isF5 = true ;
	$scope.busItemDataTableProperties = null;
	$scope.needCacheArray = ["busItemDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.busItemDataTableProperties  = sessionStorageService.getItemObj("busItemDataTableProperties");
	
	$scope.setBusItemDataTableProperties = function(obj){
		$scope.busItemDataTableProperties = obj;
		sessionStorageService.setItem("busItemDataTableProperties",obj);
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
        }else {
        	busItemTable.$('tr.selected').find("td").css("background-color","white");
        	busItemTable.$('tr.selected').removeClass('selected');
        	target.addClass('selected');
        	target.find("td").css("background-color","#b0bed9");
        	getBusAtoms(id);
        	$scope.setCanEdit(true,id);//设置可以查看和修改
        }
	}
	
	$scope.treeAPI.showBusTypeTree();
	/**
	 * 初始化该页面的api
	 * 提供给buspapckageController调用
	 * 注销其他方法
	 */
	$scope.treeAPI.clickTreeAddOrUpdateReload = null;//注销其他的方法
	$scope.treeAPI.clickTreeListReload = function(busTypeCode){
		//绑定一次后后面所有的请求都具有该参数了
		busItemTable.ajax.reload();//重新加载数据
	}
	
	$scope.$evalAsync(initDataTable);
	
	var busItemTable ;
	function initDataTable(){
		
	busItemTable = $("#busItemTable").on('preXhr.dt', function ( e, settings, data ){
		if(isF5){
			isF5 = false ;
			var oldData = sessionStorageService.getItemObj("busItemDataTableProperties"); 
			if(oldData){
				angular.copy(oldData,data);
				$scope.search.itemName = data.itemName ;
				$scope.search.itemDes = data.itemDes ;
				$scope.search.itemCode = data.itemCode ;
				$scope.search.isActivity = data.isActivity ;
				$scope.busTypeTree.selectedTypeCode = data.busTypeCode ;
			}
		}else{
			data.itemName = $scope.search.itemName;
			data.itemDes = $scope.search.itemDes;
			data.itemCode = $scope.search.itemCode ;;
			data.isActivity = $scope.search.isActivity;
			data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
			$scope.setBusItemDataTableProperties(data);
		}
	}).DataTable({
		"sAjaxSource":"base/busItemAction!listBusItem.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
    	"sServerMethod": "POST",
    	"pageLength":5,
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
              "render":function( data, type, row ){
              	return row.busItemImgs.length;
              }
            }, {
            "mDataProp": "itemCode",
          }, {
            "mDataProp": "itemName",
          }, {
            "mDataProp": "itemDes",
          }, {
            "mDataProp": "standardPrice",
          }, {
            "mDataProp": "actualPrice",
          }, {
            "mDataProp": "workHours",
          }, {
            "mDataProp": "autoPartsPrice",
          }, {
          	"mDataProp":"useState",
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
            	}}
          },{
        	//0=不参加（默认），1=参加
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
            }, {
                "mDataProp": "starTime",
            }, {
              "mDataProp": "endTime",
            }, {
              "mDataProp": "memo",
            }],
          "fnCreatedRow": function(nRow, aData, iDataIndex){
        	  $(nRow).attr("data-id",aData['fid']);
        	  $(nRow).dblclick(function(){
        		  $scope.setCanEdit(true,$(this).data('id'));
        		  $scope.seeDetails($(this).data('id'));//调用上级controller中的查看方法
        	  });
        	  $(nRow).click(function(e){
        		  if(e.target.nodeName=="TD"||e.target.nodeName=="td"){
        			  showBusAtoms($(this));
        		  }
        	  });
        	  $(nRow).find("input").click(function(e){
        		  clickTr(e,$(this));
        	  });
          },
          "drawCallback":function(setting){
        	  $scope.clearRowIds();//首先清空上级controller中的ids
        	  //清空服务子项的内容
        	  getBusAtoms("");//清空服务子项的内容
        	  $(this).parents(".dataTables_scroll").find("thead .i-checks input").prop("checked",false);
        	  //设置标题全选按钮事件
        	  $(this).parents(".dataTables_scroll").find("thead .i-checks input").click(function(){
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
            	if( $scope.busItemDataTableProperties){
            		var pageIndex = $scope.busItemDataTableProperties.iDisplayStart/$scope.busItemDataTableProperties.iDisplayLength;
            		busItemTable.page(pageIndex).draw(false);
            	}
            	initSearchDiv(settings,json);
            }
	});
	}//结束初始化方法
	
	//初始化搜索框
	var initSearchDiv = function(settings,json){
		dataTableSearchService.initSearch([
			  {formDataName:'search.itemCode',placeholder:'服务编号'},
			  {formDataName:'search.itemName',placeholder:'服务名称'},
			  {formDataName:'search.itemDes',placeholder:'服务描述'},
              {
           	   formDataName:'search.isActivity',
           	   label:'是否参加聚惠',
           	   options:[{label:'全部'},{
           		//0=加盟店、1=合作店、3=直营店、4=中心店（区域旗舰店）
			               		   value:0,
			               		   label:"不参加"
			               	   	},{
		                		   value:1,
		                   		   label:"参加"
		                   			   }
               	]
              }
		],$scope,settings,busItemTable);
	}
	
	
	/**
	 * 显示子项信息列表
	 */
	var busAtomTable = $("#busAtomTable").DataTable({
		"info":false,
		"lengthChange":false,
		"paging":false,
		"searching":false,
		"pageLength":20,
		"sAjaxSource":"base/busAtomAction!getBusAtomsByItemId.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
    	"oLanguage": {
            "sLengthMenu": "每页 _MENU_ 条",
            "sZeroRecords": "该服务没有服务子项信息",
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
            "mDataProp": "atomCode",
          },{
        	"mDataProp":"atomName"  
          },{
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
        	"mDataProp":"autoParts"  
          },{
        	"mDataProp":"eunitPrice1"  
          },{
        	"mDataProp":"yunitPrice"  
          },{
        	//0=不参加（默认），1=参加
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
            }]
	});
	
	/**
	 * 更具服务记录获取子项信息的方法
	 * 
	 */
	function getBusAtoms(busItemId){
		var url = "base/busAtomAction!getBusAtomsByItemId.action?fItemId="+busItemId ;
		busAtomTable.ajax.url(url).load();
	}
	
	
	
//结束	
}]);