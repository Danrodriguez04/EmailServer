package com.emailsender.emailsender.service;

import com.emailsender.emailsender.dto.ContactRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${mail.info}")
    private String infoMail;

    @Value("${spring.mail.username}")
    private String sendEmail;


    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendContactMail(ContactRequestDto request) throws MessagingException {

        Context context = new Context();
        context.setVariable("name", request.getName());
        context.setVariable("templates/email", request.getEmail());
        context.setVariable("message", request.getMessage());

        String html = templateEngine.process("templates/email/contact-message", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(sendEmail);
        helper.setTo(infoMail);
        helper.setSubject("Nuevo mensaje de contacto de " + request.getName());
        helper.setText(html, true);

        mailSender.send(message);
    }


}
