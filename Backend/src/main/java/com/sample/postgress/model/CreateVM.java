package com.sample.postgress.model;

public class CreateVM {
    public CreateVM() {
    }

    String userName;
    String vendor;
    String region;
    String skuOrInstance;

    String os;
    String vmName;
    String adminName;
    String group;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSkuOrInstance() {
        return skuOrInstance;
    }

    public void setSkuOrInstance(String skuOrInstance) {
        this.skuOrInstance = skuOrInstance;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}