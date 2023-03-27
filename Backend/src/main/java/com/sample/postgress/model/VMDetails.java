package com.sample.postgress.model;

public class VMDetails {
    public VMDetails() {
    }


    String vendorName;
    String vmName;
    String vmInstanceId;
    String userName;
    String skuOrInstanceName;
    String region;
    String creationTime;
    String os;
    String groupName;
    String creationError;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVmInstanceId() {
        return vmInstanceId;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public void setVmInstanceId(String vmInstanceId) {
        this.vmInstanceId = vmInstanceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSkuOrInstanceName() {
        return skuOrInstanceName;
    }

    public void setSkuOrInstanceName(String skuOrInstanceName) {
        this.skuOrInstanceName = skuOrInstanceName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getCreationError() {
        return creationError;
    }

    public void setCreationError(String creationError) {
        this.creationError = creationError;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "{" +
                " \"vmName\":\"" + vmName + "\"" +
                ", \"vmInstanceId\":\"" + vmInstanceId + "\"" +
                ", \"userName\":\"" + userName + '\"' +
                ", \"skuOrInstanceName\":\"" + skuOrInstanceName + '\"' +
                ", \"region\":\"" + region + "\"" +
                ", \"creationTime\":\"" + creationTime + "\"" +
                ", \"os\":\"" + os + "\"" +
                ", \"groupName\":\"" + groupName + "\"" +
                ", \"creationError\":\"" + creationError + "\"" +
                "}";
    }
}
