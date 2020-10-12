package com.example.services;

import com.example.Queries.ProfileServiceQueries;
import com.example.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProfileService {

    ProfileServiceQueries profileServiceQueries = new ProfileServiceQueries();

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private ProfileService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("connected");
    }

    // get profile info
    public ResponseEntity<ResponseData> profile(UserRequestModel userRequestModel) {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(profileServiceQueries.sql_get_profile_info, userRequestModel.getCustomer_account_number());
            return new ResponseEntity(new ResponseData(0, null, result.get(0)), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // update profile info
    public ResponseEntity<ResponseData> updateInfo(UpdateInfoModel updateInfoModel) {
        try {
            jdbcTemplate.update(profileServiceQueries.sql_update_info, updateInfoModel.getCustomer_name(), updateInfoModel.getCustomer_surname(), updateInfoModel.getCustomer_email(), updateInfoModel.getCustomer_account_number());
            return new ResponseEntity(new ResponseData(0, null, "ok"), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // update profile password
    public ResponseEntity<ResponseData> updatePassword(UpdatePasswordModel updatePasswordModel) {
        try {
            jdbcTemplate.update(profileServiceQueries.sql_update_password, updatePasswordModel.getCustomer_new_password(), updatePasswordModel.getCustomer_account_number(), updatePasswordModel.getCustomer_current_password());
            return new ResponseEntity(new ResponseData(0, null, "ok"), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // fill profile balance
    public ResponseEntity<ResponseData> fillBalance(BalanceFillModel balanceFillModel) {
        if (balanceFillModel.getConfirmation_code().equals(String.valueOf(369874))) {
            try {
                jdbcTemplate.update(profileServiceQueries.sql_update_balance, balanceFillModel.getMoney_amount(), balanceFillModel.getCustomer_account_number());
                return new ResponseEntity(new ResponseData(0, null, 369874), HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity(new ResponseData(1, "Confirmation number is invalid", null), HttpStatus.OK);
        }
    }
}
