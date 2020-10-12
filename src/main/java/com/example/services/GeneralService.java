package com.example.services;

import com.example.Queries.GeneralServiceQueries;
import com.example.models.ResponseData;
import com.example.models.ReviewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class GeneralService {

    private GeneralServiceQueries generalServiceQueries = new GeneralServiceQueries();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private GeneralService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("connected");
    }

    // generate random 6 digit numbers for email
    public String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(899999) + 100000;

        // this will convert any number sequence into 6 character.
        String formatted_code = String.format("%06d", number);

        return formatted_code;
    }

    // get reviews
    public ResponseEntity<ResponseData> getReviews() {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(generalServiceQueries.sql_get_reviews);
            return new ResponseEntity(new ResponseData(0, null, result), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);

        }
    }

    // post reviews
    public ResponseEntity<ResponseData> insertReviews(ReviewModel reviewModel) {
        try {
            jdbcTemplate.update(generalServiceQueries.sql_post_reviews, reviewModel.getReviewer_nickname(), reviewModel.getReviewer_comment(), reviewModel.getReviewer_mail());
            return new ResponseEntity(new ResponseData(0, null, "ok"), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // get news
    public ResponseEntity<ResponseData> getNews() {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(generalServiceQueries.sql_get_news);
            return new ResponseEntity(new ResponseData(0, null, result.get(0)), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

}
