'use strict';
app.controller('hotWordAddController', ['$scope', '$state','$http','hintService',
  function($scope, $state,$http,hintService) {
	
    $scope.formData = {};
    
    $scope.submit = function(){
    	$http({
    		url:"opr/hotWordAction!addHotWord.action",
    		method:"post",
    		data:$scope.formData 
    	}).then(function(resp){
    		if(resp.data.code === 1){
    			hintService.hint({title: "成功", content: "保存成功！" });
    			$state.go($scope.state.list);
    		}else{
    			alert(resp.data.message);
    		}
    		$scope.isDoing = false ;
    	});
    }
    
	$scope.cancel = function(){
		$state.go($scope.state.list);
	}
  
}]);