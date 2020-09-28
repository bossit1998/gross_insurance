package com.example.rest;


import com.example.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping
public class UserController {

    @GetMapping("/products")
    public Map<String, Object> getProducts() {
        return UserService.getProducts();
    }

    @GetMapping("/checkdb")
    public List<Map<String, Object>> checkdb() {
        return UserService.checkdb();
    }
}
