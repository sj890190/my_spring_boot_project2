package com.systex.test.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class rootController {
    @GetMapping("/index")
    public String returnIndex(){
        return "index";
    }
}
