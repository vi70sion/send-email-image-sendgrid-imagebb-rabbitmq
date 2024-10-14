package org.example;

import org.example.model.EmailForm;
import org.example.model.Image;
import org.example.service.RabbitMQService;

public class Main {
    public static void main(String[] args) throws Exception {

        TaskProcessor taskProcessorEmail = new TaskProcessor("email");
        Thread emailThread1 = new Thread(taskProcessorEmail);
        Thread emailThread2 = new Thread(taskProcessorEmail);

        TaskProcessor taskProcessorImage = new TaskProcessor("image");
        Thread imageThread1 = new Thread(taskProcessorImage);
        Thread imageThread2 = new Thread(taskProcessorImage);

        emailThread1.start();
        emailThread2.start();
        imageThread1.start();
        imageThread2.start();

        RabbitMQService rabbitMQService = new RabbitMQService();
        //rabbitMQService.receiveAndProcessOneMessageAtATime(EmailForm.class, "email_queue");
        rabbitMQService.receiveAndProcessOneMessageAtATime(Image.class, "image_queue");

    }
}