'use strict';

app.controller('OrgAddList', function() {
  var url = app.url.orgUnits;
  var data = null;

  app.utils.getData(url, function callback(dt){
    data = dt;
    initTable();
  });

  function initTable() {
    var dTable = $('#orgAddList').dataTable({
      "data": data,
      "sAjaxDataProp": "dataList",
      "fnCreatedRow": function(nRow, aData, iDataIndex){
        $(nRow).attr('data-id', aData['id']);
      },
      "oLanguage": {
        "sLengthMenu": "每页 _MENU_ 条",
        "sZeroRecords": "没有找到符合条件的数据",
        "sProcessing": "&lt;img src=’./loading.gif’ /&gt;",
        "sInfo": "第 _START_ - _END_ 条，共 _TOTAL_ 条",,
        "sInfoEmpty": "没有记录",
        "sInfoFiltered": "(从 _MAX_ 条记录中过滤)",
        "sSearch": "搜索",
        "oPaginate": {
          "sPrevious": "<",
          "sNext": ">"
        }
      },
      "aoColumns": [{
        "mDataProp": "code"
      }, {
        "mDataProp": "name"
      }]
    });

    dTable.$('tr').dblclick(function(e, settings) {
      //$scope.seeDetails($(this).data('id'));
      
    }).click(function(e) {
      var that = $(this), classname = 'rowSelected';
      if(that.hasClass(classname)){
        that.removeClass(classname);
      }else{
        that.addClass(classname);
      }
    });
  }
});