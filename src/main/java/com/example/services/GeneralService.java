package com.example.services;

import com.example.models.ResponseData;
import com.example.models.ReviewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public ResponseData getReviews() {
        String sql_get_reviews = "Select review_id, reviewer_account_number, reviewer_comment from gross.reviews";

        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(sql_get_reviews);
            return new ResponseData(0,"undefined",result);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseData(1,"error","undefined");
        }
    }

    // post reviews
    public ResponseData insertReviews(ReviewModel reviewModel) {
        String sql_post_reviews = "INSERT INTO gross.reviews (review_id, reviewer_account_number, reviewer_comment, review_date, reviewer_mail) " +
                "VALUES (DEFAULT, ?, ?, DEFAULT, ?)";

        int result;
        try {
            result = jdbcTemplate.update(sql_post_reviews,reviewModel.getReviewer_username(),reviewModel.getReviewer_comment(),reviewModel.getReviewer_mail());
            return new ResponseData(0,"undefined","ok");
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseData(1,"error","undefined");
        }
    }

    public ResponseData getNews() {
        String sql_get_reviews = "Select * from gross.news";

        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(sql_get_reviews);
            return new ResponseData(0,"undefined",result);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseData(1,"error","undefined");
        }
    }
}
