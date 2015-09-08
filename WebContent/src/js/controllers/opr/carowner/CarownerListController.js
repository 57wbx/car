app.controller('carownerListController',['$rootScope','$scope','$state','$timeout','$modal','$ocLazyLoad','$compile','utils','dataTableSearchService','sessionStorageService','memberStateService', 
                                         function($rootScope, $scope, $state, $timeout,$modal,$ocLazyLoad,$compile,utils,dataTableSearchService,sessionStorageService,memberStateService) {
		
	$scope.$evalAsync(initTable);
	
	$scope.clearRowIds();
	
	var isF5 = true ;
	$scope.search = {};
	$scope.carownerDataTableProperties = null;
	$scope.needCacheArray = ["carownerDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.carownerDataTableProperties  = sessionStorageService.getItemObj("carownerDataTableProperties");
	
	$scope.setCarownerDataTableProperties = function(obj){
		$scope.carownerDataTableProperties = obj;
		sessionStorageService.setItem("carownerDataTableProperties",obj);
	}
	
		var carownerTable , dTable;
		function initTable(){
			carownerTable =  $("#carownerTable");
			dTable = carownerTable.on('preXhr.dt', function ( e, settings, data ){
//				data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
				if(isF5){
					isF5 = false ;
					var oldData = sessionStorageService.getItemObj("carownerDataTableProperties"); 
					if(oldData){
						angular.copy(oldData,data);
//						data = oldData ;
						$scope.search.cell = data.cell ;
						$scope.search.name = data.name ;
						$scope.search.gender = data.gender ;
					}
				}else{
					data.cell = $scope.search.cell;
					data.name = $scope.search.name;
					data.gender = $scope.search.gender;
					$scope.setCarownerDataTableProperties(data);
				}
			
				//缓存数据
			}).DataTable({
				"sAjaxSource":"base/memberAction!listCarowner.action",
		    	"bServerSide":true,
		    	"sAjaxDataProp":"data",
		    	 "dom": '<"top">rt<"bottom"ip><"clear">',
		    	 "ordering":false,
		    	 "scrollX":true,
		    	 "sServerMethod": "POST",
				"aoColumns": [{
			        "orderable": false,
			        "render": function(param){
			          return '<label class="i-checks"><input type="checkbox"><i></i></label>';
			        }
			      },{
			        "mDataProp": "cell"
			      },{
			        "mDataProp": "name"
			      },{
			        "mDataProp": "simpleName"
			      },{
			        "mDataProp": "gender",
			        "render":function(param){
			        	return memberStateService.getGender(param);
			        }
			      },{
			        "mDataProp": "VIN"
			      },{
			    	  "mDataProp": "auditState",
			    	  "render":function(param){
			    		  return memberStateService.getAuditState(param);
			    	  }
			      },{
			    	  "mDataProp": "useState",
			    	  "render":function(param){
			    		  return memberStateService.getUseState(param);
			    	  }
			      },{
			    	  "mDataProp": "VIPtime"
			      },{
			    	  "mDataProp": "VIPlevel",
			    	  "render":function(param){
			    		  return memberStateService.getVIPLevel(param);
			    	  }
			      },{
			    	  "mDataProp": "integration"
			      },{
			        "mDataProp": "email"
			      },{
			        "mDataProp": "IDCARDNO"
			      }],
			        "fnCreatedRow": function(nRow, aData, iDataIndex){
			            $(nRow).attr('data-id', aData['id']);
			        },
			       "drawCallback": function( settings ) {
			    	   dataTableSearchService.initClick(dTable,$scope.rowIds,$scope.setBtnStatus,$scope.seeDetails);
			        },
			       "initComplete":function(settings,json){
			            	if( $scope.carShopListDataTableProperties){
			            		var pageIndex = $scope.carShopListDataTableProperties.iDisplayStart/$scope.carShopListDataTableProperties.iDisplayLength;
					              dTable.page(pageIndex).draw(false);
			            	}
			            	initSearchDiv(settings,json);
			            }
			       });
			
		}
		
		//初始化搜索框
		var initSearchDiv = function(settings,json){
			dataTableSearchService.initSearch([
				  {formDataName:'search.cell',placeholder:'手机号'},
				  {formDataName:'search.name',placeholder:'姓名'},
                  {
               	   formDataName:'search.gender',
               	   label:'性别',
               	   options:[{label:'全部'},{
               		//0=加盟店、1=合作店、3=直营店、4=中心店（区域旗舰店）
				               		   value:0,
				               		   label:"男"
				               	   	},{
			                		   value:1,
			                   		   label:"女"
			                   	   	},{
			                    		   value:2,
			                       		   label:"保密"
			                       	 }
                   	]
                  }
			],$scope,settings,dTable);
		}
		
}]);