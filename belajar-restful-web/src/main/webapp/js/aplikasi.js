angular.module('belajar', [])
    .config(['$routeProvider', function($routeProvider){
        $routeProvider
            .when('/', {templateUrl: 'pages/home.html'})
            .when('/about', {templateUrl: 'pages/about.html', controller: AboutController})
            .otherwise({templateUrl: 'pages/404.html'});
    }]);

function AboutController($scope){
    $scope.appName = "Aplikasi Belajar";
    $scope.appVersion = "Versi 1.0.0";
}
