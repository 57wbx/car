app.controller("citysController",['$scope','$modal','$http','dataTableSearchService',function($scope,$modal,$http,dataTableSearchService){
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
	    	scrollX:true,
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
	          },
	          "drawCallback":function(setting){
	        	  dataTableSearchService.initClick(table,$scope.rowIds,$scope.setBtnStatus,$scope.seeDetails);
	          }
		});
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