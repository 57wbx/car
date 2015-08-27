app.controller('carShopListController',['$rootScope','$scope','$state','$timeout','$modal','$ocLazyLoad','$compile','utils','dataTableSearchService','sessionStorageService','carShopStateService', function($rootScope, $scope, $state, $timeout,$modal,$ocLazyLoad,$compile,utils,dataTableSearchService,sessionStorageService,carShopStateService) {
		
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
				"sAjaxSource":"base/carShopAction!listCarShopWithMannager.action",
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
			        "mDataProp": "registerDate"
			      },{
			    	"mDataProp":"username",
			    	"render":function(param){
			    		if(param){
			    			return "<a name='operation'  style='text-decoration:underline;color:blue;'>"+param+"</a>";
			    		}else{
			    			return "<button name='operation'  type='button' class='btn btn-default'>新增</button>";
			    		}
			    	},
			    	"fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {
	                    $(nTd).attr("name","operation");
	                }
			      }],
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
			            $(nRow).find("button").click(function(e){
			            	showModal(nRow,aData);
			            });
			            
			            $(nRow).find("a").click(function(e){
			            	showModal(nRow,aData);
			            });
			        },
			       "drawCallback": function( settings ) {
			              var input = carShopList.find('thead .i-checks input');
			              var inputs = carShopList.find('tbody .i-checks input');
			              var len = inputs.length, allChecked = true;
			              for(var i=0; i<len; i++){
			                if(!inputs.eq(i)[0].checked){
			                  allChecked = false;
			                  break;
			                }
			              }
			              if(allChecked){
			                input[0].checked = true;
			              }else{
			                input[0].checked = false;
			              }
			              
			              input.off().click(function(){
			                for(var i=0; i<len; i++){
			                  if(!inputs.eq(i)[0].checked || !$(this)[0].checked){  
			                    clicked(inputs.eq(i).off());
			                    inputs.eq(i).trigger('click');
			                  }
			                }
			              });
			              initClickEvent();
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
		
		
		
		// 表格行事件
		
		function initClickEvent(){
			dTable.$('tr').off().dblclick(function(e, settings) {
			      $scope.seeDetails($(this).data('id'));
			    }).click(function(e) {
			    	if(e.target.nodeName=="BUTTON"||e.target.nodeName=="button"||e.target.nodeName=="A"||e.target.nodeName=="a"){
			    		return ;
			    	}
			      var evt = e || window.event;
			      var target = event.target || event.srcElement;

			      evt.preventDefault();
			      evt.stopPropagation();
			      
			      var ipt = $(this).find('.i-checks input');
			      clicked(ipt.off(), $(this));
			      ipt.trigger('click');
			      var input = carShopList.find('thead .i-checks input');
			      var inputs = carShopList.find('tbody .i-checks input');
			      var len = inputs.length, allChecked = true;
			      for(var i=0; i<len; i++){
			        if(!inputs.eq(i)[0].checked){
			          allChecked = false;
			          break;
			        }
			      }
			      if(allChecked){
			        input[0].checked = true;
			      }else{
			        input[0].checked = false;
			      }
			    });
		}
	    
	
	//var ids = [], obj;

	  function clicked(target, that){
	    var classname = 'rowSelected', id;

	    target.click(function(e){
	      var evt = e || window.event;
	      //evt.preventDefault();
	      evt.stopPropagation();
	      if(!that){
	        that = $(this).parents('tr');
	      }
//	      $rootScope.details = $rootScope.obj = app.utils.getDataByKey(data, 'id', that.data('id'));
	      id = that.data('id');

	      if(!$(this)[0].checked){
	        var idx = $scope.rowIds.indexOf(id);
	        if(idx !== -1 ) $scope.rowIds.splice(idx, 1);
	        //that.removeClass(classname);
	      }else{
	        $scope.rowIds.push(id);
	        //that.addClass(classname);
	      }
	      $scope.setBtnStatus();
	      console.info($scope.rowIds);
	    });
	  }
		
}]);