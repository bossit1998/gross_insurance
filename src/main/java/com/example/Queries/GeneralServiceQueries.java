package com.example.Queries;

public class GeneralServiceQueries {

    // get reviews
    public String sql_get_reviews = "Select review_id, reviewer_nickname, reviewer_comment from gross.reviews";

    // post reviews
    public String sql_post_reviews = "INSERT INTO gross.reviews (review_id, reviewer_nickname, reviewer_comment, review_date, reviewer_mail) VALUES (DEFAULT, ?, ?, DEFAULT, ?)";

    // get news
    public String sql_get_news = "Select * from gross.news";

}
