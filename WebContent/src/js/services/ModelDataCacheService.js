/**
 * 该服务提供了向服务端获取数据，并且缓存经常访问的数据（该数据不会经常变化），例如busType 类型
 * 可以设置刷新的次数，但其他的服务调用了多少次以后可以进行远程刷新 
 */

app.factory("modelDataCacheService",['$http','$q',function($http,$q){
	/**
	 * 全局变量@1 
	 */
	var busTypeTreeData = null;//缓存自定义指令ui-busTypeTree.js所需的数据
	var busTypeTreeDataFlushTimes = 5;//当其他的应用调用该数据五次以后，就进行远程服务端访问，获取最新的数据
	var busTypeTreeDataNowTimes = 0;//数据已经被获取几次了，每次获取数据将该次数加一
	/**
	 * 全局变量@2
	 */
	var busTypeZtreeData = null;//该数据主要是为了业务ztree提供的缓存数据
	var busTypeZtreeDataFlushTimes = 5;
	var busTypeZtreeDataNowTimes = 0;
	/**
	 * 全局变量@3
	 */
	var orgNoDeptZtreeData = null;//该数据主要是为了业务ztree提供的缓存数据
	var orgNoDeptZtreeDataFlushTimes = 5;
	var orgNoDeptZtreeDataNowTimes = 0;
	/**
	 * 全局变量@4
	 */
	var provinceData = null;//该数据主要是为了业务ztree提供的缓存数据
	var provinceDataFlushTimes = 5;
	var provinceDataNowTimes = 0;
	
	serviceInstance = {
			/**
			 * 使用全局变量 @1
			 * 获取自定义指令ui-busTypeTree.js所需的数据
			 * @Param needFlush 是否需要立即向服务器刷新数据，获取最新的数据，true是立即从服务器中刷新数据
			 */
			BusTypeTreeDataService : function(needFlush){
				var deferred = $q.defer();
				if(needFlush || !busTypeTreeData || (busTypeTreeDataNowTimes > busTypeTreeDataFlushTimes)){
					//需要从网上刷新最新的数据
					$http({
						url:"base/busTypeAction!listBusType.action",
						method : "get"
					}).then(function(resp){
						if(resp.data.code==1){
							busTypeTreeData = resp.data.rows ;
							busTypeTreeDataNowTimes = 0;
						}
						console.info("从网上获取数据");
						busTypeTreeData.push({
					 		busTypeCode:"",
					 		busTypeName:"所有类型",
					 		parentId:"",
					 		simpleName:"所有类型",
					 	});
						deferred.resolve(busTypeTreeData);
					});
				}else{
					busTypeTreeDataNowTimes ++;
					console.info("缓存数据中读取数据");
					deferred.resolve(busTypeTreeData);
				}
				return deferred.promise;
			},
			/**
			 * 使用全局变量 @2
			 * 获取自定义指令ui-busTypeZtree.js 中所需的数据
			 * @Param needFlush 是否需要立即向服务器刷新数据，获取最新的数据，true是立即从服务器中刷新数据
			 */
			busTypeZtreeDataService:function(needFlush){
				var deferred = $q.defer();
				if(needFlush || !busTypeZtreeData || (busTypeZtreeDataNowTimes > busTypeZtreeDataFlushTimes)){
					//需要从网上刷新最新的数据
					$http({
						url:"base/busTypeAction!busTypeTree.action",
						method : "get"
					}).then(function(resp){
						if(resp.data.code==1){
							busTypeZtreeData = resp.data.busTypes ;
							busTypeZtreeDataNowTimes = 0;
						}
						console.info("从网上获取数据@2");
						deferred.resolve(busTypeZtreeData);
					});
				}else{
					busTypeZtreeDataNowTimes ++;
					console.info("缓存数据中读取数据@2");
					deferred.resolve(busTypeZtreeData);
				}
				return deferred.promise;
			},
			/**
			 * 组织架构的选择树，在这个选择树中不能出现部门，其他的的组织都可以出现
			 * @3
			 */
			orgNoDeptZtreeDataService:function(needFlush){
				var deferred = $q.defer();
				if(needFlush || !orgNoDeptZtreeData || (orgNoDeptZtreeDataNowTimes > orgNoDeptZtreeDataFlushTimes)){
					//需要从网上刷新最新的数据
					$http({
						url:"basedata/orgAction!listOrgNoDept.action",
						method : "get"
					}).then(function(resp){
						if(resp.data.code==1){
							orgNoDeptZtreeData = resp.data.orgNoDept ;
							orgNoDeptZtreeDataNowTimes = 0;
						}
						console.info("从网上获取数据@3");
						deferred.resolve(orgNoDeptZtreeData);
					});
				}else{
					orgNoDeptZtreeDataNowTimes ++;
					console.info("缓存数据中读取数据@3");
					deferred.resolve(orgNoDeptZtreeData);
				}
				return deferred.promise;
			},
			//以上是方法定义
			/**
			 * 从服务端获取省份的所有数据
			 */
			provinceDataService:function(needFlush){
				var deferred = $q.defer();
				if(needFlush || !provinceData || (provinceDataNowTimes > provinceDataFlushTimes)){
					//需要从网上刷新最新的数据
					$http({
						url:"base/baseProvinceAction!listBaseProvinceNoCitys.action",
						method : "get"
					}).then(function(resp){
						if(resp.data.code==1){
							provinceData = resp.data.data ;
							provinceDataNowTimes = 0;
						}
						console.info("从网上获取数据@4");
						deferred.resolve(provinceData);
					});
				}else{
					provinceDataNowTimes ++;
					console.info("缓存数据中读取数据@4");
					deferred.resolve(provinceData);
				}
				return deferred.promise;
			},
	/**
	 * 根据省份的编码查询所有的城市
	 */
			cityDataService:function(code){
				var deferred = $q.defer();
				if(code){
					//需要从网上刷新最新的数据
					$http({
						url:"base/baseProvinceAction!listCityByProvinceCode.action",
						method : "post",
						data:{
							code:code
						}
					}).then(function(resp){
						if(resp.data.code==1){
							cityData = resp.data.data ;
						}
						deferred.resolve(cityData);
					});
				}
				return deferred.promise;
			},
	/**
	 * 根据城市的编码查询所有的区域
	 */
			areaDataService:function(code){
				var deferred = $q.defer();
				if(code){
					//需要从网上刷新最新的数据
					$http({
						url:"base/baseCityAction!listAreaByCityCode.action",
						method : "post",
						data:{
							cellCode:code
						}
					}).then(function(resp){
						if(resp.data.code==1){
							var areaData = resp.data.data ;
						}
						deferred.resolve(areaData);
					});
				}
				return deferred.promise;
			},
			/**
			 * 根据城市的编码查询所有的区域
			 */
			smallAreaDataService:function(code){
				var deferred = $q.defer();
				if(code){
					//需要从网上刷新最新的数据
					$http({
						url:"base/baseAreaAction!listSmallAreaByAreaCode.action",
						method : "post",
						data:{
							cellCode:code
						}
					}).then(function(resp){
						if(resp.data.code==1){
							var areaData = resp.data.data ;
						}
						deferred.resolve(areaData);
					});
				}
				return deferred.promise;
			},
			/**
			 * 从服务端获取所有菜单的
			 */
			menuDataService:function(){
				var deferred = $q.defer();
					//需要从网上刷新最新的数据
					$http({
						url:"sys/sys/menuAction!listMenu.action",
						method : "post"
					}).then(function(resp){
						if(resp.data.code==1){
							var menuData = resp.data.data ;
						}
						deferred.resolve(menuData);
					});
				return deferred.promise;
			}
			
	//对象定义结束
	};
	return serviceInstance;
}]);