'use strict';

angular.module('yoApp')
  .controller('SystemSessionsCtrl', function ($scope, ApplicationSessionsService) {
    $scope.refresh = function(){
            ApplicationSessionsService.list().success(function(data){
                $scope.sessioninfo = data
            });
        }
        
        $scope.refresh();
        
        $scope.kick = function(user){
            ApplicationSessionsService.kick(user).success($scope.refresh);
        };
  });
