package com.project.assignment.service;

import com.project.assignment.entity.InsurancePolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
public class SnsService {

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @Value("${sns.topic.arn}")
    private String topicArn;

    public SnsService(SnsClient snsClient, ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.objectMapper = objectMapper;
    }

    public void publishPolicy(InsurancePolicy policy) {
        try {
            String message = objectMapper.writeValueAsString(policy);
            PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(message)
                    .build();
            snsClient.publish(publishRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish to SNS", e);
        }
    }
}