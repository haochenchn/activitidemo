package com.aaron.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Aaron
 * @description TODO
 * @date 2019/8/6
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    @RequestMapping(value = "/toHome")
    public String toHome(){
        return "/home";
    }

}
