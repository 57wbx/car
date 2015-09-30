app.controller("autoPartModelController",['$scope','$modalInstance','$http','$timeout','dataTableSearchService',function($scope,$modalInstance,$http,$timeout,dataTableSearchService){
	
	$scope.search = {};
	/**
	 * 延迟加载配件选择列表
	 */
	$timeout(initAll,30);
	
	function initAll(){
		initAutoPartTable();
		$scope.treeAPI.clickTreeListReload = function(busTypeCode){
			if(autoPartChooseTable){
				autoPartChooseTable.ajax.reload();
			}
		}
	}
	/**
	 * 初始化配件列表
	 */
	var autoPartChooseTable ;
	function initAutoPartTable(){
		autoPartChooseTable = $("#autoPartChooseTableModel").on('preXhr.dt', function ( e, settings, data ){
			if($scope.busTypeTree){
				data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
			}
			data.partName = $scope.search.partName ;
			data.brandName = $scope.search.brandName ;
			data.spec = $scope.search.spec ;
		}).DataTable({
			"bServerSide":true,
			"sAjaxSource":"base/autoPartAction!listAutoPart.action",
			"sAjaxDataProp":"data",
			"sServerMethod": "POST",
			"dom": '<"top">rt<"bottom"ip><"clear">',
			"aoColumns": [{
	    	  "render":function(){
	    		  return "<button class='btn btn-info btn-sm'>选择</button>";
	    	  }},{
		    	"mDataProp":"partName"  
		      },{
		    	"mDataProp":"brandName"  
		      },{
		    	"mDataProp":"spec"  
		      },{
		    	"mDataProp":"model"  
		      },{
		    	"mDataProp":"yunitPrice"  
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
		      }
		      ], "fnCreatedRow": function(nRow, aData, iDataIndex){
	        	  $(nRow).attr("data-id",aData['id']);
	        	  $(nRow).find("Button").click(function(){
	        		  chooseAutoPart(aData);
	        	  });
	            },  "drawCallback": function( settings ) {
		        },
		            "initComplete":function(settings,json){
		            	initSearchDiv(settings,json);
		        }
		});
	}
	
	//初始化搜索框
	function initSearchDiv(settings,json){
		dataTableSearchService.initSearch([
			  {formDataName:'search.partName',placeholder:'配件名称'},
			  {formDataName:'search.brandName',placeholder:'品牌'},
			  {formDataName:'search.spec',placeholder:'规格'}
		],$scope,settings,autoPartChooseTable);
	}
	
	/**
	 * 选择其中的配件
	 */
	function chooseAutoPart(aData){
		console.info("选择的配件信息为：",aData);
		$modalInstance.close(aData);
	}
	
	/**
	 * 关闭model的方法
	 */
	$scope.cancel = function() {
	    $modalInstance.dismiss('cancel');
	};
/**
 * 结束controller
 */
}]);