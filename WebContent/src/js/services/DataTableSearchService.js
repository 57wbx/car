/**
 * 为datatable插件添加查询参数的的服务，
 * 2015 9.1 新增表格点击事件
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
	/**
	 * 初始化datatable搜索的组件
	 */
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
	};
	/**
	 * 初始化datatable组件默认的点击事件。点击行将选择其id保存到容器中
	 * @param table 为使用Datatable函数返回的对象
	 * @param ids 是用来保存所选中的id的容器 一般选择$scope.rowIds ;
	 * @param showBtnFn 是显示按钮状态的函数，ids的数量变化时，按钮也要跟着变化。一般选择 $scope.setBtnStatus
	 * @param seeDetailsFn 查看数据相信信息的方法  一般选择：$scope.seeDetails
	 */
	var initClick = function(table,ids,showBtnFn,seeDetailsFn){
		// 表格行事件
			this.table = table ;
			this.ids = ids ;//初始化用来储存id的容器，默认为空
			ids.splice(0,ids.length);
			if(showBtnFn){
				showBtnFn();
			}
			var headInput = $(table.tables().header()).find("input"); ;//列表头部input
			var bodyInputs = $(table.tables().body()).find("input") ;//内容体input
			/**
			 * 控制头部的input的显示
			 */
			var checkedHeadInput = function(){
				var isAllChecked = true ;
				for(var i=0;i<bodyInputs.length;i++){
					if(!$(bodyInputs[i]).prop("checked")){
						isAllChecked = false ;
					}
				}
				if(isAllChecked){
					headInput.prop("checked",true);
				}else{
					headInput.prop("checked",false);
				}
			}
			/**
			 * 添加一个input的的id到ids对象中
			 */
			var addInputId = function(input){
				if(ids){
					ids.push(input.parents("tr").data("id"));
				}
				console.info(ids);
			}
			/**
			 * 删除一个input中指定的id
			 */
			var deleteInputId = function(input){
				if(ids){
					var id = input.parents("tr").data("id") ;
					var idx = ids.indexOf(id);
			        if(idx !== -1 ) ids.splice(idx, 1);
				}
				console.info(ids);
			}
			//当点击表格体的input的时候
			var clickBodyInput = function(input,id){
				if(input.prop("checked")){
					deleteInputId(input);
					input.prop("checked",false);
					headInput.prop("checked",false);
				}else{
					addInputId(input);
					input.prop("checked",true);
					checkedHeadInput();
				}
				if(showBtnFn){
					showBtnFn();
				}
			}
			//单点击表头的input的时候
			var clickHeadInput = function(){
				if(headInput.prop("checked")){//为true说明要将所有的子项选上
					bodyInputs.prop("checked",true);
					ids.splice(0,ids.length);
					for(var i=0;i<bodyInputs.length;i++){
						addInputId($(bodyInputs[i]));
					}
				}else{
					ids.splice(0,ids.length);
					bodyInputs.prop("checked",false);
				}
				if(showBtnFn){
					showBtnFn();
				}
			}
			var initClickEvent = function(){
					//初始化头部选择框的点击事件
					headInput.off().click(function(){
						clickHeadInput();
					});
					//初始化表体行的点击事件  没有初始化input的事件，只是初始化 表格行的事件，表格行可以代表input的事件
					table.$('tr').off().dblclick(function(e, settings) {
							if(seeDetailsFn){
								seeDetailsFn($(this).data('id'))
							}
				    }).click(function(e) {
				    	//阻止冒泡 阻止向下或者向上传递事件
				    	var evt = e || window.event;
					    evt.preventDefault();
					    evt.stopPropagation();
					    //触发点击事件
				    	clickBodyInput($(this).find("input"),$(this).data('id'));
				    });
			}
			//赋予点击事件
			initClickEvent();
	};
	//结束初始化表单点击事件
	return {
		initSearch:initSearch,
		initClick:initClick//新增点击事件
	};
}]);