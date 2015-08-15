'use strict';

app.controller('advertisementListController',['$scope','$state','$timeout','$http','sessionStorageService',function($scope,$state,$timeout,$http,sessionStorageService){
	
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
	function showInfo(target){
		var id = target.data('id');//获取这一表格行的id
		//点击一行变色的方法
		if ( target.hasClass('selected') ) {
			target.removeClass('selected');
			target.find("td").css("background-color","white");
			$scope.setCanEdit(false);//设置不可以查看和修改
//			showInfoByChild(target);
        }else {
        	advertisementTable.$('tr.selected').find("td").css("background-color","white");
        	advertisementTable.$('tr.selected').removeClass('selected');
        	target.addClass('selected');
        	target.find("td").css("background-color","#b0bed9");
        	$scope.setCanEdit(true,id);//设置可以查看和修改
//        	showInfoByChild(target);
        }
	}
	/**
	 * 显示详细信息的方法
	 * 
	 */
	function showInfoByChild(nRow){
		if(updateVersionTable.row(nRow).child.isShown()){
			updateVersionTable.row(nRow).child.hide();
		}else if(!updateVersionTable.row(nRow).child()){//当child中没有数据的时候从网上获取
			
			updateVersionTable.row(nRow).child.show();
		}else{
			updateVersionTable.row(nRow).child.show();//直接显示数据
		}
	}
	
	function format(data){
		return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;width:100%;">'+
				    '<tr height="50px" >'+
					    '<td width="20%"  class="text-center " style="font-size:18px;font-weight:bold;">更新内容:</td>'+
					    '<td style="font-size:18px;">'+data.updateInfo+'</td>'+
					'</tr>'+
					'<tr height="50px" >'+
				    '<td width="20%"  class="text-center " style="font-size:18px;font-weight:bold;">安装包下载地址 :</td>'+
				    '<td style="font-size:18px;">'+data.downLoadUrl+'</td>'+
				'</tr>'+
				'</table>';
	}
	
	
	$scope.$evalAsync(initDataTable);//延迟加载列表
	
	var advertisementTable ;
	function initDataTable(){
		
		advertisementTable = $("#advertisementTable").DataTable({
		"sAjaxSource":"tig/advertisementAction!listAdvertisement.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
    	"dom": '<"top">rt<"bottom"ip><"clear">',
    	"pageLength":10,
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
            "mDataProp": "adverCode",
          }, {
            "mDataProp": "name",
          }, {
            "mDataProp": "adverType",
            "render":function(param){
            	switch (param) {
				case 1:
					return "项目广告";
					break;
				case 2:
					return "网页广告";
					break;
				case 3:
					return "网页地址";
					break;
				case 4:
					return "活动广告";
					break;
				default:
					return "";
					break;
				}
            }
          }, {
            "mDataProp": "filename",
          }, {
            "render":function( data, type, row ){
            	return "http://"+row.serverIP+":"+row.port+"/"+row.filepath;
            }
          }, {
            "mDataProp": "imgUrl",
          }, {
            "mDataProp": "isEnable",
            "render":function(param){
            	switch(param){
            	case 1: return "启用";break;
            	case 0: return "禁用";break;
            	default:return ""; break;
            	}
            }
          }, {
            "mDataProp": "createTime",
          }, {
            "mDataProp": "memo",
          }],
          "fnCreatedRow": function(nRow, aData, iDataIndex){
        	  $(nRow).attr("data-id",aData['id']);
        	  $(nRow).dblclick(function(){
        		  $scope.setCanEdit(true,$(this).data('id'));
        		  $scope.seeDetails($(this).data('id'));//调用上级controller中的查看方法
        	  });
        	  $(nRow).click(function(e){
        		  if(e.target.nodeName=="TD"||e.target.nodeName=="td"){
        			  showInfo($(this));
        		  }
        	  });
        	  $(nRow).find("input").click(function(e){
        		  clickTr(e,$(this));
        	  });
        	  //设置详细信息的显示方式
//        	  updateVersionTable.row(nRow).child(format(aData));
          },
          "drawCallback":function(setting){
        	  $scope.clearRowIds();//首先清空上级controller中的ids
        	  //清空服务子项的内容
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
        	 
          }
	});
	}//结束初始化方法
	
	
	
	
	
	
//结束	
}]);