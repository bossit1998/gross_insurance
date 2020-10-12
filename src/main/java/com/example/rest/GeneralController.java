package com.example.rest;

import com.example.models.*;
import com.example.services.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
