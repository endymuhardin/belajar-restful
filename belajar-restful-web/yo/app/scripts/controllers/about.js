'use strict';

angular.module('yoApp')
  .controller('AboutCtrl', function ($scope, $http) {
    $scope.appinfo = {};
    $http.get('api/homepage/appinfo').success(function(data){
        $scope.appinfo = data;
      });
  });
