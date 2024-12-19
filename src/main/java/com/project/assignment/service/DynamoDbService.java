package com.project.assignment.service;

import com.project.assignment.entity.DynamoDbPolicy;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

@Service
public class DynamoDbService {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDbService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void saveToDynamoDb(DynamoDbPolicy policy) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PolicyNumber", AttributeValue.builder().s(policy.getPolicyNumber()).build());
        item.put("PolicyHolderName", AttributeValue.builder().s(policy.getPolicyHolderName()).build());
        item.put("PolicyAmount", AttributeValue.builder().n(String.valueOf(policy.getPolicyAmount())).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName("Policies")
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }
}