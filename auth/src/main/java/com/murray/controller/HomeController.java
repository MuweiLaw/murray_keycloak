package com.murray.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping("/")
    @CrossOrigin
    public String index() {
        return "index";
    }
    @CrossOrigin
    @RequestMapping("/customer")
    public String customer() {
        return "only customer can see";
    }

    @RequestMapping("/admin")
    @CrossOrigin
    public String admin() {
        return "only admin cas see";
    }
}
