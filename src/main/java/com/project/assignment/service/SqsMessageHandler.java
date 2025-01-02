package com.project.assignment.service;

import com.project.assignment.entity.InsurancePolicyDynamoDb;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SqsMessageHandler {

    private final SqsClient sqsClient;
    private final DynamoDbService dynamoDbService;

    // Define the queue URL
    private final String queueUrl = "http://localhost:4566/000000000000/PolicyQueue"; // Update with correct queue URL for LocalStack

    @Autowired
    public SqsMessageHandler(SqsClient sqsClient, DynamoDbService dynamoDbService) {
        this.sqsClient = sqsClient;
        this.dynamoDbService = dynamoDbService;
    }

    // Process messages from the SQS queue
    public void processMessages() {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .build();

        for (Message message : sqsClient.receiveMessage(request).messages()) {
            try {
                // Print out the raw SQS message for debugging
                System.out.println("Received message: " + message.body());

                // Deserialize the message to the InsurancePolicyDynamoDb class
                InsurancePolicyDynamoDb policy = new ObjectMapper().readValue(message.body(), InsurancePolicyDynamoDb.class);
                dynamoDbService.saveInsurancePolicyToDynamoDb(policy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Delete processed message from the queue
    public void deleteMessageFromQueue(String receiptHandle) {
        sqsClient.deleteMessage(builder -> builder
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build());
    }
}