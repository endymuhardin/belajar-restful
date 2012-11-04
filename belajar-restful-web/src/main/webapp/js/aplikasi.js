var modul = angular.module('belajar', ['ngResource'])
    .config(['$routeProvider', function($routeProvider){
        $routeProvider
            .when('/', {templateUrl: 'pages/home.html'})
            .when('/system/config', {templateUrl: 'pages/system/config.html', controller: 'ApplicationConfigController'})
            .when('/about', {templateUrl: 'pages/about.html', controller: 'AboutController'})
            .otherwise({templateUrl: 'pages/404.html'});
    }]);

modul.factory('ApplicationConfigService', function($resource){
    return $resource('/config/#', {}, {
        findAll: {method: 'GET', isArray: true}
    });
});

modul.controller('AboutController', function($scope){
    $scope.appName = "Aplikasi Belajar";
    $scope.appVersion = "Versi 1.0.0";
});

modul.controller('ApplicationConfigController', function($scope, ApplicationConfigService){
    $scope.configs = ApplicationConfigService.findAll();
    
    $scope.edit = function(x){
        $scope.currentConfig = x;
    };
    
});

