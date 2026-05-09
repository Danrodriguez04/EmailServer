package com.emailsender.emailsender.dto;

public class ContactRequestDto {
    private String name;
    private String email;
    private String issue;
    private String message;
    private String language;

    public String getName() {
        return this.name;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getIssue() {
        return this.issue;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
