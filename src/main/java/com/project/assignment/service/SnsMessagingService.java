package com.project.assignment.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SnsMessagingService {

    private final AmazonSNS snsClient;

    @Autowired
    public SnsMessagingService(@Value("${aws.accessKeyId}") String accessKeyId,
                               @Value("${aws.secretKey}") String secretKey,
                               @Value("${aws.region}") String region) {
        this.snsClient = AmazonSNSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretKey)))
                .build();
    }

    public void publishToSns(String topicArn, String message) {

        try {
            snsClient.publish(topicArn, message);;
            System.out.println("Message sent with ID: " + message);
        } catch (Exception e) {
            System.err.println("Error occurred while sending message: " + e.getMessage());
        }
    }
}