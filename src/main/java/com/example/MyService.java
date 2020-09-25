package com.example;

import com.example.models.ResponseData;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;

    @Bean
    public DataSource dataSource() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            return new HikariDataSource();
        } else {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            return new HikariDataSource(config);
        }
    }


    public ResponseData dbfunc (){
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();

//      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            ResultSet rs = stmt.executeQuery("SELECT * FROM tashman.products");

//            ArrayList<String> output = new ArrayList<String>();

            HashMap<String, Object> message = new HashMap<>();
            ResponseData responseData = new ResponseData(message);

            while (rs.next()) {
//                output.add("Read from DB: " + rs.getString("product_name"));
//                output.add("Read from DB2: " + rs.getString("image"));

                message.put("product_name",rs.getString("product_name"));
                message.put("image",rs.getString("image"));
            }


//
//            message.put("product_name", rs.getString("product_name"));
//            message.put("image", rs.getString("image"));



//            model.put("records", output);
//            return "db";


            return responseData;
        } catch (Exception e) {
            HashMap<String, Object> message = new HashMap<>();
            message.put("message", e.getMessage());
            return new ResponseData(message);
        }
    }
}
