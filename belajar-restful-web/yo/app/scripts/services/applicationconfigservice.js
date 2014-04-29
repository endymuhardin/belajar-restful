'use strict';

angular.module('yoApp')
  .factory('ApplicationConfigService', function ($resource, $http) {

    // Public API here
    return {
      applicationConfig: $resource('api/config/:configId'),
      get: function (param, callback) {
        return this.applicationConfig.get(param, callback);
      },
      query: function () {
        return this.applicationConfig.query();
      },
      save: function (obj) {
        if (obj.id === null) {
          return $http.post('api/config', obj);
        } else {
          return $http.put('api/config/' + obj.id, obj);
        }
      },
      remove: function (obj) {
        if (obj.id !== null) {
          return $http.delete('api/config/' + obj.id);
        }
      }
    };
  });