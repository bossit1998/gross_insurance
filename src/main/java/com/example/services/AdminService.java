//package com.example.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class AdminService {
//
//    @Autowired
//    @Qualifier("jdbcMaster")
//    private JdbcTemplate jdbcTemplate;
//
//    public List<Map<String, Object>> index() {
//
//        return null;
//    }
//
//    public List<Map<String, Object>> users() {
//        List<Map<String, Object>> res = jdbcTemplate.queryForList("select * from clients");
//        return res;
//    }
//
//    public List<Map<String, Object>> placed_orders() {
//        return null;
//    }
//}
