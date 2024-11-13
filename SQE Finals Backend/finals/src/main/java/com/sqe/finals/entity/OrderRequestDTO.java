package com.sqe.finals.entity;

import java.util.List;

public class OrderRequestDTO {
    private String customerName;
    private String address;
    private String postalCode;
    private String contactInfo;
    private List<Long> checkedCartItemIds;  // IDs of checked CartItems

    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<Long> getCheckedCartItemIds() {
        return checkedCartItemIds;
    }

    public void setCheckedCartItemIds(List<Long> checkedCartItemIds) {
        this.checkedCartItemIds = checkedCartItemIds;
    }
}
