app.controller('pushListController',['$rootScope','$scope','$state','$timeout','$modal','dataTableSearchService','sessionStorageService','previewService','pushStateService', function($rootScope, $scope, $state, $timeout,$modal,dataTableSearchService,sessionStorageService,previewService,pushStateService) {
		
	$scope.$evalAsync(initTable);
	
	$scope.clearRowIds();
	
	var isF5 = true ;
	$scope.carNameListDataTableProperties = null;
	$scope.needCacheArray = ["pushListDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.pushListDataTableProperties  = sessionStorageService.getItemObj("pushListDataTableProperties");
	
	$scope.setPushListDataTableProperties = function(obj){
		$scope.pushListDataTableProperties = obj;
		sessionStorageService.setItem("pushListDataTableProperties",obj);
	}
	
		var table , dTable;
		function initTable(){
			table =  $("#table");
			dTable = table.on('preXhr.dt', function ( e, settings, data ){
//				data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
				if(isF5){
					isF5 = false ;
					var oldData = sessionStorageService.getItemObj("pushListDataTableProperties"); 
					if(oldData){
						angular.copy(oldData,data);
						data = oldData ;
						$scope.search.ftitle = data.ftitle ;
						$scope.search.fcontent = data.fcontent ;
					}
				}else{
					data.ftitle = $scope.search.ftitle ;
					data.fcontent = $scope.search.fcontent;
					$scope.setPushListDataTableProperties(data);
				}
			
				//缓存数据
			}).on('xhr.dt', function ( e, settings, json, xhr ) {
				if(json.code==401){
					app.state.go('access.signin');
				}
			}).DataTable({
				"sAjaxSource":"tig/pushMessageAction!listPushMessage.action",
		    	"bServerSide":true,
		    	"sAjaxDataProp":"data",
		    	 "dom": '<"top">rt<"bottom"ip><"clear">',
		    	 "sServerMethod": "POST",
				"aoColumns": [{
			        "orderable": false,
			        "render": function(param){
			          return '<label class="i-checks"><input type="checkbox"><i></i></label>';
			        }
			      },{
			        "mDataProp": "ftitle"
			      },{
			        "mDataProp": "fcontent"
			      },{
			    	 "mDataProp":"fmessageType",
			    	 "render":function(param){
			    		 return pushStateService.getFmessageType(param);
			    	 }
			     },{
			    	 "mDataProp":"fexpiresTime",
			    	 "render":function(param){
			    		 if(param){
			    			 return param ;
			    		 }else{
			    			 return "0";
			    		 }
			    	 }
			     },{
			    	 "mDataProp":"fsendTime",
				    	 "render":function(param){
				    		 if(param){
				    			 return param ;
				    		 }else{
				    			 return "立即发送";
				    		 }
				    	 }
			     },{
			    	 "mDataProp":"fdeviceType",
			    	 "render":function(param){
			    		 return pushStateService.getFdeviceType(param);
			    	 }
			     },{
			    	 "mDataProp":"fuseState",
			    	 "render":function(param){
			    		 return pushStateService.getFuseState(param);
			    	 }
			     },{
			    	 "mDataProp":"userName"
			     },{
			    	 "mDataProp":"fcreateDate"
			     }
			      ],
			        "fnCreatedRow": function(nRow, aData, iDataIndex){
			            $(nRow).attr('data-id', aData['id']);
			        },"drawCallback": function( settings ) {
			    	   	dataTableSearchService.initClick(dTable,$scope.rowIds,$scope.setBtnStatus,$scope.seeDetails);
			        },"initComplete":function(settings,json){
			            	if( $scope.pushListDataTableProperties){
			            		var pageIndex = $scope.pushListDataTableProperties.iDisplayStart/$scope.pushListDataTableProperties.iDisplayLength;
					            dTable.page(pageIndex).draw(false);
			            	}
			            	initSearchDiv(settings,json);
			            }
			       });
			
		}
		//初始化搜索框
		var initSearchDiv = function(settings,json){
			dataTableSearchService.initSearch([
				  {formDataName:'search.ftitle',placeholder:'标题'},
				  {formDataName:'search.fcontent',placeholder:'消息内容'},
			],$scope,settings,dTable);
		}
		
}]);