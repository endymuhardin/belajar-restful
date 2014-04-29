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
      .otherwise({
        redirectTo: '/'
      });
  });
