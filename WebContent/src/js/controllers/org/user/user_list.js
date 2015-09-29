app.controller('UserList', ['$scope','$state','utils','dataTableSearchService',function($scope, $state, utils,dataTableSearchService) {
  // 初始化表格 jQuery datatable
	$scope.API.isListPage = true ;
	
	$scope.$evalAsync(init);
	
	$scope.treeAPI.reloadListTable = function(){
		dTable.ajax.reload();
	}
  var orgList, dTable;
  function init(){
	if(dTable){
		dTable.fnDestroy();
    }
    orgList = $('#userList');
    dTable = orgList.on('preXhr.dt', function ( e, settings, data ){
    	 data.FLongNumber = $scope.selectData.FLongNumber ;
    	 data.carShopId = $scope.selectData.carShopId;
    }).DataTable({
    	"ordering":false,
        "bServerSide": true,
        "sAjaxSource":"basedata/userAction!listOrgUserByLoginUser.action",
        "sAjaxDataProp":"data",
        "sServerMethod" : "POST",
        "dom" : '<"top">rt<"bottom"ip><"clear">',
        "fnCreatedRow": function(nRow, aData, iDataIndex){
        	$(nRow).attr('data-id', aData['id']);
        },
        "drawCallback": function( settings ) {
        	dataTableSearchService.initClick(dTable,$scope.rowIds,$scope.setBtnStatus,$scope.seeDetails);
        },
        "aoColumns": [{
	        "orderable": false,
	        "render": function(param){
	          return '<label class="i-checks"><input type="checkbox"><i></i></label>';
	        }
        },{
          "mDataProp": "number"
        }, {
          "mDataProp": "name"
        }, {
          "mDataProp": "rootOrgUnit",
          "render":function(param){
        	  if(param){
        		  return param.name ;
        	  }else{
        		  return "";
        	  }
          }
        },{
          "mDataProp": "isEnable","render":function(o){
            return DataRender.State(o);
          }
        },{
        	"mDataProp": "isAdmin",
        	"render":function(param){
        		if(param){
        			return "是";
        		}else{
        			return "不是";
        		}
        	}
        }]
    });
  }

}]);