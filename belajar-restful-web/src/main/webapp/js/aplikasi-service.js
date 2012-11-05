angular.module('belajar.service', ['ngResource'])
    .factory('ApplicationConfigService', ['$resource', function($resource){
        return $resource('/config/:configId', {}, {
            findAll: {method: 'GET', isArray: true}
        });
    }]);