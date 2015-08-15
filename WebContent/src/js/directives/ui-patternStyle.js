/**
* input 的验证属性，当输入元素上面有ng-pattern的时候，该标签起作用
*/
app.directive("patternStyle",[function(){
	return {
		require:'ngModel',
		link:function(scope,element,attr,c){
			console.info(scope);
			console.info(element);
			console.info(attr);
			console.info(c);
			scope.$watch(attr.ngModel,function(){
				if(!c.$valid&&c.$dirty){
					console.info("sdf");
					element.removeClass("ng-valid");
					element.addClass("ng-invalid");
					console.info("sdf12312");
//					c.$setValidity("pattern",false);
				}else{
					c.$setValidity("pattern",true);
				}
			});
		}
	};
}]);