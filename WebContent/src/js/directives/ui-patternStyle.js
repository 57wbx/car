/**
* input 的验证属性，当输入元素上面有ng-pattern的时候，该标签起作用
*/
app.directive("patternStyle",[function(){
	return {
		restrict:"A",
		replace:true,
		scope:{
			formInput:'=',
			inputMessage:'='
		},
		template:'<div class="inputerror" ng-show="formInput.$invalid">\
			<span class="glyphicon glyphicon-warning-sign" ></span>   ：          <span ng-show="formInput.$error.pattern">{{inputMessage.pattern}};</span>\
													<span ng-show="formInput.$error.required&&inputMessage.required">{{inputMessage.required}};</span><span ng-show="formInput.$error.required&&!inputMessage.required">{{defaultRequiredMessage}};</span>\
												    <span ng-show="formInput.$error.minlength&&inputMessage.minlength">{{inputMessage.minlength}};</span><span ng-show="formInput.$error.minlength&&!inputMessage.minlength">{{defaultMinLengthMessage}};</span>\
													<span ng-show="formInput.$error.maxlength&&inputMessage.maxlength">{{inputMessage.maxlength}};</span><span ng-show="formInput.$error.maxlength&&!inputMessage.maxlength">{{defaultMaxLengthMessage}};</span>\
													<span ng-show="formInput.$error.email&&inputMessage.email">{{inputMessage.email}};</span><span ng-show="formInput.$error.email&&!inputMessage.email">{{defaultEamilMessage}};</span>\
													<span ng-show="formInput.$error.min&&inputMessage.min">{{inputMessage.min}};</span><span ng-show="formInput.$error.min&&!inputMessage.min">{{defaultMinMessage}};</span>\
													<span ng-show="formInput.$error.max&&inputMessage.max">{{inputMessage.max}};</span><span ng-show="formInput.$error.max&&!inputMessage.max">{{defaultMaxMessage}};</span>\
													<span ng-show="formInput.$error.custom_pattern&&inputMessage.custom_pattern">{{inputMessage.custom_pattern}};</span>\
						</div>',
		link:function(scope,ele,attr){
			scope.defaultRequiredMessage = "字段不能为空";
			scope.defaultEamilMessage = "必须为邮箱格式";
			if(ele.prev().attr("ng-minlength")){
				scope.defaultMinLengthMessage = "该字段不能少于"+ele.prev().attr("ng-minlength")+"个字符";
			}
			if(ele.prev().attr("ng-maxlength")){
				scope.defaultMaxLengthMessage = "该字段不能大于"+ele.prev().attr("ng-maxlength")+"个字符";
			}
			if(ele.prev().attr("min")){
				scope.defaultMinMessage = "输入的值不能小于"+ele.prev().attr("min");
			}
			if(ele.prev().attr("max")){
				scope.defaultMaxMessage = "输入的值不能大于"+ele.prev().attr("max");
			}
		}
	};
}]);