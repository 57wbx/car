app.controller("chooseWorkerController",['$scope','$modalInstance','$timeout','$http','dataTableSearchService','hintService','orderId',function($scope,$modalInstance,$timeout,$http,dataTableSearchService,hintService,orderId){

	$timeout(initTable,30);
	
	
	
	$scope.search = {};
	var workerTable,dTable;
	function initTable(){
		workerTable =  $("#workerTable");
		
		dTable = workerTable.on('preXhr.dt', function ( e, settings, data ){
			data.code = $scope.search.code ;
			data.cell = $scope.search.cell;
			data.name = $scope.search.name;
			data.gender = $scope.search.gender;
		}).DataTable({
			"sAjaxSource":"base/memberAction!listWorker.action",
	    	"bServerSide":true,
	    	"sAjaxDataProp":"data",
	    	 "dom": '<"top">rt<"bottom"p><"clear">',
	    	 "sServerMethod": "POST",
			"aoColumns": [{
		        "mDataProp": "code"
		      },{
		        "mDataProp": "cell"
		      },{
		        "mDataProp": "name"
		      },{
		        "mDataProp": "simpleName"
		      },{
		        "mDataProp": "gender",
		        "render":function(param){
		        	//0=加盟店、1=合作店、3=直营店、4=中心店（区域旗舰店）
		        	switch(param){
		        	case 0:
		        		return "男";break;
		        	case 1:
		        		return "女";break;
		        	case 2:
		        		return "保密";break;
		        	default:
		        		return "";break;
		        	}
		        }
		      },{
		    	  "render":function(param){
		    		  return "<button type='button' class='btn btn-info' >选择</button>";
		    	  }
		      }],
		      "oLanguage": {
		          "sLengthMenu": "每页 _MENU_ 条",
		          "sZeroRecords": "没有找到符合条件的数据",
		          "sProcessing": "&lt;img src=’./loading.gif’ /&gt;",
		          "sInfo": "当前第 _START_ - _END_ 条，共 _TOTAL_ 条",
		          "sInfoEmpty": "没有记录",
		          "sInfoFiltered": "(从 _MAX_ 条记录中过滤)",
		          "sSearch": "店面名称",
		          "oPaginate": {
		            "sFirst": "<<",
		            "sPrevious": "<",
		            "sNext": ">",
		            "sLast": ">>"
		          }
		        } ,"fnCreatedRow": function(nRow, aData, iDataIndex){
		        	$(nRow).find("button").click(function(e){
		        		distributeWorker(aData);
		            });
		        },"initComplete":function(settings,json){
		        	
		            	initSearchDiv(settings,json);
		            }
		       });
		
	}
	
	//初始化搜索框
	var initSearchDiv = function(settings,json){
		dataTableSearchService.initSearch([
			  {formDataName:'search.code',placeholder:'工号'},
			  {formDataName:'search.cell',placeholder:'手机号'},
			  {formDataName:'search.name',placeholder:'姓名'},
              {
           	   formDataName:'search.gender',
           	   label:'性别',
           	   options:[{label:'全部'},{
           		//0=加盟店、1=合作店、3=直营店、4=中心店（区域旗舰店）
			               		   value:0,
			               		   label:"男"
			               	   	},{
		                		   value:1,
		                   		   label:"女"
		                   	   	},{
		                    		   value:2,
		                       		   label:"保密"
		                       	 }
               	]
              }
		],$scope,settings,dTable);
	}
	
	
	var distributeWorker = function(aData){
		$(".modal-content button").attr("disabled","true");
		if(aData.code&&orderId){
			$http({
				url:"opr/orderAction!distributeWorker.action",
				method:"post",
				data:{
					id:orderId,
					workerId:aData.id
				}
			}).then(function(resp){
				if(resp.data.code){
					//成功
					hintService.hint({title: "成功", content: "分配成功！" });
					$modalInstance.close(aData);
				}else{
					$(".modal-content button").removeAttr("disabled");
					alert(resp.data.message);
				}
			});
		}
	}
	
	$scope.cancel = function() {
		    $modalInstance.dismiss('cancel');
	};
	
}]);