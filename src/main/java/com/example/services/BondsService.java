package com.example.services;

import com.example.Queries.BondServiceQueries;
import com.example.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BondsService {

    private final JdbcTemplate jdbcTemplate;
    private final BondServiceQueries bondServiceQueries = new BondServiceQueries();

    @Autowired
    private BondsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("connected");
    }

    // make buy/sell request - request for a buy/sell form
    public ResponseEntity<ResponseData> buySellRequest(BuySellRequestModel buySellRequestModel) {

        ResponseData responseData = new ResponseData(1, "error", null);
        int result_of_request = 0;
        try {
            if (buySellRequestModel.getTransfer_type() == 0) {
                String result_of_number_already_in_market = jdbcTemplate.queryForObject(bondServiceQueries.sql_check_for_already_in_market, new Object[]{buySellRequestModel.getBond_series(), buySellRequestModel.getBond_number(), "sell"}, String.class);
                assert result_of_number_already_in_market != null;
                if (Integer.parseInt(result_of_number_already_in_market) == 0) {
                    jdbcTemplate.update(bondServiceQueries.sql_change_bond_market_status_1, true, buySellRequestModel.getBond_series(), buySellRequestModel.getBond_number());
                    result_of_request = jdbcTemplate.update(bondServiceQueries.sql_buy_sell_request, buySellRequestModel.getRequester_account_number(), buySellRequestModel.getBond_series(), buySellRequestModel.getBond_number(), buySellRequestModel.getMoney_amount(), "sell");

                    responseData.setStatus(0);
                    responseData.setError(null);
                    responseData.setData(result_of_request);
                } else {
                    responseData.setStatus(1);
                    responseData.setError("Already in market");
                    responseData.setData(null);
                }
            } else if (buySellRequestModel.getTransfer_type() == 1) {
                String result_of_number_already_in_market = jdbcTemplate.queryForObject(bondServiceQueries.sql_check_for_already_in_market, new Object[]{buySellRequestModel.getBond_series(), buySellRequestModel.getBond_number(), "buy"}, String.class);
                assert result_of_number_already_in_market != null;
                if (Integer.parseInt(result_of_number_already_in_market) == 0) {
                    result_of_request = jdbcTemplate.update(bondServiceQueries.sql_buy_sell_request, buySellRequestModel.getRequester_account_number(), buySellRequestModel.getBond_series(), buySellRequestModel.getBond_number(), buySellRequestModel.getMoney_amount(), "buy");
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


        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(bondServiceQueries.sql_get_bond_details, firstTransferModel.getBond_series(), firstTransferModel.getBond_number());
            return new ResponseEntity(new ResponseData(0, null, result.get(0)), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // make transfer - second request to get full info of the buyer
    public ResponseEntity<ResponseData> makeTransferSecond(UserRequestModel userRequestModel) {
        List<Map<String, Object>> result;
        try {
            result = jdbcTemplate.queryForList(bondServiceQueries.sql_get_customer_details, userRequestModel.getCustomer_account_number());
            return new ResponseEntity(new ResponseData(0, null, result.get(0)), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }

    // buy request approved and necessary calculations should be taken
    public ResponseEntity<ResponseData> transferApproved(TransferApprovedModel transferApprovedModel) {

        List<Map<String, Object>> seller_data = null;
        List<Map<String, Object>> buyer_data = null;
        String result_of_transfer;
        String result_of_bond_market_status_change;
        String result_of_update_owns;
        String result_of_update_seller;
        String result_of_update_buyer;
        String result_of_transfer_approved_status;

        try {
            seller_data = jdbcTemplate.queryForList(bondServiceQueries.sql_get_seller_details, transferApprovedModel.getBond_series(), transferApprovedModel.getBond_number());
            final String[] sellerAccountNumber = new String[1];
            final Double[] sellerBalance = new Double[1];
            seller_data.forEach(map -> {
                sellerAccountNumber[0] = (String) map.get("seller_account_number");
                sellerBalance[0] = (Double) map.get("seller_balance");
            });

            buyer_data = jdbcTemplate.queryForList(bondServiceQueries.sql_get_buyer_details, transferApprovedModel.getBond_series(), transferApprovedModel.getBond_number());
            final String[] buyerAccountNumber = new String[1];
            final Double[] buyerBalance = new Double[1];
            final Float[] moneyAmount = new Float[1];
            buyer_data.forEach(map -> {
                buyerAccountNumber[0] = (String) map.get("buyer_account_number");
                buyerBalance[0] = (Double) map.get("buyer_balance");
                moneyAmount[0] = (Float) map.get("money_amount");
            });

            result_of_transfer = String.valueOf(jdbcTemplate.update(bondServiceQueries.sql_make_transfer, sellerAccountNumber[0], sellerBalance[0], buyerAccountNumber[0], buyerBalance[0], transferApprovedModel.getBond_series(), transferApprovedModel.getBond_number(), moneyAmount[0]));
            result_of_bond_market_status_change = String.valueOf(jdbcTemplate.update(bondServiceQueries.sql_change_bond_market_status_2, false, transferApprovedModel.getBond_series(), transferApprovedModel.getBond_number()));
            result_of_update_owns = String.valueOf(jdbcTemplate.update(bondServiceQueries.sql_update_owns, moneyAmount[0], sellerAccountNumber[0], buyerAccountNumber[0], sellerAccountNumber[0], transferApprovedModel.getBond_series(), transferApprovedModel.getBond_number()));
            result_of_update_seller = String.valueOf(jdbcTemplate.update(bondServiceQueries.sql_update_user, moneyAmount[0], sellerAccountNumber[0]));
            result_of_update_buyer = String.valueOf(jdbcTemplate.update(bondServiceQueries.sql_update_user, -moneyAmount[0], buyerAccountNumber[0]));
            result_of_transfer_approved_status = String.valueOf(jdbcTemplate.update(bondServiceQueries.sql_update_transfer_approved_status, transferApprovedModel.getBond_series(), transferApprovedModel.getBond_number()));

            HashMap<String, String> final_result = new HashMap<>();

            if (Integer.valueOf(result_of_transfer) > 0 && Integer.valueOf(result_of_bond_market_status_change) > 0 && Integer.valueOf(result_of_update_owns) > 0 && Integer.valueOf(result_of_update_seller) > 0 && Integer.valueOf(result_of_update_buyer) > 0 && Integer.valueOf(result_of_transfer_approved_status) > 0) {
                return new ResponseEntity(new ResponseData(0, null, "ok"), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ResponseData(1, "Error in transaction", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity(new ResponseData(1, "Can't connect to database", null), HttpStatus.OK);
        }
    }
}
