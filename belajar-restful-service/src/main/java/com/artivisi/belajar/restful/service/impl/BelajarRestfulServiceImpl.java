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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.artivisi.belajar.restful.dao.ApplicationConfigDao;
import com.artivisi.belajar.restful.dao.MenuDao;
import com.artivisi.belajar.restful.domain.ApplicationConfig;
import com.artivisi.belajar.restful.domain.Menu;
import com.artivisi.belajar.restful.service.BelajarRestfulService;
import java.util.ArrayList;

@Service("belajarRestfulService")
@Transactional
public class BelajarRestfulServiceImpl implements BelajarRestfulService {

    @Autowired
    private ApplicationConfigDao applicationConfigDao;
    @Autowired
    private MenuDao menuDao;

    @Override
    public void save(ApplicationConfig ac) {
        applicationConfigDao.save(ac);
    }

    @Override
    public void delete(ApplicationConfig ac) {
        if (ac == null || ac.getId() == null) {
            return;
        }
        applicationConfigDao.delete(ac);
    }

    @Override
    public ApplicationConfig findApplicationConfigById(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }
        return applicationConfigDao.findOne(id);
    }

    @Override
    public List<ApplicationConfig> findAllApplicationConfigs(Integer page, Integer rows) {
        if (page == null || page < 0) {
            page = 0;
        }

        if (rows == null || rows < 1) {
            rows = 20;
        }

        return applicationConfigDao.findAll(new PageRequest(page, rows)).getContent();
    }

    @Override
    public Long countAllApplicationConfigs() {
        return applicationConfigDao.countAll();
    }

    @Override
    public List<ApplicationConfig> findApplicationConfigs(String search, Integer page, Integer rows) {
        if (page == null || page < 0) {
            page = 0;
        }

        if (rows == null || rows < 1) {
            rows = 20;
        }

        if (!StringUtils.hasText(search)) {
            return findAllApplicationConfigs(page, rows);
        }

        return applicationConfigDao.search("%" + search + "%", new PageRequest(page, rows));
    }

    @Override
    public Long countApplicationConfigs(String search) {
        if (!StringUtils.hasText(search)) {
            return countAllApplicationConfigs();
        }
        return applicationConfigDao.count("%" + search + "%");
    }

    @Override
    public void save(Menu m) {
        menuDao.save(m);
    }

    @Override
    public void delete(Menu m) {
        menuDao.delete(m);
    }

    @Override
    public Menu findMenuById(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }
        return menuDao.findOne(id);
    }

    @Override
    public List<Menu> findTopLevelMenu() {
        return menuDao.findTopLevelMenu();
    }

    @Override
    public List<Menu> findMenuByParent(Menu m) {
        if(m == null || !StringUtils.hasText(m.getId())) {
            return new ArrayList<Menu>();
        }
        return menuDao.findMenuByParent(m.getId());
    }
}
