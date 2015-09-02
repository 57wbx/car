'use strict';

app.controller('menuDetailsController', ['$http','$rootScope','$scope','$state','$modal',"$timeout",'sessionStorageService','uiClassService',"utilService","hintService",function($http, $rootScope, $scope, $state,$modal,$timeout,sessionStorageService,uiClassService,utilService,hintService) {
	
//	$scope.needCacheArray = ["menuForDetails","menuListDataTableProperties"];
//	sessionStorageService.clearNoCacheItem($scope.needCacheArray);
//	if($scope.rowIds[0]){
//		sessionStorageService.setItem("menuIdForDetails",$scope.rowIds[0]);
//	}else{
//		$scope.rowIds[0]  = sessionStorageService.getItemStr("menuIdForDetails");
//	}
	$scope.showChildMenu = false ;//是否显示子菜单，只有在新增或者修改二级菜单的时候才出现
	$scope.setShowChildMenu = function(flag){
		$scope.showChildMenu = flag ;
		if(!flag){
			$scope.permItemCollection = [];
		}
	}
	
	$scope.imgArray = uiClassService.getImgArray();
	$scope.colorArray = uiClassService.getColorArray();
//	if(!$scope.rowIds[0]||$scope.rowIds[0]==""){
//		$state.go($scope.state.list);//返回到列表界面
//	}
	$scope.permItemCollection = [];
	
	$scope.treeAPI.clickTreeListReload = function(id){
		console.info("details中的id",id);
		if(permItemTable){
			permItemTable.clear().draw();
		}
		$scope.formData = {} ;
		$http({
			url:'sys/menuAction!detailsMenu.action',
			method:"post",
			data:{
				id:id
			}
		}).then(function(resp){
			if(resp.data.code==1){
				$scope.formData = resp.data.details ;
				if($scope.menuTree.selectedLevel===0){
					$scope.setShowChildMenu(false) ;
					if(permItemTable){
						permItemTable = null ;
					}
					return ;
				}
				initPermItemTable(id);//获取该菜单下面的所有权限子项
			}else{
				alert(resp.data.message);
			}
		});
	};
	$scope.treeAPI.newMenu = function(parentId){
		$scope.formData = {} ;
		
		if(parentId){
			//新增二级菜单
			$scope.setShowChildMenu(true);
			$scope.formData.parentId = parentId ;
			$scope.formData.level = 1;
			initPermItemTable(null);
		}else{
			$scope.setShowChildMenu(false);
			$scope.formData.level = 0;
		}
		
	}
    
	var permItemTable ;
	function initPermItemTable(id){
		
		$scope.setShowChildMenu(true);
		if(permItemTable){
			refeshData(id);
			return ;
		}
		if($("#permItemTable").length<1){//延迟加载
			$timeout(function(){
				initPermItemTable(id);
			},20);
			return ;
		}
		permItemTable = $("#permItemTable").DataTable({
			"dom": '<"top">rt<"bottom"><"clear">',
			"pageLength":10,
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
	      	"aoColumns": [{
		        "mDataProp": "number"
		      },{
		        "mDataProp": "name"
		      },{
		        "mDataProp": "fType",
		        "render":function(param){
		        	switch(param){
		        	case 0:
		        		return "页面资源";break;
		        	case 1:
		        		return "操作资源";break;
		        	default:
		        		return "";break;
		        	}
		        }
		      },{
		        "mDataProp": "uiClass"
		      },{
		        "mDataProp": "action"
		      },{
		        "mDataProp": "useState",
		        "render":function(param){
		        	switch(param){
		        	case 0:
		        		return "使用";break;
		        	case 1:
		        		return "禁用";break;
		        	default:
		        		return "";break;
		        	}
		        }
		      },{
		    	  "render":function(a,b,aData,d){
		    		  var buttonStr =  '<button type="button" class="btn btn-default btn-sm" name="updateButton" ><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></button>';
		    		  if(!aData.id){
		    			  buttonStr = buttonStr + '<button type="button" class="btn btn-danger btn-sm" name="deleteButton" ><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>';
		    		  }
		    		  return buttonStr;
		    	  }
		      }],"fnCreatedRow": function(nRow, aData, iDataIndex){
		            $(nRow).attr('data-id', aData['id']);
		            $(nRow).find("button[name=updateButton]").click(function(e){
		            	showModal(nRow,aData);
		            });
		            $(nRow).find("button[name=deleteButton]").click(function(e){
		            	deletePermItemMenu(iDataIndex);
		            });
		        }
		});
		refeshData(id);
	}
	
	function deletePermItemMenu(index){
		var data = $scope.permItemCollection ;
		data.splice(index,1);
		renderData(data);
	}
	
	
	
	/**
	 * 刷新数据
	 */
	function refeshData(id,Data){
		if(!Data&&id){
			$http({
				url:"sys/menuAction!listPermItemByMenuId.action",
				method:"post",
				data:{
					id:id
				}
			}).then(function(resp){
				if(resp.data.code==1){
					renderData(resp.data.data);
				}
			});
		}else{
			renderData(Data);
		}
	}
	/**
	 * 渲染数据
	 */
	function renderData(data){
		if(permItemTable){
			permItemTable.clear().draw();
		}
		if(data&&data.length>0){
			$scope.permItemCollection = data ;
			permItemTable.rows.add(data).draw();
		}
	}
	
	/**
	 * 新增菜单的action操作
	 */
	$scope.addMenuAction = function(){
		showModal();
	}
	
	/**
	 * 弹窗事件
	 */
	var showModal = function(nRow,aData){
		var modalInstance = $modal.open({
   	     templateUrl: 'src/tpl/sys/menu/addAction.html',
   	     size: 'lg',
   	     backdrop:true,
   	     controller:"addActionController",
   	     resolve: {
   	    	 permItemId:function(){
   	    		 if(aData){
   	    			 return aData.id ;
   	    		 }
   	    		 return null ;
   	    	 },
   	    	 aData:function(){
   	    		 return aData ;
   	    	 }
   	     }
   	   });
		/**
		 * 弹窗关闭事件
		 */
		modalInstance.result.then(function (permItemFormData) {
			if(permItemFormData.id){
				//修改操作
				utilService.updateObjectFromArray("id",permItemFormData.id,$scope.permItemCollection,permItemFormData)  ;
				console.info("修改",$scope.permItemCollection);
			}else{
				//新增操作
				$scope.permItemCollection.push(permItemFormData);
				console.info("新增",$scope.permItemCollection);
			}
			renderData($scope.permItemCollection);
    	});
	}
	
	$scope.submit = function(){
		$scope.formData.permItemJsonStr = JSON.stringify($scope.permItemCollection);
		$scope.formData.parentId = $scope.menuTree.selectedMenuId ;
		$http({
			url:"sys/menuAction!addOrUpdateMenu.action",
			method:"post",
			data:$scope.formData 
		}).then(function(resp){
			if(resp.data.code==1){
				hintService.hint({title: "成功", content: "保存成功！" });
				$state.reload();
			}else{
				alert(resp.data.message);
			}
			$scope.isDoing = false ;
		});
	}
	
//    $scope.clearRowIds();
}]);