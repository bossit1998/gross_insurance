package com.example.services;

import com.example.models.*;
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

//    @Autowired
//    MailServices mailServices;

    @Autowired
    SendGridService sendGridService;

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("connected");
    }

    // sign up - 1st part
    public ResponseEntity<ResponseData> signUpConfirmEmail(SignUpEmailConfirmationModel signUpEmailConfirmationModel) {
        String sql_insert_email_verification_code="INSERT INTO gross.customers (customer_id, customer_name, customer_surname, customer_password, customer_account_number, customer_mail, customer_register_date, customer_email_verification_code, customer_account_verified) " +
                "VALUES (DEFAULT, ?, ?, 'not_set', 'not_set', ?, DEFAULT, ?, DEFAULT)";
        String generated_code_for_customer = sendGridService.getRandomNumberString();

        int result;
        try {
            result = jdbcTemplate.update(sql_insert_email_verification_code,signUpEmailConfirmationModel.getCustomer_name(),signUpEmailConfirmationModel.getCustomer_surname(),signUpEmailConfirmationModel.getCustomer_email(),generated_code_for_customer);

            System.out.println("Sending Email...");
//            String sent_verification_code = mailServices.sendEmailWithCode(signUpEmailConfirmationModel,generated_code_for_customer);
            String sent_verification_code = sendGridService.sendEmailWithCode(signUpEmailConfirmationModel,generated_code_for_customer);
            System.out.println("Done");

            return new ResponseEntity(new ResponseData(0,null,sent_verification_code), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1,"Email already exist",null), HttpStatus.OK);

        }
    }

    public ResponseEntity<ResponseData> signUpEmailConfirmed(SignUpEmailConfirmedModel signUpEmailConfirmedModel) {
        String sql_get_user_info_from_verification_code = "select customer_mail, customer_email_verification_code, customer_register_date from gross.customers where customer_email_verification_code=?";

        ResponseData responseData = new ResponseData(1, "Incorrect verification code", null);
        List<Map<String,Object>> result;
        try {
            result = jdbcTemplate.queryForList(sql_get_user_info_from_verification_code, signUpEmailConfirmedModel.getSecurity_key());
            if (result.size()>0)
            {
                Date date = new Date();
                result.forEach(map -> {
                    Timestamp register_time = (Timestamp) map.get("customer_register_date");
                    String customer_mail = (String) map.get("customer_mail");
                    Timestamp now = new Timestamp(date.getTime());
                    if (now.getDate()-register_time.getDate()>1)
                    {
                       responseData.setStatus(1);
                       responseData.setError("Expired verification code");
                       responseData.setData(null);
                    }
                    else
                    {
                        String sql_get_the_last_account_number = "select customer_account_number from gross.customers where customer_account_number not in ('not_set') order by customer_account_number desc limit 1";
                        String last_account_number = jdbcTemplate.queryForObject(sql_get_the_last_account_number,new Object[]{}, String.class);

                        Integer next_account_number = Integer.valueOf(last_account_number)+1;
                        String next_account_number_in_string = next_account_number.toString();
                        String sql_update_email_verified = "UPDATE gross.customers SET customer_password=?, customer_account_number=?, customer_email_verification_code = null, customer_account_verified = true, customer_account_verified_date =? WHERE customer_mail=?";
                        jdbcTemplate.update(sql_update_email_verified,signUpEmailConfirmedModel.getCustomer_password(),next_account_number_in_string,now,customer_mail);

                        responseData.setStatus(0);
                        responseData.setError(null);
                        responseData.setData("ok");
                    }
                });

                return new ResponseEntity(responseData, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity(responseData, HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "error", "undefined"), HttpStatus.OK);
        }
    }

    // sign in
    public ResponseEntity<ResponseData> signIn(SignInModel signInModel) {
        String sql_sign_in = "select customer_account_number from gross.customers where customer_mail=? and customer_password=? and customer_account_verified=true";

        String result;
        try {
            result = jdbcTemplate.queryForObject(sql_sign_in, new Object[]{signInModel.getCustomer_email(), signInModel.getCustomer_password()}, String.class);
            if (!result.isEmpty()) {
                HashMap<String,Object> response_object = new HashMap<>();
                response_object.put("message","ok");
                response_object.put("mail",signInModel.getCustomer_email());
                response_object.put("token","here will be token");

                UserRequestModel userRequestModel = new UserRequestModel();
                userRequestModel.setCustomer_account_number(result);
                return getMyBondsDashboard(userRequestModel);
            } else {
                return new ResponseEntity(new ResponseData(1,"Incorrect username or password",null), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1,"Can't connect to database",null), HttpStatus.OK);
        }
    }

    // get the bonds of the user which will be shown in dashboard
    public ResponseEntity<ResponseData> getMyBondsDashboard(UserRequestModel userRequestModel) {
        String sql_get_my_bonds = "select " +
                "       b.bond_series, " +
                "       b.bond_number, " +
                "       b.bond_absolute_value, " +
                "       b.bond_percent " +
                "from gross.owns o natural join gross.bonds b " +
                "where o.bond_number=b.bond_number " +
                "    and o.bond_series=b.bond_series " +
                "    and o.owner_account = ?";

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(sql_get_my_bonds, userRequestModel.getCustomer_account_number());
            if (result.size() > 0) {

                HashMap res = new HashMap();
                res.put("account_number", userRequestModel.getCustomer_account_number());
                res.put("bonds",result);
                return new ResponseEntity(new ResponseData(0, null, res), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ResponseData(1, "No available bonds", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // get the bonds of the user which will be shown in popup
    public ResponseEntity<ResponseData> getMyBondsFull(BondInfoModel bondInfoModel) {
        String sql_get_my_bonds_full = "select " +
                "       b.bond_series, " +
                "       b.bond_number, " +
                "       b.bond_absolute_value, " +
                "       b.bond_percent, " +
                "       b.bond_life_time, " +
                "       to_char(b.bond_start_date,'DD.MM.YYYY') as bond_start_date, " +
                "       to_char(b.bond_end_date,'DD.MM.YYYY') as bond_end_date, " +
                "       to_char(o.buy_date,'DD.MM.YYYY') as buy_date, " +
                "       o.buy_price, " +
                "       0 as transfer_type " +
                "from gross.owns o join gross.bonds b " +
                "on o.bond_number=b.bond_number " +
                "    and o.bond_series=b.bond_series " +
                "where b.bond_series=? " +
                "    and b.bond_number=? " +
                "    and o.owner_account = ?";

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(sql_get_my_bonds_full,bondInfoModel.getBond_series(),bondInfoModel.getBond_number(),bondInfoModel.getCustomer_account_number());
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
        String sql_get_my_selling_bonds = "select b.bond_series, b.bond_number, b.bond_absolute_value, b.bond_percent " +
                "   from  gross.owns o  join gross.transfer_requests t on t.requester_account_number=o.owner_account and t.bond_series=o.bond_series and t.bond_number=o.bond_number " +
                "   join gross.bonds b on b.bond_series=t.bond_series and b.bond_number=t.bond_number " +
                "   where t.transfer_type='sell' and t.transfer_approved=false and b.bond_in_market=true and o.owner_account=?";

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(sql_get_my_selling_bonds,userRequestModel.getCustomer_account_number());
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

    // get the bonds of the user which is being sold in popup
    public ResponseEntity<ResponseData> getMySellingBondsFull(BondInfoModel bondInfoModel) {
        String sql_get_my_selling_bonds_full = "select b.bond_series, b.bond_number, b.bond_absolute_value, b.bond_percent, b.bond_start_date, b.bond_end_date, b.bond_life_time, t.money_amount as price, to_char(t.request_made_date, 'DD.MM.YYYY') as sell_request_date " +
                "   from  gross.owns o  join gross.transfer_requests t on t.requester_account_number=o.owner_account and t.bond_series=o.bond_series and t.bond_number=o.bond_number " +
                "   join gross.bonds b on b.bond_series=t.bond_series and b.bond_number=t.bond_number " +
                "   where t.transfer_type='sell' and t.transfer_approved=false and b.bond_in_market=true and b.bond_series=? and b.bond_number=? and o.owner_account=?";

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(sql_get_my_selling_bonds_full,bondInfoModel.getBond_series(),bondInfoModel.getBond_number(),bondInfoModel.getCustomer_account_number());
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
        String sql_get_my_buying_bonds = "select b.bond_series, b.bond_number, b.bond_absolute_value, b.bond_percent " +
                "   from  gross.transfer_requests t join gross.bonds b on b.bond_series=t.bond_series and b.bond_number=t.bond_number " +
                "   where t.transfer_type='buy' and t.transfer_approved=false and b.bond_in_market=true and t.requester_account_number=?";

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(sql_get_my_buying_bonds,userRequestModel.getCustomer_account_number());
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

    // get the bonds of the user which is being bought in popup
    public ResponseEntity<ResponseData> getMyBuyingBondsFull(BondInfoModel bondInfoModel) {
        String sql_get_my_buying_bonds_full = "select b.bond_series, b.bond_number, b.bond_absolute_value, b.bond_percent, b.bond_start_date, b.bond_end_date, b.bond_life_time, t.money_amount as price, to_char(t.request_made_date, 'DD.MM.YYYY') as sell_request_date " +
                "   from  gross.transfer_requests t join gross.bonds b on b.bond_series=t.bond_series and b.bond_number=t.bond_number " +
                "   where t.transfer_type='buy' and t.transfer_approved=false and b.bond_in_market=true and b.bond_series=? and b.bond_number=? and t.requester_account_number=?";

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(sql_get_my_buying_bonds_full,bondInfoModel.getBond_series(),bondInfoModel.getBond_number(),bondInfoModel.getCustomer_account_number());
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
        String sql_get_selling_bonds = "select       " +
                "                         b.bond_series,       " +
                "                         b.bond_number,       " +
                "                         b.bond_absolute_value,       " +
                "                         b.bond_percent       " +
                "                  from gross.transfer_requests t natural join gross.bonds b " +
                "                  where t.bond_number=b.bond_number " +
                "                      and t.bond_series=b.bond_series " +
                "                      and t.transfer_type = 'sell' " +
                "                      and t.transfer_approved = false";

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(sql_get_selling_bonds);
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
    public ResponseEntity<ResponseData> getSendingBondsFull(BondInfoModel bondInfoModel) {
        String sql_get_selling_bonds_full = "select      " +
                "                        b.bond_series,      " +
                "                        b.bond_number,      " +
                "                        b.bond_absolute_value,      " +
                "                        b.bond_percent,      " +
                "                        b.bond_life_time,      " +
                "                        to_char(b.bond_start_date,'DD.MM.YYYY') as bond_start_date,      " +
                "                        to_char(b.bond_end_date,'DD.MM.YYYY') as bond_end_date, " +
                "                        t.money_amount as price, " +
                "                        concat(c.customer_name, ' ',c.customer_surname) as seller_name, " +
                "                        1 as transfer_type " +
                "                 from gross.transfer_requests t join gross.bonds b " +
                "                 on t.bond_number=b.bond_number " +
                "                     and t.bond_series=b.bond_series " +
                "                        join gross.customers c on t.requester_account_number=c.customer_account_number " +
                "                 where t.bond_series=? " +
                "                     and t.bond_number=? " +
                "                    and t.transfer_type='sell'";

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(sql_get_selling_bonds_full,bondInfoModel.getBond_series(),bondInfoModel.getBond_number());
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

    // make buy/sell request - request for a buy/sell form
    public ResponseEntity<ResponseData> buySellRequest(BuySellRequestModel buySellRequestModel) {
        String sql_buy_sell_request = "INSERT INTO gross.transfer_requests (transfer_request_id, requester_account_number, bond_series, bond_number, money_amount, request_made_date, transfer_type, transfer_approved) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, DEFAULT, ?, DEFAULT)";
        String sql_change_bond_market_status = "UPDATE gross.bonds SET bond_in_market = ? WHERE bond_series = ? AND bond_number = ?";
        String sql_check_for_already_in_market = "select count(*) from gross.transfer_requests t where t.bond_series=? and t.bond_number=? and t.transfer_type=? and t.transfer_approved=false";

        ResponseData responseData = new ResponseData(1, "error", null);
        int result_of_request = 0;
        try {
            if (buySellRequestModel.getTransfer_type() == 0) {
                String result_of_number_already_in_market = jdbcTemplate.queryForObject(sql_check_for_already_in_market, new Object[]{buySellRequestModel.getBond_series(), buySellRequestModel.getBond_number(),"sell"}, String.class);
                assert result_of_number_already_in_market != null;
                if (Integer.parseInt(result_of_number_already_in_market)==0)
                {
                    jdbcTemplate.update(sql_change_bond_market_status,true, buySellRequestModel.getBond_series(), buySellRequestModel.getBond_number());
                    result_of_request = jdbcTemplate.update(sql_buy_sell_request,buySellRequestModel.getRequester_account_number(), buySellRequestModel.getBond_series(), buySellRequestModel.getBond_number(), buySellRequestModel.getMoney_amount(), "sell");

                    responseData.setStatus(0);
                    responseData.setError(null);
                    responseData.setData(result_of_request);
                } else {
                    responseData.setStatus(1);
                    responseData.setError("Already in market");
                    responseData.setData(null);
                }
            } else if (buySellRequestModel.getTransfer_type() == 1) {
                String result_of_number_already_in_market = jdbcTemplate.queryForObject(sql_check_for_already_in_market, new Object[]{buySellRequestModel.getBond_series(), buySellRequestModel.getBond_number(),"buy"}, String.class);
                assert result_of_number_already_in_market != null;
                if (Integer.parseInt(result_of_number_already_in_market)==0)
                {
                    result_of_request = jdbcTemplate.update(sql_buy_sell_request,buySellRequestModel.getRequester_account_number(), buySellRequestModel.getBond_series(), buySellRequestModel.getBond_number(), buySellRequestModel.getMoney_amount(), "buy");
                    responseData.setStatus(0);
                    responseData.setError(null);
                    responseData.setData(result_of_request);
                } else {
                    responseData.setStatus(1);
                    responseData.setError("Already in market");
                    responseData.setData(null);
                }
            }

            return new ResponseEntity(responseData, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // make transfer - first request to get all information about the bond being sold
    public ResponseEntity<ResponseData> makeTransferFirst(FirstTransferModel firstTransferModel) {
        String sql_get_bond_details = "   select b.bond_series,  " +
                "          b.bond_number,  " +
                "          b.bond_absolute_value,  " +
                "          b.bond_percent,  " +
                "          b.bond_life_time,  " +
                "          b.bond_start_date,  " +
                "          b.bond_end_date,  " +
                "          t.money_amount as price,  " +
                "          concat(c.customer_name, c.customer_surname) as seller_name,  " +
                "          c.customer_privilege  " +
                "   from gross.transfer_requests t  " +
                "            join gross.bonds b on t.bond_series = b.bond_series and t.bond_number = b.bond_number  " +
                "            join gross.customers c on c.customer_account_number = t.requester_account_number  " +
                "   where t.transfer_type = 'sell'  " +
                "     and t.bond_series=?  " +
                "     and t.bond_number=?";

        List<Map<String,Object>> result;
        try {
            result = jdbcTemplate.queryForList(sql_get_bond_details, firstTransferModel.getBond_series(), firstTransferModel.getBond_number());
            return new ResponseEntity(new ResponseData(0, null, result.get(0)), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // make transfer - second request to get full info of the buyer
    public ResponseEntity<ResponseData> makeTransferSecond(UserRequestModel userRequestModel) {
        String sql_get_customer_details = "select customer_name, customer_surname, customer_account_number,customer_balance from gross.customers where customer_account_number=?";
        List<Map<String,Object>> result;
        try {
            result = jdbcTemplate.queryForList(sql_get_customer_details, userRequestModel.getCustomer_account_number());
            return new ResponseEntity(new ResponseData(0, null, result.get(0)), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // buy request approved and necessary calculations should be taken
    public ResponseEntity<ResponseData> transferApproved(TransferApprovedModel transferApprovedModel) {

        String sql_get_seller_details = "select tr.requester_account_number as seller_account_number, c.customer_balance as seller_balance from gross.transfer_requests tr join gross.customers c on tr.requester_account_number=c.customer_account_number where tr.bond_series=? and tr.bond_number=? and tr.transfer_type='sell' and tr.transfer_approved=false";
        String sql_get_buyer_details = "select tr.requester_account_number as buyer_account_number, c.customer_balance as buyer_balance, tr.money_amount from gross.transfer_requests tr join gross.customers c on tr.requester_account_number=c.customer_account_number where tr.bond_series=? and tr.bond_number=? and transfer_type='buy' and tr.transfer_approved=false";
        String sql_make_transfer = "INSERT INTO gross.transfers (transfer_id, seller_account_number, seller_balance, buyer_account_number, buyer_balance, bond_series, bond_number, money_amount, transfer_date, transfer_finished) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, DEFAULT, true)";
        String sql_change_bond_market_status = "UPDATE gross.bonds SET bond_in_market = ? WHERE bond_series = ? AND bond_number = ?";
        String sql_update_owns = "UPDATE gross.owns SET sell_price = ?, sell_date = now(), previous_owner_account = ?, owner_account = ? WHERE owner_account = ? AND bond_series = ? AND bond_number = ?";
        String sql_update_user = "UPDATE gross.customers SET customer_balance = customer_balance + ? WHERE  customer_account_number = ?";
        String sql_update_transfer_approved_status = "UPDATE gross.transfer_requests SET transfer_approved = true WHERE bond_series = ? and bond_number = ? and transfer_approved = false";

        List<Map<String, Object>> seller_data = null;
        List<Map<String, Object>> buyer_data = null;
        String result_of_transfer;
        String result_of_bond_market_status_change;
        String result_of_update_owns;
        String result_of_update_seller;
        String result_of_update_buyer;
        String result_of_transfer_approved_status;

        try {
            seller_data = jdbcTemplate.queryForList(sql_get_seller_details,transferApprovedModel.getBond_series(),transferApprovedModel.getBond_number());
            final String[] sellerAccountNumber = new String[1];
            final Double[] sellerBalance = new Double[1];
            seller_data.forEach(map -> {
                sellerAccountNumber[0] = (String) map.get("seller_account_number");
                sellerBalance[0] = (Double) map.get("seller_balance");
            });

            buyer_data = jdbcTemplate.queryForList(sql_get_buyer_details,transferApprovedModel.getBond_series(),transferApprovedModel.getBond_number());
            final String[] buyerAccountNumber = new String[1];
            final Double[] buyerBalance = new Double[1];
            final Float[] moneyAmount = new Float[1];
            buyer_data.forEach(map -> {
                buyerAccountNumber[0] = (String) map.get("buyer_account_number");
                buyerBalance[0] = (Double) map.get("buyer_balance");
                moneyAmount[0] = (Float) map.get("money_amount");
            });

            result_of_transfer = String.valueOf(jdbcTemplate.update(sql_make_transfer,sellerAccountNumber[0],sellerBalance[0],buyerAccountNumber[0], buyerBalance[0],transferApprovedModel.getBond_series(), transferApprovedModel.getBond_number(), moneyAmount[0]));
            result_of_bond_market_status_change = String.valueOf(jdbcTemplate.update(sql_change_bond_market_status, false, transferApprovedModel.getBond_series(), transferApprovedModel.getBond_number()));
            result_of_update_owns = String.valueOf(jdbcTemplate.update(sql_update_owns,moneyAmount[0], sellerAccountNumber[0], buyerAccountNumber[0], sellerAccountNumber[0], transferApprovedModel.getBond_series(), transferApprovedModel.getBond_number()));
            result_of_update_seller = String.valueOf(jdbcTemplate.update(sql_update_user,moneyAmount[0], sellerAccountNumber[0]));
            result_of_update_buyer = String.valueOf(jdbcTemplate.update(sql_update_user,-moneyAmount[0], buyerAccountNumber[0]));
            result_of_transfer_approved_status = String.valueOf(jdbcTemplate.update(sql_update_transfer_approved_status,transferApprovedModel.getBond_series(), transferApprovedModel.getBond_number()));

            if (Integer.valueOf(result_of_transfer) > 0 && Integer.valueOf(result_of_bond_market_status_change) > 0 && Integer.valueOf(result_of_update_owns) > 0 && Integer.valueOf(result_of_update_seller) > 0 && Integer.valueOf(result_of_update_buyer) > 0 && Integer.valueOf(result_of_transfer_approved_status) > 0) {
                HashMap<String,String> final_result = null;
                final_result.put("transfer",result_of_transfer);
                final_result.put("status",result_of_bond_market_status_change);
                final_result.put("owns",result_of_update_owns);
                final_result.put("seller",result_of_update_seller);
                final_result.put("buyer",result_of_update_buyer);
                final_result.put("request",result_of_transfer_approved_status);
                return new ResponseEntity(new ResponseData(0, null, final_result), HttpStatus.OK);

            }
            else {
                return new ResponseEntity(new ResponseData(1, "Error in transaction", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }
}