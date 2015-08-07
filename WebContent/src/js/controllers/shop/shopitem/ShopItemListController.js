'use strict';

app.controller('shopItemListController',['$scope','$state','$timeout','$http','sessionStorageService',function($scope,$state,$timeout,$http,sessionStorageService){
	
	sessionStorageService.clearNoCacheItem();
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
	function showBusAtoms(target){
		var id = target.data('id');//获取这一表格行的id
		//点击一行变色的方法
		if ( target.hasClass('selected') ) {
			target.removeClass('selected');
			target.find("td").css("background-color","white");
			getBusAtoms("");//设置空id，这样从后台获取的数据为空
			$scope.setCanEdit(false);//设置不可以查看和修改
        }else {
        	busItemTable.$('tr.selected').find("td").css("background-color","white");
        	busItemTable.$('tr.selected').removeClass('selected');
        	target.addClass('selected');
        	target.find("td").css("background-color","#b0bed9");
        	getBusAtoms(id);
        	$scope.setCanEdit(true,id);//设置可以查看和修改
        }
	}
	
	$scope.treeAPI.showBusTypeTree();
	/**
	 * 初始化该页面的api
	 * 提供给buspapckageController调用
	 * 注销其他方法
	 */
	$scope.treeAPI.clickTreeAddOrUpdateReload = null;//注销其他的方法
	$scope.treeAPI.clickTreeListReload = function(busTypeCode){
		//绑定一次后后面所有的请求都具有该参数了
		busItemTable.ajax.reload();//重新加载数据
	}
	
	
	//延迟加载 客户端不会报错，具体原因还不清楚
	$timeout(function(){
		initDataTable();
	},30);
	var busItemTable ;
	function initDataTable(){
		
	busItemTable = $("#busItemTable").on('preXhr.dt', function ( e, settings, data ){
		data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
	}).DataTable({
		"sAjaxSource":"shop/shopItemAction!listShopItem.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
    	"pageLength":5,
    	"dom": '<"top">rt<"bottom"p><"clear">',
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
    	  "aoColumns": [{
            "orderable": false,
            "render": function(param){
              return '<label class="i-checks"><input type="checkbox"><i></i></label>';
            }
          }, {
            "render":function( data, type, row ){
            	return row.shopItemImgs.length;
            }
          }, {
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
            }, {
              "mDataProp": "memo",
            }],
          "fnCreatedRow": function(nRow, aData, iDataIndex){
        	  $(nRow).attr("data-id",aData['fid']);
        	  $(nRow).dblclick(function(){
        		  $scope.setCanEdit(true,$(this).data('id'));
        		  $scope.seeDetails($(this).data('id'));//调用上级controller中的查看方法
        	  });
        	  $(nRow).click(function(e){
        		  if(e.target.nodeName=="TD"||e.target.nodeName=="td"){
        			  showBusAtoms($(this));
        		  }
        	  });
        	  $(nRow).find("input").click(function(e){
        		  clickTr(e,$(this));
        	  });
          },
          "drawCallback":function(setting){
        	  $scope.clearRowIds();//首先清空上级controller中的ids
        	  //清空服务子项的内容
        	  getBusAtoms("");//清空服务子项的内容
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
        	  
//        	  $('#busItemTable tbody').on( 'click', 'tr', function () {
//        		  console.info("-------------");
//        		  console.info($(this));
//        	        if ( $(this).hasClass('selected') ) {
//        	            $(this).removeClass('selected');
//        	            $(this).find("td").css("background-color","white");
//        	        }
//        	        else {
//        	        	busItemTable.$('tr.selected').find("td").css("background-color","white");
//        	            $(this).addClass('selected');
//        	            $(this).find("td").css("background-color","#b0bed9");
//        	        }
//        	    } );
          }
	});
	}//结束初始化方法
	
	
	/**
	 * 显示子项信息列表
	 */
	var busAtomTable = $("#busAtomTable").DataTable({
		"info":false,
		"lengthChange":false,
		"paging":false,
		"searching":false,
		"pageLength":20,
		"sAjaxSource":"shop/shopAtomAction!getShopAtomsByItemId.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
    	"oLanguage": {
            "sLengthMenu": "每页 _MENU_ 条",
            "sZeroRecords": "该服务没有服务子项信息",
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
            "mDataProp": "atomCode",
          },{
        	"mDataProp":"atomName"  
          },{
        	"mDataProp":"partName"  
          },{
        	"mDataProp":"brandName"  
          },{
        	"mDataProp":"spec"  
          },{
        	"mDataProp":"model"  
          },{
        	"mDataProp":"eunitPrice"  
          },{
        	"mDataProp":"autoParts"  
          },{
        	"mDataProp":"eunitPrice1"  
          },{
        	"mDataProp":"yunitPrice"  
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
            }]
	});
	
	/**
	 * 更具服务记录获取子项信息的方法
	 * 
	 */
	function getBusAtoms(busItemId){
		var url = "shop/shopAtomAction!getShopAtomsByItemId.action?fItemId="+busItemId ;
		busAtomTable.ajax.url(url).load();
	}
	
	
	
//结束	
}]);