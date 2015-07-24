'use strict';
app.controller('OrgAttendance', function($rootScope, $scope, $state, $stateParams) {
  var url = app.url.org.attendance;
  $scope.onTime = new Date('2015/01/01 9:00:00');
  $scope.offTime = new Date('2015/01/01 18:00:00');
  $scope.hstep = 1;
  $scope.mstep = 5;
  $scope.options = {
    hstep: [1, 2, 3],
    mstep: [1, 5, 10, 15, 25, 30]
  };
  $scope.return = function(){
  	$state.go('app.org.list');
  	$scope.$parent.return();
  };
  if($scope.$parent.permBtn.indexOf("attendance")<0){//判断是否有修改权限
  	$("#attendance").remove();
  }
  $scope.ismeridian = false;
  $scope.toggleMode = function() {
    $scope.ismeridian = !$scope.ismeridian;
  };
  $scope.update = function() {
    var d = new Date();
    d.setHours(14);
    d.setMinutes(0);
    $scope.onTime = d;
    $scope.offTime = d;
  };
  $scope.changed = function() {
    console.log('OnTime is: ' + $scope.onTime);
    console.log('OffTime is: ' + $scope.offTime);
  };
  $scope.clear = function() {
    $scope.onTime = null;
    $scope.offTime = null;
  };
  // 提交并添加数据
  $scope.submit = function() {
    app.utils.getData(url, {"ids":$rootScope.ids,"onDutyTime":$scope.formatDate($scope.onTime),"offDutyTime":$scope.formatDate($scope.offTime)}, function(dt) {
      //$state.reload('app.org');
      $state.go('app.org.list');
    });
  };
  $scope.formatTen = function(num){
      return num > 9 ? (num + "") : ("0" + num);
  },
  $scope.formatDate = function(date) {
      var year = date.getFullYear();
      var month = date.getMonth() + 1;
      var day = date.getDate();
      var hour = date.getHours();
      var minute = date.getMinutes();
      var second = date.getSeconds();
      return year + "-" + $scope.formatTen(month) + "-" + $scope.formatTen(day) + " " + $scope.formatTen(hour) + ":" + $scope.formatTen(minute) + ":" + $scope.formatTen(second);
  }
});