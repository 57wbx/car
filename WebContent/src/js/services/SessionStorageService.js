/**
* 将重要的数据保存在sessionStorage中，确保用户f5的时候还有数据
* 这个服务最重要的功能是，防止f5刷新的时候数据没了，
* 但是存在一个问题，就是如果不对session中的缓存进行清除,那么不是f5刷新，而是从新进入该页面也会获取缓存，所以需要在进入每个
* 页面时进行清除缓存，
* 但是又存在一个问题，就是有些数据是需要一直在缓存中的，这就需要我们保存这些数据，缓存他们的key，然后在清除缓存的时候，
* 不清除keys中的数据
*/
app.factory("sessionStorageService",[function(){
	
	var keys = [];
	
	/**
	 *  isForeverCache 是否永久缓存，true代表永久缓存，就在keys保存该key
	 */
	var setItem = function(key,item,isForeverCache){
		if(typeof item === "object"){
			var itemStr = JSON.stringify(item)
			sessionStorage.setItem(key,itemStr);
		}else{
			if(item==="null"||item==="undefined"){
				return ;
			}
			sessionStorage.setItem(key,item);
		}
		if(isForeverCache){//永久缓存该数据
			keys.push(key);
			setItem("keys",keys);
		}
	};
	/**
	 * 获取sessionStorage中的数据
	 */
	var getItemStr = function(key){
		var str = sessionStorage.getItem(key)
		if(!str||str==="null"||str==="undefined"){
			return undefined;
		}
		return sessionStorage.getItem(key);
	}
	var getItemObj = function(key){
		var obj = sessionStorage.getItem(key);
		if(!obj||obj=="null"||obj==="undefined"){
			return undefined;
		}
		return JSON.parse(sessionStorage.getItem(key));
	}
	/**
	 * 清除sessionStorage中的数据
	 */
	var removeItem = function(key){
		sessionStorage.removeItem(key);
		for(var i=0;i<keys.length;i++){
			if(keys[i]===key){//该key为永久缓存对象
				keys.splice(i,1);
				setItem("keys",keys);
				break;
			}
		}
	}
	/**
	 * 清空sessionStroage中所有的数据
	 */
	var clearAllItem = function(){
		sessionStorage.clear();
		keys = [];
	};
	
	/**
	 * 清除除了指定数据和缓存在keys中以外的所有数据
	 */
	var clearNoCacheItem = function(keyArray){
		var cacheArray = [];
		/*
		 *缓存指定的数据 
		 */
		if(keyArray&&keyArray.length>0){//不清除keyArray中和keys中的数据
			for(var i=0;i<keyArray.length;i++){
				cacheArray.push(getItemStr(keyArray[i]));
			}
		}
		/*
		 *缓存keys中的数据 
		 */
		for(var i=0;i<keys.length;i++){
			cacheArray.push(getItemStr(keys[i]));
		}
		
		clearAllItem();//清除所有的数据
		
		if(keyArray&&keyArray.length>0){//不清除keyArray中和keys中的数据
			for(var i=0;i<keyArray.length;i++){
				setItem(keyArray[i],cacheArray.shift());
			}
		}
		for(var i=0;i<keys.length;i++){
			setItem(keys[i],cacheArray.shift());
		}
	};
	
	keys = getItemObj("keys") || [];//不要在sessionStorage中清除的key集合
	return {
		/**
		 * 保存一个对象，如果是基本类型的数据，直接保存，如果是对象，则先字符串操作，然后在进行保存
		 * setItem(key,item,isForeverCache)
		 * @param key
		 * @param item
		 * @param isForeverCache    true或者false   true代表在session的生命周期中永久缓存
		 */
		setItem:setItem,
		/**
		 * 获得一个字符串对象
		 */
		getItemStr:getItemStr,
		/**
		 * 获取一个对象
		 */
		getItemObj:getItemObj,
		/**
		 * 移出一个指定的缓存对象
		 * @param key
		 */
		removeItem:removeItem,
		/**
		 * 清除所有的缓存对象
		 */
		clearAllItem:clearAllItem,
		/**
		 * 清除所有除了keys中和指定array参数中以外的数据
		 * clearNoCacheItem(array)
		 * @param array  代表一个不需要清除缓存的的keys字符串数组
		 */
		clearNoCacheItem:clearNoCacheItem
		
	};
	
}]);