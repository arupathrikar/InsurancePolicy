package com.project.assignment.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.assignment.entity.InsurancePolicyDynamoDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.Iterator;
import java.util.List;

@Service
public class SqsMessageHandler {

    private final SqsClient sqsClient;
    private final DynamoDbService dynamoDbService;
    private final ObjectMapper objectMapper; // For JSON parsing

    private final String queueUrl = "http://localhost:4566/000000000000/PolicyQueue";

    @Autowired
    public SqsMessageHandler(SqsClient sqsClient, DynamoDbService dynamoDbService, ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.dynamoDbService = dynamoDbService;
        this.objectMapper = objectMapper;
    }

    public void processMessages() {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .build();

        List<Message> messages = sqsClient.receiveMessage(request).messages();
        if (messages.isEmpty()) {
            System.out.println("No messages available in the queue.");
            return;
        }

        for (Message message : messages) {
            try {
                System.out.println("Received message: " + message.body());

                // Parse the JSON string from SQS into a JsonNode object
                JsonNode jsonNode = objectMapper.readTree(message.body());

                InsurancePolicyDynamoDb policy = new InsurancePolicyDynamoDb();

                JsonNode Message = objectMapper.readTree(jsonNode.get("Message").asText());
                policy.setPolicyNumber(Message.get("PolicyNumber").asText());
                policy.setPolicyHolderName(Message.get("PolicyHolderName").asText());
                policy.setPolicyAmount(Message.get("PolicyAmount").asDouble());

                // Saving the policy to DynamoDB
                dynamoDbService.saveInsurancePolicyToDynamoDb(policy);

                // WHY? - Delete the message from SQS after successful processing
                deleteMessageFromQueue(message.receiptHandle());

            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void deleteMessageFromQueue(String receiptHandle) {
        sqsClient.deleteMessage(builder -> builder
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build());
    }
}
