app.controller("operatorLogController",['$scope','dataTableSearchService','sessionStorageService','operatorLogStateService',function($scope,dataTableSearchService,sessionStorageService,operatorLogStateService){
	
	$scope.search = {};
	
	var isF5 = true ;
	$scope.operatorLogDataTableProperties = null;
	$scope.needCacheArray = ["operatorLogDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.operatorLogDataTableProperties  = sessionStorageService.getItemObj("operatorLogDataTableProperties");
	
	$scope.setOperatorLogDataTableProperties = function(obj){
		$scope.operatorLogDataTableProperties = obj;
		sessionStorageService.setItem("operatorLogDataTableProperties",obj);
	}
	
	/**
	 * 初始化表格
	 */
	var dTable = initTable();
	
	function initTable(){
		return $("#operatorTable").on('preXhr.dt', function ( e, settings, data ){
			if(isF5){
				isF5 = false ;
				var oldData = sessionStorageService.getItemObj("operatorLogDataTableProperties"); 
				if(oldData){
					angular.copy(oldData,data);
					data = oldData ;
					$scope.search.operatorName = data.operatorName ;
					$scope.search.ip = data.ip ;
					$scope.search.operationType = data.operationType ;
				}
			}else{
				data.operatorName = $scope.search.operatorName ;
				data.ip = $scope.search.ip;
				data.operationType = $scope.search.operationType;
				$scope.setOperatorLogDataTableProperties(data);
			}
		}).DataTable({
			"sAjaxSource":"sys/operatorLogAction!listOperatorLog.action",
	    	"bServerSide":true,
	    	"sAjaxDataProp":"data",
	    	 "dom": '<"top">rt<"bottom"ip><"clear">',
	    	 "sServerMethod": "POST",
	    	 scrollX:true,
	    	 "aoColumns": [{
			        "mDataProp": "operatorName"
			      },{
			        "mDataProp": "operationType",
			        "render":function(param){
			        	return operatorLogStateService.getOperationType(param);
			        }
			      },{
			        "mDataProp": "objName",
			        "render":function(param){
			        	return operatorLogStateService.getObjName(param);
			        }
			      },{
			        "mDataProp": "operationTime"
			      },{
			        "mDataProp": "ip"
			      }],
			      "drawCallback": function( settings ) {
			        },"initComplete":function(settings,json){
			            	if( $scope.operatorLogDataTableProperties){
			            		var pageIndex = $scope.operatorLogDataTableProperties.iDisplayStart/$scope.operatorLogDataTableProperties.iDisplayLength;
					            dTable.page(pageIndex).draw(false);
			            	}
			            	initSearchDiv(settings,json);
			            }
		});
	}
	
	//初始化搜索框
	var initSearchDiv = function(settings,json){
		dataTableSearchService.initSearch([
		      {formDataName:'search.operatorName',placeholder:'操作者姓名'},
			  {formDataName:'search.ip',placeholder:'ip地址'},
			 {
               	   formDataName:'search.operationType',
               	   label:'操作类型',
               	   options:[{label:'全部'},{
               		//0=加盟店、1=合作店、3=直营店、4=中心店（区域旗舰店）
				               		   value:1,
				               		   label:"新增操作"
				               	   	},{
			                		   value:2,
			                   		   label:"删除操作"
			                   	   	},{
			                    		   value:3,
			                       		   label:"修改操作"
			                       	 }
                   	]
                  }
		],$scope,settings,dTable);
	}
	
	
}]);