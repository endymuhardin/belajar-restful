var modul = angular.module('belajar', [])
    .config(['$routeProvider', function($routeProvider){
        $routeProvider
            .when('/', {templateUrl: 'pages/home.html'})
            .when('/system/config', {templateUrl: 'pages/system/config.html'})
            .when('/about', {templateUrl: 'pages/about.html', controller: 'AboutController'})
            .otherwise({templateUrl: 'pages/404.html'});
    }]);

modul.controller('AboutController', function($scope){
    $scope.appName = "Aplikasi Belajar";
    $scope.appVersion = "Versi 1.0.0";
});
