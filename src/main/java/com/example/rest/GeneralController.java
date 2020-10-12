package com.example.rest;

import com.example.models.*;
import com.example.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GeneralController {

    @Autowired
    GeneralService generalService;

    //reviews
    @GetMapping("/reviews")
    public ResponseEntity<ResponseData> getReviews() {
        return generalService.getReviews();
    }
    @PostMapping("/reviews")
    public ResponseEntity<ResponseData> insertReviews(@RequestBody ReviewModel reviewModel) {
        return generalService.insertReviews(reviewModel);
    }

    // news
    @GetMapping("/news")
    public ResponseEntity<ResponseData> getNews() {
        return generalService.getNews();
    }

    // profile info
    @PostMapping("/profile")
    public ResponseEntity<ResponseData> transferApproved(@RequestBody UserRequestModel userRequestModel) {
        return generalService.profile(userRequestModel);
    }

    // edit profile info
    @PostMapping("/update-info")
    public ResponseEntity<ResponseData> updateInfo(@RequestBody UpdateInfoModel updateInfoModel) {
        return generalService.updateInfo(updateInfoModel);
    }

    // edit profile password
    @PostMapping("/update-password")
    public ResponseEntity<ResponseData> updatePassword(@RequestBody UpdatePasswordModel updatePasswordModel) {
        return generalService.updatePassword(updatePasswordModel);
    }
}
