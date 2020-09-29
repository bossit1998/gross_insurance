package com.example.rest;


import com.example.models.ResponseData;
import com.example.models.ReviewModel;
import com.example.models.SignInModel;
import com.example.models.SignUpModel;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/products")
    public Map<String, Object> getProducts() {
        return userService.getProducts();
    }

    @GetMapping("/checkdb")
    public List<Map<String, Object>> checkdb() {
        return userService.checkdb();
    }


    //reviews
    @GetMapping("/reviews")
    public ResponseData getReviews() {
        return userService.getReviews();
    }
    @PostMapping("/reviews")
    public ResponseData postReviews(@RequestBody ReviewModel reviewModel) {
        return userService.postReviews(reviewModel);
    }

    // sign up
    @PostMapping("/sign-up")
    public ResponseData signUp(@RequestBody SignUpModel signUpModel) {
        return userService.signUp(signUpModel);
    }

    // sign in
    @PostMapping("/sign-in")
    public ResponseData signIn(@RequestBody SignInModel signInModel) {
        return userService.signIn(signInModel);
    }

}
