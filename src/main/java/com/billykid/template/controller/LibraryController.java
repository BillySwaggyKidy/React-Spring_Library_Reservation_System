package com.billykid.template.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/")
public class LibraryController {

    // Mapping HTTP GET requests to the /hello endpoint to this method
    @GetMapping("/hello")
    public String hello() {
        // Returning the injected message value
        return "Hello, you are in dev mode";
    }
}
