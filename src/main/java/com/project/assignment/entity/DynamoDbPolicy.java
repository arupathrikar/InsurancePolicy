package com.project.assignment.entity;

public class DynamoDbPolicy {

    private String policyNumber;
    private String policyHolderName;
    private Double policyAmount;

    //constructor
    public DynamoDbPolicy(String policyNumber, String policyHolderName, Double policyAmount) {
        this.policyNumber = policyNumber;
        this.policyHolderName = policyHolderName;
        this.policyAmount = policyAmount;
    }

    // Getters and Setters

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


    //toString

    @Override
    public String toString() {
        return "DynamoDbPolicy{" +
                "policyNumber='" + policyNumber + '\'' +
                ", policyHolderName='" + policyHolderName + '\'' +
                ", policyAmount=" + policyAmount +
                '}';
    }
}