package com.emailsender.emailsender.controller;

import com.emailsender.emailsender.dto.ContactRequestDto;
import com.emailsender.emailsender.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MailService mailService;


    MessageController(MailService mailService){
        this.mailService = mailService;
    }

    @PostMapping("/email")
    public ResponseEntity<Void> send(@RequestBody ContactRequestDto request) throws MessagingException {
        mailService.sendContactMail(request);
        return ResponseEntity.ok().build();
    }



}
