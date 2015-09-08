app.controller('carNameListController',['$rootScope','$scope','$state','$timeout','$modal','$ocLazyLoad','$compile','utils','dataTableSearchService','sessionStorageService','previewService', function($rootScope, $scope, $state, $timeout,$modal,$ocLazyLoad,$compile,utils,dataTableSearchService,sessionStorageService,previewService) {
		
	$scope.$evalAsync(initTable);
	
	$scope.clearRowIds();
	
	var isF5 = true ;
	$scope.carNameListDataTableProperties = null;
	$scope.needCacheArray = ["carNameListDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.carNameListDataTableProperties  = sessionStorageService.getItemObj("carNameListDataTableProperties");
	
	$scope.setCarNameListDataTableProperties = function(obj){
		$scope.carNameListDataTableProperties = obj;
		sessionStorageService.setItem("carNameListDataTableProperties",obj);
	}
	
		var table , dTable;
		function initTable(){
			table =  $("#table");
			dTable = table.on('preXhr.dt', function ( e, settings, data ){
//				data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
				if(isF5){
					isF5 = false ;
					var oldData = sessionStorageService.getItemObj("carNameListDataTableProperties"); 
					if(oldData){
						angular.copy(oldData,data);
						data = oldData ;
						$scope.search.firstLetter = data.firstLetter ;
						$scope.search.makeName = data.makeName ;
						$scope.search.photoUrl = data.photoUrl ;
					}
				}else{
					data.firstLetter = $scope.search.firstLetter ;
					data.makeName = $scope.search.makeName;
					data.photoUrl = $scope.search.photoUrl;
					$scope.setCarNameListDataTableProperties(data);
				}
			
				//缓存数据
			}).on('xhr.dt', function ( e, settings, json, xhr ) {
				if(json.code==401){
					app.state.go('access.signin');
				}
			}).DataTable({
				"sAjaxSource":"base/carNameAction!listCarName.action",
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
			    	 "mDataProp":"photoUrl",
			    	 "render":function(param){
			    		 if(param){
			    			 return "<a style='text-decoration:underline;color:blue;'>查看图片</a>";
			    		 }else{
			    			 return "无";
			    		 }
			    	 }
			     },{
			    	 "mDataProp":"updateTime"
			     }
			      ],
			        "fnCreatedRow": function(nRow, aData, iDataIndex){
			            $(nRow).attr('data-id', aData['id']);
			            $(nRow).find("a").click(function(e){
			            	showImg(e,aData);	
			            });
			        },"drawCallback": function( settings ) {
			    	   	dataTableSearchService.initClick(dTable,$scope.rowIds,$scope.setBtnStatus,$scope.seeDetails);
			        },"initComplete":function(settings,json){
			            	if( $scope.carNameListDataTableProperties){
			            		var pageIndex = $scope.carNameListDataTableProperties.iDisplayStart/$scope.carNameListDataTableProperties.iDisplayLength;
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
				  {
                  	   formDataName:'search.photoUrl',
                   	   label:'是否有图片',
                   	   options:[{label:'全部'},{
                   		//1=正常、2=停用、3=注销（黑名单）
    				               		   value:"isNotNull",
    				               		   label:"有"
    				               	   	},{
    			                		   value:"isNull",
    			                   		   label:"无"
    			                   	   	}
                       	]
                      }
			],$scope,settings,dTable);
		}
		/*
		 * 预览图片功能
	     */
	    var previewImg = $scope.previewImg = function(photoUrl){
	    	 previewService.preview(photoUrl);
	    }
		
		function showImg(e,aData){
		    previewImg(aData.photoUrl);
		}
		
}]);