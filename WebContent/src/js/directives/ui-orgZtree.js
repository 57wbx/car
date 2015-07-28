/**
 * <div bus_type_ztree inputName="autoPartAllName" showElement="showAddBusAtomForm"></div>
 * 使用方法：为该标签添加两个属性 inputName：为指定input的id，树将出现在这个input下面，并且，当用户点击了一个业务类型的时候，会将业务名称反写到该input中
 *  showElement：是触发显示该树的元素，触发该元素的点击事件
 *  needClick:是否触发点击事件，更新view中的数据
 */
app.directive("orgZtree",['$http','modelDataCacheService',function($http,modelDataCacheService){
	return {
		restrict:"A",
		replace:true,
		template: "<div id='menuContent' class='menuContent' style='display:none; position: absolute; z-index:20;background-color:white;border: 1px solid #617775;'>"+
					"<ul id='treeDemo' class='ztree' style='margin-top:0; width:160px;'></ul></div>",
		link: function(scope, iElement, iAttrs){
			$("#"+iAttrs.showelement).click(function(){
				showMenu();
			});
			/**
			 * 业务类型zTree的配置文件
			 */
			var setting = {
					view: {
						dblClickExpand: false,
						showIcon: false
					},
					data: {
						simpleData: {
							enable: true
						}
					},
					callback: {
						onClick: onClick
					}
				};
				
				/**
				 * 获取业务类型的数据
				 */
//				$http({
//					url:"base/busTypeAction!busTypeTree.action",
//					method:'get'
//				}).then(function(resp){
//					var zNodes = resp.data.busTypes;
//					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
//				});
				modelDataCacheService.orgNoDeptZtreeDataService().then(function(data){
					var zNodes = data;
					$.fn.zTree.init($("#treeDemo"), setting, zNodes);
				});
				
				function onClick(e, treeId, treeNode) {
					scope.formData.orgName = treeNode.name ;
					$("#"+iAttrs.inputname).val(treeNode.name);
					scope.formData.orgId = treeNode.id ;
					if(iAttrs.needclick){
						$("#clickId").trigger("click");
					}
				}

				function showMenu() {
					if($("#menuContent").attr("style").indexOf("none")>-1){
						var cityObj = $("#"+iAttrs.inputname);
						$("#menuContent").css({left:15 + "px", top:34 + "px"}).slideDown("fast");
						$("#menuContent").css("width",cityObj.innerWidth());
						$("body").bind("mousedown", onBodyDown);
					}else{
						hideMenu();
					}
				}
				function hideMenu() {
					$("#menuContent").fadeOut("fast");
					$("body").unbind("mousedown", onBodyDown);
				}
				function onBodyDown(event) {
					if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
						hideMenu();
					}
				}
			
			//
		}
	}
}]);