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

import com.artivisi.belajar.restful.domain.Role;
import com.artivisi.belajar.restful.domain.User;
import com.jayway.restassured.authentication.FormAuthConfig;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HomepageControllerTestIT {

    private String target = "http://localhost:10000/homepage";
    private String login = "http://localhost:10000/j_spring_security_check";
    private String username = "endy";
    private String password = "123";


    @Test
    public void testGetAppinfo() {
        with().header("Accept", "application/json")
                .auth().form(username, password, new FormAuthConfig(login, "j_username", "j_password"))
                .expect()
                .statusCode(200)
                .body("profileDefault", equalTo("development"),
                    "profileActive", equalTo(""),
                    "namaAplikasi", equalTo("Aplikasi Belajar"),
                    "versiAplikasi", containsString("belajar-restful-")
                )
                .when()
                .get(target + "/" + "appinfo");
    }

}
