package com.example.services;

import com.example.models.ResponseData;
import com.example.models.ReviewModel;
import com.example.models.SignInModel;
import com.example.models.SignUpModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("connected");
    }

    public static List<Map<String, Object>> checkdb() {
        return jdbcTemplate.queryForList("SELECT * FROM gross.customers");
//        return jdbcTemplate.queryForList("select * from information_schema.tables");

//        return null;
    }

    public static Map<String, Object> getProducts() {
        HashMap map = new HashMap();
        map.put("name", "abdul");
        map.put("surname", "aaaab");

        return map;
    }




    // get reviews
    public ResponseData getReviews() {
        String sql_get_reviews = "Select * from gross.reviews";

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
    public ResponseData postReviews(ReviewModel reviewModel) {
        String sql_post_reviews = "INSERT INTO gross.reviews (review_id, reviewer_username, reviewer_comment, review_date, reviewer_mail) VALUES (DEFAULT, ?, ?, DEFAULT, ?)";

        int result;
        try {
            result = jdbcTemplate.update(sql_post_reviews,reviewModel.getReviewer_username(),reviewModel.getReviewer_comment(),reviewModel.getReviewer_mail());
            return new ResponseData(0,"undefined",result);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseData(1,"error","undefined");
        }
    }

    // sign up
    public ResponseData signUp(SignUpModel signUpModel) {
        String sql_sign_up = "INSERT INTO gross.customers (customer_id, customer_name, customer_username, customer_password, customer_account_number, customer_balance_number, customer_balance, customer_phone_number, customer_mail, customer_privilege, customer_register_date) " +
            "VALUES (DEFAULT, ?, ?, ?, DEFAULT, DEFAULT, DEFAULT, ?, ?, DEFAULT, DEFAULT)";

        int result;
        try {
            result = jdbcTemplate.update(sql_sign_up,signUpModel.getName() +" "+ signUpModel.getSurName(),signUpModel.getUserName(),signUpModel.getPassword(),signUpModel.getPhoneNumber(),signUpModel.getEmail());
            return new ResponseData(0,"undefined",result);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseData(1,"error","undefined");
        }
    }

    // sign in
    public ResponseData signIn(SignInModel signInModel) {
        String sql_sign_in = "select count(*) from gross.customers where customer_mail=? and customer_password=?";

        String result;
        try {
            result = jdbcTemplate.queryForObject(sql_sign_in,new Object[]{signInModel.getEmail(),signInModel.getPassword()},String.class);
            if (Integer.valueOf(result) > 0) {
                return new ResponseData(0,"undefined",result);
            }
            else {
                return new ResponseData(1,"not exist","undefined");
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseData(1,"error","undefined");
        }
    }
}