'use strict';

angular.module('app').run(
  ['$rootScope', '$state', '$stateParams',
    function($rootScope, $state, $stateParams) {
      $rootScope.$state = $state;
      $rootScope.$stateParams = $stateParams;
    }
  ]).config(
  ['$stateProvider', '$urlRouterProvider', 'JQ_CONFIG',
    function($stateProvider, $urlRouterProvider, JQ_CONFIG) {
      //$urlRouterProvider.when('/app/home');
      $urlRouterProvider.otherwise('/app/home');
      $stateProvider.state('app', {
        abstract: true,
        url: '/app',
        //templateUrl: 'src/tpl/app.html',
        views: {
          '': {
            templateUrl: 'src/tpl/app.html'
          },
          'footer': {
            template: '<div id="dialog-container" ui-view></div>'
          }
        }
      }).state('app.home', {
        url: '/home',
        templateUrl: 'src/tpl/home.html',
        resolve: {
          deps: ['$ocLazyLoad',
            function($ocLazyLoad) {
              return $ocLazyLoad.load(['src/js/controllers/home.js']);
            }
          ]
        }
      }).state('app.frame', {
        url: '/frame',
        templateUrl: 'src/tpl/org/frame.html',
        resolve: {
          deps: ['$ocLazyLoad', 'uiLoad',
            function($ocLazyLoad, uiLoad) {
              return $ocLazyLoad.load('angularBootstrapNavTree').then(function() {
                //return $ocLazyLoad.load('src/js/controllers/org/frameTest.js');
              })
            }
          ]
        }
      }).state('app.org', {
        url: '/org',
        templateUrl: 'src/tpl/org/org.html',
        resolve: {
          deps: ['$ocLazyLoad', 'uiLoad',
            function($ocLazyLoad, uiLoad) {
              return $ocLazyLoad.load('angularBootstrapNavTree').then(function() {
                return $ocLazyLoad.load('src/js/controllers/org/org.js');
              });
            }
          ]
        }
      }).state('app.org.list', {
        url: '/list',
        templateUrl: 'src/tpl/org/org_list.html',
        resolve: {
          deps: ['$ocLazyLoad', 'uiLoad',
            function($ocLazyLoad, uiLoad) {
              return $ocLazyLoad.load('src/js/controllers/org/org_list.js').then(function() {
                return uiLoad.load(JQ_CONFIG.dataTable);
              });
            }
          ]
        }
      }).state('app.org.add', {
        url: '/add',
        templateUrl: 'src/tpl/org/org_add.html',
        resolve: {
          deps: ['$ocLazyLoad',
            function($ocLazyLoad) {
              return $ocLazyLoad.load('src/js/controllers/org/org_add.js');
            }
          ]
        }
      }).state('app.org.edit', {
        url: '/edit',
        templateUrl: 'src/tpl/org/org_edit.html',
        resolve: {
          deps: ['$ocLazyLoad',
            function($ocLazyLoad) {
              return $ocLazyLoad.load('src/js/controllers/org/org_edit.js');
            }
          ]
        }
      }).state('app.org.add.list', {
        url: '/list',
        templateUrl: 'src/tpl/org/org_add_list.html',
        resolve: {
          deps: ['$ocLazyLoad','uiLoad',
            function($ocLazyLoad, uiLoad) {
              return uiLoad.load(JQ_CONFIG.dataTable).then(function() {
                return $ocLazyLoad.load('src/js/controllers/org/org_add_list.js');
              });
            }
          ]
        }
      }).state('app.org.details', {
        url: '/details',
        templateUrl: 'src/tpl/org/org_details.html',
        resolve: {
          deps: ['$ocLazyLoad','uiLoad',
            function($ocLazyLoad, uiLoad) {
              return uiLoad.load(JQ_CONFIG.chosen).then(function() {
                return $ocLazyLoad.load('src/js/controllers/org/org_details.js');
              });
            }
          ]
        }
      }).state('app.org.attendance', {
          url: '/attendance',
          templateUrl: 'src/tpl/org/org_attendance.html',
          resolve: {
            deps: ['$ocLazyLoad','uiLoad',
              function($ocLazyLoad, uiLoad) {
                return uiLoad.load(JQ_CONFIG.chosen).then(function() {
                  return $ocLazyLoad.load('src/js/controllers/org/org_attendance.js');
                });
              }
            ]
          }
      }).state('app.trip', {
          url: '/trip',
          templateUrl: 'src/tpl/oa/trip/trip.html',
          resolve: {
            deps: ['$ocLazyLoad', 'uiLoad',
              function($ocLazyLoad, uiLoad) {
                return $ocLazyLoad.load('angularBootstrapNavTree').then(function() {
                  return $ocLazyLoad.load('src/js/controllers/oa/trip/trip.js');
                });
              }
            ]
          }
        }).state('app.trip.list', {
        url: '/list',
        templateUrl: 'src/tpl/oa/trip/trip_list.html',
        resolve: {
          deps: ['$ocLazyLoad', 'uiLoad',
            function($ocLazyLoad, uiLoad) {
              return $ocLazyLoad.load('src/js/controllers/oa/trip/trip_list.js').then(function() {
                return uiLoad.load(JQ_CONFIG.dataTable);
              });
            }
          ]
        }
      }).state('app.trip.add', {
          url: '/add',
          templateUrl: 'src/tpl/oa/trip/trip_add.html',
          resolve: {
            deps: ['$ocLazyLoad',
              function($ocLazyLoad) {
                return $ocLazyLoad.load('src/js/controllers/oa/trip/trip_add.js');
              }
            ]
          }
        }).state('app.trip.edit', {
            url: '/edit',
            templateUrl: 'src/tpl/oa/trip/trip_edit.html',
            resolve: {
              deps: ['$ocLazyLoad',
                function($ocLazyLoad) {
                  return $ocLazyLoad.load('src/js/controllers/oa/trip/trip_edit.js');
                }
              ]
            }
          }).state('app.trip.details', {
              url: '/details',
              templateUrl: 'src/tpl/oa/trip/trip_details.html',
              resolve: {
                deps: ['$ocLazyLoad','uiLoad',
                  function($ocLazyLoad, uiLoad) {
                    return uiLoad.load(JQ_CONFIG.chosen).then(function() {
                      return $ocLazyLoad.load('src/js/controllers/oa/trip/trip_details.js');
                    });
                  }
                ]
              }
            }).state('app.employee', {
        url: '/employee',
        templateUrl: 'src/tpl/org/employee/employee.html',
        resolve: {
            deps: ['$ocLazyLoad', 'uiLoad',
              function($ocLazyLoad, uiLoad) {
                return $ocLazyLoad.load('angularBootstrapNavTree').then(function() {
                  return $ocLazyLoad.load('src/js/controllers/org/employee/employee.js');
                });
              }
            ]
          }
      }).state('app.employee.list', {
        url: '/list',
        templateUrl: 'src/tpl/org/employee/employee_list.html',
        resolve: {
            deps: ['$ocLazyLoad', 'uiLoad',
              function($ocLazyLoad, uiLoad) {
                return $ocLazyLoad.load('src/js/controllers/org/employee/employee_list.js').then(function() {
                  return uiLoad.load(JQ_CONFIG.dataTable);
                });
              }
            ]
          }
      }).state('app.employee.add', {
          url: '/add',
          templateUrl: 'src/tpl/org/employee/employee_add.html',
          resolve: {
            deps: ['$ocLazyLoad',
              function($ocLazyLoad) {
                return $ocLazyLoad.load('src/js/controllers/org/employee/employee_add.js');
              }
            ]
          }
        }).state('app.employee.edit', {
            url: '/edit',
            templateUrl: 'src/tpl/org/employee/employee_edit.html',
            resolve: {
              deps: ['$ocLazyLoad',
                function($ocLazyLoad) {
                  return $ocLazyLoad.load('src/js/controllers/org/employee/employee_edit.js');
                }
              ]
            }
        }).state('app.employee.details', {
            url: '/details',
            templateUrl: 'src/tpl/org/employee/employee_details.html',
            resolve: {
              deps: ['$ocLazyLoad','uiLoad',
                function($ocLazyLoad, uiLoad) {
                  return uiLoad.load(JQ_CONFIG.chosen).then(function() {
                    return $ocLazyLoad.load('src/js/controllers/org/employee/employee_details.js');
                  });
                }
              ]
            }
        }).state('app.position', {
            url: '/position',
            templateUrl: 'src/tpl/org/position/position.html',
            resolve: {
              deps: ['$ocLazyLoad', 'uiLoad',
                function($ocLazyLoad, uiLoad) {
                  return $ocLazyLoad.load('angularBootstrapNavTree').then(function() {
                    return $ocLazyLoad.load('src/js/controllers/org/position/position.js');
                  });
                }
              ]
           }
      }).state('app.position.list', {
        url: '/list',
        templateUrl: 'src/tpl/org/position/position_list.html',
        resolve: {
            deps: ['$ocLazyLoad', 'uiLoad',
              function($ocLazyLoad, uiLoad) {
                return $ocLazyLoad.load('src/js/controllers/org/position/position_list.js').then(function() {
                  return uiLoad.load(JQ_CONFIG.dataTable);
                });
              }
            ]
          }
      }).state('app.position.add', {
          url: '/add',
          templateUrl: 'src/tpl/org/position/position_add.html',
          resolve: {
            deps: ['$ocLazyLoad',
              function($ocLazyLoad) {
                return $ocLazyLoad.load('src/js/controllers/org/position/position_add.js');
              }
            ]
          }
        }).state('app.position.edit', {
          url: '/edit',
          templateUrl: 'src/tpl/org/position/position_edit.html',
          resolve: {
            deps: ['$ocLazyLoad',
              function($ocLazyLoad) {
                return $ocLazyLoad.load('src/js/controllers/org/position/position_edit.js');
              }
            ]
          }
        }).state('app.position.details', {
          url: '/details',
          templateUrl: 'src/tpl/org/position/position_details.html',
          resolve: {
            deps: ['$ocLazyLoad','uiLoad',
              function($ocLazyLoad, uiLoad) {
                return uiLoad.load(JQ_CONFIG.chosen).then(function() {
                  return $ocLazyLoad.load('src/js/controllers/org/position/position_details.js');
                });
              }
            ]
          }
      }).state('app.user', {
          url: '/user',
          templateUrl: 'src/tpl/org/user/user.html',
          resolve: {
              deps: ['$ocLazyLoad', 'uiLoad',
                function($ocLazyLoad, uiLoad) {
                  return $ocLazyLoad.load('angularBootstrapNavTree').then(function() {
                    return $ocLazyLoad.load('src/js/controllers/org/user/user.js');
                  });
                }
              ]
            }
        }).state('app.user.list', {
          url: '/list',
          templateUrl: 'src/tpl/org/user/user_list.html',
          resolve: {
              deps: ['$ocLazyLoad', 'uiLoad',
                function($ocLazyLoad, uiLoad) {
                  return $ocLazyLoad.load('src/js/controllers/org/user/user_list.js').then(function() {
                    return uiLoad.load(JQ_CONFIG.dataTable);
                  });
                }
              ]
            }
        }).state('app.user.add', {
            url: '/add',
            templateUrl: 'src/tpl/org/user/user_add.html',
            resolve: {
              deps: ['$ocLazyLoad',
                function($ocLazyLoad) {
                  return $ocLazyLoad.load('src/js/controllers/org/user/user_add.js');
                }
              ]
            }
          }).state('app.user.edit', {
              url: '/edit',
              templateUrl: 'src/tpl/org/user/user_edit.html',
              resolve: {
                deps: ['$ocLazyLoad',
                  function($ocLazyLoad) {
                    return $ocLazyLoad.load('src/js/controllers/org/user/user_edit.js');
                  }
                ]
              }
          }).state('app.user.details', {
            url: '/details',
            templateUrl: 'src/tpl/org/user/user_details.html',
            resolve: {
              deps: ['$ocLazyLoad','uiLoad',
                function($ocLazyLoad, uiLoad) {
                  return uiLoad.load(JQ_CONFIG.chosen).then(function() {
                    return $ocLazyLoad.load('src/js/controllers/org/user/user_details.js');
                  });
                }
              ]
            }
        }).state('app.deptCount.list', {
                      url: '/list',
                      templateUrl: 'src/tpl/oa/attence/count/deptCount/deptCount_list.html',
                      resolve: {
                          deps: ['$ocLazyLoad', 'uiLoad',
                            function($ocLazyLoad, uiLoad) {
                              return $ocLazyLoad.load('src/js/controllers/oa/attence/count/deptCount/deptCount_list.js').then(function() {
                                return uiLoad.load(JQ_CONFIG.dataTable);
                              });
                            }
                          ]
                        }
                  }).state('app.role', {
	            url: '/role',
	            templateUrl: 'src/tpl/org/role/role.html',
	            resolve: {
	              deps: ['$ocLazyLoad', 'uiLoad',
	                function($ocLazyLoad, uiLoad) {
	                  return $ocLazyLoad.load('angularBootstrapNavTree').then(function() {
	                    return $ocLazyLoad.load('src/js/controllers/org/role/role.js');
	                  });
	                }
	              ]
	            }
		      }).state('app.role.list', {
		        url: '/list',
		        templateUrl: 'src/tpl/org/role/role_list.html',
		        resolve: {
		          deps: ['$ocLazyLoad', 'uiLoad',
		            function($ocLazyLoad, uiLoad) {
		              return $ocLazyLoad.load('src/js/controllers/org/role/role_list.js').then(function() {
		                return uiLoad.load(JQ_CONFIG.dataTable);
		              });
		            }
		          ]
		        }
		      }).state('app.role.add', {
	            url: '/add',
	            templateUrl: 'src/tpl/org/role/role_add.html',
	            resolve: {
	              deps: ['$ocLazyLoad',
	                function($ocLazyLoad) {
	                  return $ocLazyLoad.load('src/js/controllers/org/role/role_add.js');
	                }
	              ]
	            }
	          }).state('app.role.edit', {
	              url: '/edit',
	              templateUrl: 'src/tpl/org/role/role_edit.html',
	              resolve: {
	                deps: ['$ocLazyLoad',
	                  function($ocLazyLoad) {
	                    return $ocLazyLoad.load('src/js/controllers/org/role/role_edit.js');
	                  }
	                ]
	              }
	          }).state('app.role.details', {
	            url: '/details',
	            templateUrl: 'src/tpl/org/role/role_details.html',
	            resolve: {
	              deps: ['$ocLazyLoad','uiLoad',
	                function($ocLazyLoad, uiLoad) {
	                  return uiLoad.load(JQ_CONFIG.chosen).then(function() {
	                    return $ocLazyLoad.load('src/js/controllers/org/role/role_details.js');
	                  });
	                }
	              ]
	            }
	        }).state('app.role.allot', {
	            url: '/allot',
	            templateUrl: 'src/tpl/org/role/role_allot.html',
	            resolve: {
	              deps: ['$ocLazyLoad','uiLoad',
	                function($ocLazyLoad, uiLoad) {
	                    return $ocLazyLoad.load('src/js/controllers/org/role/role_allot.js');
	                }
	              ]
	            }
	        }).state('app.role.distribution', {
	            url: '/distribution',
	            templateUrl: 'src/tpl/org/role/role_distribution.html',
	            resolve: {
	              deps: ['$ocLazyLoad','uiLoad',
	                function($ocLazyLoad, uiLoad) {
	            	  	return $ocLazyLoad.load('src/js/controllers/org/role/role_distribution.js');
	                }
	              ]
	            }
	        }).state('app.leaveapp', {
	            url: '/leaveapp',
	            templateUrl: 'src/tpl/oa/leave/leavelist.html',
	            resolve: {
	              deps: ['$ocLazyLoad','uiLoad',
	                function($ocLazyLoad, uiLoad) {
	                  return uiLoad.load(JQ_CONFIG.chosen).then(function() {
	                    return $ocLazyLoad.load('src/js/controllers/oa/leave/leaveControllers.js');
	                  });
	                }
	              ]
	            }
	        }).state('userf7', {
	            url: '/userlist',
	            templateUrl: 'src/mobile/f7/userF7.html',
	            resolve: {
	              deps: ['$ocLazyLoad', 'uiLoad',
	                function($ocLazyLoad, uiLoad) {
	                  return $ocLazyLoad.load('angularBootstrapNavTree').then(function() {
	                    return $ocLazyLoad.load('src/js/mobile/f7/userF7.js');
	                  });
	                }
	              ]
	            }
		      })
      // form
      .state('app.form', {
        url: '/form',
        template: '<div ui-view class="fade-in"></div>',
        resolve: {
          deps: ['uiLoad',
            function(uiLoad) {
              return uiLoad.load('src/js/controllers/form.js');
            }
          ]
        }
      }).state('app.form.editor', {
        url: '/editor',
        templateUrl: 'src/tpl/form_editor.html',
        controller: 'EditorCtrl',
        resolve: {
          deps: ['$ocLazyLoad',
            function($ocLazyLoad) {
              return $ocLazyLoad.load('textAngular').then(function() {
                return $ocLazyLoad.load('src/js/controllers/editor.js');
              });
            }
          ]
        }
      })
      // pages
      .state('app.page', {
        url: '/page',
        template: '<div ui-view class="fade-in-down"></div>'
      }).state('app.page.profile', {
        url: '/profile',
        templateUrl: 'src/tpl/temp/page_profile.html'
      }).state('app.docs', {
        url: '/docs',
        templateUrl: 'src/tpl/temp/docs.html'
      })
      // others
      .state('lockme', {
        url: '/lockme',
        templateUrl: 'src/tpl/lockme.html'
      }).state('access', {
        url: '/access',
        template: '<div ui-view class="fade-in-right-big smooth"></div>'
      }).state('access.signin', {
        url: '/signin',
        templateUrl: 'src/tpl/signin.html',
        resolve: {
          deps: ['uiLoad',
            function(uiLoad) {
              return uiLoad.load(['src/js/controllers/signin.js']);
            }
          ]
        }
      }).state('access.signup', {
        url: '/signup',
        templateUrl: 'src/tpl/signup.html',
        resolve: {
          deps: ['uiLoad',
            function(uiLoad) {
              return uiLoad.load(['src/js/controllers/signup.js']);
            }
          ]
        }
      }).state('access.forgotpwd', {
        url: '/forgotpwd',
        templateUrl: 'src/tpl/forgotpwd.html'
      }).state('access.404', {
        url: '/404',
        templateUrl: 'src/tpl/404.html'
      }).state('app.mail', {
        abstract: true,
        url: '/mail',
        templateUrl: 'src/tpl/mail.html',
        resolve: {
          deps: ['uiLoad',
            function(uiLoad) {
              return uiLoad.load(['src/js/app/mail/mail.js', 'src/js/app/mail/mail-service.js',
                JQ_CONFIG.moment
              ]);
            }
          ]
        }
      }).state('app.mail.list', {
        url: '/inbox/{fold}',
        templateUrl: 'src/tpl/mail.list.html'
      }).state('app.mail.detail', {
        url: '/{mailId:[0-9]{1,4}}',
        templateUrl: 'src/tpl/mail.detail.html'
      }).state('layout.mobile', {
        url: '/mobile',
        views: {
          '': {
            templateUrl: 'src/tpl/layout_mobile.html'
          },
          'footer': {
            templateUrl: 'src/tpl/layout_footer_mobile.html'
          }
        }
      }).state('app.todo.list', {
        url: '/{fold}'
      })
    }
  ]);