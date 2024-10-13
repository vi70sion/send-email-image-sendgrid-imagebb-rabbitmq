package org.example.model;

import java.time.LocalDateTime;

public class EmailForm {
    private int id;
    private String recipient;
    private String content;
    private LocalDateTime sent;

    public EmailForm() { }

    public EmailForm(int id, String recipient, String content, LocalDateTime sent) {
        this.id = id;
        this.recipient = recipient;
        this.content = content;
        this.sent = sent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSent() {
        return sent;
    }

    public void setSent(LocalDateTime sent) {
        this.sent = sent;
    }
}
