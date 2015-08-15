app.controller("shopItemEditController",['$scope','$state','$http','checkUniqueService','FileUploader','hintService','sessionStorageService',
                                         function($scope,$state,$http,checkUniqueService,FileUploader,hintService,sessionStorageService){
	
//	$scope.formData.fitemID  新增开始的时候需要从服务器中下载下来，以便于子项的操作
	$scope.treeAPI.hiddenBusTypeTree();
	
	$scope.needCacheArray = ["shopItemDataTableProperties","shopItemIdForEdit"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds[0]){
		sessionStorageService.setItem("shopItemIdForEdit",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("shopItemIdForEdit");
	}
	
	console.info("------------需要修改的id为："+$scope.rowIds[0]);
	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	
	getServerData();
	/**
	 * 从服务器端获取业务数据
	 */
	function getServerData(){
		$http({
			url:"shop/shopItemAction!detailsShopItem.action",
			method:"get",
			params:{
				fid:$scope.rowIds[0]
			}
		}).then(function(resp){
			var code = resp.data.code ;
			if(code == 1){
				renderData(resp.data);//渲染数据
			}else{
				$state.go($scope.state.list);
			}
		});
	}
	
	
	/**
	 * 服务编码是否已经存在的标识符
	 */
	$scope.isUnique = true;
	$scope.notUniqueMessage = "已存在";
	$scope.checkIsUnique = function(fid,itemCode){//检查服务编码是否已经存在需要服务id和更新的服务编号
		checkUniqueService.checkShopItemCodeUnique(fid,itemCode).then(function(resp){
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
	 * 将获取的数据显示到页面上面
	 */
	function renderData(data){
		/*
		 * 渲染服务详细信息
		 */
		$scope.formData = data.details ;
		$scope.formData.photoUrlName = data.details.photoUrl?"请点击&nbsp;<a style='color: blue; text-decoration: underline;'  onClick='$(\"#photoUrl\").click();'>预览！</a>":undefined;
		$scope.formData.chooseAutoPartName='选择';
		$scope.formData.chooseAutoPartButton=false;
		//时间控件需要进行dom操作渲染
		$("#starTime").val(data.details.starTime);
		$("#endTime").val(data.details.endTime);
		$scope.formData.starTimeStr = data.details.starTime;
		$scope.formData.endTimeStr = data.details.endTime;
		/*
		 * 渲染子项列表信息
		 */
		var busAtomsArray = data.busAtoms;
		if(busAtomsArray){
			//填充数据
			for(var i=0;i<busAtomsArray.length;i++){
				var newBusAtom = {};
				newBusAtom.fid = busAtomsArray[i].fid;
				newBusAtom.atomCode = busAtomsArray[i].atomCode || "";
				newBusAtom.atomName = busAtomsArray[i].atomName || "";
				newBusAtom.autoParts = busAtomsArray[i].autoParts || "";
				newBusAtom.photoUrl = busAtomsArray[i].photoUrl || "";
				newBusAtom.partID = busAtomsArray[i].autoPart.id ;
				newBusAtom.eunitPrice = busAtomsArray[i].eunitPrice || "";
				newBusAtom.memo = busAtomsArray[i].memo || "";
				newBusAtom.partName = busAtomsArray[i].autoPart.partName || "";
				newBusAtom.brandName = busAtomsArray[i].autoPart.brandName || "";
				newBusAtom.spec = busAtomsArray[i].autoPart.spec || "";
				newBusAtom.model = busAtomsArray[i].autoPart.model || "";
				newBusAtom.autoPartId = busAtomsArray[i].autoPart.id ;
				
				newBusAtom.isActivity = busAtomsArray[i].autoPart.isActivity ;
				newBusAtom.yunitPrice = busAtomsArray[i].autoPart.yunitPrice ;
				newBusAtom.eunitPrice1 = busAtomsArray[i].autoPart.eunitPrice ;
				
				busAtomTable.row.add(newBusAtom).draw();
				$scope.busAtomData.push(newBusAtom);
				
			}
			
		}
	}
	
	
	
	$scope.formData = {};
	$scope.busAtomData = [];//用来保存该服务项中的所有子项，用来显示在列表中，并在保存的时候提交到服务器端
	$scope.formData.busAtomDataStr = "";//该对象是将busAtomData对象进行字符串化，以便于后台进行操作
	$scope.deleteBusAtomIds = [];//这个属性只有在修改页面才有，用来保存删除子项的id，上传到服务端进行删除操作
	
	
	//初始化选中的数据
	$scope.setCanEdit(false);
	$scope.clearRowIds();
	
	//初始化时间控件
	$('#starTime').focus(
	    		function(){
		    		var optionSet = {
							singleDatePicker : true,
							timePicker : true,
							format : 'YYYY-MM-DD HH:mm'
						};
		    		$('#starTime').daterangepicker(optionSet).on('apply.daterangepicker', function(ev){
		    			$scope.formData.starTimeStr=$('#starTime').val();
		    		});
	    		}
	);
	$('#endTime').focus(
    		function(){
	    		var optionSet = {
						singleDatePicker : true,
						timePicker : true,
						format : 'YYYY-MM-DD HH:mm'
					};
	    		$('#endTime').daterangepicker(optionSet).on('apply.daterangepicker', function(ev){
	    			$scope.formData.endTimeStr=$('#endTime').val();
	    		});
    		}
	);
	
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
	 * 删除一行，如果该一行是从后台获取的数据，那么有fid，删除该行的同时将fid保存到$scope.deleteBusAtomIds数组当中，方便后台进行修改
	 */
	$scope.deleteRow = function(nRow){
		var index = busAtomTable.row(nRow).index();//每次获取的这个位置都是最新的，datatables会进行实时更新，利用这个顺序将缓存中的数据删减
		var oldData = busAtomTable.row(nRow).data();
		$scope.deleteAutoPartsPrice(oldData.autoParts * oldData.eunitPrice);
		busAtomTable.row(nRow).remove().draw();
		if($scope.busAtomData[index].fid&&$scope.busAtomData[index].fid!=""){//当该数据有fid的时候，进行缓存id
			$scope.deleteBusAtomIds.push($scope.busAtomData[index].fid);
		}
		$scope.busAtomData.splice(index,1);
		console.info("删除的的fid有：");
		console.info($scope.deleteBusAtomIds);
		console.info("busatomData 中保存的数据：");
		console.info($scope.busAtomData);
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
		console.info("新增或者修改之后的数据：");
		console.info($scope.busAtomData);
		$scope.hiddenAddBusAtomForm();
	}
	
	/**
	 * 获取子项详细信息和隐藏的信息，将该信息放到一个对象中，以便于在子项列表中显示，这是新增busatom时候调用的方法 下面的另一个方法是修改一条记录，
	 * 在修改完成之后进行的操作
	 */
	$scope.getNewBusAtom = function(){
		var newBusAtom = {};
		newBusAtom.fid = "";
		newBusAtom.atomCode = $scope.formData.atomCode ;
		newBusAtom.atomName = $scope.formData.atomName ;
		newBusAtom.fitemID = $scope.formData.fid ;
		newBusAtom.autoParts = $scope.formData.autoParts || "";
		newBusAtom.partID = $scope.formData.partID ;
		newBusAtom.eunitPrice = $scope.formData.eunitPrice || "";
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
		if(!$scope.formData.chooseAutoPartButton){
			$scope.formData.chooseAutoPartButton = true;
			if(!autoPartChooseTable){
				initAutoPartChooseTable();
			}
			$("#autoPartChooseTablePanel").removeClass("none");
			$scope.formData.chooseAutoPartName = "关闭选择框";
		}else{
			$scope.formData.chooseAutoPartButton = false ;
			$("#autoPartChooseTablePanel").addClass("none");
			$scope.formData.chooseAutoPartName = "选择";
		}
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
		
		$scope.formData.autoPartAllName = aData['partName']+"+"+aData['brandName']+"+"+aData['spec']+"+"+aData['model'] ;
		$("#autoPartAllName").val(aData['partName']+"+"+aData['brandName']+"+"+aData['spec']+"+"+aData['model']);
	}
	
	
	
	
	/**
	 * 点击保存操作
	 */
	$scope.submit = function(){
		$scope.formData.busAtomDataStr = JSON.stringify($scope.busAtomData);
		$scope.formData.deleteBusAtomIds = $scope.deleteBusAtomIds;
		$scope.formData.busPackages = undefined ;
		$http({
			url:"shop/shopItemAction!saveShopItem.action",
			method:'post',
			data : $scope.formData
		}).then(function(resp){
			var code = resp.data.code ;
			if(code == 1){//代表保存成功
				$state.go($scope.state.list);
			}else{//代表保存失败
				alert("保存失败");
			}
		},function(resp){
			alert("保存出错");
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
//		$("#clickId").trigger("click");
		$scope.$evalAsync();
	}
	
	
	/**
	 * 页面显示控制功能，当显示服务信息的时候，不显示服务子项新增的页面
	 * 当显示服务子项页面的时候 不显示服务信息页面
	 */
	$scope.showAddBusAtomForm = function(){
		$scope.needUpdateRowData = null ;//清空需要更新的对象
		$scope.clearBusAtomForm();
		$("form[name=busAtomForm]").removeClass(" none ");
		$("form[name=busItemForm]").addClass(" none ");
		$("#busItemSumbit").addClass("none");
	}
	$scope.hiddenAddBusAtomForm = function(){
		$("form[name=busAtomForm]").addClass(" none ");
		$("form[name=busItemForm]").removeClass(" none ");
		$("#busItemSumbit").removeClass("none");
	}
	
	
}]);