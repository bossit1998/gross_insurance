package com.example.rest;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MailController {

    @Value("${app.sendgrid.key}")
    private String appKey;

    @GetMapping("twilio")
    public Response twilio() {
        Email from = new Email("me@mail.com");
        Email to = new Email("bossbmw55@gmail.com");
        String subject = "Sending with Twilio";
        Content content = new Content("text/plain","and easy");
        Mail mail = new Mail(from,subject,to,content);

        SendGrid sg = new SendGrid(appKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api((request));
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(System.getenv("SENDGRID_API_KEY"));
        return null;
    }
}