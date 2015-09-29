'use strict';
app.controller('OrgList',['$scope','$state','utils','dataTableSearchService',function($scope, $state, utils,dataTableSearchService) {
							// 初始化表格 jQuery datatable
							
	$scope.$evalAsync(initTable);
							
	$scope.treeAPI.reloadListTable = function(){
		dTable.ajax.reload();
	}
	
	var orgList, dTable;
	function initTable() {
		orgList = $('#orgList');
		dTable = orgList.on('preXhr.dt', function ( e, settings, data ){
			data.FLongNumber = $scope.FLongNumber ;
		}).DataTable({
					"ordering" : false,
					// basedata/orgAction!directSubAdminOrgUnits.action
					"sAjaxSource" : "basedata/orgAction!directSubAdminOrgUnits.action",
					"bServerSide" : true,
					"sServerMethod" : "POST",
					"dom" : '<"top">rt<"bottom"ip><"clear">',
					"fnCreatedRow" : function(nRow,aData, iDataIndex) {
						$(nRow).attr('data-id',aData['id']);
					},
					"drawCallback" : function(settings) {
						dataTableSearchService.initClick(dTable,$scope.rowIds,$scope.setBtnStatus,$scope.seeDetails);
					},
					"aoColumns" : [
							{
								"orderable" : false,
								"render" : function(param) {
									return '<label class="i-checks"><input type="checkbox"><i></i></label>';
								}
							},
							{
								"mDataProp" : "code",
							},
							{
								"mDataProp" : "name"
							},
							{
								"mDataProp" : "simpleName"
							},
							{
								"mDataProp" : "parentName"
							},
							{
								"mDataProp" : "fax"
							},
							{
								"mDataProp" : "createTime"
							},
							{
								"mDataProp" : "creator"
							},
							{
								"mDataProp" : "lastModifyTime"
							} ]
				});
	}
//结束controller
} ]);