app.controller('navCtrl', ['$scope', '$http',
  function($scope, $http) {
    /*	jQuery.ajax({
		url: 'http://192.168.0.38:8080/weixun/user/login.iv',
		data: {
			"number": "admin",
			"password": "admin"
		},
		dataType: "jsonp",
		jsonp: 'callback',
		jsonpCallback: 'jsonpCallback',
		success:function(dt){
			console.log(dt);
		}
	});*/
    function jsonpCallback(data) {
      console.log(data);
    }
  /*  $http.jsonp('http://192.168.0.38:8080/weixun/user/login.iv?number=admin&password=admin&jsonp=JSON_CALLBACK').then(function(resp) {
      debugger;
      if (resp.data.datalist) {
        var dt = resp.data.datalist,
          len = dt.length;
        for (var i = 0; i < len; i++) {
          $scope.lists = dt[i];
          //console.log($scope.lists.name);
        }
      };
    });*/
/*    $http.post('api/nav_items.js').then(function(resp) {
      //debugger;
      if (resp.data.datalist) {
        var dt = resp.data.datalist,
          len = dt.length;
        for (var i = 0; i < len; i++) {
          $scope.lists = dt[i];
          //console.log($scope.lists.name);
        }
      };
    });*/
  }
])