'use strict';

// 登录控制器
app.controller('SigninFormController', ['$scope', '$http', '$state', '$cookieStore',
  function($scope, $http, $state, $cookieStore) {
	
    $scope.user = {};
    $scope.authError = null;
    $scope.login = function() {
    $scope.authError = null;
    $http.post(app.url.login, {
        number: $scope.user.name,
        password: $scope.user.password
      }).then(function(response) {
        if (response.data.code == 1) {
          var days = 1;
          var exp = new Date();
          exp.setTime(exp.getTime() + 30 * 1000);
          $cookieStore.put('username', escape($scope.user.name) + ';expires=' + exp.toGMTString());
          $scope.user.userName = response.data.userName ;
          $scope.user.carShopName = response.data.carShopName ;
          $scope.user.id = response.data.id ;
          $cookieStore.put('user',JSON.stringify($scope.user));
          //重新生成菜单数据
          $scope.$emit("refreshMenu");
          $state.go('app.home');
        } else {
          $scope.authError = '用户名或密码错误';
        }
      }, function(x) {
        $scope.authError = '服务器错误';
      });
    };
  }
]);