package com.example.services;

import com.example.Queries.UserServiceQueries;
import com.example.models.*;
import com.example.rest.MailController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserServiceQueries userServiceQueries = new UserServiceQueries();

    private final JdbcTemplate jdbcTemplate;

    private final MailController mailController = new MailController();

    @Autowired
    GeneralService generalService;

    @Autowired
    private UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("connected");
    }

    // sign up - 1st part
    public ResponseEntity<ResponseData> signUpConfirmEmail(SignUpEmailConfirmationModel signUpEmailConfirmationModel) {
        String generated_code_for_customer = generalService.getRandomNumberString();

        ResponseData responseData = new ResponseData(1, "failed to initialize", null);

        String customers;

        try {
            customers = jdbcTemplate.queryForObject(userServiceQueries.sql_get_registered_users_count, new String[]{signUpEmailConfirmationModel.getCustomer_email()}, String.class);

            if (customers != null) {
                if (Integer.parseInt(customers) == 0) {
                    jdbcTemplate.update(userServiceQueries.sql_insert_email_verification_code, signUpEmailConfirmationModel.getCustomer_name(), signUpEmailConfirmationModel.getCustomer_surname(), signUpEmailConfirmationModel.getCustomer_email(), generated_code_for_customer);

                    System.out.println("Sending Email...");
                    mailController.twilio(signUpEmailConfirmationModel, generated_code_for_customer);
                    System.out.println("Done");

                    responseData.setStatus(0);
                    responseData.setError(null);
                    responseData.setData("success");
                } else {
                    responseData.setStatus(1);
                    responseData.setError("Email already exist");
                    responseData.setData(null);
                }
            }

            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
    }

    // sign up 2 part
    public ResponseEntity<ResponseData> signUpEmailConfirmed(SignUpEmailConfirmedModel signUpEmailConfirmedModel) {
        ResponseData responseData = new ResponseData(1, "Incorrect verification code", null);
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(userServiceQueries.sql_get_user_info_from_verification_code, signUpEmailConfirmedModel.getSecurity_key());
            if (result.size() > 0) {
                Date date = new Date();
                result.forEach(map -> {
                    Timestamp register_time = (Timestamp) map.get("customer_register_date");
                    String customer_mail = (String) map.get("customer_mail");
                    Timestamp now = new Timestamp(date.getTime());
                    if (now.getDate() - register_time.getDate() > 1) {
                        responseData.setStatus(1);
                        responseData.setError("Expired verification code");
                        responseData.setData(null);
                    } else {
                        String last_account_number = jdbcTemplate.queryForObject(userServiceQueries.sql_get_the_last_account_number, new Object[]{}, String.class);

                        assert last_account_number != null;
                        int next_account_number = Integer.parseInt(last_account_number) + 1;
                        String next_account_number_in_string = Integer.toString(next_account_number);
                        jdbcTemplate.update(userServiceQueries.sql_update_email_verified, signUpEmailConfirmedModel.getCustomer_password(), next_account_number_in_string, now, customer_mail);

                        responseData.setStatus(0);
                        responseData.setError(null);
                        responseData.setData("ok");
                    }
                });
            }
            return new ResponseEntity(responseData, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "error", "undefined"), HttpStatus.OK);
        }
    }

    // sign in
    public ResponseEntity<ResponseData> signIn(SignInModel signInModel) {
        String result;
        try {
            result = jdbcTemplate.queryForObject(userServiceQueries.sql_sign_in, new Object[]{signInModel.getCustomer_email(), signInModel.getCustomer_password()}, String.class);
            if (!result.isEmpty()) {
                HashMap<String, Object> response_object = new HashMap<>();
                response_object.put("message", "ok");
                response_object.put("mail", signInModel.getCustomer_email());
                response_object.put("token", "here will be token");

                UserRequestModel userRequestModel = new UserRequestModel();
                userRequestModel.setCustomer_account_number(result);
                return getMyBondsDashboard(userRequestModel);
            } else {
                return new ResponseEntity(new ResponseData(1, "Incorrect username or password", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // get the bonds of the user which will be shown in dashboard
    public ResponseEntity<ResponseData> getMyBondsDashboard(UserRequestModel userRequestModel) {
        List<Map<String, Object>> result;
        try {
            HashMap res = new HashMap();
            res.put("account_number", userRequestModel.getCustomer_account_number());

            result = jdbcTemplate.queryForList(userServiceQueries.sql_get_my_bonds, userRequestModel.getCustomer_account_number());
            if (result.size() > 0) {
                res.put("bonds", result);
                return new ResponseEntity(new ResponseData(0, null, res), HttpStatus.OK);
            } else {
                res.put("bonds", new String[0]);
                return new ResponseEntity(new ResponseData(0, null, res), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // get the bonds of the user which will be shown in popup
    public ResponseEntity<ResponseData> getMyBondsFull(BondInfoModel bondInfoModel) {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(userServiceQueries.sql_get_my_bonds_full, bondInfoModel.getBond_series(), bondInfoModel.getBond_number(), bondInfoModel.getCustomer_account_number());
            if (result.size() > 0) {
                return new ResponseEntity(new ResponseData(0, null, result.get(0)), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ResponseData(1, "No available bonds", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // get the bonds of the user which is being sold in dashboard
    public ResponseEntity<ResponseData> getMySellingBondsDashboard(UserRequestModel userRequestModel) {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(userServiceQueries.sql_get_my_selling_bonds, userRequestModel.getCustomer_account_number());
            if (result.size() > 0) {
                return new ResponseEntity(new ResponseData(0, null, result), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ResponseData(0, null, new String[0]), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // get the bonds of the user which is being sold in popup
    public ResponseEntity<ResponseData> getMySellingBondsFull(BondInfoModel bondInfoModel) {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(userServiceQueries.sql_get_my_selling_bonds_full, bondInfoModel.getBond_series(), bondInfoModel.getBond_number(), bondInfoModel.getCustomer_account_number());
            if (result.size() > 0) {
                return new ResponseEntity(new ResponseData(0, null, result.get(0)), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ResponseData(1, "No available bonds", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // get the bonds of the user which is being bought in dashboard
    public ResponseEntity<ResponseData> getMyBuyingBondsDashboard(UserRequestModel userRequestModel) {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(userServiceQueries.sql_get_my_buying_bonds, userRequestModel.getCustomer_account_number());
            if (result.size() > 0) {
                return new ResponseEntity(new ResponseData(0, null, result), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ResponseData(0, null, new String[0]), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // get the bonds of the user which is being bought in popup
    public ResponseEntity<ResponseData> getMyBuyingBondsFull(BondInfoModel bondInfoModel) {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(userServiceQueries.sql_get_my_buying_bonds_full, bondInfoModel.getBond_series(), bondInfoModel.getBond_number(), bondInfoModel.getCustomer_account_number());
            if (result.size() > 0) {
                return new ResponseEntity(new ResponseData(0, null, result.get(0)), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ResponseData(1, "No available bonds", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // get the bonds which are being sold in dashboard
    public ResponseEntity<ResponseData> getSellingBondsDashboard() {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(userServiceQueries.sql_get_selling_bonds);
            if (result.size() > 0) {
                return new ResponseEntity(new ResponseData(0, null, result), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ResponseData(1, "No available bonds", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // get the bonds which are being sold in popup
    public ResponseEntity<ResponseData> getSellingBondsFull(BondInfoModel bondInfoModel) {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(userServiceQueries.sql_get_selling_bonds_full, bondInfoModel.getBond_series(), bondInfoModel.getBond_number());
            if (result.size() > 0) {
                return new ResponseEntity(new ResponseData(0, null, result.get(0)), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ResponseData(1, "No available bonds", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }
}