app.controller("shopItemAddController",['$scope','$state','$http','checkUniqueService','FileUploader','hintService','sessionStorageService',function($scope,$state,$http,checkUniqueService,FileUploader,hintService,sessionStorageService){
	
//	$scope.formData.fitemID  新增开始的时候需要从服务器中下载下来，以便于子项的操作
	$scope.treeAPI.hiddenBusTypeTree();
	
	$scope.model.resultCallBack = function(aData){
		clickTr(aData);
	}
	
	$scope.options = {
			hello:"hello"
	}
	$scope.dateController = {};
	
	$scope.formData = {};
	defaultFormData($scope.formData);
	$scope.busAtomData = [];//用来保存该服务项中的所有子项，用来显示在列表中，并在保存的时候提交到服务器端
	$scope.formData.busAtomDataStr = "";//该对象是将busAtomData对象进行字符串化，以便于后台进行操作
	/**
	 * 初始化要填的数据
	 */
	
	
	//初始化选中的数据
	$scope.setCanEdit(false);
	$scope.clearRowIds();
	
	$scope.needCacheArray = ["shopItemDataTableProperties"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	
	/**
	 * 服务编码是否已经存在的标识符
	 */
	$scope.isUnique = true;
	$scope.notUniqueMessage = "已存在";
	$scope.checkIsUnique = function(fid,itemCode){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkShopItemCodeUnique(fid,$scope.formData.busTypeCode + itemCode).then(function(resp){
			if(resp.data.code==1){
				$scope.isUnique = true;
			}else{//存在
				$scope.isUnique = false;
			}
		});
	}
	$("#itemCode").change(function(e){
		if($scope.busItemForm.itemCode.$valid){
			$scope.checkIsUnique($scope.formData.fid,$(e.target).val());
		}
	});
	
	/**
	 * 检查服务子项编码是否已经存在
	 */
	$scope.atomCodeIsUnique = true;
	$scope.checkAtomCodeIsUnique = function(fid,atomCode){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkShopAtomCodeUnique(fid,atomCode).then(function(resp){
			if(resp.data.code==1){
				$scope.atomCodeIsUnique = true;
			}else{
				$scope.atomCodeIsUnique = false;
			}
		});
	}
	$("#atomCode").change(function(e){
		if($scope.busAtomForm.atomCode.$valid){
			$scope.checkAtomCodeIsUnique($scope.needUpdateRowData?$scope.needUpdateRowData.fid:null,$(e.target).val());
		}
	});
	
	/**
	 * 初始化文件上传控件
	 */
	 var uploaderForAtom = $scope.uploaderForAtom = new FileUploader({
	        url: 'common/fileUploadAction!uploadOnePictrue.action',
	        alias:"files",
	        autoUpload:true,
	        removeAfterUpload:true
	    });
	
	 /**
	  * 过滤器
	  */
	 uploaderForAtom.filters.push({
          name: 'imageFilter',
          fn: function(item /*{File|FileLikeObject}*/, options) {
        	  var isImage = false;
        	  isImage = item.type.indexOf("image")>=0;
        	  if(!isImage){
        		  alert("请选择图片文件!");
        	  }
              return isImage;
          }
      });
	  /**
	   * 上传开始之前
	   */
	 uploaderForAtom.onBeforeUploadItem = function(item) {
		  $scope.uploading = true ;
      };
	  /**
		  * 返回值
		  */
     uploaderForAtom.onSuccessItem = function(fileItem, response, status, headers) {
		 $scope.uploading = false ;
         if(response.code==1){
        	 hintService.hint({title: "成功", content: "上传成功！" });
        	 //上传成功的返回数据：{"name":"13293618_1200x1000_0.jpg","code":1,"url":"http://120.25.149.142:8048/group1/M00/00/0C/eBmVjlXCvHCERq5wAAAAAKmTBbk746.jpg"}
        	$scope.formData.atomPhotoUrl = response.url ;
         }else{
        	 alert(response.message)
         }
      };
	
	/**
	 * 初始化服务子项的datatables列表
	 */
	var busAtomTable = $("#busAtomTable").DataTable({
		searching: false,
	    ordering:  false,
	    info:false,
	    lengthChange:false,
	    paging:false,
	    scrollX:true,
	    "aoColumns":[{
            "mDataProp": "atomCode",
          },{
        	"mDataProp":"atomName"  
          },{
        	"mDataProp":"partName"  
          },{
        	"mDataProp":"brandName"  
          },{
        	"mDataProp":"spec"  
          },{
        	"mDataProp":"model"  
          },{
        	"mDataProp":"eunitPrice"  
          },{
        	"mDataProp":"autoParts"  
          },{
        	//0=不参加（默认），1=参加
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
        	"mDataProp":"memo"  
          },{"render": function(param){
                      return '<button type="button" class="btn btn-default btn-sm" name="updateButton">'+
                      '<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></button>'+
               		 '<button type="button" class="btn btn-danger btn-sm" name="deleteButton"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>';
                    }
          }],"language":{
        	  "emptyTable":"没有服务子项信息，请添加"
          },"fnCreatedRow": function(nRow, aData, iDataIndex){
        	 $(nRow).find("button[name=deleteButton]").click(function(){
        		 $scope.deleteRow(nRow);
        	 });
        	 $(nRow).find("button[name=updateButton]").click(function(){
        		 $scope.updateRowTable(nRow);
        	 });
        	 $(nRow).dblclick(function(){
        		 $scope.copyRow(nRow);
        	 });
          }
	});
	/**
	 * 删除一行
	 */
	$scope.deleteRow = function(nRow){
		var index = busAtomTable.row(nRow).index();
		var oldData = busAtomTable.row(nRow).data();
		$scope.deleteAutoPartsPrice(oldData.autoParts * oldData.eunitPrice);
		busAtomTable.row(nRow).remove().draw();
		$scope.busAtomData.splice(index,1);
	}
	/**
	 * 修改一行  用一个变量记录需要修改的记录，保存之后自动该把变量清空，
	 * 在保存操作中判断该变量是否不为空，如果不为空，就进行修改操作。如果为空进行新增操作
	 * 修改操作的的时候，需要将数据手动进行修改
	 */
	$scope.needUpdateRowData = null;
	$scope.updateRowTable = function(nRow){
		var index = busAtomTable.row(nRow).index();
		$scope.showAddBusAtomForm();
		$scope.needUpdateRowData = $scope.busAtomData[index];
		$scope.needUpdateRowData.index = index;//保存该数据的位置，用来找到在缓存中的数据，替换缓存中的数据
		$scope.copyRow(nRow);
	}
	/**
	 * 拷贝一行，双击一行，将表单中的空白部分进行填充
	 */
	$scope.copyRow = function(nRow){
		var index = busAtomTable.row(nRow).index();
		var data = $scope.busAtomData[index];
		if(!$scope.formData.autoPartId || $scope.formData.autoPartId==""){
			$scope.formData.autoPartId = data.autoPartId ;
		}
		if(!$scope.formData.partName || $scope.formData.partName==""){
			$scope.formData.partName = data.partName ;
		}
		if(!$scope.formData.brandName || $scope.formData.brandName==""){
			$scope.formData.brandName = data.brandName ;
		}
		if(!$scope.formData.spec || $scope.formData.spec==""){
			$scope.formData.spec = data.spec ;
		}
		if(!$scope.formData.model || $scope.formData.model==""){
			$scope.formData.model = data.model ;
		}
		if(!$scope.formData.autoIsActivity || $scope.formData.autoIsActivity==""){
			$scope.formData.autoIsActivity = data.isActivity ;
		}
		if(!$scope.formData.autoPartAllName || $scope.formData.autoPartAllName==""){
			$scope.formData.autoPartAllName = data.partName+"_"+data.brandName+"_"+data.spec+"_"+data.model;
		}
		if(!$scope.formData.atomCode || $scope.formData.atomCode==""){
			$scope.formData.atomCode = data.atomCode ;
		}
		if(!$scope.formData.atomName || $scope.formData.atomName==""){
			$scope.formData.atomName = data.atomName ;
		}
		if(!$scope.formData.autoParts || $scope.formData.autoParts==""){
			$scope.formData.autoParts = data.autoParts ;
		}
		if(!$scope.formData.eunitPrice || $scope.formData.eunitPrice==""){
			$scope.formData.eunitPrice = data.eunitPrice ;
		}
		if(!$scope.formData.busAtomMemo || $scope.formData.busAtomMemo==""){
			$scope.formData.busAtomMemo = data.memo ;
		}
		if(!$scope.formData.atomPhotoUrl || $scope.formData.atomPhotoUrl==""){
			$scope.formData.atomPhotoUrl = data.photoUrl ;
		}
		$scope.$evalAsync();
//		var hiddenButton = $("#clickId");
//		hiddenButton.trigger("click");
	}
	
	
	/**
	 * 重新画子项的datatables列表
	 */
	$scope.drawTable = function(){
		if($scope.needUpdateRowData){
			
			var oldData = $scope.needUpdateRowData;
			$scope.deleteAutoPartsPrice(oldData.autoParts * oldData.eunitPrice);//对配件合计做减法
			//修改记录的方法
			//1、现将form中的数据更新到该缓存对象中，保证fid不能被更新
			$scope.getUpdateBusAtom();
			//渲染到列表中
			
			
			busAtomTable.row($scope.needUpdateRowData.index).data($scope.needUpdateRowData);
			//对服务表单中的配件合计做运算
			$scope.addAutoPartsPrice($scope.needUpdateRowData.autoParts * $scope.needUpdateRowData.eunitPrice);//将价钱反写到服务中
			//需要重新绑定更新按钮和删除按钮事件
			var nRow = busAtomTable.row($scope.needUpdateRowData.index);
			 nRow.$("button[name=updateButton]").click(function(){
        		 $scope.updateRowTable(nRow.node());
        	 });
			 nRow.$("button[name=deleteButton]").click(function(){
        		 $scope.deleteRow(nRow.node());
        	 });
			 //需要清空保存的需要修改的记录$scope.needUpdateRowData
			$scope.needUpdateRowData = null;
		}else{
			//新怎记录的方法
			var newBusAtom = $scope.getNewBusAtom();
			busAtomTable.row.add(newBusAtom).draw();
			$scope.busAtomData.push(newBusAtom);
			$scope.addAutoPartsPrice(newBusAtom.autoParts * newBusAtom.eunitPrice);//将价钱反写到服务中
		}
		$scope.clearBusAtomForm();
		console.info($scope.busAtomData);
//		$scope.formData.busAtomDataStr = JSON.stringify($scope.busAtomData);
//		console.info($scope.formData.busAtomDataStr);
		$scope.hiddenAddBusAtomForm();
	}
	
	/**
	 * 获取子项详细信息和隐藏的信息，将该信息放到一个对象中，以便于在子项列表中显示
	 */
	$scope.getNewBusAtom = function(){
		var newBusAtom = {};
		newBusAtom.atomCode = $scope.formData.atomCode ;
		newBusAtom.atomName = $scope.formData.atomName ;
		newBusAtom.fitemID = $scope.formData.fid ;
		newBusAtom.autoParts = $scope.formData.autoParts || 0;
		newBusAtom.partID = $scope.formData.partID ;
		newBusAtom.eunitPrice = $scope.formData.eunitPrice || 0;
		newBusAtom.memo = $scope.formData.busAtomMemo || "";
		newBusAtom.partName = $scope.formData.partName ;
		newBusAtom.brandName = $scope.formData.brandName ;
		newBusAtom.spec = $scope.formData.spec ;
		newBusAtom.model = $scope.formData.model ;
		newBusAtom.autoPartId = $scope.formData.autoPartId ;
		newBusAtom.photoUrl = $scope.formData.atomPhotoUrl ;
		
		newBusAtom.isActivity = $scope.formData.autoIsActivity ;
		newBusAtom.yunitPrice = $scope.formData.yunitPrice ;
		newBusAtom.eunitPrice1 = $scope.formData.eunitPrice1 ;
		
		return newBusAtom;
	}
	
	/**
	 * 修改记录之后需要调用的防范，作用：将修改之后的数据保存到缓存数据中和渲染到列表中
	 */
	$scope.getUpdateBusAtom = function(){
		
		$scope.needUpdateRowData.atomCode = $scope.formData.atomCode ;
		$scope.needUpdateRowData.atomName = $scope.formData.atomName ;
		$scope.needUpdateRowData.autoParts = $scope.formData.autoParts || "";
		$scope.needUpdateRowData.partID = $scope.formData.partID ;
		$scope.needUpdateRowData.eunitPrice = $scope.formData.eunitPrice || "";
		$scope.needUpdateRowData.memo = $scope.formData.busAtomMemo || "";
		$scope.needUpdateRowData.partName = $scope.formData.partName ;
		$scope.needUpdateRowData.brandName = $scope.formData.brandName ;
		$scope.needUpdateRowData.spec = $scope.formData.spec ;
		$scope.needUpdateRowData.model = $scope.formData.model ;
		$scope.needUpdateRowData.autoPartId = $scope.formData.autoPartId ;
		$scope.needUpdateRowData.photoUrl = $scope.formData.atomPhotoUrl ;
		
		$scope.needUpdateRowData.isActivity = $scope.formData.autoIsActivity ;
		$scope.needUpdateRowData.yunitPrice = $scope.formData.yunitPrice ;
		$scope.needUpdateRowData.eunitPrice1 = $scope.formData.eunitPrice1 ;
	}
	
	
	/**
	 * 清空子项信息的数据的按钮
	 */
	$scope.resetBusAtomButton = function(){
		$scope.clearBusAtomForm();
	}
	
	/**
	 * 清空子项详细信息中的数据，当子项点击确定的时候进行该操作
	 */
	$scope.clearBusAtomForm = function(){
		$scope.formData.autoPartId = undefined ;
		$scope.formData.partName = undefined ;
		$scope.formData.brandName = undefined ;
		$scope.formData.spec = undefined ;
		$scope.formData.model = undefined ;
		$scope.formData.autoIsActivity = undefined ;
		$scope.formData.autoPartAllName = undefined ;
		$scope.formData.atomPhotoUrl = undefined ;
		
		$scope.formData.atomCode = undefined ;
		$scope.formData.atomName = undefined ;
		$scope.formData.autoParts = undefined ;
		$scope.formData.eunitPrice = undefined ;
		$scope.formData.busAtomMemo = undefined ;
		
	}
	
	/**
	 * 子项配件的相关方法
	 */
	$scope.choose = function(){
		$scope.showModel();
//		if(!$scope.formData.chooseAutoPartButton){
//			$scope.formData.chooseAutoPartButton = true;
//			if(!autoPartChooseTable){
//				initAutoPartChooseTable();
//			}
//			$("#autoPartChooseTablePanel").removeClass("none");
//			$scope.formData.chooseAutoPartName = "关闭选择框";
//		}else{
//			$scope.formData.chooseAutoPartButton = false ;
//			$("#autoPartChooseTablePanel").addClass("none");
//			$scope.formData.chooseAutoPartName = "选择";
//		}
	}
	
	/**
	 * 初始化配件选择列表
	 */
	var autoPartChooseTable ;
	function initAutoPartChooseTable(){
		autoPartChooseTable = $("#autoPartChooseTable").dataTable({
			"bServerSide":true,
			"sAjaxSource":"base/autoPartAction!listAutoPart.action",
			"sAjaxDataProp":"data",
			"aoColumns": [{
		    	"mDataProp":"partName"  
		      },{
		    	"mDataProp":"brandName"  
		      },{
		    	"mDataProp":"spec"  
		      },{
		    	"mDataProp":"model"  
		      },{
		    	"mDataProp":"eunitPrice"  
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
		      }],"oLanguage": {
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
	            }, "fnCreatedRow": function(nRow, aData, iDataIndex){
	        	  $(nRow).attr("data-id",aData['id']);
	        	  $(nRow).click(function(){
	        		  clickTr(aData);
	        	  });
	          }
		});
	}
	
	
	/**
	 * 点击一行触发的事件
	 */
	function clickTr(aData){
		var id = aData['id'];
		$scope.formData.autoPartId = id;
		$scope.formData.partName = aData['partName'];
		$scope.formData.brandName = aData['brandName'];
		$scope.formData.spec = aData['spec'];
		$scope.formData.model = aData['model'];
		$scope.formData.autoIsActivity = aData['isActivity'];
		$scope.formData.eunitPrice = aData['yunitPrice'];
		
		$scope.formData.autoPartAllName = aData['partName']+"+"+aData['brandName']+"+"+aData['spec']+"+"+aData['model'] ;
		$("#autoPartAllName").val(aData['partName']+"+"+aData['brandName']+"+"+aData['spec']+"+"+aData['model']);
	}
	
	
	
	
	/**
	 * 点击保存操作
	 */
	$scope.submit = function(){
		$scope.formData.busAtomDataStr = JSON.stringify($scope.busAtomData);//对json数组进行序列化
		$scope.formData.itemCode = ($scope.formData.busTypeCode || "") + $scope.formData.itemCode;
		
		$scope.formData.standardPrice = $scope.formData.workHours + $scope.formData.autoPartsPrice ;
		if($scope.formData.isActivity == 0){
			$scope.formData.actualPrice = $scope.formData.workHours + $scope.formData.autoPartsPrice ;
			$scope.formData.starTimeStr = undefined;
			$scope.formData.endTimeStr = undefined;
		}
		$http({
			url:"shop/shopItemAction!addShopItem.action",
			method:'post',
			data : $scope.formData
		}).then(function(resp){
			hintService.hint({title: "成功", content: "保存成功！" });
			var code = resp.data.code ;
			if(code == 1){//代表保存成功
				$state.go($scope.state.list);
			}else{//代表保存失败
				alert(resp.data.message);
			}
			$scope.isDoing = false ;
		},function(resp){
			alert("保存出错");
			$scope.isDoing = false ;
		});
	}
	
	/**
	 * 取消按钮的操作，直接跳转到列表页面上
	 */
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
	
	/**
	 * 对配件合计进行运算的方法
	 */
	//增加配件合计
	$scope.addAutoPartsPrice = function(autoPartsPrice){
		$scope.formData.autoPartsPrice = ($scope.formData.autoPartsPrice || 0) + autoPartsPrice;
	}
	//减少配件合计价
	$scope.deleteAutoPartsPrice = function(autoPartsPrice){
		$scope.formData.autoPartsPrice = ($scope.formData.autoPartsPrice || 0) - autoPartsPrice;
		$scope.$evalAsync();
//		$("#clickId").trigger("click");
	}
	
	/**
	 * 页面显示控制功能，当显示服务信息的时候，不显示服务子项新增的页面
	 * 当显示服务子项页面的时候 不显示服务信息页面
	 */
	$scope.showAddBusAtomForm = function(){
		$scope.needUpdateRowData = null ;//清空需要更新的对象
		$scope.resetBusAtomButton();
		$("form[name=busAtomForm]").removeClass(" none ");
		$("form[name=busItemForm]").addClass(" none ");
		$("#busItemSumbit").addClass("none");
	}
	$scope.hiddenAddBusAtomForm = function(){
		$("form[name=busAtomForm]").addClass(" none ");
		$("form[name=busItemForm]").removeClass(" none ");
		$("#busItemSumbit").removeClass("none");
	}
	
	/**
	 * 初始化要填的默认数据
	 */
	function defaultFormData(formData){
		if(formData){
			formData.useState = 0 ;//初始化
			formData.isActivity = 0 ; //不参加聚会
			formData.standardPrice = 0; //标准价
			formData.actualPrice = 0 ;//实际价
			formData.workHours = 0 ; //工时费
			formData.autoPartsPrice = 0 ;//配件合计价
		}
	}
	
	/**
	 * 监控是否参加活动的变化
	 */
	$scope.$watch("formData.isActivity",function(val){
		if(val == 0){
			$scope.formData.actualPrice = 0 ;
		}else if(val == 1){
			$scope.formData.actualPrice = $scope.formData.workHours + $scope.formData.autoPartsPrice ;
		}
	})
	
}]);