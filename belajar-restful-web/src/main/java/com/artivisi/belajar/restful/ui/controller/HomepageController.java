/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artivisi.belajar.restful.ui.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
}
