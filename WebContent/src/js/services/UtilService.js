/**
*工具service
*@author zw
*/
app.factory("utilService",[function(){
	/**
//	 * 从一个对象数组中找到一个指定的的对象
	 */
	var getObjectFromArray = function(key,value,array){
		if(key&&value&&array&&array.length>0){
			for(var i=0;i<array.length;i++){
				if(array[i][key]==value){
					return array[i];
				}
			}
		}
		return null;
	}
	
	/**
	 * 替换对象数组中某一个值
	  *   @param key 需要匹配的属性 ，例如id
		 *   @param value  匹配属性的值
		 *   @param array 对象数组，从该数组中进行寻找
		 *   @param newObj 替代找到的元素
	 */
	var updateObjectFromArray = function(key,value,array,newObj){
		if(key&&value&&array&&array.length>0){
			for(var i=0;i<array.length;i++){
				if(array[i][key]==value){
					array[i] = newObj ;
				}
			}
		}
	}
	
	/**
	 * 删除对象数组中的一个值
	 */
	var deleteObjectFromArray = function(key,value,array,newObj){
		if(key&&value&&array&&array.length>0){
			for(var i=0;i<array.length;i++){
				if(array[i][key]==value){
					array.splice(i,1);
				}
			}
		}
	}
	/**
	 * 将数据普通的数组数据：[{"id":"00000000-0000-0000-0000-000000000000CCE7AED4","pId":null,"name":"深圳总部"},{"id":"9c76d944-22dd-11e5-8e71-00163e00172b","pId":"00000000-0000-0000-0000-000000000000CCE7AED4","name":"深圳分公司"},{"id":"c4cf3050-31de-11e5-ae87-54ee75379faf","pId":"00000000-0000-0000-0000-000000000000CCE7AED4","name":"湖南分公司"}]
	 * 装换成abn-tree需要的格式的数据
	 */
	 /**
	    * 元数组格式的对象的形式为：{id:1,pid:222,name=1232}
	    */
	   function wrapData(sourceData,id,fid,label,fn){
		   var _rootBranch = [];//等于根 id 为空的集合
		   for(var i = 0,_length = sourceData.length; i<_length; i++){
			   if(!getObjectFromArray(id,sourceData[i][fid],sourceData)){
				   //当父id没有任何对象的id和它对应时，该对象是根节点
				   var node = setNode(sourceData[i],sourceData,id,fid,label,fn) ;
				   _rootBranch.push(node);
			   }
		   }
		   console.info("包装之后树的数据",_rootBranch);
		   return _rootBranch ;
	   }
	   /**
	    * 设置成指定形式对象的数据 {label:"显示名称"}
	    */
	   function setNode(obj,sourceArray,id,fid,label,fn){
		   var node = {};
		   node["label"] = obj[label] ;
		   node["id"] = obj[id] ;
		   node["fid"] = obj[fid] ;
		   /*
		    * 获取子对象
		    */
		   var childs = []; 
		   for(var j = 0;j<sourceArray.length;j++){
			   if(sourceArray[j][fid] == obj[id]){
				   childs.push(setNode(sourceArray[j],sourceArray,id,fid,label,fn));
			   }
		   }
		   node["children"] = childs ;
		   /*
		    * 回调函数
		    */
		   if(fn){
			   fn(obj,node) ;
		   }
		   
		   return node ;
	   }
	
	
	return {
		/**
		 *   从一个对象数组中找到一个指定的的对象,如果有多个重复的对象，那么只返回第一个对象
		 *   @param key 需要匹配的属性 ，例如id
		 *   @param value  匹配属性的值
		 *   @param array 对象数组，从该数组中进行寻找
		 */
		getObjectFromArray:getObjectFromArray,
		updateObjectFromArray:updateObjectFromArray,
		deleteObjectFromArray:deleteObjectFromArray,
		/**
		 * 将普通的数据转换成abn—tree 需要形式的数据格式 调用格式：utilService.wrapData(orgData,"id","pId","name") ;
		 * @param sourceData 必须；数据元，必须是数组格式的数据
		 * @id 必须；对象的id属性名
		 * @fid 必须；对象的父id属性名
		 * @label 必须；对象中需要显示的属性名称
		 * @fn 选择；回调函数，需要显示的对象都会进行该函数的调用；fn(obj,node)、obj,源数据对象，node转化成abn-tree需要的数据格式。其中可以对其进行事件绑定
		 * 			例如：function fn(obj,node){
		 * 										//事件处理
		 * 										node.onSelect = function(branch){//进行点击事件处理。};
		 *										//个性话属性
		 *										node.hello = "";
		 * 									  }
		 */
		wrapData:wrapData
	}
}]);