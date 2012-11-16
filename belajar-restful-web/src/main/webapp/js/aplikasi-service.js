/*
 * Copyright (C) 2011 ArtiVisi Intermedia <info@artivisi.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
angular.module('belajar.service', ['ngResource'])
    .factory('ApplicationConfigService', ['$resource', '$http', function($resource, $http){
        var service = {
            applicationConfig: $resource('/config/:configId'),
            get: function(param){ return this.applicationConfig.get(param) }, 
            query: function(){ return this.applicationConfig.query() },
            save: function(obj){
                if(obj.id == null){
                    return $http.post('/config', obj);
                } else {
                    return $http.put('/config/'+obj.id, obj);
                }
            }, 
            remove: function(obj){
                if(obj.id != null){
                    return $http.delete('/config/'+obj.id);
                }
            }
        };
            
        return service;
    }])
    .factory('ApplicationSessionsService', ['$http', function($http){
        var service = {
            list: function(){ 
                return $http.get('/homepage/sessioninfo');
            }, 
            kick: function(user){
                return $http.delete('/homepage/kick/'+user.sessionid);
            }
        };
            
        return service;
    }])
;