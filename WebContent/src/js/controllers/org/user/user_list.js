'use strict';

app.controller('UserList', function($rootScope, $scope, $state, utils) {
  $rootScope.ids = [];
  function clicked(target, that){
    var classname = 'rowSelected', id;
    target.click(function(e){
      var evt = e || window.event;
      evt.stopPropagation();
      if(!that){
        that = $(this).parents('tr');
      }
      id = that.data('id');
      if(!$(this)[0].checked){
        var idx = $rootScope.ids.indexOf(id);
        if(idx !== -1 ) $rootScope.ids.splice(idx, 1);
      }else{
        $rootScope.ids.push(id);
      }
      $scope.setBtnStatus();
    });
  }
  
  function bindClick(){
	orgList.$('tr').dblclick(function(e, settings) {
	  $scope.seeDetails($(this).data('id'));
    }).click(function(e) {
	  var evt = e || window.event;
	  var target = event.target || event.srcElement;
	  evt.preventDefault();
	  var ipt = $(this).find('.i-checks input');
	  clicked(ipt.off(), $(this));
	  ipt.trigger('click');
	  var input = orgList.find('thead .i-checks input');
	  var inputs = orgList.find('tbody .i-checks input');
	  var len = inputs.length, allChecked = true;
	  for(var i=0; i<len; i++){
	    if(!inputs.eq(i)[0].checked){
	      allChecked = false;
	      break;
	    }
	  }
	  if(allChecked){
	    input[0].checked = true;
	  }else{
	    input[0].checked = false;
	  }
	});
  }
  $rootScope.reload = function(){
	  
  };
  // 初始化表格 jQuery datatable
  var orgList, dTable;
  $rootScope.init = function() {
	if(dTable){
		dTable.fnDestroy();
    }
    orgList = $('#userList');
    dTable = orgList.on('preXhr.dt', function ( e, settings, data ){
    	 data.FLongNumber = $scope.FLongNumber ;
    }).dataTable({
    	"ordering":false,
        "bServerSide": true,
        "sAjaxSource":app.url.user.api.list,
//         "ajax": {
//          "url": app.url.user.api.list,
//          "async": false,
//          "type": 'POST',
//          "data": {
//            "FLongNumber": $scope.FLongNumber
//          }
//        },
      "sAjaxDataProp": "rows",
      "oLanguage": {
        "sLengthMenu": "每页 _MENU_ 条",
        "sZeroRecords": "没有找到符合条件的数据",
        "sProcessing": "&lt;img src=’./loading.gif’ /&gt;",
        "sInfo": "当前第 _START_ - _END_ 条，共 _TOTAL_ 条",
        "sInfoEmpty": "没有记录",
        "sInfoFiltered": "(从 _MAX_ 条记录中过滤)",
        "sSearch": "搜索",
        "oPaginate": {
          "sFirst": "<<",
          "sPrevious": "<",
          "sNext": ">",
          "sLast": ">>"
        }
      },
      "fnCreatedRow": function(nRow, aData, iDataIndex){
        $(nRow).attr('data-id', aData['id']);
      },
      "drawCallback": function( settings ) {
    	  bindClick();
    	  $rootScope.ids = [];
    	  $scope.setBtnStatus();
        var input = orgList.find('thead .i-checks input');
        var inputs = orgList.find('tbody .i-checks input');
        var len = inputs.length, allChecked = true;
        for(var i=0; i<len; i++){
          if(!inputs.eq(i)[0].checked){
            allChecked = false;
            break;
          }
        }
        if(allChecked){
          input[0].checked = true;
        }else{
          input[0].checked = false;
        }
        
        input.off().click(function(){
          for(var i=0; i<len; i++){
            if(!inputs.eq(i)[0].checked || !$(this)[0].checked){  
              clicked(inputs.eq(i).off());
              inputs.eq(i).trigger('click');
            }
          }
        });
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
          "mDataProp": "orgName"
        },{
          "mDataProp": "roleName"
        },{
          "mDataProp": "isEnable","render":function(o){
            return DataRender.State(o);
          }
        },{
          "mDataProp": "personName"
        },{
          "mDataProp": "creator"
        }, {
          "mDataProp": "createTime",
          "render": function(o) {
            return DataRender.DateTime(o);
          }
        },{
          "mDataProp": "lastUpdateUser"
        }, {
          "mDataProp": "lastUpdateTime",
          "render": function(o) {
            return DataRender.DateTime(o);
          }
      }]
    });
  }

});