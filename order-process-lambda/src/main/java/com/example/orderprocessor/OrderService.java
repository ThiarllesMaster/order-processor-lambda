package com.example.orderprocessor;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

public class OrderService {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public OrderService(
            DynamoDbClient dynamoDbClient,
            String tableName) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
    }

    public void save(Order order) {

        Map<String, AttributeValue> item = new HashMap<>();

        item.put("id", AttributeValue.builder()
                .s(order.orderId())
                .build());

        item.put("customerId", AttributeValue.builder()
                .s(order.customerId())
                .build());

        item.put("product", AttributeValue.builder()
                .s(order.product())
                .build());

        item.put("quantity", AttributeValue.builder()
                .n(order.quantity().toString())
                .build());

        item.put("total", AttributeValue.builder()
                .n(order.total().toPlainString())
                .build());

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build());
    }
}