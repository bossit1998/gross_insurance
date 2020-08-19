//package com.example.rest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import uz.tm.tashman.services.AdminService;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/tm/admin")
//public class AdminController {
//
//    @Autowired
//    private AdminService adminService;
//
//    @GetMapping("/index")
//    public List<Map<String,Object>> index() {
//        return adminService.index();
//    }
//
//    @GetMapping("/get-users")
//    public List<Map<String, Object>> users() {
//        return adminService.users();
//    }
//
//    @GetMapping("/get-placed_orders")
//    public List<Map<String, Object>> placedOrders() {
//        return adminService.placed_orders();
//    }
//}
