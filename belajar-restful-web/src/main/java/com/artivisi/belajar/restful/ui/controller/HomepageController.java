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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artivisi.belajar.restful.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.artivisi.belajar.restful.service.BelajarRestfulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author endy
 */
@Controller
public class HomepageController {

    @Autowired private BelajarRestfulService belajarRestfulService;
    @Autowired private SessionRegistry sessionRegistry;
    @Autowired private MessageSource messageSource;
    
    @RequestMapping("/homepage/userinfo")
    @ResponseBody
    public Map<String, String> userInfo(){
        Map<String, String> hasil = new HashMap<String, String>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            Object principal = auth.getPrincipal();
            if(principal != null && User.class.isAssignableFrom(principal.getClass())){
                User u = (User) principal;
                hasil.put("user", u.getUsername());
                com.artivisi.belajar.restful.domain.User ux = belajarRestfulService.findUserByUsername(u.getUsername());
                if(ux != null || ux.getRole() != null || ux.getRole().getName() != null) {
                    hasil.put("group", ux.getRole().getName());
                } else {
                    hasil.put("group", "undefined");
                }
            }
        }
        
        return hasil;
    }

    @RequestMapping("/homepage/appinfo")
    @ResponseBody
    public Map<String, String> appInfo(HttpServletRequest request){

        ApplicationContext ctx = WebApplicationContextUtils
                .getWebApplicationContext(request.getSession().getServletContext());

        Map<String, String> hasil = new HashMap<String, String>();

        hasil.put("profileDefault", StringUtils.arrayToCommaDelimitedString(ctx.getEnvironment().getDefaultProfiles()));
        hasil.put("profileActive", StringUtils.arrayToCommaDelimitedString(ctx.getEnvironment().getActiveProfiles()));
        hasil.put("namaAplikasi", messageSource.getMessage("app.name", null, "undefined", null));
        hasil.put("versiAplikasi", messageSource.getMessage("app.version", null, "x.x.x", null));

        return hasil;
    }
    
    @RequestMapping("/homepage/sessioninfo")
    @ResponseBody
    public List<Map<String,String>> sessionInfo(){
        
        List<Map<String, String>> userAktif = new ArrayList<Map<String,String>>();
        
        for (Object object : sessionRegistry.getAllPrincipals()) {
            List<SessionInformation> info = sessionRegistry.getAllSessions(object, true);
            for (SessionInformation i : info) {
                Object p = i.getPrincipal();
                if(p != null && User.class.isAssignableFrom(p.getClass())){
                    Map<String, String> usermap = new HashMap<String, String>();
                    
                    User u = (User) p;
                    usermap.put("username", u.getUsername());
                    usermap.put("permission", u.getAuthorities().toString());
                    usermap.put("sessionid", i.getSessionId());
                    usermap.put("status", i.isExpired() ? "Expired" : "Aktif");
                    userAktif.add(usermap);
                }
            }
        }
        
        
        return userAktif;
    }
    
    @RequestMapping(value="/homepage/kick/{sessionid}", method= RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void forceLogout(@PathVariable String sessionid){
        SessionInformation info = sessionRegistry.getSessionInformation(sessionid);
        if(info != null){
            info.expireNow();
        }
    }
}
