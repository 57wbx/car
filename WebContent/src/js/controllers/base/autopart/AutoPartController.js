'use strict';

app.controller('autoPartController', [
		'$rootScope',
		'$scope',
		'$state',
		'$timeout',
		'$http',
		'roleBtnService',
		'sessionStorageService',
		'warnService',
		'hintService',
		function($rootScope, $scope, $state, $timeout,$http, roleBtnService,
				sessionStorageService, warnService,hintService) {

			var roleBtnUiClass = "app.autopart.";// 用于后台查找按钮权限
			roleBtnService.getRoleBtnService(roleBtnUiClass, $scope);

			$scope.rowIds = [];

			/**
			 * 在session中不能清除的内容，应该包含子缓存对象
			 */
			$scope.session = {};
			$scope.session.cacheArray = [ "autoPartListDataTableProperties",
					"autoPartIdForEdit", "autoPartIdForDetails" ];
			sessionStorageService.clearNoCacheItem($scope.session.cacheArray);

			/**
			 * 清空需要操作的id，主要是在busAtomListController中调用
			 */
			$scope.clearRowIds = function() {
				$scope.rowIds = [];
				$scope.setBtnStatus();
			}

			$scope.search = {};

			$scope.state = {
				add : "app.autopart.add",
				edit : "app.autopart.edit",
				details : "app.autopart.details",
				list : "app.autopart.list"
			}

			// 添加组织（工具栏按钮）
			$scope.addAutoPart = function() {
				$state.go($scope.state.add);
			};

			// 编辑某一组织（工具栏按钮）
			$scope.editAutoPart = function() {
				$state.go($scope.state.edit);
			};

			// / 查看某一组织详情（工具栏按钮）
			$scope.seeDetails = function(id) {
				if (id) {
					$scope.rowIds.push(id);
				}
				$state.go($scope.state.details);
			};
			
			/**
			 * 删除的调用的远程服务
			 */
			function deleteAutoPart(ids){
				return $http({
					url:"base/autoPartAction!deleteAutoPartByIds.action",
					method:"POST",
					data:{
						ids:ids
					}
				});
			}
			
			//删除配件
			$scope.removeAutoPart = function(){
				if(!$scope.rowIds||$scope.rowIds.length<=0){
					alert("请选择需要删除的配件信息");
				}
				 warnService.warn("操作提示","您确定要删除这些配件信息吗？",function(){return deleteAutoPart($scope.rowIds)},function(resp){
					 if(resp.data.code == 1){//成功
						 hintService.hint({title: "成功", content: "删除成功！" });
						 $state.reload();
					 }else{
						 alert(resp.data.message);
					 }
				 })
			}
			
			// 设置按钮的状态值
			$scope.setBtnStatus = function() {
				console.info("设置按钮", $scope.rowIds);
				if ($scope.rowIds.length === 0) {
					$scope.single = true;
					$scope.locked = true;
					$scope.mutiple = true;
				} else if ($scope.rowIds.length === 1) {
					$scope.single = false;
					$scope.locked = false;
					$scope.mutiple = false;
				} else {
					$scope.single = true;
					$scope.locked = true;
					$scope.mutiple = false;
				}

				$scope.$evalAsync();
			};

			/**
			 * 用户输入框提示信息
			 */
			$scope.message = {
				partCode : {
					pattern : "编号只能为数字、字母和点的组成"
				},
				sunitPrice : {
					pattern : "出厂价格最多只能带两位小数"
				},
				yunitPrice : {
					pattern : "优惠价格最多只能带两位小数"
				},
				eunitPrice : {
					pattern : "市场价格最多只能带两位小数"
				},
				stock : {
					pattern : "库存数只能为整数"
				}
			}
			
//结束controller
		} ]);