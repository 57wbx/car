app.controller('hotWordListController',['$rootScope','$scope','$state','$timeout','$modal','$ocLazyLoad','$compile','utils','dataTableSearchService','sessionStorageService','hotWordStateService', function($rootScope, $scope, $state, $timeout,$modal,$ocLazyLoad,$compile,utils,dataTableSearchService,sessionStorageService,hotWordStateService) {
		
	$scope.$evalAsync(initTable);
	
	$scope.clearRowIds();
	
	var isF5 = true ;
	$scope.hotWordListDataTableProperties = null;
	$scope.needCacheArray = ["hotWordListDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.hotWordListDataTableProperties  = sessionStorageService.getItemObj("hotWordListDataTableProperties");
	
	$scope.setHotWordListDataTableProperties = function(obj){
		$scope.hotWordListDataTableProperties = obj;
		sessionStorageService.setItem("hotWordListDataTableProperties",obj);
	}
	
		var table , dTable;
		function initTable(){
			table =  $("#table");
			dTable = table.on('preXhr.dt', function ( e, settings, data ){
//				data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
				if(isF5){
					isF5 = false ;
					var oldData = sessionStorageService.getItemObj("hotWordListDataTableProperties"); 
					if(oldData){
						angular.copy(oldData,data);
						data = oldData ;
						$scope.search.name = data.name ;
						$scope.search.objType = data.objType ;
						$scope.search.useState = data.useState ;
					}
				}else{
					data.name = $scope.search.name ;
					data.objType = $scope.search.objType ;
					data.useState = $scope.search.useState ;
					$scope.setHotWordListDataTableProperties(data);
				}
			
				//缓存数据
			}).on('xhr.dt', function ( e, settings, json, xhr ) {
				if(json.code==401){
					app.state.go('access.signin');
				}
			}).DataTable({
				"sAjaxSource":"opr/hotWordAction!listHotWord.action",
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
			        "mDataProp": "name"
			      },{
			        "mDataProp": "objType",
			        "render":function(param){
			        	return hotWordStateService.getObjType(param);
			        }
			      },{
			        "mDataProp": "useState",
			        "render":function(param){
			        	return hotWordStateService.getUseState(param);
			        }
			      },{
			    	  "mDataProp": "updateTime"
			    	}
			      ],
			      "oLanguage": {
			          "sLengthMenu": "每页 _MENU_ 条",
			          "sZeroRecords": "没有找到符合条件的数据",
			          "sProcessing": "&lt;img src=’./loading.gif’ /&gt;",
			          "sInfo": "当前第 _START_ - _END_ 条，共 _TOTAL_ 条",
			          "sInfoEmpty": "没有记录",
			          "sInfoFiltered": "(从 _MAX_ 条记录中过滤)",
			          "sSearch": "店面名称",
			          "oPaginate": {
			            "sFirst": "<<",
			            "sPrevious": "<",
			            "sNext": ">",
			            "sLast": ">>"
			          }
			        },
			        "fnCreatedRow": function(nRow, aData, iDataIndex){
			            $(nRow).attr('data-id', aData['id']);
			        },"drawCallback": function( settings ) {
			    	   	dataTableSearchService.initClick(dTable,$scope.rowIds,$scope.setBtnStatus,$scope.seeDetails);
			        },"initComplete":function(settings,json){
			            	if( $scope.hotWordListDataTableProperties){
			            		var pageIndex = $scope.hotWordListDataTableProperties.iDisplayStart/$scope.hotWordListDataTableProperties.iDisplayLength;
					              dTable.page(pageIndex).draw(false);
			            	}
			            	initSearchDiv(settings,json);
			            }
			       });
			
		}
		//初始化搜索框
		var initSearchDiv = function(settings,json){
			dataTableSearchService.initSearch([
				  {formDataName:'search.name',placeholder:'名称'},
				  {
                	   formDataName:'search.objType',
                  	   label:'类型',
                  	   options:[{label:'全部'},{
                  		//1=门店、2=技工、3=（服务项）、4=套餐，5=其他
   				               		   value:1,
   				               		   label:"门店"
   				               	   	},{
   			                		   value:2,
   			                   		   label:"技工"
   			                   	   	},{
   			                    		   value:3,
   			                       		   label:"服务"
   			                       	 },{
   			                    		   value:4,
   			                       		   label:"套餐"
   			                       	 },{
   			                    		   value:5,
   			                       		   label:"其他"
   			                       	 }
                      	]
                  },
				  {
                  	   formDataName:'search.useState',
                   	   label:'状态',
                   	   options:[{label:'全部'},{
                   		//1=正常、2=停用、3=注销（黑名单）
    				               		   value:1,
    				               		   label:"正常"
    				               	   	},{
    			                		   value:2,
    			                   		   label:"停用"
    			                   	   	},{
    			                    		   value:3,
    			                       		   label:"注销"
    			                       	 }
                       	]
                   }
			],$scope,settings,dTable);
		}
		
		
}]);