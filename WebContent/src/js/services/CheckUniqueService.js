/**
 * 主要总用时用来判断各种编码是否是唯一的
 */
app.factory("checkUniqueService",['$http',function($http){
	serviceInstance = {
			/**
			 * 检查子项的编码是否唯一
			 * @fid 是子项的id，如果没有就为null
			 * @atomCode 需要验证的子项编码
			 */
			checkBusAtomCodeUnique:function(fid,atomCode){
				return $http({
					url:"base/busAtomAction!checkBusAtomCodeUnique.action",
					method:"get",
					params:{
						fid:fid,
						atomCode:atomCode
					}
				});
			},
			/**
			 * 检查配件的编码是否唯一
			 */
			checkAutoPartCodeUnique:function(id,partCode){
				return $http({
					url:"base/autoPartAction!checkPartCodeUnique.action",
					method:"get",
					params:{
						id:id,
						partCode:partCode
					}
				});
			},
			/**
			 * 检查app版本 内部号是否唯一
			 */
			checkVersionCodeUnique:function(id,versionCode){
				return $http({
					url:"tig/updateVersionAction!checkVersionCodeUnique.action",
					method:"get",
					params:{
						id:id,
						versionCode:versionCode
					}
				});
			},
			/**
			 * 检查app版本 外部版本号是否唯一
			 */
			checkVersionNameUnique:function(id,versionName){
				return $http({
					url:"tig/updateVersionAction!checkVersionNameUnique.action",
					method:"get",
					params:{
						id:id,
						versionName:versionName
					}
				});
			},
			/**
			 * 检查后台用户登陆账号是否唯一
			 */
			checkUserCodeUnique:function(id,userCode){
					return $http({
						url:"basedata/userAction!checkUserCodeUnique.action",
						method:"get",
						params:{
							id:id,
							number:userCode
						}
				});
			},
			/**
			 * 检查商家的服务项是否重复
			 */
			checkShopItemCodeUnique:function(id,itemCode){
				return $http({
					url:"shop/shopItemAction!checkShopItemCodeUnique.action",
					method:"get",
					params:{
						fid:id,
						itemCode:itemCode
					}
			});
			},
			/**
			 * 检查商家的服务子项编码是否存在
			 */
			checkShopAtomCodeUnique:function(fid,atomCode){
				return $http({
					url:"shop/shopAtomAction!checkShopAtomCodeUnique.action",
					method:"get",
					params:{
						fid:fid,
						atomCode:atomCode
					}
				});
			},
			/**
			 * 检查商家的套餐编码是否重复
			 */
			checkShopPackageCodeIsUnique:function(fid,packageCode){
				return $http({
					url:"shop/shopPackageAction!checkShopPackageCodeIsUnique.action",
					method:"get",
					params:{
						fid:fid,
						packageCode:packageCode
					}
				});
			}
			
			
			//
	};
	return serviceInstance;
}]);