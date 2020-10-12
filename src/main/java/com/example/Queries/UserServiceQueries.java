package com.example.Queries;

public class UserServiceQueries {

    // sign up 1
    public String sql_get_registered_users_count = "select count(*) from  gross.customers where customer_mail = ? and customer_account_verified = false";
    public String sql_insert_email_verification_code = "INSERT INTO gross.customers (customer_id, customer_name, customer_surname, customer_password, customer_account_number, customer_mail, customer_register_date, customer_email_verification_code, customer_account_verified) VALUES (DEFAULT, ?, ?, 'not_set', 'not_set', ?, DEFAULT, ?, DEFAULT)";

    // sign up 2
    public String sql_get_user_info_from_verification_code = "select customer_mail, customer_email_verification_code, customer_register_date from gross.customers where customer_email_verification_code=?";

    public String sql_get_the_last_account_number = "select customer_account_number from gross.customers where customer_account_number not in ('not_set') order by customer_account_number desc limit 1";

    public String sql_update_email_verified = "UPDATE gross.customers SET customer_password=?, customer_account_number=?, customer_email_verification_code = null, customer_account_verified = true, customer_account_verified_date =? WHERE customer_mail=?";


    // sign in
    public String sql_sign_in = "select customer_account_number from gross.customers where customer_mail=? and customer_password=? and customer_account_verified=true";

    // get bonds dashboard
    public String sql_get_my_bonds = "select " +
            "       b.bond_series, " +
            "       b.bond_number, " +
            "       b.bond_absolute_value, " +
            "       b.bond_percent " +
            "from gross.owns o natural join gross.bonds b " +
            "where o.bond_number=b.bond_number " +
            "    and o.bond_series=b.bond_series " +
            "    and o.owner_account = ?";

    // get bonds popup
    public String sql_get_my_bonds_full = "select " +
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

    // sell dashboard
    public String sql_get_my_selling_bonds = "select b.bond_series, b.bond_number, b.bond_absolute_value, b.bond_percent " +
            "   from  gross.owns o  join gross.transfer_requests t on t.requester_account_number=o.owner_account and t.bond_series=o.bond_series and t.bond_number=o.bond_number " +
            "   join gross.bonds b on b.bond_series=t.bond_series and b.bond_number=t.bond_number " +
            "   where t.transfer_type='sell' and t.transfer_approved=false and b.bond_in_market=true and o.owner_account=?";

    // sell popup
    public String sql_get_my_selling_bonds_full = "select b.bond_series, b.bond_number, b.bond_absolute_value, b.bond_percent, b.bond_start_date, b.bond_end_date, b.bond_life_time, t.money_amount as price, to_char(t.request_made_date, 'DD.MM.YYYY') as sell_request_date " +
            "   from  gross.owns o  join gross.transfer_requests t on t.requester_account_number=o.owner_account and t.bond_series=o.bond_series and t.bond_number=o.bond_number " +
            "   join gross.bonds b on b.bond_series=t.bond_series and b.bond_number=t.bond_number " +
            "   where t.transfer_type='sell' and t.transfer_approved=false and b.bond_in_market=true and b.bond_series=? and b.bond_number=? and o.owner_account=?";

    // buy dashboard
    public String sql_get_my_buying_bonds = "select b.bond_series, b.bond_number, b.bond_absolute_value, b.bond_percent " +
            "   from  gross.transfer_requests t join gross.bonds b on b.bond_series=t.bond_series and b.bond_number=t.bond_number " +
            "   where t.transfer_type='buy' and t.transfer_approved=false and b.bond_in_market=true and t.requester_account_number=?";

    // buy popup
    public String sql_get_my_buying_bonds_full = "select b.bond_series, b.bond_number, b.bond_absolute_value, b.bond_percent, b.bond_start_date, b.bond_end_date, b.bond_life_time, t.money_amount as price, to_char(t.request_made_date, 'DD.MM.YYYY') as sell_request_date " +
            "   from  gross.transfer_requests t join gross.bonds b on b.bond_series=t.bond_series and b.bond_number=t.bond_number " +
            "   where t.transfer_type='buy' and t.transfer_approved=false and b.bond_in_market=true and b.bond_series=? and b.bond_number=? and t.requester_account_number=?";



    // from here for market

    // selling in dashboard
    public String sql_get_selling_bonds = "select       " +
            "                         b.bond_series,       " +
            "                         b.bond_number,       " +
            "                         b.bond_absolute_value,       " +
            "                         b.bond_percent       " +
            "                  from gross.transfer_requests t natural join gross.bonds b " +
            "                  where t.bond_number=b.bond_number " +
            "                      and t.bond_series=b.bond_series " +
            "                      and t.transfer_type = 'sell' " +
            "                      and t.transfer_approved = false";

    // selling in full
    public String sql_get_selling_bonds_full = "select      " +
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

}
