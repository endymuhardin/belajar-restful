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
angular.module('belajar.controller',['belajar.service'])
    .controller('LoginRedirectorController', ['$window', function($window){
        $window.location = 'login.html';
    }])
    .controller('MenubarController', ['$http', '$scope', function($http, $scope){
        $scope.userinfo = {};
        $http.get('/homepage/userinfo').success(function(data){
            $scope.userinfo = data;
        });
    }])
    .controller('AboutController', ['$scope', function($scope){
        $scope.appName = "Aplikasi Belajar";
        $scope.appVersion = "Versi 1.0.0";
    }])
    .controller('ApplicationSessionsController', ['ApplicationSessionsService', '$scope', function(ApplicationSessionsService, $scope){
        $scope.refresh = function(){
            ApplicationSessionsService.list().success(function(data){
                $scope.sessioninfo = data
            });
        }
        
        $scope.refresh();
        
        $scope.kick = function(user){
            ApplicationSessionsService.kick(user).success($scope.refresh);
        };
        
    }])
    .controller('ApplicationConfigController', ['$scope', 'ApplicationConfigService', function($scope, ApplicationConfigService){
        $scope.configs = ApplicationConfigService.query();
        $scope.edit = function(x){
            if(x.id == null){
                return; 
            }
            $scope.currentConfig = ApplicationConfigService.get({
                configId: x.id
                });
        };
        $scope.baru = function(){
            $scope.currentConfig = null;
        }
        $scope.simpan = function(){
            ApplicationConfigService.save($scope.currentConfig)
            .success(function(){
                $scope.configs = ApplicationConfigService.query();
                $scope.currentConfig = null;
            });
        }
        $scope.remove = function(x){
            if(x.id == null){
                return;
            }
            ApplicationConfigService.remove(x).success(function(){
                $scope.configs = ApplicationConfigService.query();
            });
        }
    }])
    .controller('SystemMenuController', ['$scope', 'SystemMenuService', function($scope, SystemMenuService){
        $scope.menus = SystemMenuService.query();
        $scope.edit = function(x){
            if(x.id == null){
                return; 
            }
            $scope.currentMenu = SystemMenuService.get({
                id: x.id
                });
        };
        $scope.baru = function(){
            $scope.currentMenu = null;
        }
        $scope.simpan = function(){
            SystemMenuService.save($scope.currentMenu)
            .success(function(){
                $scope.menus = SystemMenuService.query();
                $scope.currentMenu = null;
            });
        }
        $scope.remove = function(x){
            if(x.id == null){
                return;
            }
            SystemMenuService.remove(x).success(function(){
                $scope.menus = SystemMenuService.query();
            });
        }
    }])
    .controller('PermissionController', ['$scope', 'PermissionService', function($scope, PermissionService){
        $scope.permissions = PermissionService.query();
        $scope.edit = function(x){
            if(x.id == null){
                return; 
            }
            $scope.currentPermission = PermissionService.get({
                id: x.id
                });
        };
        $scope.baru = function(){
            $scope.currentPermission = null;
        }
        $scope.simpan = function(){
            PermissionService.save($scope.currentPermission)
            .success(function(){
                $scope.permissions = PermissionService.query();
                $scope.currentPermission = null;
            });
        }
        $scope.remove = function(x){
            if(x.id == null){
                return;
            }
            PermissionService.remove(x).success(function(){
                $scope.permissions = PermissionService.query();
            });
        }
    }])
    .controller('RoleController', ['$scope', 'RoleService', function($scope, RoleService){
        $scope.roles = RoleService.query();
        
        $scope.unselectedPermission = [];
        $scope.unselectedMenu = [];
        
        $scope.selectedPermission = [];
        $scope.selectedMenu = [];
        
        $scope.edit = function(x){
            if(x.id == null){
                return; 
            }
            $scope.currentRole = RoleService.get({
                id: x.id
                });
            RoleService.unselectedPermission(x).success(function(data){
                $scope.unselectedPermission = data;
            });
            RoleService.unselectedMenu(x).success(function(data){
                $scope.unselectedMenu = data;
            });
        };
        $scope.baru = function(){
            $scope.currentRole = null;
        }
        $scope.simpan = function(){
            RoleService.save($scope.currentRole)
            .success(function(){
                $scope.roles = RoleService.query();
                $scope.currentRole = null;
            });
        }
        $scope.remove = function(x){
            if(x.id == null){
                return;
            }
            RoleService.remove(x).success(function(){
                $scope.roles = RoleService.query();
            });
        }
        
        $scope.selectAllPermission = function($event){
            if($event.target.checked){
                for ( var i = 0; i < $scope.unselectedPermission.length; i++) {
                    var p = $scope.unselectedPermission[i];
                    if($scope.selectedPermission.indexOf(p.id) < 0){
                        $scope.selectedPermission.push(p.id);
                    }
                }
            } else {
                $scope.selectedPermission = [];
            }
        }
        
        $scope.updateSelectedPermission = function($event, id){
            var checkbox = $event.target;
            if(checkbox.checked  && $scope.selectedPermission.indexOf(id) < 0){
                $scope.selectedPermission.push(id);
            } else {
                $scope.selectedPermission.splice($scope.selectedPermission.indexOf(id), 1);
            }
        }
        
        $scope.isPermissionSelected = function(id){
            return $scope.selectedPermission.indexOf(id) >= 0;
        }

        $scope.isAllPermissionSelected = function(){
            return $scope.unselectedPermission.length === $scope.selectedPermission.length;
        }
        
        $scope.saveSelectedPermission = function(){
            console.log($scope.selectedPermission);
            for ( var i = 0; i < $scope.selectedPermission.length; i++) {
                var p = {id: $scope.selectedPermission[i]};
                $scope.currentRole.permissionSet.push(p);
            }
            RoleService.save($scope.currentRole)
            .success(function(){
                $scope.currentRole = RoleService.get({
                    id: $scope.currentRole.id
                });;
            });
            $scope.showPermissionDialog = false;
        }
        
        $scope.cancelSelectedPermission = function(){
            $scope.selectedPermission = [];
            console.log($scope.selectedPermission);
            $scope.showPermissionDialog = false;
        }
        
        $scope.selectAllMenu = function($event){
            if($event.target.checked){
                for ( var i = 0; i < $scope.unselectedMenu.length; i++) {
                    var p = $scope.unselectedMenu[i];
                    if($scope.selectedMenu.indexOf(p.id) < 0){
                        $scope.selectedMenu.push(p.id);
                    }
                }
            } else {
                $scope.selectedMenu = [];
            }
        }
        
        $scope.updateSelectedMenu = function($event, id){
            var checkbox = $event.target;
            if(checkbox.checked  && $scope.selectedMenu.indexOf(id) < 0){
                $scope.selectedMenu.push(id);
            } else {
                $scope.selectedMenu.splice($scope.selectedMenu.indexOf(id), 1);
            }
        }
        
        $scope.isMenuSelected = function(id){
            return $scope.selectedMenu.indexOf(id) >= 0;
        }

        $scope.isAllMenuSelected = function(){
            return $scope.unselectedMenu.length === $scope.selectedMenu.length;
        }
        
        $scope.saveSelectedMenu = function(){
            console.log($scope.selectedMenu);
            $scope.showMenuDialog = false;
        }
        
        $scope.cancelSelectedMenu = function(){
            $scope.selectedMenu = [];
            console.log($scope.selectedMenu);
            $scope.showMenuDialog = false;
        }
    }])
    .controller('UserController', ['$scope', 'UserService', 'RoleService', function($scope, UserService, RoleService){
        $scope.users = UserService.query();
        $scope.roles = RoleService.query();
        $scope.edit = function(x){
            if(x.id == null){
                return; 
            }
            $scope.currentUser = UserService.get({
                id: x.id
                });
        };
        $scope.baru = function(){
            $scope.currentUser = null;
        }
        $scope.simpan = function(){
            if($scope.currentUser.active == null){
                $scope.currentUser.active = false;
            }
            UserService.save($scope.currentUser)
            .success(function(){
                $scope.users = UserService.query();
                $scope.currentUser = null;
            });
        }
        $scope.remove = function(x){
            if(x.id == null){
                return;
            }
            UserService.remove(x).success(function(){
                $scope.users = UserService.query();
            });
        }
    }])
;