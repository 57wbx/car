'use strict';

app.controller('baseCityController',['$rootScope','$scope','$state','$timeout','$http','sessionStorageService','utilService','roleBtnService',function($rootScope,$scope,$state,$timeout,$http,sessionStorageService,utilService,roleBtnService){
	
	var roleBtnUiClass = "app.basecity";//用于后台查找按钮权限
	roleBtnService.getRoleBtnService(roleBtnUiClass,$scope);
	
	$scope.rowIds = [];//用来保存所选列表的id
	
	$scope.province = [];
	$scope.city = [];
	$scope.area = [];
	
	$scope.show = {};
	
	$scope.select = {
			province : null,
			city:null,
			area:null
	}
	/**
	 * 清除缓存中的除了指定数组中和永久缓存的数据
	 */
	var cacheKeys = ["province","select.province","city","select.city","area","select.area","show.titleName"];
	sessionStorageService.clearNoCacheItem(cacheKeys);
	/**
	 * 刷新的时候获取这些数据
	 */
	$scope.province = sessionStorageService.getItemObj("province");
	$scope.select.province = sessionStorageService.getItemStr("select.province");
	$scope.city = sessionStorageService.getItemObj("city");
	$scope.select.city = sessionStorageService.getItemStr("select.city");
	$scope.area = sessionStorageService.getItemObj("area");
	$scope.select.area = sessionStorageService.getItemStr("select.area");
	$scope.show.titleName = sessionStorageService.getItemStr("show.titleName");
	/**
	 * 是否刚刚刷新的标识符,3代表不是刚刚刷新 0-3代表刚刚刷新 现在还在初始化
	 */
	var firstLen = 3;
	if($scope.province){
		firstLen = 0;
	}
	
	
	
	/**
	 * 保存在父容器中的API  需要在子容器中初始化
	 */
	$scope.cityAPI = {
			/**
			 * 当在选择框中选择省份、城市、区域的时候  刷新子view中城市或者区域的显示内容
			 * 需要在子类中初始化
			 * 调用方法  $scope.cityAPI.freshData();
			 */
			freshDataTable:undefined,
			/**
			 * 新增的方法，当用户点击新增的时候将调用这个方法
			 * 需要在子类中初始化
			 * 调用方法 $scope.cityAPI.addRow();
			 */
			addRow:undefined,
			/**
			 * 修改一行记录的方法
			 * 需要在子类中初始化
			 * 调用方法 $scope.cityAPI.editRow();
			 */
			editRow:undefined,
			/**
			 * 查看一条记录的详细信息
			 * 需要在子类中初始化
			 * 调用方法  $scope.cityAPI.seeDetails();
			 */
			seeDetails:undefined,
			/**
			 * 删除记录的方法
			 * 需要在子类中初始化
			 * 调用方法 $scope.cityAPI.deleteRow();
			 */
			deleteRow:undefined
	};
	
	
	if(firstLen>=3){
		/**
		 * 初始化省份内容，当用户是第一次进该页面的时候被调用
		 */
		$http({
			url:"base/baseProvinceAction!listBaseProvinceNoCitys.action",
			method:"get"
		}).then(function(resp){
			if(resp.data.code==1){
				$scope.province = resp.data.data ;
				sessionStorageService.setItem("province",$scope.province);
			}
		});
	}
	
	
	
	  // 更换国家的时候清空省
	  $scope.$watch('select.province', function(country) {
		  
		  if(firstLen>=3){
			  //调用子容器中需要显示的内容
			  if($scope.cityAPI.freshData){
				  $scope.cityAPI.freshData();
			  }
			  /**
			   * 初始化城市内容
			   */
			  if($scope.select.province){
				  $http({
					  url:"base/baseProvinceAction!listBaseCityByProvince.action",
					  method:"get",
					  params:{
						  id:$scope.select.province
					  }
				  }).then(function(resp){
					  if(resp.data.code==1){
						  $scope.city = resp.data.data;
						  sessionStorageService.setItem("select.province",$scope.select.province);
						  $scope.show.titleName = utilService.getObjectFromArray("id",$scope.select.province,$scope.province).name;
						  sessionStorageService.setItem("show.titleName",$scope.show.titleName);
						  sessionStorageService.setItem("city",$scope.city);
						  $state.go("app.basecity.city");
					  }
				  });
			  }
			  
			  $scope.select.city = null;
			  sessionStorageService.removeItem("select.city");
		  }else{
			  firstLen++;
		  }
	  });
	  // 更换省的时候清空城市
	  $scope.$watch('select.city', function(province) {
		  if(firstLen>=3){
			  
		
			  
			  if($scope.select.city){
				  $http({
					  url:"base/baseCityAction!listBaseAreaByBaseCity.action",
					  method:"get",
					  params:{
						  id:$scope.select.city
					  }
				  }).then(function(resp){
					  if(resp.data.code==1){
						  $scope.area = resp.data.data;
						  sessionStorageService.setItem("select.city",$scope.select.city);
						  $scope.show.titleName = utilService.getObjectFromArray("id",$scope.select.city,$scope.city).cityName;
						  sessionStorageService.setItem("show.titleName",$scope.show.titleName);
						  sessionStorageService.setItem("area",$scope.area);
						  $state.go("app.basecity.area");
					  }
				  });
			  }
			  
			  $scope.select.area = null;
			  sessionStorageService.removeItem("select.area");
			  
			//调用子容器中需要显示的内容
			  if($scope.cityAPI.freshData){
				  $scope.cityAPI.freshData();
			  }
		  }else{
			  firstLen++;
		  }
	  });
	  // 更换地区的时候查询子地区的信息
	  $scope.$watch('select.area', function(province) {
		  if(firstLen>=3){
				  sessionStorageService.setItem("select.area",$scope.select.area);
				  if(utilService.getObjectFromArray("id",$scope.select.area,$scope.area)||utilService.getObjectFromArray("id",$scope.select.city,$scope.city)){
					  $scope.show.titleName = utilService.getObjectFromArray("id",$scope.select.area,$scope.area)?
							  utilService.getObjectFromArray("id",$scope.select.area,$scope.area).name
							  :utilService.getObjectFromArray("id",$scope.select.city,$scope.city).cityName;
							  sessionStorageService.setItem("show.titleName",$scope.show.titleName);
				  }
				  
				  sessionStorageService.setItem("area",$scope.area);
				  
				  
					//调用子容器中需要显示的内容
				  if($scope.cityAPI.freshData){
					  $scope.cityAPI.freshData();
				  }
			  }else{
				  firstLen++;
			  }
	  });
	
	
	/**
	 * 并无实际的意义，但是如果没有用这个方法的话，页面中菜单按钮将不会根据属性的值进行变化
	 * 
	 * 2015.7.15 利用一个隐藏的按钮来解决按钮状态值更新的事件clickId
	 */
//	var hiddenButton = $("#clickId");
	function show(){
		$timeout(function(){
			//无实际作用，更新页面中的按钮菜单样式
		},0);
	}
	/**
	 * 设置按钮的状态按钮 其中包括三个属性 当选择0个 、1个、多个
	 * 0个 都不显示
	 * 1个显示修改和查看
	 * 多个显示删除
	 * canEdit 变量是在该页面中显示是否可以修改、详细信息的按钮
	 * 
	 */
	$scope.canEdit = false;
	$scope.setCanEdit = function(booleanValue,id){
		$scope.canEdit = booleanValue ;
		if(id){
			$scope.editId = id;
		}
		show();
	}
	$scope.setButtonStatus = function(){
		var len = $scope.rowIds.length ;//id的个数
		if(len==0){
			$scope.isSingle = false ;
			$scope.isMulti = false ;
		}else if(len==1){
			$scope.isSingle = true ;
			$scope.isMulti = false ;
		}else{
			$scope.isSingle = false ;
			$scope.isMulti = true ;
		}
		show();
//		hiddenButton.trigger("click");
	}
	
	  // 设置按钮的状态值
	  $scope.setBtnStatus = function(){
		  console.info("设置按钮",$scope.rowIds);
	    if($scope.rowIds.length === 0){
	      $scope.single = true;
	      $scope.locked = true;
	      $scope.mutiple = true;
	    }else if($scope.rowIds.length === 1){
	      $scope.single = false;
	      $scope.locked = false;
	      $scope.mutiple = false;
	    }else{
	      $scope.single = true;
	      $scope.locked = true;
	      $scope.mutiple = false;
	    }

	    $scope.$evalAsync();
	  };
	
	/**
	 * 清空需要操作的id，主要是在busAtomListController中调用
	 */
	$scope.clearRowIds = function(){
		$scope.rowIds = [];
		$scope.setButtonStatus();
	}
	
	/**
	 * 新增按钮的方法
	 */
	$scope.addRow = function(){
		if($scope.cityAPI.addRow){
			$scope.cityAPI.addRow();
		}
	}
	
	/**
	 * 查看按钮的方法
	 */
	$scope.seeDetails = function(id){
		if(id!=null&&id!=""){
			$scope.clearRowIds();
			$scope.rowIds.push(id);
		}else if($scope.editId){
			$scope.rowIds.push($scope.editId);
		}
		 if($scope.cityAPI.seeDetails){
			 $scope.cityAPI.seeDetails();
		 }
	}
	
	/**
	 * 修改方法的按钮
	 */
	$scope.editRow = function(){
		if($scope.editId){
			$scope.clearRowIds();
			$scope.rowIds.push($scope.editId);
		}
		if($scope.cityAPI.editRow){
			$scope.cityAPI.editRow();
		}
	}
	
	/**
	 * 页面弹出框对象
	 */
	var mask = $('<div class="mask"></div>');
	var container = $('#dialog-container');	
	var doIt = function(){};
	 // 执行操作
	  $rootScope.do = function(){
	    doIt();
	  };

	  // 模态框退出
	  $rootScope.cancel = function(){
	    mask.remove();
	    container.addClass('none');
	  };  
	/**
	 * 删除方法的按钮
	 */
	$scope.deleteRow = function(){
		if($scope.cityAPI.deleteRow){
			 mask.insertBefore(container);
			 container.removeClass('none');
			 doIt = function(){
				 if($scope.rowIds.length>0){
					 $scope.cityAPI.deleteRow();
					 mask.remove();
					  container.addClass('none');
				  }
			 }
		}
	}
	
	
	
	
	
	//结束方法
}]);