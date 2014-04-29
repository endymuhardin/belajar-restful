'use strict';

angular.module('yoApp')
  .controller('MenubarCtrl', function ($scope, $http) {
    $scope.userinfo = {
      'user': 'endy',
      'group': 'admin'
    };
    $http.get('api/homepage/userinfo')
        .success(function(data){
            $scope.userinfo = data;
          })
        .error(function(){
            $scope.userinfo = {
                'user': 'unknown',
                'group': 'unknown'
              };
          });
  });