app.controller('workerListController',['$rootScope','$scope','$state','$timeout','$modal','$ocLazyLoad','$compile','utils','dataTableSearchService','sessionStorageService', function($rootScope, $scope, $state, $timeout,$modal,$ocLazyLoad,$compile,utils,dataTableSearchService,sessionStorageService) {
		
	$scope.$evalAsync(initTable);
	
	$scope.clearRowIds();
	
	var isF5 = true ;
	$scope.search = {};
	$scope.memberDataTableProperties = null;
	$scope.needCacheArray = ["memberDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.memberDataTableProperties  = sessionStorageService.getItemObj("memberDataTableProperties");
	
	$scope.setMemerDataTableProperties = function(obj){
		$scope.memberDataTableProperties = obj;
		sessionStorageService.setItem("memberDataTableProperties",obj);
	}
	
		var workerTable , dTable;
		function initTable(){
			workerTable =  $("#workerTable");
			dTable = workerTable.on('preXhr.dt', function ( e, settings, data ){
//				data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
				if(isF5){
					isF5 = false ;
					var oldData = sessionStorageService.getItemObj("memberDataTableProperties"); 
					if(oldData){
						angular.copy(oldData,data);
//						data = oldData ;
						$scope.search.code = data.code ;
						$scope.search.cell = data.cell ;
						$scope.search.name = data.name ;
						$scope.search.gender = data.gender ;
					}
				}else{
					data.code = $scope.search.code ;
					data.cell = $scope.search.cell;
					data.name = $scope.search.name;
					data.gender = $scope.search.gender;
					$scope.setMemerDataTableProperties(data);
				}
			
				//缓存数据
			}).DataTable({
				"sAjaxSource":"base/memberAction!listWorker.action",
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
			        "mDataProp": "code"
			      },{
			        "mDataProp": "cell"
			      },{
			        "mDataProp": "name"
			      },{
			        "mDataProp": "simpleName"
			      },{
			        "mDataProp": "gender",
			        "render":function(param){
			        	//0=加盟店、1=合作店、3=直营店、4=中心店（区域旗舰店）
			        	switch(param){
			        	case 0:
			        		return "男";break;
			        	case 1:
			        		return "女";break;
			        	case 2:
			        		return "保密";break;
			        	default:
			        		return "";break;
			        	}
			        }
			      },{
			        "mDataProp": "email"
			      },{
			        "mDataProp": "MCARDNO"
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
				  {formDataName:'search.code',placeholder:'工号'},
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