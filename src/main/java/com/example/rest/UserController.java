package com.example.rest;


import com.example.models.*;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    // sign up
    @PostMapping("/sign-up-confirm-email")
    public ResponseEntity<ResponseData> signUp(@RequestBody SignUpEmailConfirmationModel signUpEmailConfirmationModel) throws MessagingException {
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
    @PostMapping("/my-bonds")
    public ResponseData getBonds(@RequestBody UserRequestModel userRequestModel) {
        return userService.getMyBonds(userRequestModel);
    }

    // make transfer - first request to get all information about the bond being sold
    @PostMapping("/make-transfer-first")
    public ResponseData makeTransferFirst(@RequestBody FirstTransferModel firstTransferModel) {
        return userService.makeTransferFirst(firstTransferModel);
    }

    // make transfer - second request to get full info of the buyer
    @PostMapping("/make-transfer-second")
    public ResponseData makeTransferSecond(@RequestBody UserRequestModel userRequestModel) {
        return userService.makeTransferSecond(userRequestModel);
    }

    // make buy/sell request - request for a buy/sell form
    @PostMapping("/buy-sell-request")
    public ResponseData buySellRequest(@RequestBody BuySellRequestModel buySellRequestModel) {
        return userService.buySellRequest(buySellRequestModel);
    }

    // approve the transfer by seller
    @PostMapping("/transfer-approved")
    public ResponseData transferApproved(@RequestBody TransferApprovedModel transferApprovedModel) {
        return userService.transferApproved(transferApprovedModel);
    }

}
