package com.project.assignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@JsonIgnoreProperties(ignoreUnknown = true) // Add this annotation
public class InsurancePolicyDynamoDb {

    private String policyNumber;
    private String policyHolderName;
    private Double policyAmount;

    // Default constructor required by DynamoDB Enhanced SDK
    public InsurancePolicyDynamoDb() {}

    public InsurancePolicyDynamoDb(String policyNumber, String policyHolderName, Double policyAmount) {
        this.policyNumber = policyNumber;
        this.policyHolderName = policyHolderName;
        this.policyAmount = policyAmount;
    }

    @DynamoDbPartitionKey
    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyHolderName() {
        return policyHolderName;
    }

    public void setPolicyHolderName(String policyHolderName) {
        this.policyHolderName = policyHolderName;
    }

    public Double getPolicyAmount() {
        return policyAmount;
    }

    public void setPolicyAmount(Double policyAmount) {
        this.policyAmount = policyAmount;
    }
}