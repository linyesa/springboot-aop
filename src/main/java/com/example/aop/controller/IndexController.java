package com.example.aop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @GetMapping("/hello")
    @ResponseBody
    public String Helle(){
        return "hello";
    }
    @GetMapping("hello1")
    @ResponseBody
    public String Helle1(){
        return "print hello1";
    }
}
