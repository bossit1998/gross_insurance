package com.example.rest;


import com.example.models.*;
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
    public ResponseData insertReviews(@RequestBody ReviewModel reviewModel) {
        return userService.insertReviews(reviewModel);
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

    // get my bonds
    @PostMapping("/my-bonds")
    public ResponseData getBonds(@RequestBody UserRequestModel userRequestModel) {
        return userService.getMyBonds(userRequestModel);
    }

    // make transaction
    @PostMapping("/make-transaction")
    public ResponseData makeTransfer(@RequestBody TransferModel transferModel) {
        return userService.makeTransfer(transferModel);
    }

}
