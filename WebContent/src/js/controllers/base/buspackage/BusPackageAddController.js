app.controller("busPackageAddController",['$scope','$state','$http',function($scope,$state,$http){
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
	 * 套餐编码是否已经存在的标识符
	 */
	$scope.isUnique = true;
	$scope.notUniqueMessage = "已存在";
	$scope.checkIsUnique = function(fid,packageCode){//检查套餐编码是否已经存在需要套餐id和更新的套餐编号
		$http({
			url:"base/busPackageAction!checkPackageCodeIsUnique.action",
			method:"get",
			params:{
				packageCode : $scope.formData.busTypeCode + packageCode,
				fid:fid
			}
		}).then(function(resp){
			if(resp.data.code==1){
				$scope.isUnique = true;
			}else{//存在
				$scope.isUnique = false;
			}
		});
		
	}
	$("#packageCode").change(function(e){
		if($scope.busPackageForm.packageCode.$valid){
			$scope.checkIsUnique($scope.formData.fid,$(e.target).val());
		}
	});
	
	/**
	 * 初始化树点击的方法,注销其他的点击树的方法
	 */
	$scope.treeAPI.clickTreeListReload = null;
	$scope.treeAPI.clickTreeAddOrUpdateReload = function(){
		if(busItemTableForChoose){//给服务选择列表进行重新加载数据操作
			busItemTableForChoose.ajax.reload();
		}
	}
	
	
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
            },{"render": function(param){
                      return  '<button type="button" class="btn btn-danger btn-sm" name="deleteButton"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>';
                    }
          }],"language":{
        	  "emptyTable":"没有服务信息，请添加"
          },"fnCreatedRow":function(nRow, aData, iDataIndex){
        	  $(nRow).find("button").click(function(){
        		  deleteBusItem(aData.fid);
        	  });
        	  $(nRow).click(function(e){
        		  if(e.target.nodeName=="TD"||e.target.nodeName=="td"){
        			  showBusAtomInfo(busItemTable,nRow,aData.fid);//根据这一行的id来获取所有服务子项的信息
        		  }
        	  });
          }
	});
	
	
	/**
	 * 初始化服务选择列表的方法，服务选择列表并不是一开始进行数据查询的，当用户点击需要新增的时候进行选择
	 */
	var busItemTableForChoose ;
	function initBusItemTableForChoose(){
		busItemTableForChoose = $("#busItemTableForChoose").on('preXhr.dt', function ( e, settings, data ) {
			//获取被选择树的id，来进行相关查询，树id 在上级controller中定义了
			if($scope.busTypeTree.selectedTypeCode){
				data.busTypeCode = $scope.busTypeTree.selectedTypeCode;
			}
	    }).DataTable({
			"sAjaxSource":"base/busItemAction!listBusItemByTypeCode.action",
	    	"bServerSide":true,
	    	"sAjaxDataProp":"data",
	    	"pageLength":5,
	    	"dom": '<"top">rt<"bottom"p><"clear">',
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
	          },scrollX:true,
	    	  "aoColumns": [ {
	    		  "render":function(param){
	    			  return "<button type='button' class='btn btn-default' ><span class='glyphicon glyphicon-ok'></span>选择</button>" +
	    			  		"";
	    		  }
	    	  },{
	            "mDataProp": "itemCode",
	          }, {
	            "mDataProp": "itemName",
	          }, {
	            "mDataProp": "standardPrice",
	          }, {
	            "mDataProp": "actualPrice",
	          }, {
	            "mDataProp": "workHours",
	          }, {
	            "mDataProp": "autoPartsPrice",
	          }, {
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
	            }, {
	                "mDataProp": "starTime",
	            }, {
	              "mDataProp": "endTime",
	            }],
	          "fnCreatedRow": function(nRow, aData, iDataIndex){
	        	  $(nRow).attr("data-id",aData.fid);
	        	  $(nRow).find("button").click(function(e){
	        		  //初始化这个text的点击事件
	        		  showBusItemAndCacheBusItemId(aData);
	        	  });
	        	  $(nRow).click(function(e){
	        		  if(e.target.nodeName=="TD"||e.target.nodeName=="td"){
	        			  showBusAtomInfo(busItemTableForChoose,nRow,aData.fid);//根据这一行的id来获取所有服务子项的信息
	        		  }
	        	  });
	          }
		});
	}
	
	/**
	 * 初始化显示text的事件
	 */
	function initTextEvent(id){
		var textName = "#busItemsText span .glyphicon-remove[data-id="+id+"]" ;
				$(textName).mouseover(function(){
			  $(textName).css("background-color","red").css("cursor","pointer");
		  });
		  $(textName).mouseout(function(){
			  $(textName).css("background-color","#5cb85c")
		  });
		  $(textName).click(function(){
			  deleteBusItem(id);
		  });
	}
	/**
	 * 将选择的服务信息保存在缓存中，并在服务列表中显示 缓存,并将显示标签也删除
	 * 参数data 为一行记录
	 */
	function showBusItemAndCacheBusItemId(data){
		if($scope.itemIds.indexOf(data.fid)>-1){
			alert("该服务项已经存在");
			return ;
		}
		$("#busItemsText").append("<span class='label label-success'>" +
				data.itemName+" <span class='glyphicon glyphicon-remove' aria-hidden='true' data-id='"+data.fid+"' '></span></span>&nbsp;");
		initTextEvent(data.fid);
		$scope.itemIds.push(data.fid);
		busItemTable.row.add(data).draw();
		console.info($scope.itemIds);
		addBusItemPrice(data.workHours,data.autoPartsPrice);
	}
	/**
	 * 删除服务信息表中的一条记录，并在缓存中删除该id记录
	 */
	function deleteBusItem(id){
		 var index = $scope.itemIds.indexOf(id);
		 deleteBusItemPrice(busItemTable.row(index).data().workHours,busItemTable.row(index).data().autoPartsPrice);
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
		$("#clickId").trigger("click");
	}
	/**
	 * 删除一行服
	 */
	function deleteBusItemPrice(workHours,autoPartsPrice){
		$scope.formData.workHours = $scope.formData.workHours - workHours;
		$scope.formData.autoPartsPrice = $scope.formData.autoPartsPrice - autoPartsPrice;
		$("#clickId").trigger("click");
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
				url:"base/busItemAction!detailsBusItem.action",
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
		 * 
		 */
	
	
}]);