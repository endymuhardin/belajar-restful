angular.module('belajar.controller',['belajar.service'])
    .controller('AboutController', ['$scope', function($scope){
        $scope.appName = "Aplikasi Belajar";
        $scope.appVersion = "Versi 1.0.0";
    }])
    .controller('ApplicationConfigController', ['$scope', 'ApplicationConfigService', function($scope, ApplicationConfigService){
        $scope.configs = ApplicationConfigService.findAll();
        $scope.edit = function(x){
            $scope.currentConfig = x;
        };
    }]);