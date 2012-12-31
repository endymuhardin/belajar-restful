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
package com.artivisi.belajar.restful.service.impl;

import com.artivisi.belajar.restful.domain.User;
import com.artivisi.belajar.restful.service.BelajarRestfulService;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:com/artivisi/**/applicationContext.xml")
public class UserServiceTestIT {

    @Autowired
    private BelajarRestfulService service;

    @Test
    public void testFindById() {
        User ac = service.findUserById("endy");
        assertNotNull(ac);
        assertEquals("endy", ac.getUsername());
        assertEquals("Endy Muhardin", ac.getFullname());
        assertEquals("123", ac.getPassword());
        assertEquals(Boolean.TRUE, ac.getActive());
        assertEquals("Super User", ac.getRole().getName());
        
        assertNull(service.findUserById(null));
        assertNull(service.findUserById(""));
    }

    @Test
    public void testFindAll() {
        Page<User> result = service.findAllUsers(new PageRequest(0, service.countAllUsers().intValue()));
        assertTrue(result.getTotalElements() > 0);
    }

    @Test
    public void testFindByUsername() {
        assertNotNull(service.findUserByUsername("endy"));
        assertNull(service.findUserByUsername("adi"));
    }
}
