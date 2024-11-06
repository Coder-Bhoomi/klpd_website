package com.klpdapp.klpd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class maincontroller {


    @GetMapping({"/home"})
    public String ShowIndex() {
        return "index";
    }


}
