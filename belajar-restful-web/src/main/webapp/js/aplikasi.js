/*
 * Copyright (C) 2011 ArtiVisi Intermedia <info@artivisi.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
angular.module('belajar', ['ngRoute', 'ui', 'belajar.controller'])
    .config(['$routeProvider', function($routeProvider){
        $routeProvider
            .when('/', {templateUrl: 'pages/home.html'})
            .when('/401', {templateUrl: 'pages/404.html', controller: 'LoginRedirectorController'})
            .when('/system/config', {templateUrl: 'pages/system/config.html', controller: 'ApplicationConfigController'})
            .when('/system/sessions', {templateUrl: 'pages/system/sessions.html', controller: 'ApplicationSessionsController'})
            .when('/system/user', {templateUrl: 'pages/system/user.html', controller: 'UserController'})
            .when('/system/role', {templateUrl: 'pages/system/role.html', controller: 'RoleController'})
            .when('/system/permission', {templateUrl: 'pages/system/permission.html', controller: 'PermissionController'})
            .when('/system/menu', {templateUrl: 'pages/system/menu.html', controller: 'SystemMenuController'})
            .when('/about', {templateUrl: 'pages/about.html', controller: 'AboutController'})
            .otherwise({templateUrl: 'pages/404.html'});
    }])
    .config(['$httpProvider', function($httpProvider){
        var sessionTimeoutInterceptor = ['$rootScope', '$location', '$q', function($rootScope, $location, $q){
            function success(response){
                return response;
            }
            
            function error(response){
                if (response.status === 401) {
                    $location.path('/401');
                }
            }
            
            return function(promise) {
                return promise.then(success, error);
            }
        }];
        
        
        $httpProvider.responseInterceptors.push(sessionTimeoutInterceptor);
        $httpProvider.responseInterceptors.push('httpLoadingSpinner');
        var spinnerFunction = function (data, headersGetter) {
            $('#loading').show();
            return data;
        };
        $httpProvider.defaults.transformRequest.push(spinnerFunction);
    }])
    .factory('httpLoadingSpinner', function ($q, $window) {
        return function (promise) {
            return promise.then(function (response) {
                // do something on success
                $('#loading').hide();
                return response;

            }, function (response) {
                // do something on error
                $('#loading').hide();
                return $q.reject(response);
            });
        };
    })
;
