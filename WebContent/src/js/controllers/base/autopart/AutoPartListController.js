'use strict';
app.controller('autoPartListController', ['$rootScope','$scope','$state','$timeout','utils','dataTableSearchService','sessionStorageService'
,function($rootScope, $scope, $state,$timeout, utils,dataTableSearchService,sessionStorageService) {
	
	$scope.search = {};
	
	var isF5 = true ;
	$scope.autoPartListDataTableProperties = null;
	$scope.needCacheArray = ["autoPartListDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.autoPartListDataTableProperties  = sessionStorageService.getItemObj("autoPartListDataTableProperties");
	
	$scope.setAutoPartListDataTableProperties = function(obj){
		$scope.autoPartListDataTableProperties = obj;
		sessionStorageService.setItem("autoPartListDataTableProperties",obj);
	}
	
	
	$scope.$evalAsync(initTable);
	
	var data = null;
 
	$scope.treeAPI.showBusTypeTree();
	/**
	 * 初始化该页面的api
	 * 提供给buspapckageController调用
	 * 注销其他方法
	 */
	$scope.treeAPI.clickTreeAddOrUpdateReload = null;//注销其他的方法
	$scope.treeAPI.clickTreeListReload = function(busTypeCode){
		//绑定一次后后面所有的请求都具有该参数了
		dTable.ajax.reload();//重新加载数据
	}
	

  // 初始化表格 jQuery datatable
  var autoPartList, dTable;
  function initTable() {
	autoPartList = $('#autoPartList');
    dTable = autoPartList.on('preXhr.dt', function ( e, settings, data ){
    	if(isF5){
			isF5 = false ;
			var oldData = sessionStorageService.getItemObj("autoPartListDataTableProperties"); 
			if(oldData){
				angular.copy(oldData,data);
//				data = oldData ;
				$scope.search.partName = data.partName ;
				$scope.search.brandName = data.brandName ;
				$scope.search.spec = data.spec ;
				$scope.search.model = data.model ;
				$scope.busTypeTree.selectedTypeCode = data.busTypeCode ;
			}
		}else{
			data.partName = $scope.search.partName ;
			data.brandName = $scope.search.brandName;
			data.spec = $scope.search.spec;
			data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
			data.model = $scope.search.model ;
			$scope.setAutoPartListDataTableProperties(data);
		}
	}).DataTable({
    	"sAjaxSource":"base/autoPartAction!listAutoPart.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
    	 "dom": '<"top">rt<"bottom"ip><"clear">',
    	 "sServerMethod": "POST",
         "aoColumns": [{
        "orderable": false,
        "render": function(param){
          return '<label class="i-checks"><input type="checkbox"><i></i></label>';
        }
      }, {
        "mDataProp": "partCode",
      },{
    	"mDataProp":"partName"  
      },{
    	"mDataProp":"brandName"  
      },{
    	"mDataProp":"spec"  
      },{
    	"mDataProp":"model"  
      },{
    	"mDataProp":"sunitPrice"  
      },{
    	"mDataProp":"eunitPrice"  
      },{
    	"mDataProp":"yunitPrice"  
      },{
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
      },{
    	"mDataProp":"stock"  
      },{
    	"mDataProp":"updateTime"  
      },{
    	"mDataProp":"partSource"  
      },{
    	"mDataProp":"memo"  
      }],
      "fnCreatedRow": function(nRow, aData, iDataIndex){
     	 //初始化每一行的点击行为
         $(nRow).attr('data-id', aData['id']);
         $(nRow).dblclick(function(e, settings) {
             $scope.seeDetails($(this).data('id'));
         });
       },
       "drawCallback": function( settings ) {
    	   dataTableSearchService.initClick(dTable,$scope.rowIds,$scope.setBtnStatus,$scope.seeDetails);
       }, "initComplete":function(settings,json){
       	if( $scope.autoPartListDataTableProperties){
    		var pageIndex = $scope.autoPartListDataTableProperties.iDisplayStart/$scope.autoPartListDataTableProperties.iDisplayLength;
              dTable.page(pageIndex).draw(false);
    	}
    	initSearchDiv(settings,json);
    }
    });
  }
  
//初始化搜索框
	function initSearchDiv(settings,json){
		dataTableSearchService.initSearch([
			  {formDataName:'search.partName',placeholder:'配件名称'},
			  {formDataName:'search.brandName',placeholder:'品牌'},
			  {formDataName:'search.spec',placeholder:'规格'},
			  {formDataName:'search.model',placeholder:'型号'}
		],$scope,settings,dTable);
	}
  
  
}]);