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
			              var input = carownerTable.find('thead .i-checks input');
			              var inputs = carownerTable.find('tbody .i-checks input');
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
			      var input = carownerTable.find('thead .i-checks input');
			      var inputs = carownerTable.find('tbody .i-checks input');
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