'use strict';

angular.module('yoApp')
    .controller('SystemConfigCtrl', function($scope, ApplicationConfigService) {
        $scope.configs = ApplicationConfigService.query();
        $scope.edit = function(x) {
            if (x.id == null) {
                return;
            }
            $scope.currentConfig = ApplicationConfigService.get({configId: x.id}, function(data) {
                $scope.original = angular.copy(data);
            });
        };
        $scope.baru = function() {
            $scope.currentConfig = null;
            $scope.original = null;
        }
        $scope.simpan = function() {
            ApplicationConfigService.save($scope.currentConfig)
                .success(function() {
                    $scope.configs = ApplicationConfigService.query();
                    $scope.baru();
                });
        }
        $scope.remove = function(x) {
            if (x.id == null) {
                return;
            }
            ApplicationConfigService.remove(x).success(function() {
                $scope.configs = ApplicationConfigService.query();
            });
        }
        $scope.isClean = function() {
            return angular.equals($scope.original, $scope.currentConfig);
        }
        $scope.isConfigNameAvailable = function(value) {
            if ($scope.currentConfig != null && $scope.currentConfig.id != null) {
                return true;
            }
            for (var i = 0; i < $scope.configs.length; i++) {
                var u = $scope.configs[i];
                if (u.name === value) {
                    return false;
                }
            }
            return true;
        }
    });
