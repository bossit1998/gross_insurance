package com.example.services;

//import com.sun.org.apache.xpath.internal.operations.String;
import com.example.models.SignUpEmailConfirmationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class MailServices {

    private final JavaMailSender javaMailSender;
    @Autowired
    public MailServices(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(899999)+100000;

        // this will convert any number sequence into 6 character.
        String formatted_code = String.format("%06d", number);

        return formatted_code;
    }

    public void sendEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("bossbmw55@gmail.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World \n Spring Boot Email");
        javaMailSender.send(msg);
    }

    public void sendEmailWithAttachment() throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo("abduw_1806@mail.ru");
        helper.setSubject("Testing from Spring Boot");
        helper.setText("<h1>Check attachment for image!</h1>", true);
        //FileSystemResource file = new FileSystemResource(new File("classpath:android.png"));
        //Resource resource = new ClassPathResource("android.png");
        //InputStream input = resource.getInputStream();
        //ResourceUtils.getFile("classpath:android.png");
        helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

        javaMailSender.send(msg);

    }

    public String sendEmailWithCode(SignUpEmailConfirmationModel signUpEmailConfirmationModel, String my_gen_code) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

//        String email="bossbmw55@gmail.com";
        String email=signUpEmailConfirmationModel.getCustomer_email();
        String customer_full_name=signUpEmailConfirmationModel.getCustomer_name()+" "+signUpEmailConfirmationModel.getCustomer_surname();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("GROSS Insurance");
        helper.setText("<html>" +
                "<body> " +
                "    <div class=\"confirm\"> " +
                "         <img width=150px height=50px src='cid:logo'/> " +
                "        <div class=\"code\"> " +
                "            <div> <b>"+customer_full_name+",</b> <br> Ro'yxatdan o'tishni  tasdiqlovchi kod: </div> " +
                "            <h3> "+my_gen_code+" </h3> " +
                "        </div> " +
                "        <div>Kodni hech kimga aytmang</div> " +
                "    </div> " +
                "</body> " +
                "</html> ", true);
        helper.addInline("logo", new File("C:\\Users\\Bossit\\java-getting-started\\src\\main\\resources\\gross-logo-light.png"));

        //FileSystemResource file = new FileSystemResource(new File("classpath:android.png"));
        //InputStream input = resource.getInputStream();
        //helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

        javaMailSender.send(message);
        return my_gen_code;
    }

}
