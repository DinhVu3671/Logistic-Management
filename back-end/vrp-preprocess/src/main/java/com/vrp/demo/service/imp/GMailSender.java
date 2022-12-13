package com.vrp.demo.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GMailSender {

    @Autowired
    public JavaMailSender emailSender;

    @Async
    public void sendSimpleMessage(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@route.scheduler.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
        return;
    }
}
