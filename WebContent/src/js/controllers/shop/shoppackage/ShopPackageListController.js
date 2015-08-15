'use strict';

app.controller('shopPackageListController',['$scope','$state','$timeout','$http','sessionStorageService','dataTableSearchService',function($scope,$state,$timeout,$http,sessionStorageService,dataTableSearchService){
	
	
	var isF5 = true ;
	$scope.search = {};
	$scope.shopPackageDataTableProperties = null;
	$scope.needCacheArray = ["shopPackageDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	$scope.shopPackageDataTableProperties  = sessionStorageService.getItemObj("shopPackageDataTableProperties");
	
	$scope.setShopPackageDataTableProperties = function(obj){
		$scope.shopPackageDataTableProperties = obj;
		sessionStorageService.setItem("shopPackageDataTableProperties",obj);
	}
	//显示树
	$scope.treeAPI.showBusTypeTree();
	/**
	 * 初始化该页面的api
	 * 提供给buspapckageController调用
	 * 注销其他方法
	 */
	$scope.treeAPI.clickTreeAddOrUpdateReload = null;//注销其他的方法
	$scope.treeAPI.clickTreeListReload = function(busTypeCode){
		//绑定一次后后面所有的请求都具有该参数了
		busPackageTable.ajax.reload();//重新加载数据
	}
	
	
	
	//单击表格中的一行应该调用这个方法
	function clickTr(e,inp){
	      var evt = e || window.event;
	      //evt.preventDefault();
	      evt.stopPropagation();
		
		var target = $(inp.parents("tr")['0']);
		var id = target.data('id');//获取这一表格行的id
		//执行选择效果
		if(target.find("input")['0'].checked){
			target.find("input")['0'].checked = true;
			//将id保存在上级scope中的
			if($scope.rowIds.indexOf(id)==-1){
				$scope.rowIds.push(id);
			}
		}else{
			target.find("input")['0'].checked = false ;
			//移除指定的id
			var index = $scope.rowIds.indexOf(id);
			if(index !== -1 ) $scope.rowIds.splice(index, 1);
		}
		//标题的选择框
		var inputsCount = target.parents("table").find("tbody input").length;
		var checkCount = target.parents("table").find("tbody input:checked").length;
		
		if(inputsCount == checkCount){
			target.parents(".dataTables_scroll").find(" thead .i-checks input").prop("checked",true);//.dataTables_scroll 是datatable插件自动生成的css
		}else{
			target.parents(".dataTables_scroll").find(" thead .i-checks input").prop("checked",false);//.dataTables_scroll 是datatable插件自动生成的css
		}
		$scope.setButtonStatus();//设置按钮状态
		console.info("-------------------------------点击之后的数据");
		console.info($scope.rowIds);
	}
	/**
	 * 点击一行的时候，显示子项信息
	 */
	function showBusItems(target){
		var id = target.data('id');//获取这一表格行的id
		//点击一行变色的方法
		if ( target.hasClass('selected') ) {
			target.removeClass('selected');
			target.find("td").css("background-color","white");
			getBusItems("");//设置空id，这样从后台获取的数据为空
			$scope.setCanEdit(false);//设置不可以查看和修改
        }else {
        	busPackageTable.$('tr.selected').find("td").css("background-color","white");
        	busPackageTable.$('tr.selected').removeClass('selected');
        	target.addClass('selected');
        	target.find("td").css("background-color","#b0bed9");
        	getBusItems(id);
        	$scope.setCanEdit(true,id);//设置可以查看和修改
        }
	}
	
	
	//延迟加载 客户端不会报错，具体原因还不清楚
	$timeout(function(){
		initDataTable();
	},30);
	var busPackageTable ;
	function initDataTable(){
		busPackageTable = $("#busPackageTable").on('preXhr.dt', function ( e, settings, data ){
			if(isF5){
				isF5 = false ;
				var oldData = sessionStorageService.getItemObj("shopPackageDataTableProperties"); 
				if(oldData){
					angular.copy(oldData,data);
//					data = oldData ;
//					$scope.search.itemCode = data.itemCode ;
					$scope.search.packageName = data.packageName ;
					$scope.search.isActivity = data.isActivity ;
					$scope.busTypeTree.selectedTypeCode = data.busTypeCode ;
				}
			}else{
//				data.itemCode = $scope.search.itemCode ;
				data.packageName = $scope.search.packageName;
				data.isActivity = $scope.search.isActivity;
				data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
				$scope.setShopPackageDataTableProperties(data);
			}
			
		}).DataTable({
			"sAjaxSource":"shop/shopPackageAction!listShopPackage.action",
	    	"bServerSide":true,
	    	"sAjaxDataProp":"data",
	    	"pageLength":5,
	    	"dom": '<"top">rt<"bottom"ip><"clear">',
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
	          },scrollX:true,
	    	  "aoColumns": [ {
	              "orderable": false,
	              "render": function(param){
	                return '<label class="i-checks"><input type="checkbox"><i></i></label>';
	              }
	            } ,{
	                "render":function( data, type, row ){
	                	return row.shopPackageImgs.length;
	                }
	              },{
	            "mDataProp": "packageCode",
	          }, {
	            "mDataProp": "packageName",
	          }, {
	            "mDataProp": "serviceType",
	            "render":function(param){
	            	//1=标准型、2=经济型、3=高效型、4=原厂型
	            	switch(param){
	            	case 1: return "标准型" ;break;
	            	case 2: return "经济型" ;break;
	            	case 3: return "高效型" ;break;
	            	case 4: return "原厂型" ;break;
	            	default : return "";break;
	            	}
	            }
	          }, {
	            "mDataProp": "workHours",
	          }, {
	            "mDataProp": "expectHours",
	          }, {
	            "mDataProp": "autoPartsPrice",
	          }, {
	            "mDataProp": "standardPrice",
	          }, {
	          	"mDataProp":"actualPrice"
	          }, {
	          	"mDataProp":"useState",
	          	"render":function(param){
	          		//0=初始化、1=待审核、2=发布（审核通过）、3=停止/下架、4=强制下架（违规）
	          		switch(param){
	            	case 0:
	            		return "初始化";break;
	            	case 1:
	            		return "待审核";break;
	            	case 2:
	            		return "发布（审核通过）";break;
	            	case 3:
	            		return "停止/下架";break;
	            	case 4:
	            		return "强制下架（违规）";break;
	            	default:
	            		return "";break;
	          		}
	          	}
	          },{
	        	//0=不参加（默认），1=参加
	          	"mDataProp":"isActivity",
	          	"render":function(param){
	            	//0=不参加（默认），1=参加
	            	switch(param){
	            	case 0:
	            		return "不参加";break;
	            	case 1:
	            		return "参加";break;
	            	default:
	            		return "";break;
	            	}}
		      }, {
	          	"mDataProp":"starTime"
	          }, {
	          	"mDataProp":"endTime"
	          }, {
	             "mDataProp": "publishTime",
	          }],
	          "fnCreatedRow": function(nRow, aData, iDataIndex){
	        	  $(nRow).attr("data-id",aData['fid']);
	        	  $(nRow).dblclick(function(){
	        		  $scope.setCanEdit(true,$(this).data('id'));
	        		  $scope.seeDetails($(this).data('id'));//调用上级controller中的查看方法
	        	  });
	        	  $(nRow).click(function(e){
	        		  if(e.target.nodeName=="TD"){
	        			  showBusItems($(this));
	        		  }
	        	  });
	        	  $(nRow).find("input").click(function(e){
	        		  clickTr(e,$(this));
	        	  });
	          },
	          "drawCallback":function(setting){
	        	  $scope.clearRowIds();//首先清空上级controller中的ids
	        	  //清空服务项中的所有内容
	        	  getBusItems("");
	        	  $(this).parents(".dataTables_scroll").find("thead .i-checks input").prop("checked",false);
	        	  //设置标题全选按钮事件
	        	  $(this).parents(".dataTables_scroll").find("thead .i-checks input").click(function(){
	        		  var inputs = $(setting.nScrollBody).find("tbody input");
	        		  if($(setting.nScrollHead).find(".i-checks input").prop("checked")){
	        			  inputs.prop("checked",true);
	        			  $scope.clearRowIds();
	        			  for(var i=0;i<inputs.length;i++){
	        				  $scope.rowIds.push($(inputs[i]).parents("tr").data('id'));
	        			  }
	        			  $scope.setButtonStatus();//设置按钮状态
	        		  }else{
	        			  inputs.prop("checked",false);
	        			  $scope.clearRowIds();//调用上级controller中的清空ids的方法
	        		  }
	        	  });
	        	  
	          },
	          "initComplete":function(settings,json){
	            	if( $scope.shopPackageDataTableProperties){
	            		var pageIndex = $scope.shopPackageDataTableProperties.iDisplayStart/$scope.shopPackageDataTableProperties.iDisplayLength;
	            		busPackageTable.page(pageIndex).draw(false);
	            	}
	            	initSearchDiv(settings,json);
	            }
		});
	
	}//结束初始化方法
	
	//初始化搜索框
	var initSearchDiv = function(settings,json){
		dataTableSearchService.initSearch([
			  {formDataName:'search.packageName',placeholder:'套餐名称'},
              {
           	   formDataName:'search.isActivity',
           	   label:'是否参加聚惠',
           	   options:[{label:'全部'},{
           		//0=加盟店、1=合作店、3=直营店、4=中心店（区域旗舰店）
			               		   value:0,
			               		   label:"不参加"
			               	   	},{
		                		   value:1,
		                   		   label:"参加"
		                   			   }
               	]
              }
		],$scope,settings,busPackageTable);
	}
	
	
	/**
	 * 显示子项信息列表
	 */
	var busItemTable = $("#busItemTable").DataTable({
		"info":false,
		"lengthChange":false,
		"paging":false,
		"searching":false,
		"pageLength":5,
		"sAjaxSource":"shop/shopItemAction!listShopItem.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
    	"oLanguage": {
            "sLengthMenu": "每页 _MENU_ 条",
            "sZeroRecords": "没有服务信息",
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
          },scrollX:true,
    	  "aoColumns": [{
            "mDataProp": "itemCode",
          }, {
            "mDataProp": "itemName",
          }, {
            "mDataProp": "itemDes",
          }, {
            "mDataProp": "standardPrice",
          }, {
            "mDataProp": "actualPrice",
          }, {
            "mDataProp": "workHours",
          }, {
            "mDataProp": "autoPartsPrice",
          }, {
          	"mDataProp":"useState",
        	"render":function(param){
            	//0=初始化、1=待审核、2=发布（审核通过）、3=停止/下架、4=强制下架（违规）
            	switch(param){
            	case 0:
            		return "初始化";break;
            	case 1:
            		return "待审核";break;
            	case 2:
            		return "发布（审核通过）";break;
            	case 3:
            		return "停止/下架";break;
            	case 4:
            		return "强制下架（违规）";break;
            	default:
            		return "";break;
            	}}
          },{
        	//0=不参加（默认），1=参加
          	"mDataProp":"isActivity",
          	"render":function(param){
            	//0=不参加（默认），1=参加
            	switch(param){
            	case 0:
            		return "不参加";break;
            	case 1:
            		return "参加";break;
            	default:
            		return "";break;
            	}}
            }, {
                "mDataProp": "starTime",
            }, {
              "mDataProp": "endTime",
            }],"fnCreatedRow": function(nRow, aData, iDataIndex){
            	 $(nRow).click(function(e){
	        		  if(e.target.nodeName=="TD"||e.target.nodeName=="td"){
	        			  showBusAtomInfo(busItemTable,nRow,aData.fid);//根据这一行的id来获取所有服务子项的信息
	        		  }
	        	  });
            }
	});
	
	
	/**
	 * 显示所有的服务子项信息
	 */
	function showBusAtomInfo(table,nRow,itemId){
		console.info(table.row(nRow).child);
		if(table.row(nRow).child.isShown()){
			table.row(nRow).child.hide();
		}else if(!table.row(nRow).child()){//当child中没有数据的时候从网上获取
			//从网上获取数据，并将数据添加到子行中
			$http({
				url:"shop/shopItemAction!detailsShopItem.action",
				method:"get",
				params:{
					fid:itemId
				}
			}).then(function(resp){
				if(resp.data.code == 1 && resp.data.busAtoms && resp.data.busAtoms.length>0){
					var detailsTable = "<table class='table table-bordered table-condensed '><tr class='danger'><th >子项编号</th><th>子项名称</th><th>配件名称</th><th>配件数</th><th>配件单价</th></tr>";
					for(var i=0;i<resp.data.busAtoms.length;i++){
						detailsTable = detailsTable + "<tr class='danger'>" ;
						detailsTable = detailsTable + "<td>"+resp.data.busAtoms[i].atomCode +"</td>" ;
						detailsTable = detailsTable + "<td>"+resp.data.busAtoms[i].atomName +"</td>" ;
						detailsTable = detailsTable + "<td>"+resp.data.busAtoms[i].autoPart.partName +"</td>" ;
						detailsTable = detailsTable + "<td>"+resp.data.busAtoms[i].autoParts +"</td>" ;
						detailsTable = detailsTable + "<td>"+resp.data.busAtoms[i].eunitPrice +"</td>" ;
						detailsTable = detailsTable + "</tr>" ;
					}
					detailsTable = detailsTable + "</table>";
					
					table.row(nRow).child(detailsTable).show();
				}else{
					table.row(nRow).child("<div class='text-center' style=''><h5>无子项信息</h5><div>").show();
				}
			});
		}else{
			table.row(nRow).child.show();//直接显示数据
		}
	}
	
	
	/**
	 * 更具服务记录获取子项信息的方法
	 * 
	 */
	function getBusItems(busPackageId){
//		var url = "base/busAtomAction!getBusAtomsByItemId.action?fItemId="+busItemId ;
		var url = "shop/shopPackageAction!getShopItemsByShopPackage.action?fid="+busPackageId;
		busItemTable.ajax.url(url).load();
	}
	
	
	
//结束	
}]);