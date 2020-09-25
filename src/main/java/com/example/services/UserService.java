package com.example.services;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.example.models.ResponseData;
import com.example.models.SignInModel;
import com.example.models.SignUpModel;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

//    @Value("${spring.datasource.url}")
//    private String dbUrl;
//
//    @Autowired
//    private static DataSource dataSource;
//
//    @Bean
//    public DataSource dataSource() throws SQLException {
//        if (dbUrl == null || dbUrl.isEmpty()) {
//            return new HikariDataSource();
//        } else {
//            HikariConfig config = new HikariConfig();
//            config.setJdbcUrl(dbUrl);
//            return new HikariDataSource(config);
//        }
//    }
//
////    @Autowired
////    @Qualifier("jdbcMaster")
////    private JdbcTemplate jdbcTemplate;
//
//
////    public List<Map<String, Object>> index() {
////        return null;
////    }
//
//    public static Map<String, Object> checkdb() {
//        try (Connection connection = dataSource.getConnection()) {
//            Statement stmt = connection.createStatement();
//
////      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
//            ResultSet rs = stmt.executeQuery("SELECT * FROM tashman.products");
//
////            ArrayList<String> output = new ArrayList<String>();
//
//            HashMap<String, Object> message = new HashMap<>();
//            ResponseData responseData = new ResponseData(message);
//
//            while (rs.next()) {
////                output.add("Read from DB: " + rs.getString("product_name"));
////                output.add("Read from DB2: " + rs.getString("image"));
//
//                message.put("product_name",rs.getString("product_name"));
//                message.put("image",rs.getString("image"));
//            }
//            return message;
//
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
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
