package com.example.rest;

import com.example.models.*;
import com.example.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    @Autowired
    ProfileService profileService;

    // profile info
    @PostMapping("/profile")
    public ResponseEntity<ResponseData> transferApproved(@RequestBody UserRequestModel userRequestModel) {
        return profileService.profile(userRequestModel);
    }

    // edit profile info
    @PostMapping("/update-info")
    public ResponseEntity<ResponseData> updateInfo(@RequestBody UpdateInfoModel updateInfoModel) {
        return profileService.updateInfo(updateInfoModel);
    }

    // edit profile password
    @PostMapping("/update-password")
    public ResponseEntity<ResponseData> updatePassword(@RequestBody UpdatePasswordModel updatePasswordModel) {
        return profileService.updatePassword(updatePasswordModel);
    }

    // fill the balance
    @PostMapping("/fill-balance")
    public ResponseEntity<ResponseData> fillBalance(@RequestBody BalanceFillModel balanceFillModel) {
        return profileService.fillBalance(balanceFillModel);
    }

}
