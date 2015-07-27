app.controller("busItemDetailsController",['$scope','$state','$http',function($scope,$state,$http){
	
//	$scope.formData.fitemID  新增开始的时候需要从服务器中下载下来，以便于子项的操作
	$scope.busItemAPI.hiddenBusTypeTree();
	
	
	console.info("------------需要修改的id为："+$scope.rowIds[0]);
	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
		$state.go("app.busitem.list");//返回到列表界面
	}
	$("form[name=busItemDetailsform] input").attr("disabled",true);
	$("form[name=busItemDetailsform] select").attr("disabled",true);
	$("form[name=busItemDetailsform] textarea").attr("disabled",true);
	getServerData();
	/**
	 * 从服务器端获取业务数据
	 */
	function getServerData(){
		$http({
			url:"base/busItemAction!detailsBusItem.action",
			method:"get",
			params:{
				fid:$scope.rowIds[0]
			}
		}).then(function(resp){
			var code = resp.data.code ;
			if(code == 1){
				renderData(resp.data);//渲染数据
			}else{
				$state.go("app.busitem.list");
			}
		});
	}
	/**
	 * 将获取的数据显示到页面上面
	 */
	function renderData(data){
		/*
		 * 渲染服务详细信息
		 */
		$scope.formData = data.details ;
		//时间控件需要进行dom操作渲染
		$("#starTime").val(data.details.starTime);
		$("#endTime").val(data.details.endTime);
		$scope.formData.starTimeStr = data.details.starTime;
		$scope.formData.endTimeStr = data.details.starTime;
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
	$scope.clearRowIds();
	
	
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
	    "language":{
      	  "emptyTable":"没有服务子项信息"
        },
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
          }]
	});
	
	
	
	/**
	 * 重新画子项的datatables列表
	 */
	$scope.drawTable = function(){
		var newBusAtom = $scope.getNewBusAtom();
		busAtomTable.row.add(newBusAtom).draw();
		$scope.busAtomData.push(newBusAtom);
		$scope.clearBusAtomForm();
		console.info($scope.busAtomData);
	}
	
	/**
	 * 获取子项详细信息和隐藏的信息，将该信息放到一个对象中，以便于在子项列表中显示
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
		
		newBusAtom.isActivity = $scope.formData.autoIsActivity ;
		newBusAtom.yunitPrice = $scope.formData.yunitPrice ;
		newBusAtom.eunitPrice1 = $scope.formData.eunitPrice1 ;
		
		return newBusAtom;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 取消按钮的操作，直接跳转到列表页面上
	 */
	$scope.cancel = function(){
		$state.go("app.busitem.list");
	}
	
	
}]);