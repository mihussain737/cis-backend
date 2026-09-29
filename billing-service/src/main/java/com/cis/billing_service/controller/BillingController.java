package com.cis.billing_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @GetMapping("/test")
        public String test(){
            return"testing my api with no auth";
    }

    @GetMapping("/test2")
        public String test2(){
            return"testing my api with no auth";
    }
}
