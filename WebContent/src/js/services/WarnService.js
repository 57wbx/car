/**
 * 提供警告消息的service
 * @author zw
 */
app.factory("warnService",['$rootScope','$compile',function($rootScope,$compile){
	 

	  
	
//	  $compile(container)($rootScope);

	  /**
	   * 被调用的函数，提供提示消息的功能
	   * @headMessage 消息的头部  默认为 "操作确认";
	   * @bodyMessage 消息的主体部分  默认为："你确定要执行该操作吗？"
	   * @fn 当点击确定的时候需要执行的函数
	   * @callBackFn 为$http服务提供的回调函数，也就是当fn=function(){ return $http({}) };的时候@callBackFn=then函数中需要执行的函数
	   * 
	   * 示例：
	   * 			1、当@callBackFn为空或者为null时
	   * 			只执行fn函数
	   * 
	   * 			2、当@callBackFn不为空的时候，说明fn是一个可以回调的函数，类似于$http服务,目前只提供给$http服务进行使用
	   * 			执行规则：先执行fn函数，然后在fn.then(function(resp){
	   * 																				callBackFn(resp);
	   * 																		})
	   */
	  function warn(headMessage,bodyMessage,fn,callBackFn){
		  
		  var mask = $('<div class="mask"></div>');
		  var container = $('#dialog-container');
		  var dialog = $('#dialog');
		  var msgHead = $('#msgHead');
		  var msgBody = $('#msgP');
		  var hButton = $('#clickId');
		  var bodyMessage_default = "你确定要执行该操作吗？";
		  var headMessage_default = "操作确认";
		  var doIt = function(){};
		  
		  // 重新绑定事件
		  $rootScope.cancel = function(){
		    mask.remove();
		    container.addClass('none');
		  };  
		  $rootScope.do = function(){
			  doIt();
		  };
		  
		  if(headMessage){
			  msgHead.html(headMessage);
		  }else{
			  msgHead.html(headMessage_default);
		  }
		  if(bodyMessage){
			  msgBody.html(bodyMessage);
		  }else{
			  msgBody.html(bodyMessage_default);
		  }
		  mask.insertBefore(container);
		  container.removeClass('none');
		  doIt = function(){//回调函数
			  if(fn){
				  var result = fn();
				  if(callBackFn&&result){
					  
					  result.then(function(resp){
						  try{
							  callBackFn(resp);
						  } catch(e){
							  console.error(e);
						  }finally{
							  $rootScope.cancel();
						  }
					  });
					  
				  }else{
					  $rootScope.cancel();
				  }
				  
			  }
		  }
	  }
	    
	  return {
		  warn:warn
	  }
	 
}]);