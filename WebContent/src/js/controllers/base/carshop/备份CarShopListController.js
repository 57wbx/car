app.controller('carShopListController', function($rootScope, $scope, $state, utils) {
	
		var url = "base/carShopAction!listCarShop.action";
		var data = null;
		
		utils.getData(url,function callback(dt){
			 $rootScope.tableData=dt;
		      data = dt;
		      if(dTable){
		        dTable.fnDestroy();
		        initTable();
		      }else{
		    	initTable();
		      }
		 });
		 
		var carShopList , dTable;
		function initTable(){
			carShopList =  $("#store_List");
			dTable = carShopList.dataTable({data:data,
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
			      }],"sAjaxDataProp": "dataList",
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
			            $(nRow).attr('data-id', aData['id']);
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
			            },
			       });
			initClickEvent();
		}
		// 表格行事件
		
		function initClickEvent(){
			dTable.$('tr').dblclick(function(e, settings) {
			      $scope.seeDetails($(this).data('id'));
			    }).click(function(e) {
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

	      $rootScope.details = $rootScope.obj = app.utils.getDataByKey(data, 'id', that.data('id'));
	      id = $rootScope.obj['id'];

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
		
});