app.controller("loginLogController",['$scope','dataTableSearchService','sessionStorageService',function($scope,dataTableSearchService,sessionStorageService){
	
	$scope.search = {};
	
	var isF5 = true ;
	$scope.loginLogDataTableProperties = null;
	$scope.needCacheArray = ["loginLogDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.loginLogDataTableProperties  = sessionStorageService.getItemObj("loginLogDataTableProperties");
	
	$scope.setLoginLogDataTableProperties = function(obj){
		$scope.loginLogDataTableProperties = obj;
		sessionStorageService.setItem("loginLogDataTableProperties",obj);
	}
	
	/**
	 * 初始化表格
	 */
	var dTable = initTable();
	
	function initTable(){
		return $("#loginTable").on('preXhr.dt', function ( e, settings, data ){
			if(isF5){
				isF5 = false ;
				var oldData = sessionStorageService.getItemObj("loginLogDataTableProperties"); 
				if(oldData){
					angular.copy(oldData,data);
					data = oldData ;
					$scope.search.userName = data.userName ;
					$scope.search.ip = data.ip ;
					$scope.search.clientType = data.clientType ;
				}
			}else{
				data.userName = $scope.search.userName ;
				data.ip = $scope.search.ip;
				data.clientType = $scope.search.clientType;
				$scope.setLoginLogDataTableProperties(data);
			}
		}).DataTable({
			"sAjaxSource":"sys/loginLogAction!listLoginLog.action",
	    	"bServerSide":true,
	    	"sAjaxDataProp":"data",
	    	 "dom": '<"top">rt<"bottom"ip><"clear">',
	    	 "sServerMethod": "POST",
	    	 scrollX:true,
	    	 "aoColumns": [{
			        "mDataProp": "userName"
			      },{
			        "mDataProp": "ip"
			      },{
			        "mDataProp": "port"
			      },{
			    	  "mDataProp": "loginTime"
			      },{
			        "mDataProp": "exitTime",
			        "render":function(param){
			        	if(param){
			        		return param ;
			        	}else{
			        		return "";
			        	}
			        }
			      },{
			        "mDataProp": "clientType"
			      }],
			      "drawCallback": function( settings ) {
			        },"initComplete":function(settings,json){
			            	if( $scope.loginLogDataTableProperties){
			            		var pageIndex = $scope.loginLogDataTableProperties.iDisplayStart/$scope.loginLogDataTableProperties.iDisplayLength;
					            dTable.page(pageIndex).draw(false);
			            	}
			            	initSearchDiv(settings,json);
			            }
		});
	}
	
	//初始化搜索框
	var initSearchDiv = function(settings,json){
		dataTableSearchService.initSearch([
		      {formDataName:'search.userName',placeholder:'用户姓名'},
			  {formDataName:'search.ip',placeholder:'ip地址'},
			  {formDataName:'search.clientType',placeholder:'客户端类型'},
		],$scope,settings,dTable);
	}
	
	
}]);