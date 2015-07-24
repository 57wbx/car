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