/**
 * 时间控件
 * zw
 */
app.directive("date",['$compile',function($compile){
	return {
		restrict:"A",
		replace:true,
		scope:{
			dateModel:'='
		},
		template:'<input type="text" class="form-control" ng-model=dateModel >',
		link:function(scope,ele,attrs){
			//初始化时间控件
			ele.focus(
			    		function(){
				    		var optionSet = {
									singleDatePicker : true,
									timePicker : true,
									format : 'YYYY-MM-DD HH:mm'
								};
				    		ele.daterangepicker(optionSet).on('apply.daterangepicker', function(ev){
				    			scope.$evalAsync(function(){
				    				scope.dateModel = ele.val();
				    			});
				    		});
			    		}
			);
		}
	}
	
}]);