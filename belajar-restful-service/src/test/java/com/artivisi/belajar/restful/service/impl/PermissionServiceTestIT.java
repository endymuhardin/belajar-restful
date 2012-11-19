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

import com.artivisi.belajar.restful.domain.Permission;
import com.artivisi.belajar.restful.service.BelajarRestfulService;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:com/artivisi/**/applicationContext.xml")
public class PermissionServiceTestIT {

    @Autowired
    private BelajarRestfulService service;

    @Test
    public void testFindById() {
        Permission ac = service.findPermissionById("user-edit");
        assertNotNull(ac);
        assertEquals("Edit User", ac.getLabel());
        assertEquals("ROLE_USER_EDIT", ac.getValue());
        assertNull(service.findPermissionById(null));
        assertNull(service.findPermissionById(""));
    }

    @Test
    public void testFindAll() {
        List<Permission> result = service.findAllPermissions(0, service.countAllPermissions().intValue());
        assertTrue(result.size() > 0);
    }
}
