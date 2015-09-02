app.controller("citysController",['$scope','$modal','$http',function($scope,$modal,$http){
	if($scope.select.province){
		$scope.$evalAsync(function(){//延迟加载
			initCityTable();
		});
	}
	
	
	
	
	
	/**
	 * 初始话父容器中的方法
	 */
	//刷新数据的方法
	  $scope.cityAPI.freshData = function(){
		  table.ajax.reload();//重新加载数据
	  }
	  //新增一行数据的方法
	  $scope.cityAPI.addRow = function(){
		  showModal($scope.show.titleName,$scope.select.province,null,false);
	  }
	//修改一行的数据的方法
	  $scope.cityAPI.editRow = function(){
		  showModal($scope.show.titleName,$scope.select.province,$scope.rowIds[0],false);
	  }
	  //查看一行数据的详细信息
	  $scope.cityAPI.seeDetails = function(){
		  showModal($scope.show.titleName,$scope.select.province,$scope.rowIds[0],true);
	  }
	  //删除指定的数据
	  $scope.cityAPI.deleteRow= function(){
		  $http({
			  url:"base/baseCityAction!deleteBaseCityByIds.action",
			  method:"get",
			  params:{
				 ids : $scope.rowIds
			  }
		  }).then(function(resp){
			  if(resp.data.code==1){
				  table.ajax.reload();
			  }else{
				  alert(resp.data.message);
			  }
		  });
	  }
	
	/**
	 * 表单的初始方法
	 */
	var table ;
	var initCityTable = function(){
		table = $("#citysTable").on('preXhr.dt', function ( e, settings, data ){
			data.id = $scope.select.province ;
			data.timeStamp = new Date().getTime();//添加时间戳
		}).DataTable({
			"sAjaxSource":"base/baseProvinceAction!listBaseCityByProvince.action",
	    	"bServerSide":true,
	    	"sAjaxDataProp":"data",
	    	"pageLength":10,
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
	    	  "aoColumns": [{
	            "orderable": false,
	            "render": function(param){
	              return '<label class="i-checks"><input type="checkbox"><i></i></label>';
	            }
	          }, {
	            "mDataProp": "cityCode"
	          }, {
	            "mDataProp": "cityName"
	          }, {
	            "mDataProp": "cellCode"
	          }, {
	            "mDataProp": "isHot",
	            "render":function(param){
	            	switch(param){
	            	case 0 : return "不是"; break;
	            	case 1 : return "是";break;
	            	}
	            }
	          }, {
	            "mDataProp": "isShow",
	            "render":function(param){
	            	switch(param){
	            	case 0 : return "不是"; break;
	            	case 1 : return "是";break;
	            	}
	            }
	          }, {
	            "mDataProp": "memo"
	          }],
	          "fnCreatedRow": function(nRow, aData, iDataIndex){
	        	  $(nRow).attr('data-id', aData['id']);
	        	  $(nRow).find("input").click(function(e){
	        		  clickTr(e,$(this));
	        	  });
	        	  $(nRow).dblclick(function(){
	        		  $scope.setCanEdit(true,$(this).data('id'));
	        		  $scope.seeDetails($(this).data('id'));//调用上级controller中的查看方法
	        	  });
	        	  $(nRow).click(function(e){
	        		  if(e.target.nodeName=="TD"||e.target.nodeName=="td"){
	        			  showBusAtoms($(this));
	        		  }
	        	  });
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
	function showBusAtoms(target){
		var id = target.data('id');//获取这一表格行的id
		//点击一行变色的方法
		if ( target.hasClass('selected') ) {
			target.removeClass('selected');
			target.find("td").css("background-color","white");
			$scope.setCanEdit(false);//设置不可以查看和修改
        }else {
        	table.$('tr.selected').find("td").css("background-color","white");
        	table.$('tr.selected').removeClass('selected');
        	target.addClass('selected');
        	target.find("td").css("background-color","#b0bed9");
        	$scope.setCanEdit(true,id);//设置可以查看和修改
        }
	}
	
	/**
	 * 弹窗事件
	 */
	var showModal = function(name,id,cityId,isDetails){
		var modalInstance = $modal.open({
   	     templateUrl: 'src/tpl/base/district/city/addCity.html',
   	     size: 'lg',
   	     backdrop:true,
   	     resolve:{
   	    	provinceName:function(){
   	    		return name;
   	    	},
   	    	 provinceId:function(){
   	    		 return id;
   	    	 },
   	    	 cityId:function(){
   	    		 return cityId;
   	    	 },
   	    	 isDetails:function(){
   	    		 return isDetails;
   	    	 }
   	     },
   	     controller:"addCityController"
   	   });
		/**
		 * 弹窗关闭事件
		 */
		modalInstance.result.then(function () {
			table.ajax.reload();
    	});
	}
	
	
	
	//结束
}]);