'use strict';
/* Controllers */
angular.module('app').controller('AppCtrl', ['$scope', '$translate', '$localStorage', '$window', '$http', '$state','$cookieStore','$location','$templateCache','loginedUserService',
  function($scope, $translate, $localStorage, $window, $http, $state,$cookieStore,$location,$templateCache,loginedUserService) {
    // add 'ie' classes to html
    var isIE = !! navigator.userAgent.match(/MSIE/i);
    isIE && angular.element($window.document.body).addClass('ie');
    isSmartDevice($window) && angular.element($window.document.body).addClass('smart');
    app.state = $state;
    
    // config
    $scope.app = {
      name: '优加养车',
      version: '1.0.0',
      // for chart colors
      color: {
        primary: '#7266ba',
        info: '#23b7e5',
        success: '#27c24c',
        warning: '#fad733',
        danger: '#f05050',
        light: '#e8eff0',
        dark: '#3a3f51',
        black: '#1c2b36'
      },
      settings: {
        themeID: 1,
        navbarHeaderColor: 'bg-black',
        navbarCollapseColor: 'bg-white-only',
        asideColor: 'bg-black',
        headerFixed: true,
        asideFixed: false,
        asideFolded: false,
        asideDock: false,
        container: false
      }
    }
    // save settings to local storage
    if (angular.isDefined($localStorage.settings)) {
      $scope.app.settings = $localStorage.settings;
    } else {
      $localStorage.settings = $scope.app.settings;
    }
    $scope.$watch('app.settings', function() {
      if ($scope.app.settings.asideDock && $scope.app.settings.asideFixed) {
        // aside dock and fixed must set the header fixed.
        $scope.app.settings.headerFixed = true;
      }
      // save to local storage
      $localStorage.settings = $scope.app.settings;
    }, true);
    // angular translate
    $scope.lang = {
      isopen: false
    };
    $scope.langs = {
      zh_CN: '中文（简体）',
      en: 'English',
      de_DE: 'German',
      it_IT: 'Italian'
    };
    $scope.selectLang = $scope.langs[$translate.proposedLanguage()] || "中文（简体）";
    $scope.setLang = function(langKey, $event) {
      // set the current lang
      $scope.selectLang = $scope.langs[langKey];
      // You can change the language during runtime
      $translate.use(langKey);
      $scope.lang.isopen = !$scope.lang.isopen;
    };

    // 设置默认语言为中文
    $scope.selectLang = $scope.langs['zh_CN'];
    $translate.use('zh_CN');
    $scope.app.ui = {};
    //旧的初始化左侧菜单
//    var uiInit = $scope.app.ui.init = function() {
//      $http.get('src/api/nav_items').then(function(resp) {
//        if (resp.data.datalist) {
//          $scope.app.ui.items = resp.data.datalist;
//        };
//      });
//    };
    
    var uiInit = $scope.app.ui.init  = function(){
    	var cookie = $cookieStore.get('username');
        if(!cookie){
        	return;
        }
    	var url = app.url.permission.menuList;
    	$.ajax({ 
    		type: "post", 
    		url: url,
            cache: false, 
            async: false, //默认是true：异步，false：同步。
            data:{},
            success: function(dt){ 
            	$scope.app.centerMenuArray=dt.rows;
            }
    	});
    }
    uiInit();
    var initLeftMenu = $scope.app.ui.initLeftMenu = function(){
    	var cookie = $cookieStore.get('username');
        if(!cookie){
        	return;
        }
    	var url = app.url.permission.firstMenuList;
    	var menuArray = new Array();//parentmenList
    	var childMenus = new Array();//childmenuList
    	$.ajax({ 
    		type: "post", 
    		url: url,
            cache:false, 
            async:false, //默认是true：异步，false：同步。
            data:{},
            success: function(dt){ 
            	for(var i=0; i<dt.rows.length; i++){
            		var obj = dt.rows[i];
            		if(!obj.child||obj.child.length<=0){
            			continue ;
            		}
            		var menu = new Object();//parentmen
            		menu.icon = "fa "+obj.imageClass+" "+obj.bgClass;
            		menu.name = obj.name;
            		menu.id = obj.id;
            		menuArray.push(menu);
            		//child menu
            		var child = new Object();
            		child.id = obj.id;
            		child.name = obj.name;
            		child.items = obj.child;
            		childMenus.push(child);
            	}
            }
    	});
    	var menuObject = new Object();
    	menuObject.type = "导航";
    	menuObject.items = menuArray;
    	var menus = new Array();
    	menus.push(menuObject);
    	$scope.app.ui.items = menus;
    	$scope.app.ui.childItems = childMenus;
    }
    initLeftMenu();
 // 用户退出
    $scope.logout = function(){
    	$http.post(app.url.logout).then(function(response) {
            if (response.data.code == 1) {
               $cookieStore.remove('username');
               loginedUserService.logout();
               $templateCache.removeAll();//清除所有的缓存
               $state.go('access.signin');
            } else {
            	 console.log("Logout: " + response.data.msg);
            }
          }, function(x) {
          });
    };
    
    /**
     * 重新生成菜单列表
     */
    $scope.$on("refreshMenu",function(){
    	uiInit();
    	 initLeftMenu();
    });

    // 公用函数工具
    app.utils = {};
    app.utils.getData = function(u, d, m, h) {
      var args = arguments;
      $http({
        url: u,
        data: typeof d !== 'function' ? d || {} : {},
        method: typeof m !== 'function' ? m || 'POST' : 'POST',
        headers: typeof h !== 'function' ? h || {} : {}
      }).then(function(dt) {
        if ((dt.data.rows && dt.data.rows.length !== 0) || dt.data.code == 1) {
          for (var i = 0; i < args.length; i++) {
            if (typeof args[i] === 'function') {
              args[i].call(null, dt.data.rows);
            };
          }
        } else if(dt.data.code == 2){
          alert(dt.data.msg);
        }
      }, function(x) {
        console.error(x.statusText);
      });
    };
    app.utils.getDataByKey = function(data, key, val){
      var len = data.length;
      for(var i=0; i<len; i++){
        if(data[i][key] == val){
          return data[i];
        }
      }
    };

    app.utils.localData = function(key, val){
      if(window.localStorage){
        if(val){
          localStorage.setItem(key, val);
          return true;
        }else if(val === null){
          localStorage.removeItem(key)  
        }else{
          var dt = localStorage.getItem(key);
          if(dt){
            return dt;
          }else{
            return null;
          }
        }
      }else{
        return false;
      }
    };
    app.utils.rolePerm = function(url,menuNum,allBtnArr){
      var permBtn = new Array();//已有权限项数组
      if(menuNum&&url){
    	  app.utils.getData(url,{"menuNum":menuNum}, function callback(dt){
    		  for(var i in dt){
    			  permBtn.push(dt[i].number);//添加已有的权限按钮数据
    		  }
    		  for(var i in allBtnArr){
    			  if(permBtn.indexOf(allBtnArr[i])<0){
    				  $("#"+allBtnArr[i]).remove();//删除没有权限的按钮
    			  }
    		  }
    	  });
      }
	  return permBtn;
    };
    app.utils.showMsg = function(msg,w){
    	var width = Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth);
    	if(w){
    		width = (width-w)/2;
    	}else{
    		width = width/2;
    	}
		var layer=jQuery("<div></div>");  
		layer.html(msg);
		layer.css({"height":"40px","position":"fixed","top":"50%","left":width,"background-color":"#000","color":"white","z-index":"9998","opacity":"0.6","padding":"10px"});
		jQuery("body").append(layer);
		setTimeout(function(){
			layer.remove();
		},3000);
    };
    
    $scope.$on("firstOn",function(){
    	alert("hello");
    });
    
    function isSmartDevice($window) {
      // Adapted from http://www.detectmobilebrowsers.com
      var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
      // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
      return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
    }
  }
]);