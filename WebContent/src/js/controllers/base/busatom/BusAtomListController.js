'use strict';

app.controller('busAtomListController',['$scope','$state','$timeout',function($scope,$state,$timeout){
	//单击表格中的一行应该调用这个方法
	function clickTr(target){
		
		var id = target.data('id');//获取这一表格行的id
		//执行选择效果
		if(!target.find("input")['0'].checked){
			target.find("input")['0'].checked = true;
			//将id保存在上级scope中的
			
			if($scope.busAtomIds.indexOf(id)==-1){
				$scope.busAtomIds.push(id);
			}
		}else{
			target.find("input")['0'].checked = false ;
			//移除指定的id
			var index = $scope.busAtomIds.indexOf(id);
			if(index !== -1 ) $scope.busAtomIds.splice(index, 1);
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
//		console.info("-------------------------------点击之后的数据");
//		console.info($scope.busAtomIds);
	}
	
	
	
	$scope.busAtomAPI.showBusTypeTree();
	/**
	 * 初始化该页面的api
	 * 提供给buspapckageController调用
	 * 注销其他方法
	 */
	$scope.busAtomAPI.clickTreeAddOrUpdateReload = null;//注销其他的方法
	$scope.busAtomAPI.clickTreeListReload = function(busTypeCode){
		//绑定一次后后面所有的请求都具有该参数了
		busAtomTable.ajax.reload();//重新加载数据
	}
	
	//延迟加载 客户端不会报错，具体原因还不清楚
	$timeout(function(){
		initDataTable();
	},30);
	var busAtomTable ;
	function initDataTable(){
		
	 busAtomTable = $("#busAtomTable").on('preXhr.dt', function ( e, settings, data ){
		data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
	}).DataTable({
		"sAjaxSource":"base/busAtomAction!listBusAtom.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
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
            "mDataProp": "atomCode",
          },{
        	"mDataProp":"atomName"  
          },{
        	"mDataProp":"itemCode"  
          },{
        	"mDataProp":"itemName"  
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
            },{
        	"mDataProp":"memo"  
          }],
          "fnCreatedRow": function(nRow, aData, iDataIndex){
        	  $(nRow).attr("data-id",aData['fid']);
        	  $(nRow).dblclick(function(){
        		  $scope.seeDetails($(this).data('id'));//调用上级controller中的查看方法
        	  });
        	  $(nRow).click(function(e){
        		  clickTr($(this));
        	  });
          },
          "drawCallback":function(setting){
        	  $scope.clearBusAtomIds();//首先清空上级controller中的ids
        	  $(this).parents(".dataTables_scroll").find("thead .i-checks input").prop("checked",false);
        	  //设置标题全选按钮事件
        	  $(this).parents(".dataTables_scroll").find("thead .i-checks input").click(function(){
        		  var inputs = $(setting.nScrollBody).find("tbody input");
        		  if($(setting.nScrollHead).find(".i-checks input").prop("checked")){
        			  inputs.prop("checked",true);
        			  $scope.clearBusAtomIds();
        			  for(var i=0;i<inputs.length;i++){
        				  $scope.busAtomIds.push($(inputs[i]).parents("tr").data('id'));
        			  }
        			  $scope.setButtonStatus();//设置按钮状态
        		  }else{
        			  inputs.prop("checked",false);
        			  $scope.clearBusAtomIds();//调用上级controller中的清空ids的方法
        		  }
        	  });
          }
	});
	}//结束初始化方法
	
	
	
	
}]);