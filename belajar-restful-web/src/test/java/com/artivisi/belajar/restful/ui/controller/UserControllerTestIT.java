/**
 * Copyright (C) 2011 ArtiVisi Intermedia <info@artivisi.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.artivisi.belajar.restful.ui.controller;

import com.artivisi.belajar.restful.domain.User;
import com.artivisi.belajar.restful.domain.Role;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import com.jayway.restassured.authentication.FormAuthConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UserControllerTestIT {

    private String target = "http://localhost:10000/user";
    private String login = "http://localhost:10000/j_spring_security_check";
    private String username = "endy";
    private String password = "123";

    @Test
    public void testSaveInvalid(){
        User u = new User();
        
        given()
            .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
            .contentType("application/json")
            .body(u)
            .expect().statusCode(400).when().post(target);
    }
    
    @Test
    public void testSaveUpdateDelete() {

        Role r = new Role();
        r.setId("superuser");

        User x = new User();
        x.setFullname("User Percobaan");
        x.setUsername("test");
        x.setPassword("test123");
        x.setActive(true);
        x.setRole(r);

        String id = testSave(target, x);
        System.out.println("Id : " + id);
        testGetExistingById(id, x);
        
        x.setFullname("Ganti Label");
        x.setUsername("testganti");
        
        testUpdateExisting(id, x);
        testGetExistingById(id, x);
        testDeleteExistingById(id);
    }

    private String testSave(String target, User x) {
        String location = given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(x)
                .expect().statusCode(201).when().post(target)
                .getHeader("Location");

        assertNotNull(location);
        assertTrue(location.startsWith(target));

        String[] locationSplit = location.split("/");
        String id = locationSplit[locationSplit.length - 1];

        return id;
    }

    private void testGetExistingById(String id, User x) {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("username", equalTo(x.getUsername()), "fullname", equalTo(x.getFullname()))
                .when().get(target + "/" + id);
    }

    private void testUpdateExisting(String id, User x) {

        given()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .contentType("application/json")
                .body(x).expect()
                .statusCode(200).when().put(target + "/" + id);
    }

    private void testDeleteExistingById(String id) {
        given().auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(200).when().delete(target + "/" + id);

        given().auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(404).when().get(target + "/" + id);
    }

    @Test
    public void testGetExistingById() {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("id", equalTo("endy"),
                "username", equalTo("endy"),
                "fullname", equalTo("Endy Muhardin")).when()
                .get(target + "/" + "endy");
    }

    @Test
    public void testGetNonExistentById() {
        with()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect().statusCode(404).when().get(target + "/" + "/nonexistentconfig");
    }

    @Test
    public void testFindAll() {
        with()
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .header("Accept", "application/json").expect().statusCode(200)
                .body("id", hasItems("endy", "anton")).when().get(target);
    }

}
