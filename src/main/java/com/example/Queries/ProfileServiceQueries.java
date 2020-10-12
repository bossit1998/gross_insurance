package com.example.Queries;

public class ProfileServiceQueries {

    // get profile info
    public String sql_get_profile_info = "select customer_name, customer_surname, customer_account_number, customer_balance, customer_balance, customer_phone_number,customer_mail from gross.customers where customer_account_number = ?";

    // update profile info
    public String sql_update_info = "UPDATE gross.customers SET customer_name = ?, customer_surname = ?, customer_mail = ? WHERE  customer_account_number = ?";

    // update profile password
    public String sql_update_password = "UPDATE gross.customers SET customer_password = ? WHERE  customer_account_number = ? and customer_password = ?";

    // fill balance
    public String sql_update_balance = "UPDATE gross.customers SET customer_balance = customer_balance + ? WHERE  customer_account_number = ?";

}
