/**
*  该标签的作用是：用户点击按钮，将按钮禁用、并且提示正在操作。避免表单重复提交
*  使用方法：
*  <div doing button-value="保存" doing-value="正在保存中..." is-doing="isDoing" doing-method="submit();" form-valid="form.$valid"></div>
*  @param 必填 is-doing 为true 的时候显示 正在保存中；为false的时候，显示 保存。
*  @param 可填 doing-method 当用户在点击保存按钮的时候需要调用的方法
*  @param 可填 form-valid 为true时 保存按钮为可以点击的状态，为false时 保存按钮为不可点击状态   
*  @author zw
*/
app.directive("doing",[function(){
	return {
		restrict:"A",
		replace:true,
		scope:{
			buttonValue:'@',
			doingValue:'@',
			isDoing:'=',
			doingMethod:'&',
			formValid:'='
		},
		template:'<div class="row">\
								<div ng-if="!isDoing">\
										<button style="width:100%;" ng-disabled="!formValid && defaultFormValid" class="btn btn-success" ng-click="submit();">{{buttonValue}}</button>\
								</div>\
								<div ng-if="isDoing">\
										<button style="width:100%;" class="btn btn-success" disabled>{{doingValue}}</button>\
								</div>\
						</div>',
		link:function(scope,ele,attr){
			if(!attr.buttonValue){
				//点击按钮的值不存在时，默认为确定
				scope.buttonValue = "确定" ;
			}
			if(!attr.doingValue){
				//点击按钮的值不存在时，默认为确定
				scope.doingValue = "操作中..." ;
			}
			if(!attr.formValid){
				//单表单验证不存在时，默认为true
				scope.defaultFormValid = false ;
			}else{
				scope.defaultFormValid = true ;
			}
			//执行点击操作
			scope.submit = function(){
				scope.isDoing = true ;
				if(scope.doingMethod){
					scope.doingMethod();
				}
			}
		}
		//link 结束
	}
}]);