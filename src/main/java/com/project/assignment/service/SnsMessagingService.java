package com.project.assignment.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

@Service
public class SnsMessagingService {

    private final AmazonSNS snsClient;

    @Autowired
    private SqsMessageHandler sqsMessageHandler;

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
            PublishResult result = snsClient.publish(topicArn, message);
            System.out.println("Message sent with ID: " + message);
            sqsMessageHandler.processMessages();
        } catch (Exception e) {
            System.err.println("Error occurred while sending message: " + e.getMessage());
        }
    }

//    public void SubscriptionToSnsService(String topicArn, String protocol, String endpoint){
//
//        SnsClient snsClient = SnsClient.builder() .region(Region.US_EAST_1) .build();
//        SubscribeRequest request = SubscribeRequest.builder() .topicArn(topicArn) .protocol(protocol) .endpoint(endpoint) .build();
//        SubscribeResponse response = snsClient.subscribe(request);
//        System.out.println("Subscription ARN: " + response.subscriptionArn());
//    }
//
//    public void SendEmail() {
//        final String usage = """
//                Usage:     <topicArn> <email>
//
//                Where:
//                   topicArn - The ARN of the topic to subscribe.
//                   email - The email address to use.
//                """;
//
//        String topicArn = "arn:aws:sns:us-east-1:000000000000:PolicyTopic:f86c6a8a-073d-4616-9c9c-a0ce28f85f83";
//        String email = "arupathrikar2899@gmail.com";
//        SnsClient snsClient = SnsClient.builder()
//                .region(Region.US_EAST_1)
//                .build();
//
//        SubscriptionToSnsService(topicArn, "email" , email);
//        snsClient.close();
//    }
}