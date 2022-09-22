package com.twoauth.test.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SecuredResourceController {

    @GetMapping
    public String userResource() {
        return "Access granted";
    }
}
