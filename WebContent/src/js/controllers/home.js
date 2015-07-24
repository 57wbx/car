'use strict';

// 首页控制器
app.controller('HomeController', ['$scope','$state', '$cookieStore',
  function($scope,$state, $cookieStore) {
	var cookie = $cookieStore.get('username');
    console.log(cookie);
    if(!cookie){
      $state.go('access.signin');
      return;
    }
    //left menu Array
	$scope.initCenterMenu = function(){
		for(var i=0; i<$scope.app.centerMenuArray.length; i++){
			var obj = $scope.app.centerMenuArray[i];
			var centerMenu = "";
			centerMenu+= "<div class='col-md-2 col-sm-3 col-xs-4'>";
			centerMenu+= 	("<a ui-sref='"+obj.url+"' class='bg"+obj.bgClass+" app-item' href='#/"+obj.url.replace(/\./g,"/")+"'>");
			centerMenu+= 		("<i class='fa "+obj.imageClass+" text-white text-3x'></i>");
			centerMenu+= 		("<span class='text-white font-thin h4'>"+obj.name+"</span>");
			centerMenu+= 	"</a>";
			centerMenu+= "</div>";
			$(".row.text-center").append(centerMenu);
		}
		var addBtn = "<div class='col-md-2 col-sm-3 col-xs-4'><a ui-sref='app.org' class='bg-white app-item add-item'>"
			+"<i class='fa fa-plus text-dark text-3x'></i><span class='text-dark font-thin h4 block'>添加模块</span></a></div>";
		$(".row.text-center").append(addBtn);
	}
	if($scope.app.centerMenuArray&&$scope.app.centerMenuArray.length>0){
		$scope.initCenterMenu();
	}else{
		$scope.app.ui.init();
		$scope.initCenterMenu();
		$scope.app.ui.initLeftMenu();
  }
	
    //$scope.user = {};
    //var days = 1;
    //var exp = new Date();
    //exp.setTime(exp.getTime() + days * 24 * 60 * 60 * 1000);
    //console.log(response.headers('Set-Cookie'));
    //document.cookie = 'JSESSIONID=' + escape('1CAEA6F42C32BDC35784264D187EBB92') + ';expires = ' + exp.toGMTString();
    
    //$cookies.username = escape($scope.user.name) + ';expires = ' + exp.toGMTString();;
    //$state.go('app.home');
  }
]);