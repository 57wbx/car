/**
* 城市地区联动的div
* 使用方法  可以参考carshop_edit_html
*  示例：<div city province="formData.province"  city-value="formData.city"  area="formData.area" small-area="formData.smallArea"></div>
*  其中 formData.province  是需要绑定的省份信息
*  formData.city 是需要绑定的城市信息
*  formData.area 是需要绑定的区域信息
*  formData.smallArea 是需要绑定的子区域信息
*  
*  @author zw
*/
app.directive("city",['modelDataCacheService',function(modelDataCacheService){
	return {
		restrict:"A",
		replace:true ,
		scope:{
			selectProvince:'=province',
			selectCityValue:'=cityValue',
			selectArea:'=area',
			selectSmallArea:'=smallArea'
		},
		template:'<div class="row">\
									<label class="col-sm-1 control-label">省份</label>\
									<div class="col-sm-2">\
											<select class="form-control" ng-model="selectProvince" ng-options="p.code as p.name for p in provinces">\
											</select>\
									</div>\
									<label class="col-sm-1 control-label" ng-show="selectProvince && citys">城市</label>\
									<div class="col-sm-2" ng-show="selectProvince && citys">\
											<select class="form-control" ng-model="selectCityValue" ng-options="c.cellCode as c.cityName for c in citys"  >\
											</select>\
									</div>\
									<label class="col-sm-1 control-label" ng-show="selectCityValue && areas">区/县</label>\
									<div class="col-sm-2" ng-show="selectCityValue && areas">\
												<select class="form-control" ng-model="selectArea" ng-options="a.cellCode as a.name for a in areas" >\
												</select>\
									</div>\
									<label class="col-sm-1 control-label" ng-show="selectArea && smallAreas">镇/片区</label>\
									<div class="col-sm-2" ng-show="selectArea && smallAreas">\
													<select class="form-control" ng-model="selectSmallArea" ng-options="sa.cellCode as sa.name for sa in smallAreas" >\
													</select>\
									</div>\
						</div>',
		link:function(scope,ele,attr){
			modelDataCacheService.provinceDataService().then(function(resp){
				scope.provinces = resp ;
			});
			scope.$watch("selectProvince",function(n,o){
				if(o){//当旧值有值时才将其他的变为空，防止在修改页面的时候将数据清空了
					scope.citys = null ;
					scope.selectCityValue = null ;	
				}
				modelDataCacheService.cityDataService(n).then(function(resp){
					if(resp&&resp.length>0){
						scope.citys = resp ;
					}
				});
			});
			scope.$watch("selectCityValue",function(n,o){
				if(o){
					scope.areas = null ;
					scope.selectArea = null ;
				}
				modelDataCacheService.areaDataService(n).then(function(resp){
					if(resp&&resp.length>0){
						scope.areas = resp ;
					}
				});
			});
			scope.$watch("selectArea",function(n,o){
				if(o){
					scope.smallAreas = null;
					scope.selectSmallArea = null ;
				}
				modelDataCacheService.smallAreaDataService(n).then(function(resp){
					if(resp&&resp.length>0){
						scope.smallAreas = resp ;
					}
				});
			});
			
		}
	};
}]);