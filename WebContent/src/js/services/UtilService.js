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
	
	return {
		/**
		 *   从一个对象数组中找到一个指定的的对象,如果有多个重复的对象，那么只返回第一个对象
		 *   @param key 需要匹配的属性 ，例如id
		 *   @param value  匹配属性的值
		 *   @param array 对象数组，从该数组中进行寻找
		 */
		getObjectFromArray:getObjectFromArray
	}
}]);