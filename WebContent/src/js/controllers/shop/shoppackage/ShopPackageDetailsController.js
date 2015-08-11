app.controller("shopPackageDetailsController",['$scope','$state','$http','sessionStorageService',function($scope,$state,$http,sessionStorageService){
	
	
	$scope.needCacheArray = ["shopPackageDataTableProperties","shopPackageIdForDetails"];
	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
	if($scope.rowIds[0]){
		sessionStorageService.setItem("shopPackageIdForDetails",$scope.rowIds[0]);
	}else{
		$scope.rowIds[0]  = sessionStorageService.getItemStr("shopPackageIdForDetails");
	}
	
	console.info("------------需要修改的id为："+$scope.rowIds[0]);
	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
		$state.go($scope.state.list);//返回到列表界面
	}
	/*
	 * 初始化 隐藏树 
	 */
	
	$scope.treeAPI.hiddenBusTypeTree();
	
	$scope.formData = {};
	
	/**
	 * 用来缓存该套餐中的服务的所有id
	 */
	$scope.itemIds = [];
	
	/**
	 * 初始化树点击的方法,注销其他的点击树的方法
	 */
	$scope.treeAPI.clickTreeListReload = null;
	$scope.treeAPI.clickTreeAddOrUpdateReload = function(){
		if(busItemTableForChoose){//给服务选择列表进行重新加载数据操作
			busItemTableForChoose.ajax.reload();
		}
	}
	

	
	
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
	 * 初始化服务的datatables列表
	 */
	var busItemTable = $("#busItemTable").DataTable({
		searching: false,
	    ordering:  false,
	    info:false,
	    lengthChange:false,
	    paging:false,
	    scrollX:true,
	    "aoColumns":[{
            "mDataProp": "itemCode",
          },{
        	"mDataProp":"itemName"  
          },{
        	"mDataProp":"standardPrice"  
          },{
        	"mDataProp":"actualPrice"  
          },{
        	"mDataProp":"workHours"  
          },{
        	"mDataProp":"autoPartsPrice"  
          },{
        	"mDataProp":"useState",
        	"render":function(param){
        		//0=初始化、1=待审核、2=发布（审核通过）、3=停止/下架、4=强制下架（违规）
        		switch(param){
        			case 0: return "初始化";break;
        			case 1: return "待审核";break;
        			case 2: return "发布（审核通过）";break;
        			case 3: return "停止/下架";break;
        			case 4: return "强制下架（违规）";break;
        			default: return "";break;
        		}
        	}
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
            	"mDataProp":"starTime"  
            },{
            	"mDataProp":"endTime"  
            }
          ],"language":{
        	  "emptyTable":"没有服务信息，请添加"
          },"fnCreatedRow":function(nRow, aData, iDataIndex){
        	  $(nRow).click(function(e){
        		  if(e.target.nodeName=="TD"||e.target.nodeName=="td"){
        			  showBusAtomInfo(busItemTable,nRow,aData.fid);//根据这一行的id来获取所有服务子项的信息
        		  }
        	  });
          }
	});
	
	
	
	
	
	/**
	 * 将选择的服务信息保存在缓存中，并在服务列表中显示 缓存,并将显示标签也删除
	 * 参数data 为一行记录
	 */
	function showBusItemAndCacheBusItemId(data){
		if($scope.itemIds.indexOf(data.fid)>-1){
			alert("该服务项已经存在");
			return ;
		}
		$scope.itemIds.push(data.fid);
		busItemTable.row.add(data).draw();
		addBusItemPrice(data.workHours,data.autoPartsPrice);
	}
	/**
	 * 删除服务信息表中的一条记录，并在缓存中删除该id记录
	 */
	function deleteBusItem(id){
		 var index = $scope.itemIds.indexOf(id);
		  if(index>-1){
			  $scope.itemIds.splice(index,1);//从缓存中删除该id
			  busItemTable.row(index).remove().draw();
		  }
		  var textName = "#busItemsText span .glyphicon-remove[data-id="+id+"]" ;
		  $(textName).parent(".label-success").remove();
		  console.info($scope.itemIds);
	}
	/**
	 * 没选择一行服务或者删除一行服务都进行工时费和配件费的合计
	 * 选择一行服务
	 */
	function addBusItemPrice(workHours,autoPartsPrice){
		$scope.formData.workHours = ($scope.formData.workHours || 0 )+ workHours;
		$scope.formData.autoPartsPrice = ($scope.formData.autoPartsPrice || 0) + autoPartsPrice;
	}
	/**
	 * 删除一行服
	 */
	function deleteBusItemPrice(workHours,autoPartsPrice){
		$scope.formData.workHours = $scope.formData.workHours - workHours;
		$scope.formData.autoPartsPrice = $scope.formData.autoPartsPrice - autoPartsPrice;
	}
	
	/**
	 * 显示所有的服务子项信息
	 */
	function showBusAtomInfo(table,nRow,itemId){
		console.info(table.row(nRow).child);
		if(table.row(nRow).child.isShown()){
			table.row(nRow).child.hide();
		}else if(!table.row(nRow).child()){//当child中没有数据的时候从网上获取
			//从网上获取数据，并将数据添加到子行中
			$http({
				url:"shop/shopItemAction!detailsShopItem.action",
				method:"get",
				params:{
					fid:itemId
				}
			}).then(function(resp){
				if(resp.data.code == 1 && resp.data.busAtoms && resp.data.busAtoms.length>0){
					var detailsTable = "<table class='table table-bordered table-condensed '><tr class='danger'><th >子项编号</th><th>子项名称</th><th>配件名称</th><th>配件数</th><th>配件单价</th></tr>";
					for(var i=0;i<resp.data.busAtoms.length;i++){
						detailsTable = detailsTable + "<tr class='danger'>" ;
						detailsTable = detailsTable + "<td>"+resp.data.busAtoms[i].atomCode +"</td>" ;
						detailsTable = detailsTable + "<td>"+resp.data.busAtoms[i].atomName +"</td>" ;
						detailsTable = detailsTable + "<td>"+resp.data.busAtoms[i].autoPart.partName +"</td>" ;
						detailsTable = detailsTable + "<td>"+resp.data.busAtoms[i].autoParts +"</td>" ;
						detailsTable = detailsTable + "<td>"+resp.data.busAtoms[i].eunitPrice +"</td>" ;
						detailsTable = detailsTable + "</tr>" ;
					}
					detailsTable = detailsTable + "</table>";
					
					table.row(nRow).child(detailsTable).show();
				}else{
					table.row(nRow).child("<div class='text-center' style=''><h5>无子项信息</h5><div>").show();
				}
			});
		}else{
			table.row(nRow).child.show();//直接显示数据
		}
	}
	
	/**
	 * 点击保存操作
	 */
	$scope.submit = function(){
		$scope.formData.itemIds = $scope.itemIds;
		$scope.formData.packageCode = $scope.formData.busTypeCode + $scope.formData.packageCode;
		
		$http({
			url:"base/busPackageAction!addBusPackage.action",
			method:'post',
			data : $scope.formData
		}).then(function(resp){
			var code = resp.data.code ;
			if(code == 1){//代表保存成功
				$state.go($scope.state.list);
				$scope.treeAPI.showBusTypeTree();
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
		$scope.treeAPI.showBusTypeTree();
	}
	/**
	 * 显示或者隐藏增加服务的列表
	 */
	$scope.showOrHiddenItemTable = function(){
		if($("#busPackageForm").attr("class").indexOf("none")>0){//此时是隐藏的，该方法进行打开操作
			$("#busPackageForm").removeClass("none");
			$("#chooseBusItemTable").addClass("none");
			//因残树
			$scope.treeAPI.hiddenBusTypeTree();
		}else{
			if(!busItemTableForChoose){
				initBusItemTableForChoose();//初始化选择服务的表格
			}
			$("#busPackageForm").addClass("none");
			$("#chooseBusItemTable").removeClass("none");
			//显示树
			$scope.treeAPI.showBusTypeTree();
		}
	}
	/**
	 * 初始话提供选择的业务类型树
	 */
	$scope.initBusTypeZTree = function(e){
		showMenu();
	}
	
	/**
	 * 业务类型zTree的配置文件
	 */
	var setting = {
			view: {
				dblClickExpand: false,
				showIcon: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onClick
			}
		};
		
		/**
		 * 获取业务类型的数据
		 */
		$http({
			url:"base/busTypeAction!busTypeTree.action",
			method:'get'
		}).then(function(resp){
			var zNodes = resp.data.busTypes;
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});
		
		function onClick(e, treeId, treeNode) {
			$scope.formData.busTypeName = treeNode.name ;
			$("#busTypeName").val(treeNode.name);
			$scope.formData.busTypeCode = treeNode.id ;
			$("#clickId").trigger("click");
		}

		function showMenu() {
			if($("#menuContent").attr("style").indexOf("none")>-1){
				var cityObj = $("#busTypeName");
				$("#menuContent").css({left:15 + "px", top:34 + "px"}).slideDown("fast");
				$("#menuContent").css("width",cityObj.innerWidth());
				$("body").bind("mousedown", onBodyDown);
			}else{
				hideMenu();
			}
			
		}
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
	
		/**
		 * 初始化表单中的数据
		 */
		$http({
			url:"shop/shopPackageAction!detailsShopPackage.action",
			method:"get",
			params:{
				fid:$scope.rowIds[0]
			}
		}).then(function(resp){
			if(resp.data.code == 1){//返回成功
				$scope.formData = resp.data.details;
				//时间控件的初始化
				$('#starTime').val($scope.formData.starTime);
				$('#endTime').val($scope.formData.endTime);
				$scope.formData.starTimeStr = $scope.formData.starTime;
				$scope.formData.endTimeStr = $scope.formData.endTime;
				$scope.formData.starTime = undefined;
				$scope.formData.endTime = undefined;
				//套餐编号的初始化
				$scope.formData.packageCode = 
					$scope.formData.packageCode.split($scope.formData.busTypeCode)[1];
				//初始化服务列表数据
				for(var i=0;i<resp.data.details.shopItems.length;i++){
					showBusItemAndCacheBusItemId(resp.data.details.shopItems[i]);
				}
				//初始化业务类型树的数据
				$scope.formData.busTypeName = resp.data.busTypeName;
				
				//将参数
				$scope.formData.shopItems = undefined;
				$scope.formData.updateTime = undefined;
				
			}else{
				alert(resp.data.message);
				$state.go($scope.state.list);
			}
		});
		
		//初始化选中的数据
		$scope.setCanEdit(false);
		$scope.clearRowIds();
	
	
}]);