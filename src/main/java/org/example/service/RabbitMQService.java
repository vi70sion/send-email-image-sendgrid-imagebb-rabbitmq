package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.example.model.EmailForm;
import org.example.repository.EmailRepository;

public class RabbitMQService {

    EmailRepository emailRepository = new EmailRepository();
    private static final String QUEUE_NAME = "email_queue";
    private static final String HOST = "localhost";
    private final ConnectionFactory factory;
    private final ObjectMapper objectMapper;

    private long recievedCount = 0;

    public RabbitMQService() {
        this.factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        this.objectMapper = new ObjectMapper();
    }

    //Be patvirtinimo
    public <T> void continuousReceiveAndProcess(Class<T> clazz) throws Exception {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String jsonMessage = new String(delivery.getBody(), "UTF-8");
                System.out.println(recievedCount +" Gautas JSON: " + jsonMessage);
                recievedCount++;
                try {
                    T obj = objectMapper.readValue(jsonMessage, clazz);
                    System.out.println("Gautas: "+obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

            System.out.println("Laukiama");
            while (true) {
                Thread.sleep(1000);
            }
        }
    }

    //Su patvirtinimu ir re-enqueue
    public <T> void receiveAndProcessOneMessageAtATime(Class<T> clazz) throws Exception {

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            channel.basicQos(1);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String jsonMessage = new String(delivery.getBody(), "UTF-8");
                System.out.println(recievedCount+" Gautas JSON: " + jsonMessage);
                recievedCount++;
                try {
                    T obj = objectMapper.readValue(jsonMessage, clazz);
                    System.out.println("Gautas: "+obj);

                    //Patvirtinimas jog žinutė apdorota sėkmingai
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                    if (obj.getClass() == EmailForm.class) {
                        emailRepository.addEmail((EmailForm) obj);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    //Praneša, jog nepavyko apdoroti žinutės ir žinutė vėl grąžinama į eilę
                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                }
            };

            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});

            System.out.println("Laukiama");
            while (true) {
                Thread.sleep(1000);
            }
        }
    }


}
