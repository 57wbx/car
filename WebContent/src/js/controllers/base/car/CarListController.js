app.controller('carListController',['$rootScope','$scope','$state','$timeout','$modal','$ocLazyLoad','$compile','utils','dataTableSearchService','sessionStorageService','carShopStateService', function($rootScope, $scope, $state, $timeout,$modal,$ocLazyLoad,$compile,utils,dataTableSearchService,sessionStorageService,carShopStateService) {
		
	$scope.$evalAsync(initTable);
	
	$scope.clearRowIds();
	
	var isF5 = true ;
	$scope.carListDataTableProperties = null;
	$scope.needCacheArray = ["carListDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.carListDataTableProperties  = sessionStorageService.getItemObj("carListDataTableProperties");
	
	$scope.setCarListDataTableProperties = function(obj){
		$scope.carListDataTableProperties = obj;
		sessionStorageService.setItem("carListDataTableProperties",obj);
	}
	
		var table , dTable;
		function initTable(){
			table =  $("#table");
			dTable = table.on('preXhr.dt', function ( e, settings, data ){
//				data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
				if(isF5){
					isF5 = false ;
					var oldData = sessionStorageService.getItemObj("carListDataTableProperties"); 
					if(oldData){
						angular.copy(oldData,data);
						data = oldData ;
						$scope.search.firstLetter = data.firstLetter ;
						$scope.search.makeName = data.makeName ;
						$scope.search.modelSeries = data.modelSeries ;
						$scope.search.typeSeries = data.typeSeries ;
						$scope.search.typeName = data.typeName ;
					}
				}else{
					data.firstLetter = $scope.search.firstLetter ;
					data.makeName = $scope.search.makeName;
					data.modelSeries = $scope.search.modelSeries;
					data.typeSeries = $scope.search.typeSeries;
					data.typeName = $scope.search.typeName;
					$scope.setCarListDataTableProperties(data);
				}
			
				//缓存数据
			}).on('xhr.dt', function ( e, settings, json, xhr ) {
				if(json.code==401){
					app.state.go('access.signin');
				}
			}).DataTable({
				"sAjaxSource":"base/carAction!listCar.action",
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
			        "mDataProp": "firstLetter"
			      },{
			        "mDataProp": "makeName"
			      },{
			        "mDataProp": "modelSeries"
			      },{
			    	  "mDataProp": "modelName"
			      },{
			        "mDataProp": "typeSeries"
			      },{
			        "mDataProp": "typeName"
			      },{
			        "mDataProp": "country"
			      },{
			    	"mDataProp":"technology"
			     },{
			    	 "mDataProp":"vehicleClass"
			     },{
			    	 "mDataProp":"engineCapacity"
			     },{
			    	 "mDataProp":"transmission"
			     },{
			    	 "mDataProp":"updateTime"
			     }
			      ],
			        "fnCreatedRow": function(nRow, aData, iDataIndex){
			            $(nRow).attr('data-id', aData['id']);
			        },"drawCallback": function( settings ) {
			    	   	dataTableSearchService.initClick(dTable,$scope.rowIds,$scope.setBtnStatus,$scope.seeDetails);
			        },"initComplete":function(settings,json){
			            	if( $scope.carListDataTableProperties){
			            		var pageIndex = $scope.carListDataTableProperties.iDisplayStart/$scope.carListDataTableProperties.iDisplayLength;
					              dTable.page(pageIndex).draw(false);
			            	}
			            	initSearchDiv(settings,json);
			            }
			       });
			
		}
		//初始化搜索框
		var initSearchDiv = function(settings,json){
			dataTableSearchService.initSearch([
				  {formDataName:'search.firstLetter',placeholder:'品牌首字母'},
				  {formDataName:'search.makeName',placeholder:'品牌名称'},
				  {formDataName:'search.modelSeries',placeholder:'厂商'},
				  {formDataName:'search.typeSeries',placeholder:'年款'},
				  {formDataName:'search.typeName',placeholder:'型号'}
			],$scope,settings,dTable);
		}
		
}]);