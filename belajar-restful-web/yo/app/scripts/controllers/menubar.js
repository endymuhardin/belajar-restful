'use strict';

angular.module('yoApp')
  .controller('MenubarCtrl', function ($scope) {
    $scope.userinfo = {
      'user': 'endy',
      'group': 'admin'
    };
  });