/**
*工具service
*by zw
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
	
	return {
		/**
		 *   从一个对象数组中找到一个指定的的对象,如果有多个重复的对象，那么只返回第一个对象
		 *   @param key 需要匹配的属性 ，例如id
		 *   @param value  匹配属性的值
		 *   @param array 对象数组，从该数组中进行寻找
		 */
		getObjectFromArray:getObjectFromArray,
		updateObjectFromArray:updateObjectFromArray,
		deleteObjectFromArray:deleteObjectFromArray
	}
}]);