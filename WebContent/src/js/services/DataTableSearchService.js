/**
 * 为datatable插件添加查询参数的的服务，
 * 参数：serachValue,scope,settings,dataTable
 * serachValue 为需要查询的参数是一个数组对象，其中input查询的对象为{formDataName：你自己定义的名称，在查询的时候用到。,placeholder:'显示内容'}
 * 									select 也是一个对象 {
													                	   formDataName:'hellowo',   ---查询的参数名 使用方法：$scope.hellowo
														                	   options:[{                         ---select中的选项内容
														                		   value:1,
														                		   label:"hello"    
														                	   	}]
														                   }
	scope：为需要绑定的$scope
	settings：为datatables中的参数
	dataTable： 为dTable = carShopList.DataTable()返回的的一个对象		
*
*@authot zw	
 */
app.factory("dataTableSearchService",['$compile',function($compile){
//	var serachValue = [
//	                   {formDataName:'hello',placeholder:'显示内容'}
//	                   ,
//	                   {
//	                	   formDataName:'hellowo',
//	                	   options:[{
//	                		   value:1,
//	                		   label:"hello"
//	                	   	}]
//	                   },{
//	                	   formDataName:'towo',
//	                	   placeholder:'显示内容听我'
//	                   }
//	                   ];
	var initSearch = function(serachValue,scope,settings,dataTable){
		if(!scope){console.error("scope不能为空！");return;};
		if(!serachValue&&serachValue.length>0){console.error("查询数组对象不能为空");return;};
		var formStr = "<form novalidate style='margin-right:10px;' class='navbar-form navbar-right form-inline text-right' role='search'>";
		for(var i=0;i < serachValue.length;i++){
			if(!serachValue[i].formDataName){console.error("配置对象没有指定的formDataName属性");return;};
			var element ;
			if(serachValue[i].options&&serachValue[i].options.length>0){//代表是select的选择框
				element = "<label class='control-label'>"+serachValue[i].label+":</label><select ng-model='"+serachValue[i].formDataName+"' class='form-control' >";
				for(var j=0;j<serachValue[i].options.length;j++){
					var value = -1;
					if(serachValue[i].options[j].value===0||serachValue[i].options[j].value){
						value = serachValue[i].options[j].value ;
					}
					element = element + "<option value='"+value+"'>"+serachValue[i].options[j].label+"</option>" ;
				}
				element = element + "</select>";
			}else{//普通的input
				element = "<input type='text' class='form-control' placeholder='"+serachValue[i].placeholder+"' ng-model='"+serachValue[i].formDataName+"' />" ;
			}
			formStr = formStr + element ;
		}
		formStr = formStr +"<button class='btn btn-default'  ng-click='searchMethod()'  type='button'>搜索</button></from>" ;
		
		scope.searchMethod = function(){
			if(dataTable){
				dataTable.ajax.reload();
			}else{
				console.error("需要刷新的datatable不能为空！");
			}
		};
		var form = angular.element(formStr);
		$compile(form)(scope);
		angular.element(settings.nTableWrapper.firstChild).html(form);
		
//		console.info(formStr);
	}
	return {
		initSearch:initSearch
	};
}]);