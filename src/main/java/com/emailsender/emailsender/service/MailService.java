package com.emailsender.emailsender.service;

import com.emailsender.emailsender.dto.ContactRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Locale;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final MessageSource emailMessageSource;


    @Value("${mail.personal}")
    private String personalEmail;

    @Value("${mail.info}")
    private String infoMail;

    @Value("${spring.mail.username}")
    private String sendEmail;


    public MailService(
            JavaMailSender mailSender,
            SpringTemplateEngine templateEngine,
            MessageSource emailMessageSource
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.emailMessageSource = emailMessageSource;
    }

    public void sendContactMail(ContactRequestDto request) throws MessagingException {
        sendInternalContactEmail(request);
        sendClientConfirmationEmail(request);
    }

    private void sendInternalContactEmail(ContactRequestDto request) throws MessagingException {
        Context context = new Context();

        context.setVariable("name", request.getName());
        context.setVariable("issue", request.getIssue());
        context.setVariable("email", request.getEmail());
        context.setVariable("message", request.getMessage());
        context.setVariable("lang", request.getLanguage());

        String html = templateEngine.process("contact-message", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(sendEmail);
        helper.setTo(infoMail);
        helper.setCc(personalEmail);

        helper.setSubject("Contacto:  " + request.getIssue());
        message.setContent(html, "text/html; charset=UTF-8");

        mailSender.send(message);
    }

    private void sendClientConfirmationEmail(ContactRequestDto request) throws MessagingException {
        Locale locale = resolveLocale(request.getLanguage());

        String subject = emailMessageSource.getMessage(
                "email.client.subject",
                null,
                locale
        );

        Context context = new Context();

        context.setVariable("title", emailMessageSource.getMessage(
                "email.client.title",
                new Object[]{request.getName()},
                locale
        ));

        context.setVariable("intro", emailMessageSource.getMessage("email.client.intro", null, locale));
        context.setVariable("body", emailMessageSource.getMessage("email.client.body", null, locale));
        context.setVariable("highlight", emailMessageSource.getMessage("email.client.highlight", null, locale));
        context.setVariable("footer", emailMessageSource.getMessage("email.client.footer", null, locale));

        String html = templateEngine.process("contact-message-client", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(sendEmail);
        helper.setTo(request.getEmail());
        helper.setSubject(subject);
        message.setContent(html, "text/html; charset=UTF-8");

        mailSender.send(message);
    }

    private Locale resolveLocale(String lang) {
        if (lang == null) return Locale.of("es");

        return switch (lang.toLowerCase()) {
            case "en" -> Locale.of("en");
            case "it" -> Locale.of("it");
            default -> Locale.of("es");
        };
    }


}
