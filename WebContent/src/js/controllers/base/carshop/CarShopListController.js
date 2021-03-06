app.controller('carShopListController',['$rootScope','$scope','$state','$timeout','$modal','$ocLazyLoad','$compile','utils','dataTableSearchService','sessionStorageService','carShopStateService', function($rootScope, $scope, $state, $timeout,$modal,$ocLazyLoad,$compile,utils,dataTableSearchService,sessionStorageService,carShopStateService) {
		
	$scope.API.isListPage = true ;
	$scope.$evalAsync(initTable);
	$scope.clearRowIds();
	
	var isF5 = true ;
	$scope.carShopListDataTableProperties = null;
	$scope.needCacheArray = ["carShopListDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.carShopListDataTableProperties  = sessionStorageService.getItemObj("carShopListDataTableProperties");
	
	$scope.setCarShopListDataTableProperties = function(obj){
		$scope.carShopListDataTableProperties = obj;
		sessionStorageService.setItem("carShopListDataTableProperties",obj);
	}
	
		var carShopList , dTable;
		function initTable(){
			carShopList =  $("#store_List");
			dTable = carShopList.on('preXhr.dt', function ( e, settings, data ){
//				data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
				if(isF5){
					isF5 = false ;
					var oldData = sessionStorageService.getItemObj("carShopListDataTableProperties"); 
					if(oldData){
						angular.copy(oldData,data);
//						data = oldData ;
						$scope.search.shopName = data.shopName ;
						$scope.search.shopType = data.shopType ;
						$scope.search.useState = data.useState ;
					}
				}else{
					data.shopName = $scope.search.shopName ;
					data.shopType = $scope.search.shopType;
					data.useState = $scope.search.useState;
					$scope.setCarShopListDataTableProperties(data);
				}
			
				//缓存数据
			}).on('xhr.dt', function ( e, settings, json, xhr ) {
				if(json.code==401){
					app.state.go('access.signin');
				}
			}).DataTable({
				"sAjaxSource":"base/carShopAction!listCarShop.action",
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
			        "mDataProp": "shopCode"
			      },{
			        "mDataProp": "shopName"
			      },{
			        "mDataProp": "shopType",
			        "render":function(param){
			        	//0=加盟店、1=合作店、3=直营店、4=中心店（区域旗舰店）
			        	switch(param){
			        	case 0:
			        		return "加盟店";break;
			        	case 1:
			        		return "合作店";break;
			        	case 3:
			        		return "直营店";break;
			        	case 4:
			        		return "中心店（区域旗舰店）";break;
			        	default:
			        		return "";break;
			        	}
			        }
			      },{
			    	  "mDataProp": "useState",
			    	  "render":function(param){
			    		  return carShopStateService.getUseStateWithStyle(param);
			    	  }
			      },{
			        "mDataProp": "address"
			      },{
			        "mDataProp": "VIPLevel",
			        "render":function(param){
			        	return carShopStateService.getVIPLevel(param);
			        }
			      },{
			        "mDataProp": "registerDate",
			        "render":function(param){
			        	if(param){
			        		return param.substr(0,10);
			        	}else{
			        		return "";
			        	}
			        }
			      }
//			      ,
//			      {
//			    	"mDataProp":"username",
//			    	"render":function(param){
//			    		if(param){
//			    			return "<a name='operation'  style='text-decoration:underline;color:blue;'>"+param+"</a>";
//			    		}else{
//			    			return "<button name='operation'  type='button' class='btn btn-default'>新增</button>";
//			    		}
//			    	},
//			    	"fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {
//	                    $(nTd).attr("name","operation");
//	                }
//			      }
			],
			        "fnCreatedRow": function(nRow, aData, iDataIndex){
			            $(nRow).attr('data-id', aData['id']);
			            $(nRow).find("button").click(function(e){
			            	showModal(nRow,aData);
			            });
			            
			            $(nRow).find("a").click(function(e){
			            	showModal(nRow,aData);
			            });
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
		/**
		 * 弹窗事件
		 */
		var showModal = function(nRow,aData){
			var modalInstance = $modal.open({
       	     templateUrl: 'src/tpl/base/carshop/addManager.html',
       	     size: 'lg',
       	     backdrop:true,
       	     controller:"addManager",
       	     resolve: {
       	    	 carShopId: function () {
           	           return  aData['id'];
           	         },
           	         orgId:function(){
           	        	 return  aData['orgID'];
           	         },
           	         userId:function(){
           	        	 return aData['userid'];
           	         }
       	     }
       	   });
			/**
			 * 弹窗关闭事件
			 */
			modalInstance.result.then(function (name) {
				var operationTd = $(nRow).find("td[name=operation]") ;
				var cellObject = dTable.cell(operationTd).data(name).draw();
        	});
		}
		
		//初始化搜索框
		var initSearchDiv = function(settings,json){
			dataTableSearchService.initSearch([
				  {formDataName:'search.shopName',placeholder:'店家名称'},
                  {
               	   formDataName:'search.shopType',
               	   label:'店家类型',
               	   options:[{label:'全部'},{
               		//0=加盟店、1=合作店、3=直营店、4=中心店（区域旗舰店）
				               		   value:0,
				               		   label:"加盟店"
				               	   	},{
			                		   value:1,
			                   		   label:"合作店"
			                   	   	},{
			                    		   value:3,
			                       		   label:"直营店"
			                       	 },{
			                    		   value:4,
			                       		   label:"中心店（区域旗舰店）"
			                       	 }
                   	]
                  },{
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
    			                       	 }, {
  			                    		   value:4,
  			                       		   label:"黑名单"
  			                       	 }
                       	]
                      }
			],$scope,settings,dTable);
		}
		
}]);