package com.example.orderprocessor;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class OrderProcessorHandler implements RequestHandler<SQSEvent, Void> {

    private static final String TABLE_NAME =
            System.getenv("ORDERS_TABLE");

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper();

    private final DynamoDbClient dynamoDbClient =
            DynamoDbClient.builder().build();

    private final OrderService orderService =
            new OrderService(dynamoDbClient, TABLE_NAME);

    @Override
    public Void handleRequest(SQSEvent event, Context context) {

        for (SQSEvent.SQSMessage message : event.getRecords()) {

            try {
                Order order = OBJECT_MAPPER.readValue(
                        message.getBody(), Order.class);

                orderService.save(order);

                context.getLogger().log(
                        "Pedido persistido: " + order.orderId());

            } catch (Exception e) {
                context.getLogger().log(
                        "Erro ao processar mensagem: " + e.getMessage());

                throw new RuntimeException(
                        "Falha no processamento da mensagem SQS", e);
            }
        }

        return null;
    }
}
