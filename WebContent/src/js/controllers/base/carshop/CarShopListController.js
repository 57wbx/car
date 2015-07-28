app.controller('carShopListController',['$rootScope','$scope','$state','$timeout','$modal','$ocLazyLoad','utils', function($rootScope, $scope, $state, $timeout,$modal,$ocLazyLoad,utils) {
		
	$timeout(function(){
		initTable();
	},30);
		
		var carShopList , dTable;
		function initTable(){
			carShopList =  $("#store_List");
			dTable = carShopList.DataTable({
				"sAjaxSource":"base/carShopAction!listCarShopWithMannager.action",
		    	"bServerSide":true,
		    	"sAjaxDataProp":"data",
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
			        "mDataProp": "address"
			      },{
			        "mDataProp": "VIPLevel",
			        "render":function(param){
			        	//0=0星、1=1星、2=2星、3=3星、4=4星、5=5星
			        	switch(param){
			        	
			        		case 0 : return "0星";
			        		break;
			        		case 1 : return "1星";
			        		break;
			        		case 2 : return "2星";
			        		break;
			        		case 3 : return "3星";
			        		break;
			        		case 4 : return "4星";
			        		break;
			        		case 5 : return "5星";
			        		break;
			        		default:
				        		return "";break;
			        	}
			        }
			      },{
			        "mDataProp": "registerDate"
			      },{
			    	"mDataProp":"username",
			    	"render":function(param){
			    		if(param){
			    			return "<a name='operation' href='#' style='text-decoration:underline;color:blue;'>"+param+"</a>";
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
			          "sSearch": "搜索",
			          "oPaginate": {
			            "sFirst": "<<",
			            "sPrevious": "<",
			            "sNext": ">",
			            "sLast": ">>"
			          }
			        },
			        "fnCreatedRow": function(nRow, aData, iDataIndex){
			            $(nRow).attr('data-id', aData['ID']);
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
           	           return  aData['ID'];
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
		
		
		// 表格行事件
		
		
		
		function initClickEvent(){
			dTable.$('tr').dblclick(function(e, settings) {
			      $scope.seeDetails($(this).data('id'));
			    }).click(function(e) {
			    	if(e.target.nodeName=="BUTTON"||e.target.nodeName=="button"||e.target.nodeName=="A"||e.target.nodeName=="a"){
			    		return ;
			    	}
			      var evt = e || window.event;
			      var target = event.target || event.srcElement;

			      evt.preventDefault();
			      //evt.stopPropagation();
			      
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
	  $rootScope.ids = [], $rootScope.obj;

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
	        var idx = $rootScope.ids.indexOf(id);
	        if(idx !== -1 ) $rootScope.ids.splice(idx, 1);
	        //that.removeClass(classname);
	      }else{
	        $rootScope.ids.push(id);
	        //that.addClass(classname);
	      }
	      $scope.setBtnStatus();
	    });
	  }
		
}]);