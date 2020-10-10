package com.example.rest;

import com.example.models.ResponseData;
import com.example.models.ReviewModel;
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


    @GetMapping("/products")
    public Map<String, Object> getProducts() {
        return generalService.getProducts();
    }

    @GetMapping("/checkdb")
    public List<Map<String, Object>> checkdb() {
        return generalService.checkdb();
    }

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
    public ResponseData getNews() {
        return generalService.getNews();
    }
}
