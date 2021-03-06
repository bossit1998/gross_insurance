package com.example.rest;


import com.example.models.*;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    // sign up
    @PostMapping("/sign-up-confirm-email")
    public ResponseEntity<ResponseData> signUp(@RequestBody SignUpEmailConfirmationModel signUpEmailConfirmationModel) {
        return userService.signUpConfirmEmail(signUpEmailConfirmationModel);
    }

    @PostMapping("/sign-up-email-confirmed")
    public ResponseEntity<ResponseData> signUp(@RequestBody SignUpEmailConfirmedModel signUpEmailConfirmedModel) {
        return userService.signUpEmailConfirmed(signUpEmailConfirmedModel);
    }

    // sign in
    @PostMapping("/sign-in")
    public ResponseEntity<ResponseData> signIn(@RequestBody SignInModel signInModel) {
        return userService.signIn(signInModel);
    }

    // get the bonds of the user which will be shown in dashboard
    @PostMapping("/my-bonds-dash")
    public ResponseEntity<ResponseData> getMyBondsDashboard(@RequestBody UserRequestModel userRequestModel) {
        return userService.getMyBondsDashboard(userRequestModel);
    }

    // get the bonds of the user which will be shown in full
    @PostMapping("/my-bonds-info")
    public ResponseEntity<ResponseData> getMyBondsFull(@RequestBody BondInfoModel bondInfoModel) {
        return userService.getMyBondsFull(bondInfoModel);
    }

    // get the bonds of the user which is being sold in dashboard
    @PostMapping("/my-selling-bonds-dash")
    public ResponseEntity<ResponseData> getMySellingBondsDashboard(@RequestBody UserRequestModel userRequestModel) {
        return userService.getMySellingBondsDashboard(userRequestModel);
    }
// dadd
    // get the bonds of the user which is being sold in full
    @PostMapping("/my-selling-bonds-info")
    public ResponseEntity<ResponseData> getMySellingBondsFull(@RequestBody BondInfoModel bondInfoModel) {
        return userService.getMySellingBondsFull(bondInfoModel);
    }

    // get the bonds of the user which is being bought in dashboard
    @PostMapping("/my-buying-bonds-dash")
    public ResponseEntity<ResponseData> getMyBuyingBondsDashboard(@RequestBody UserRequestModel userRequestModel) {
        return userService.getMyBuyingBondsDashboard(userRequestModel);
    }

    // get the bonds of the user which is being bought in full
    @PostMapping("/my-buying-bonds-info")
    public ResponseEntity<ResponseData> getMyBuyingBondsFull(@RequestBody BondInfoModel bondInfoModel) {
        return userService.getMyBuyingBondsFull(bondInfoModel);
    }

    // get the bonds which being sold in dashboard
    @GetMapping("/selling-bonds-dash")
    public ResponseEntity<ResponseData> getSellingBondsDashboard() {
        return userService.getSellingBondsDashboard();
    }

    // get the bonds which being sold in full
    @PostMapping("/selling-bonds-info")
    public ResponseEntity<ResponseData> getSellingBondsFull(@RequestBody BondInfoModel bondInfoModel) {
        return userService.getSellingBondsFull(bondInfoModel);
    }

}
