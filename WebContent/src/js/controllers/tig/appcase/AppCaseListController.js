app.controller('appCaseListController',['$rootScope','$scope','$state','$timeout','$modal','$ocLazyLoad','$compile','utils','dataTableSearchService','sessionStorageService','appCaseStateService', function($rootScope, $scope, $state, $timeout,$modal,$ocLazyLoad,$compile,utils,dataTableSearchService,sessionStorageService,appCaseStateService) {
		
	$scope.$evalAsync(initTable);
	
	$scope.clearRowIds();
	
	var isF5 = true ;
	$scope.search = {};
	$scope.appCaseDataTableProperties = null;
	$scope.needCacheArray = ["appCaseDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.appCaseDataTableProperties  = sessionStorageService.getItemObj("appCaseDataTableProperties");
	
	$scope.setAppCaseDataTableProperties = function(obj){
		$scope.appCaseDataTableProperties = obj;
		sessionStorageService.setItem("appCaseDataTableProperties",obj);
	}
	
		var appCaseTable , dTable;
		function initTable(){
			appCaseTable =  $("#appCaseTable");
			dTable = appCaseTable.on('preXhr.dt', function ( e, settings, data ){
//				data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
				if(isF5){
					isF5 = false ;
					var oldData = sessionStorageService.getItemObj("appCaseDataTableProperties"); 
					if(oldData){
						angular.copy(oldData,data);
//						data = oldData ;
						$scope.search.appName = data.appName ;
						$scope.search.versionName = data.versionName ;
						$scope.search.isUse = data.isUse ;
						$scope.search.appLevel = data.appLevel ;
					}
				}else{
					data.appName = $scope.search.appName ;
					data.versionName = $scope.search.versionName;
					data.isUse = $scope.search.isUse;
					data.appLevel = $scope.search.appLevel;
					$scope.setAppCaseDataTableProperties(data);
				}
			
				//缓存数据
			}).DataTable({
				"sAjaxSource":"tig/appCaseAction!listAppCase.action",
		    	"bServerSide":true,
		    	"sAjaxDataProp":"data",
		    	 "dom": '<"top">rt<"bottom"p><"clear">',
		    	 "sServerMethod": "POST",
		    	 "scrollX":true,
		    	 "ordering":false,
				"aoColumns": [{
			        "orderable": false,
			        "render": function(param){
			          return '<label class="i-checks"><input type="checkbox"><i></i></label>';
			        }
			      },{
			        "mDataProp": "appName"
			      },{
			        "mDataProp": "versionCode"
			      },{
			        "mDataProp": "versionName"
			      },{
			        "mDataProp": "appLevel",
			        "render":function(param){
			        	return appCaseStateService.getAppLevel(param);
			        }
			      },{
			        "mDataProp": "appAmount"
			      },{
			        "mDataProp": "downNum"
			      },{
			        "mDataProp": "isUse",
			        "render":function(param){
			        	return appCaseStateService.getIsUse(param);
			        }
			      },{
			        "mDataProp": "downLoadUrl"
			      },{
			        "mDataProp": "logoUrl"
			      },{
			        "mDataProp": "uploadTime"
			      },{
			        "mDataProp": "user.name"
			      },{
			        "mDataProp": "sortCode"
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
			        },
			       "drawCallback": function( settings ) {
			              var input = appCaseTable.find('thead .i-checks input');
			              var inputs = appCaseTable.find('tbody .i-checks input');
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
			            	if( $scope.appCaseDataTableProperties){
			            		var pageIndex = $scope.appCaseDataTableProperties.iDisplayStart/$scope.appCaseDataTableProperties.iDisplayLength;
					              dTable.page(pageIndex).draw(false);
			            	}
			            	initSearchDiv(settings,json);
			            }
			       });
			
		}
		
		//初始化搜索框
		var initSearchDiv = function(settings,json){
			dataTableSearchService.initSearch([
				  {formDataName:'search.appName',placeholder:'应用名称'},
				  {formDataName:'search.versionName',placeholder:'外部版本号'},
                  {
               	   formDataName:'search.isUse',
               	   label:'使用状态',
               	   options:[{label:'全部'},{
               		//0=启用（默认）、1=停用
				               		   value:0,
				               		   label:"启用"
				               	   	},{
			                		   value:1,
			                   		   label:"停用"
			                   	   	}
                   	]
                  },
                  {
                	  //1=1星、2=2星、3=3星、4=4星、5=5星（默认0）
                  	   formDataName:'search.appLevel',
                  	   label:'推荐等级',
                  	   options:[{label:'全部'},{
                  		//0=启用（默认）、1=停用
   				               		   value:1,
   				               		   label:"1星"
   				               	   	},{
   			                		   value:2,
   			                   		   label:"2星"
   			                   	   	},{
    			                		   value:3,
       			                   		   label:"3星"
       			                   	   	},{
        			                		   value:4,
           			                   		   label:"4星"
           			                   	   	},{
            			                		   value:5,
               			                   		   label:"5星"
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
			      var input = appCaseTable.find('thead .i-checks input');
			      var inputs = appCaseTable.find('tbody .i-checks input');
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
	      $scope.setButtonStatus();
	      console.info($scope.rowIds);
	    });
	  }
		
}]);