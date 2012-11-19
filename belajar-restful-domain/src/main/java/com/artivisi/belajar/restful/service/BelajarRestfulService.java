/**
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
package com.artivisi.belajar.restful.service;

import java.util.List;

import com.artivisi.belajar.restful.domain.ApplicationConfig;
import com.artivisi.belajar.restful.domain.Menu;
import com.artivisi.belajar.restful.domain.Permission;
import com.artivisi.belajar.restful.domain.Role;
import com.artivisi.belajar.restful.domain.User;

public interface BelajarRestfulService extends MonitoredService {
        // konfigurasi aplikasi
	void save(ApplicationConfig ac);
	void delete(ApplicationConfig ac);
	ApplicationConfig findApplicationConfigById(String id);
	List<ApplicationConfig> findAllApplicationConfigs(Integer page, Integer rows);
	Long countAllApplicationConfigs();
	Long countApplicationConfigs(String search);
	List<ApplicationConfig> findApplicationConfigs(String search, Integer page, Integer rows);
        
        // menu
        void save(Menu m);
        void delete(Menu m);
        Menu findMenuById(String id);
        List<Menu> findTopLevelMenu();
        List<Menu> findMenuByParent(Menu m);
        
        // permission
        void save(Permission m);
        void delete(Permission m);
        Permission findPermissionById(String id);
        List<Permission> findAllPermissions(Integer page, Integer rows);
        Long countAllPermissions();
        
        // role
        void save(Role role);
        void delete(Role role);
        Role findRoleById(String id);
        List<Role> findAllRoles(Integer page, Integer rows);
        Long countAllRoles();
        
        // permission
        void save(User m);
        void delete(User m);
        User findUserById(String id);
        List<User> findAllUsers(Integer page, Integer rows);
        Long countAllUsers();
}
