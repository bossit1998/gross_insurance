package com.example.Queries;

public class BondServiceQueries {

    //buy-sell
    public String sql_buy_sell_request = "INSERT INTO gross.transfer_requests (transfer_request_id, requester_account_number, bond_series, bond_number, money_amount, request_made_date, transfer_type, transfer_approved) " +
            "VALUES (DEFAULT, ?, ?, ?, ?, DEFAULT, ?, DEFAULT)";
    public String sql_change_bond_market_status_1 = "UPDATE gross.bonds SET bond_in_market = ? WHERE bond_series = ? AND bond_number = ?";
    public String sql_check_for_already_in_market = "select count(*) from gross.transfer_requests t where t.bond_series=? and t.bond_number=? and t.transfer_type=? and t.transfer_approved=false";

    // make transfer 1
    public String sql_get_bond_details = "   select b.bond_series,  " +
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

    // make transfer 2
    public String sql_get_customer_details = "select customer_name, customer_surname, customer_account_number,customer_balance from gross.customers where customer_account_number=?";

    // buy request approved
    public String sql_get_seller_details = "select tr.requester_account_number as seller_account_number, c.customer_balance as seller_balance from gross.transfer_requests tr join gross.customers c on tr.requester_account_number=c.customer_account_number where tr.bond_series=? and tr.bond_number=? and tr.transfer_type='sell' and tr.transfer_approved=false";
    public String sql_get_buyer_details = "select tr.requester_account_number as buyer_account_number, c.customer_balance as buyer_balance, tr.money_amount from gross.transfer_requests tr join gross.customers c on tr.requester_account_number=c.customer_account_number where tr.bond_series=? and tr.bond_number=? and transfer_type='buy' and tr.transfer_approved=false";
    public String sql_make_transfer = "INSERT INTO gross.transfers (transfer_id, seller_account_number, seller_balance, buyer_account_number, buyer_balance, bond_series, bond_number, money_amount, transfer_date, transfer_finished) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, DEFAULT, true)";
    public String sql_change_bond_market_status_2 = "UPDATE gross.bonds SET bond_in_market = ? WHERE bond_series = ? AND bond_number = ?";
    public String sql_update_owns = "UPDATE gross.owns SET sell_price = ?, sell_date = now(), previous_owner_account = ?, owner_account = ? WHERE owner_account = ? AND bond_series = ? AND bond_number = ?";
    public String sql_update_user = "UPDATE gross.customers SET customer_balance = customer_balance + ? WHERE  customer_account_number = ?";
    public String sql_update_transfer_approved_status = "UPDATE gross.transfer_requests SET transfer_approved = true WHERE bond_series = ? and bond_number = ? and transfer_approved = false";
}
