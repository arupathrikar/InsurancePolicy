package com.project.assignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Add this import
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "insurance_policy")
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Policy number is mandatory")
    @Column(unique = true, nullable = false, length = 6)
    private String policyNumber;

    @NotBlank(message = "Policy holder name is mandatory")
    private String policyHolderName;

    @NotNull(message = "Policy amount is mandatory")
    private Double policyAmount;

    // Getters and Setters

    // Suggested by JPA
    public InsurancePolicy() {
    }

    public InsurancePolicy(Long id, String policyNumber, String policyHolderName, Double policyAmount) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.policyHolderName = policyHolderName;
        this.policyAmount = policyAmount;
    }

    public String getPolicyHolderName() {
        return policyHolderName;
    }

    public void setPolicyHolderName(String policyHolderName) {
        this.policyHolderName = policyHolderName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPolicyAmount() {
        return policyAmount;
    }

    public void setPolicyAmount(Double policyAmount) {
        this.policyAmount = policyAmount;
    }

    @Override
    public String toString() {
        return "InsurancePolicy{" +
                "id=" + id +
                ", policyNumber='" + policyNumber + '\'' +
                ", policyHolderName='" + policyHolderName + '\'' +
                ", policyAmount=" + policyAmount +
                '}';
    }
}