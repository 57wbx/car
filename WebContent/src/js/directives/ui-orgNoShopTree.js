/**
 * 该树是业务类型的树，主要作用是供list界面筛选出编码规范服务业务类型的所有记录
 * @author zw
 */
app.directive("orgNoShopTree",['$http','utils','modelDataCacheService','utilService',function($http,utils,modelDataCacheService,utilService){
	return {
		restrict:"A",
		transclude:true,
		template:'<div class="panel panel-default">'+
					'<div class="panel-body">'+
					'<div class="wrapper-md">'+
					'<div class="b-a r-2x" ng-init="loading=true">'+
					'<div ng-if="loading" class="loading">'+
						'<i class="glyphicon glyphicon-repeat"></i>'+
					'</div>'+
					'<span ng-if="doing_async">...加载中...</span>'+
					'<div ng-transclude></div>'+
			'</div></div></div>',
			compile: function(tEle, tAttrs, transcludeFn){
				   return {
					   pre: function(scope, iElem, iAttrs){
						   
						   scope.treeData1 = [];
						   
						   modelDataCacheService.orgNoDeptZtreeDataService().then(function(orgData){
							   scope.treeData1 = utilService.wrapData(orgData,"id","pId","name") ;
						   });
						   
						   
						   
					   }
				   }
			}
	//结束对象
	}
}]);