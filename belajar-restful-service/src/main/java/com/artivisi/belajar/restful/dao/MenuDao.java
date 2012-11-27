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
package com.artivisi.belajar.restful.dao;

import com.artivisi.belajar.restful.domain.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface MenuDao extends PagingAndSortingRepository<Menu, String> {

    @Query("select m from Menu m " +
			"where m.parent is null " +
			"order by m.level, m.order")
    public List<Menu> findTopLevelMenu();

    @Query("select m from Menu m " +
			"where m.parent.id = :id " +
			"order by m.level, m.order")
    public List<Menu> findMenuByParent(@Param("id") String id);

    public List<Menu> findByIdNotIn(List<String> ids);

}
