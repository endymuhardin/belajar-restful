/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artivisi.belajar.restful.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author endy
 */
@Controller
public class HomepageController {
    
    @Autowired private SessionRegistry sessionRegistry;
    
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
                hasil.put("group", "superuser");
            }
        }
        
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
}
