package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender,
                            @Value("${spring.mail.username}") String fromEmail,TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendOtpEmail(String to, String otp) {
        try {
            System.out.println("Sending OTP to: " + to);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("SmartBuy OTP Verification");

            Context context = new Context();
            context.setVariable("otp", otp);

            String html = templateEngine.process("email-otp", context);

            helper.setText(html, true);

            mailSender.send(message);
           System.out.println("Email sent successfully ");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email");
        }
    }
}

