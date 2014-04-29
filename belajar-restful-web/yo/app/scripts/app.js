'use strict';

angular
  .module('yoApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/system/config', {
        templateUrl: 'views/system/config.html',
        controller: 'SystemConfigCtrl'
      })
      .when('/system/user', {
        templateUrl: 'views/system/user.html',
        controller: 'SystemUserCtrl'
      })
      .when('/system/role', {
        templateUrl: 'views/system/role.html',
        controller: 'SystemRoleCtrl'
      })
      .when('/system/permission', {
        templateUrl: 'views/system/permission.html',
        controller: 'SystemPermissionCtrl'
      })
      .when('/system/menu', {
        templateUrl: 'views/system/menu.html',
        controller: 'SystemMenuCtrl'
      })
      .when('/system/sessions', {
        templateUrl: 'views/system/sessions.html',
        controller: 'SystemSessionsCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
