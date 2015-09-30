/**
 * 时间控件 
 * <div date date-model="formData.endTimeStr" format="YYYY-MM-DD HH:mm:ss" time-picker=true start-model="start" end-model="end"  date-controller="dateController"></div>
 * <div date date-model="formData.starTimeStr" format="YYYY-MM-DD" time-picker=true single-date-picker="true" date-controller="dateController"></div>
 * <div date date-model="date" format="YYYY-MM-DD" time-picker=true start-model="formData.starTimeStr" end-model="formData.endTimeStr"  date-controller="dateController"></div>
 * @author zw
 */
app.directive("date",['$compile',function($compile){
	return {
		restrict:"A",
		replace:true,
		scope:{
			dateModel:'=',
			format:'@',
			timePicker:'@',
			singleDatePicker:'@',
			dateController:'=',
			startModel:'=',
			endModel:'=',
		},
		template:'<input type="text" class="form-control" ng-model=dateModel >',
		link:function(scope,ele,attrs){
			console.info("日志的配置属性",scope.options);
			var simpleDateFormate = "YYYY-MM-DD";
			if(scope.format){
				simpleDateFormate = scope.format ;
			}
			var timePicker = false ;
			console.info("scope.timePicker",scope.timePicker);
			if(scope.timePicker && scope.timePicker == "true"){
				timePicker = true ;
			}
			var singleDatePicker = false ;
			console.info("scope.singleDatePicker",scope.singleDatePicker);
			if(scope.singleDatePicker && scope.singleDatePicker == "true"){
				singleDatePicker = true ;
			}
			
			var separator = " 至 " ;
			
			var _zh_cn = {
			        "format": simpleDateFormate,
			        "separator": separator,
			        "applyLabel": "确定",
			        "cancelLabel": "取消",
			        "fromLabel": "From",
			        "toLabel": "To",
			        "customRangeLabel": "Custom",
			        "daysOfWeek": [
			            "日",
			            "一",
			            "二",
			            "三",
			            "四",
			            "五",
			            "六"
			        ],
			        "monthNames": [
			            "一月",
			            "二月",
			            "三月",
			            "四月",
			            "五月",
			            "六月",
			            "七月",
			            "八月",
			            "九月",
			            "十月",
			            "十一月",
			            "十二月"
			        ],
			        "firstDay": 1
			    };
			
			var optionSet = {
    				singleDatePicker:singleDatePicker,
    				"timePicker": timePicker,
    				"showDropdowns": true,
    			    "timePicker24Hour": true,
    			    "timePickerSeconds": true,
    				"locale": _zh_cn,
    				"autoApply":true
			};
			
			//初始化时间控件
			ele.focus(
			    		function(){
				    		scope.dateController = ele.daterangepicker(optionSet, function(start, end, label) {
				    			
				    		}).on('apply.daterangepicker', function(ev){
				    			
				    		});
			    		}
			);
			
			scope.$watch("dateModel",function(val){
				if(val && val.indexOf(separator)>=0){
					scope.startModel = val.substring(0,val.indexOf(separator));
					scope.endModel = val.substr(val.indexOf(separator)+separator.length);
				}
			});
			scope.$watch("startModel",function(val){
				if(scope.endModel){
					scope.dateModel = scope.startModel + separator + scope.endModel;
				}
			});
			
			
		}
	}
	
}]);