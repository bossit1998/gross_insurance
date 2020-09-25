package com.example.rest;

import com.example.models.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.services.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api")
public class UserController {

    @GetMapping("products")
//    public Map<String, Object> getProducts() {
//        return UserService.get_products();
//    }
    public Map<String, Object> getProducts() {
        return UserService.get_products();
    }


    @GetMapping("checkdb")
    public Map<String, Object> checkdb() {
        return UserService.checkdb();
    }

//    @PostMapping("/delete/month")
//    public ResponseData deleteFromStartMonth(@RequestBody String baseModel) {
//        return service. delete_and_rewrite_service(baseModel, baseModel.getUserId());
//    }


}
