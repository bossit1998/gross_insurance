package com.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.example.models.ResponseData;
import com.example.models.SignInModel;
import com.example.models.SignUpModel;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

//    @Autowired
//    @Qualifier("jdbcMaster")
//    private JdbcTemplate jdbcTemplate;


//    public List<Map<String, Object>> index() {
//        return null;
//    }

    public static Map<String, Object> get_products() {
        HashMap map = new HashMap();
        map.put("name","abdul");
        map.put("surname","aaaab");

        return map;
    }

//    public List<Map<String, Object>> sign_in(SignInModel signInModel) {
//        return null;
//    }

//    public ResponseEntity<?> sign_up(SignUpModel signUpModel) {
//       // if (jdbcTemplate.query("select count(*) from public.tashman where username = ? ", new String[]{signUpModel.getUsername()}))
//
//        try {
//            jdbcTemplate.update(queries.INSERT_USER, signUpModel.getName(),signUpModel.getSurName(),signUpModel.getEmail(),signUpModel.getUsername(),signUpModel.getPassword(),signUpModel.getPhoneNumber(),signUpModel.getRegion(),signUpModel.getCompanyName(),signUpModel.getPrivilege());
//
//            return ResponseEntity.ok(new ResponseData("User has been registered"));
//        }
//        catch (Exception e) {
//            ResponseData responseData = new ResponseData();
//            responseData.setStatus(1);
//            responseData.setMessage("Error! User already exists!");
//            responseData.setData(null);
////            responseData.setData(e.getMessage());
//            return ResponseEntity.badRequest().body(responseData);
//        }
//    }

//    public List<Map<String, Object>> place_order() {
//        return null;
//    }
}
