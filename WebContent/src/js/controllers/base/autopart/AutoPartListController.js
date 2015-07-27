'use strict';
app.controller('autoPartListController', ['$rootScope','$scope','$state','$timeout','utils'
,function($rootScope, $scope, $state,$timeout, utils) {
	
	$timeout(function(){
		//等待页面加载
		initTable();
	},10);
	
	
	var data = null;
//  $scope.init = function(){
	  
	  /**
	   * 删除这段代码angularjs会报错 虽然在这里的远程获取数据并没有什么用
	   * base/autoPartAction!listAutoPart.action?iDisplayStart=0&iDisplayLength=10
	   * 
	   * 2015.7.14 用timeout延迟加载解决该报错的问题
	   */
////    utils.getData("base/busTypeAction!listBusType.action", function callback(dt){
//    	
//      if(dTable){
//        dTable.fnDestroy();
//        initTable();
//      }else{
//        initTable();
//      }
////    });
//  };
//  $scope.init();
 
	$scope.treeAPI.showBusTypeTree();
	/**
	 * 初始化该页面的api
	 * 提供给buspapckageController调用
	 * 注销其他方法
	 */
	$scope.treeAPI.clickTreeAddOrUpdateReload = null;//注销其他的方法
	$scope.treeAPI.clickTreeListReload = function(busTypeCode){
		//绑定一次后后面所有的请求都具有该参数了
		dTable.ajax.reload();//重新加载数据
	}
	

 

  //var ids = [], obj;
  $rootScope.ids = [], $rootScope.obj;

  function clicked(target, that){
    var classname = 'rowSelected', id;

    target.click(function(e){
      var evt = e || window.event;
      //evt.preventDefault();
      evt.stopPropagation();
      if(!that){
        that = $(this).parents('tr');
      }

//      $rootScope.details = $rootScope.obj = app.utils.getDataByKey(data, 'id', that.data('id'));
//      id = $rootScope.obj['id'];
      id = that.data('id');
      $rootScope.details = {
    		  id:id
      };
      if(!$(this)[0].checked){
        var idx = $rootScope.ids.indexOf(id);
        if(idx !== -1 ) $rootScope.ids.splice(idx, 1);
        //that.removeClass(classname);
      }else{
        $rootScope.ids.push(id);
        //that.addClass(classname);
      }
      $scope.setBtnStatus();
    });
  }

  // 初始化表格 jQuery datatable
  var autoPartList, dTable;
  function initTable() {
	autoPartList = $('#autoPartList');
    dTable = autoPartList.on('preXhr.dt', function ( e, settings, data ){
		data.busTypeCode = $scope.busTypeTree.selectedTypeCode ;
	}).DataTable({
    	"sAjaxSource":"base/autoPartAction!listAutoPart.action",
    	"bServerSide":true,
    	"sAjaxDataProp":"data",
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
    	  //初始化每一行的点击行为
        $(nRow).attr('data-id', aData['id']);
        $(nRow).dblclick(function(e, settings) {
            $scope.seeDetails($(this).data('id'));
        });
        $(nRow).click(function(e) {
            var evt = e || window.event;
            var target = event.target || event.srcElement;

            evt.preventDefault();
            //evt.stopPropagation();
            var ipt = $(this).find('.i-checks input');
            clicked(ipt.off(), $(this));
            ipt.trigger('click');
            var input = autoPartList.find('thead .i-checks input');
            var inputs = autoPartList.find('tbody .i-checks input');
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
        })
      },
      "drawCallback": function( settings ) {
        var input = autoPartList.find('thead .i-checks input');
        var inputs = autoPartList.find('tbody .i-checks input');
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
      }, {
        "mDataProp": "partCode",
      },{
    	"mDataProp":"partName"  
      },{
    	"mDataProp":"brandName"  
      },{
    	"mDataProp":"spec"  
      },{
    	"mDataProp":"model"  
      },{
    	"mDataProp":"sunitPrice"  
      },{
    	"mDataProp":"eunitPrice"  
      },{
    	"mDataProp":"yunitPrice"  
      },{
    	"mDataProp":"useState",
    	"render":function(param){
        	//0=初始化、1=待审核、2=发布（审核通过）、3=停止/下架、4=强制下架（违规）
        	switch(param){
        	case 0:
        		return "初始化";break;
        	case 1:
        		return "待审核";break;
        	case 2:
        		return "发布（审核通过）";break;
        	case 3:
        		return "停止/下架";break;
        	case 4:
        		return "强制下架（违规）";break;
        	default:
        		return "";break;
        	}}
      },{
    	"mDataProp":"isActivity",
    	"render":function(param){
        	//0=不参加（默认），1=参加
        	switch(param){
        	case 0:
        		return "不参加";break;
        	case 1:
        		return "参加";break;
        	default:
        		return "";break;
        	}}
      },{
    	"mDataProp":"stock"  
      },{
    	"mDataProp":"updateTime"  
      },{
    	"mDataProp":"partSource"  
      },{
    	"mDataProp":"memo"  
      }]
    });
    
  }

  
  
  
}]);