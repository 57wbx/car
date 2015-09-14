app.controller("areasController",['$scope','$modal','$http','dataTableSearchService',function($scope,$modal,$http,dataTableSearchService){
	
	
	$scope.clearRowIds();//首先清空上级controller中的ids
	
	if($scope.select.province){
		$scope.$evalAsync(function(){//延迟加载
			initAreaTable();
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
		  if($scope.select.area){//新增的行是属于地区下面的地区
			  showModal($scope.show.titleName,$scope.select.area,false,null,false);
		  }else if($scope.select.city){
			  showModal($scope.show.titleName,$scope.select.city,true,null,false);
		  }
	  }
	//修改一行的数据的方法
	  $scope.cityAPI.editRow = function(){
		  if($scope.select.area){//新增的行是属于地区下面的地区
			  showModal($scope.show.titleName,$scope.select.area,false,$scope.rowIds[0],false);
		  }else if($scope.select.city){
			  showModal($scope.show.titleName,$scope.select.city,true,$scope.rowIds[0],false);
		  }
	  }
	  //查看一行数据的详细信息
	  $scope.cityAPI.seeDetails = function(){
		  showModal($scope.show.titleName,null,false,$scope.rowIds[0],true);
	  }
	  //删除指定的数据
	  $scope.cityAPI.deleteRow= function(){
		  $http({
			  url:"base/baseAreaAction!deleteBaseAreaByIds.action",
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
	var initAreaTable = function(){
		table = $("#areasTable").on('preXhr.dt', function ( e, settings, data ){
			if($scope.select.area){
				data.parenAreaId = $scope.select.area ;
			}
			data.id = $scope.select.city ;
			data.timeStamp = new Date().getTime();
		}).DataTable({
			"sAjaxSource":"base/baseCityAction!listBaseAreaByBaseCity.action",
	    	"bServerSide":true,
	    	"sAjaxDataProp":"data",
	    	"pageLength":10,
	    	"dom": '<"top">rt<"bottom"ip><"clear">',
	    	  "aoColumns": [{
	            "orderable": false,
	            "render": function(param){
	              return '<label class="i-checks"><input type="checkbox"><i></i></label>';
	            }
	          }, {
	            "mDataProp": "cellCode"
	          }, {
	            "mDataProp": "name"
	          }, {
	            "mDataProp": "sortCode"
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
	var showModal = function(name,id,parentIsCity,areaId,isDetails){
		var modalInstance = $modal.open({
   	     templateUrl: 'src/tpl/base/district/city/addArea.html',
   	     size: 'lg',
   	     backdrop:true,
   	     resolve:{
   	    	parentName:function(){//上级 名称
   	    		return name;
   	    	},
   	    	 parentId:function(){//上级 id
   	    		 return id;
   	    	 },
   	    	 parentIsCity:function(){
   	    		return parentIsCity ;
   	    	 },
   	    	 areaId:function(){//修改或这查看时，本记录的id
   	    		 return areaId;
   	    	 },
   	    	 isDetails:function(){//是否是查看
   	    		 return isDetails;
   	    	 }
   	     },
   	     controller:"addAreaController"
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