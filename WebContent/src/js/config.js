// config
var app = angular.module('app').config(
  ['$controllerProvider', '$compileProvider', '$filterProvider', '$provide', '$stateProvider',
    function($controllerProvider, $compileProvider, $filterProvider, $provide, $stateProvider) {
      // lazy controller, directive and service
      app.controller = $controllerProvider.register;
      app.directive = $compileProvider.directive;
      app.filter = $filterProvider.register;
      app.factory = $provide.factory;
      app.service = $provide.service;
      app.constant = $provide.constant;
      app.value = $provide.value;
      // API路径集合
      //app.urlRoot = '/weixun/';
      app.urlRoot = 'http://localhost:8080/car/';
      var common = {
        list: 'loadList.action',
        save: 'save.action',
        edit: 'getDataById.action',
        modify: 'modify.action',
        //delete: 'delete.action',
        delete: 'batchDelete.action',
        find: 'findByIds.action',
        view: 'view.action',
        freeze:'freeze.action',
        unfreeze:'unfreeze.action',
        menuPerm:'checkBtnPerm.action'
      };

      function getApi(name, common) {
        var apis = {};
        for (var n in common) {
          apis[n] = app.urlRoot + name + '!' + common[n];
        }
        return apis;
      }
      app.url = {
        login: app.urlRoot + 'basedata/userAction!login.action',
        logout: app.urlRoot + 'basedata/userAction!logout.action',
        currentUser: app.urlRoot + 'user/currentUserInfo.iv',
        menus: app.urlRoot + 'mainMenuItem/list.iv',
        orgTypes: app.urlRoot + 'orgUnitLayerType/allAdminOrgUnits.iv',
        org: {
          api: getApi('basedata/orgAction', common),
          allUnits: app.urlRoot + 'basedata/orgAction!allAdminOrgUnits.action',
          subUnits: app.urlRoot + 'basedata/orgAction!directSubAdminOrgUnits.action',
          attendance: app.urlRoot + 'basedata/orgAction!attendanceSet.action',
          loadOrgList: app.urlRoot + 'basedata/orgAction!loadOrgList.action',
        }, 
//        orgLayer: {
//          api: getApi('orgUnitLayer', common)
//        },
        employee: {
          api: getApi('basedata/personAction', common),
          orgUnits: app.urlRoot + 'basedata/personAction!loadOrgAndPositionList.action',
          getPositionByOrgId: app.urlRoot + 'basedata/positionAction!findByOrgId.action',
          loadNoneUserPersonList:app.urlRoot + 'basedata/personAction!loadNoneUserPersonList.action'
        },
        position: {
          api: getApi('basedata/positionAction', common)
        },
        leave: {
            api: getApi('oa/leaveAction', common),
            getPerson: app.urlRoot + 'oa/leaveAction!getPerson.action'
        },
        user:{
        	api: getApi('basedata/userAction', common)
        },
        trip: {
            api: getApi('oa/tripAction', common),
            getPerson: app.urlRoot + 'oa/tripAction!personList.action'
        },
        role: {
            api: getApi('basedata/roleAction', common),
            loadOrgList: app.urlRoot + 'basedata/roleAction!loadOrgList.action',
            loadUser: app.urlRoot + 'basedata/roleAction!loadUserListByOrgId.action',
            saveUserRoles: app.urlRoot + 'basedata/roleAction!saveUserRoles.action'
        },
        permission: {
        	api: getApi('permission/permissionAction', common),
        	menuList: app.urlRoot + 'permission/permissionAction!getMenuList.action',
        	firstMenuList: app.urlRoot + 'permission/permissionAction!getFirstMenuList.action'
        },
        attence: {//考勤管理
        	personal: {
        		work: {//上下班记录
        			api: getApi('oa/workAction', common)
        		},
        		egression: {//外出登记
        			api: getApi('oa/egressionAction', common),
        			getUser: app.urlRoot + 'oa/egressionAction!getBaseData.action',
        			getApprover: app.urlRoot + 'oa/egressionAction!approverList.action'
        		},
        		leave: {//请假登记
        			api: getApi('oa/attenceLeaveAction', common),
        			getUser: app.urlRoot + 'oa/attenceLeaveAction!getBaseData.action',
        			getApprover: app.urlRoot + 'oa/attenceLeaveAction!approverList.action'
        		},
        		overtime: {//加班登记
        			api: getApi('oa/overTimeAction', common),
        			getUser: app.urlRoot + 'oa/overTimeAction!getBaseData.action',
        			getApprover: app.urlRoot + 'oa/attenceLeaveAction!approverList.action'
        		},
        		trip: {//出差登记
        		}
        	},
        	count: {
        		attenceCount: {//个人考勤
        		},
        		egressionCount: {//外出统计
        		},
        		leave: {//请假统计
        		},
        		overtime: {//加班统计
        		},
        		trip: {//出差统计
        		}
        	},
        	attendset: {
        		schedu: {//排班设置
        			api: getApi('oa/scheduAction',common),
        			loadPerson:app.urlRoot + 'oa/scheduAction!loadPerson.action',
        			loadPersonTree:app.urlRoot + 'oa/scheduAction!loadPersonTree.action',
        			loadOrgList:app.urlRoot + 'oa/scheduAction!loadOrgList.action',
        			getDeptTreeData:app.urlRoot + 'oa/scheduAction!getDeptTreeData.action',
        			getPersonTreeData:app.urlRoot + 'oa/scheduAction!getPersonTreeData.action',
        			loadClass:app.urlRoot + 'oa/scheduAction!loadClass.action'
        		},
        		overtimeOption:{//加班设置
        			api:getApi('oa/overtimeOptionAction',common)
        		},
        		timeOption:{//考勤时段设置
        			api:getApi('oa/timeOptionAction',common)
        		},
        		shift:{//班次设置
        			api:getApi('oa/shiftAction',common),
        			checkNum: app.urlRoot + 'oa/shiftAction!checkNumber.action'
        		}
        	}
        },
        expenseBill: {
        	api: getApi('oa/expenseAction', common),
        	feeTypeList: app.urlRoot + 'oa/expenseAction!loadFeeTypeList.action',
        	initData: app.urlRoot+ 'oa/expenseAction!getLoginUserInfo.action',
        	loadExpenseBillById: app.urlRoot+ 'oa/expenseAction!loadExpenseBillById.action'
        },
        f7:{
        	userf7: app.urlRoot + 'basedata/f7Action!loadUserList.action'
        }
      };
    }
  ]).config(['$translateProvider',
  function($translateProvider) {
    // Register a loader for the static files
    // So, the module will search missing translation tables under the specified urls.
    // Those urls are [prefix][langKey][suffix].
    $translateProvider.useStaticFilesLoader({
      prefix: 'src/l10n/',
      suffix: '.js'
    });
    // Tell the module what language to use by default
    $translateProvider.preferredLanguage('en');
    // Tell the module to store the language in the local storage
    $translateProvider.useLocalStorage();
  }
]).factory('authorityInterceptor', [

  function() {
    var authorityInterceptor = {
      response: function(response) {
        //console.log(response);
        if ('no permission' == response.data) {
          //console.log(response);
          app.controller('Interceptor', ['$state',
            function($state) {
              app.state.go('access.404');
            }
          ]);
        }
        if ("no login" == response.data) {
          //console.log(response);
          app.state.go('access.signin');
        }
        return response;
      }
    };
    return authorityInterceptor;
  }
]).config(
  ['$httpProvider',
    function($httpProvider) {
      $httpProvider.interceptors.push('authorityInterceptor');
      $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
      $httpProvider.defaults.transformRequest = [

        function(data) {
          return angular.isObject(data) && String(data) !== '[object File]' ? $.param(data,true) : data;
        }
      ];
    }
  ]);