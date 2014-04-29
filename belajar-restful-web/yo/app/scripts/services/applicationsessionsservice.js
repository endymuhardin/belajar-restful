'use strict';

angular.module('yoApp')
  .factory('ApplicationSessionsService', function ($http) {
    return {
      list: function(){ 
            return $http.get('api/homepage/sessioninfo');
        }, 
      kick: function(user){
            return $http.delete('api/homepage/kick/'+user.sessionid);
        }
    };
  });
