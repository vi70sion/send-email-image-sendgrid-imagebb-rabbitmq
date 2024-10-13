package org.example.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import java.io.IOException;
import static org.example.utility.Constants.SENDEREMAIL;
import static org.example.utility.Constants.SENDGRIDAPIKEY;

public class EmailSender {

     public EmailSender() { }

    public void sendEmail(String to, String content) throws IOException {
        Email from = new Email(SENDEREMAIL);
        Email recipient = new Email(to);
        String subject = "Email from RabbitMQ";
        Content mailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, recipient, mailContent);

        SendGrid sg = new SendGrid(SENDGRIDAPIKEY);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }

}
