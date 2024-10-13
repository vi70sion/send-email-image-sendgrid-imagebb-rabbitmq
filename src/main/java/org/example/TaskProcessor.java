package org.example;

import org.example.model.EmailForm;
import org.example.model.Image;
import org.example.repository.EmailRepository;
import org.example.repository.ImgRepository;
import org.example.service.EmailSender;
//import org.example.service.ImageUploader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class TaskProcessor implements Runnable{

    EmailRepository emailRepository = new EmailRepository();
    ImgRepository imgRepository = new ImgRepository();
    EmailSender emailSender = new EmailSender();
    //ImageUploader imageUploader = new ImageUploader();
    ConcurrentHashMap<Integer, EmailForm> mailMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, Image> imageMap = new ConcurrentHashMap<>();
    private String taskType; // "email" or "image"

    public TaskProcessor(String taskType) {
        this.taskType = taskType;
    }

    @Override
    public void run() {
        while (true) {
            if (taskType.equals("email")) {
                try {
                    processEmails();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (taskType.equals("image")) {
                try {
                    //processImages();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.currentThread().sleep(40000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " thread interrupted.");
            }
        }
    }

    private synchronized void processEmails() throws IOException {
        EmailForm emailForm = emailRepository.getOneEmail();
        if (emailForm != null) {
            if (!mailMap.containsKey(emailForm.getId())) {
                //there is no such email yet in mailMap
                mailMap.putIfAbsent(emailForm.getId(),emailForm);
                System.out.println(Thread.currentThread().getName() + " sending email with ID: " + emailForm.getId());
                emailSender.sendEmail(emailForm.getRecipient(), emailForm.getContent());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " thread interrupted.");
                }
                emailRepository.updateEmail(emailForm);
                mailMap.remove(emailForm.getId());
            } else {
                System.out.println(Thread.currentThread().getName() + " Email with ID: " + emailForm.getId() + " already processing by another thread.");
            }
        }
    }

//    private synchronized void processImages() throws Exception {
//        Image image = imgRepository.getOneImage();
//        if (image != null) {
//            if (!imageMap.containsKey(image.getId())) {
//                //there is no such image yet in mailMap
//                imageMap.putIfAbsent(image.getId(), image);
//                System.out.println(Thread.currentThread().getName() + " sending image with ID: " + image.getId());
//                image.setImageUrl(imageUploader.uploadImage(image.getImage()));
//                //image.setImageUrl("http:\\");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    System.out.println(Thread.currentThread().getName() + " thread interrupted.");
//                }
//                imgRepository.updateImage(image);
//                imageMap.remove(image.getId());
//            } else {
//                System.out.println(Thread.currentThread().getName() + " image with ID: " + image.getId() + " already processing by another thread.");
//            }
//        }
//    }

}
