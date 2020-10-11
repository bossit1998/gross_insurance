package com.example.services;

import com.example.models.ResponseData;
import com.example.models.ReviewModel;
import com.example.models.UserRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeneralService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private GeneralService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("connected");
    }

    public List<Map<String, Object>> checkdb() {
        return jdbcTemplate.queryForList("SELECT * FROM gross.customers");
    }

    public Map<String, Object> getProducts() {
        HashMap map = new HashMap();
        map.put("name", "abdul");
        map.put("surname", "aaaab");
        return map;
    }

    // get reviews
    public ResponseEntity<ResponseData> getReviews() {
        String sql_get_reviews = "Select review_id, reviewer_nickname, reviewer_comment from gross.reviews";

        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(sql_get_reviews);
            return new ResponseEntity(new ResponseData(0,null,result), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1,"Can't connect to database",null), HttpStatus.OK);

        }
    }

    // post reviews
    public ResponseEntity<ResponseData> insertReviews(ReviewModel reviewModel) {
        String sql_post_reviews = "INSERT INTO gross.reviews (review_id, reviewer_nickname, reviewer_comment, review_date, reviewer_mail) " +
                "VALUES (DEFAULT, ?, ?, DEFAULT, ?)";

        int result;
        try {
            result = jdbcTemplate.update(sql_post_reviews,reviewModel.getReviewer_nickname(),reviewModel.getReviewer_comment(),reviewModel.getReviewer_mail());
            return new ResponseEntity(new ResponseData(0,null,"ok"), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseEntity(new ResponseData(1,"Can't connect to database",null), HttpStatus.OK);
        }
    }

    // get news
    public ResponseEntity<ResponseData> getNews() {
        String sql_get_reviews = "Select * from gross.news";

        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(sql_get_reviews);
            return new ResponseEntity(new ResponseData(0,null, result.get(0)), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseEntity(new ResponseData(1,"Can't connect to database",null), HttpStatus.OK);
        }
    }

    public ResponseEntity<ResponseData> profile(UserRequestModel userRequestModel) {
        String sql_get_reviews = "select customer_name, customer_surname, customer_account_number, customer_balance, customer_balance, customer_phone_number,customer_mail " +
                "        from gross.customers " +
                "        where customer_account_number = ?";

        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(sql_get_reviews, userRequestModel.getCustomer_account_number());
            return new ResponseEntity(new ResponseData(0,null, result.get(0)), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseEntity(new ResponseData(1,"Can't connect to database",null), HttpStatus.OK);
        }
    }
}
