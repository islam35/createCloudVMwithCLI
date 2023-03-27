
package com.sample.postgress.model;

public class PricingApi {
    private  String username;
    private String vcpu;
    private String memory;
    private String  region;
    private String operatingSystem;

    public String getVcpu() {
        return vcpu;
    }

    public void setVcpu(String vcpu) {
        this.vcpu = vcpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "username=" + username +
                ", vcpu=" + vcpu +
                ", memory=" + memory +
                ", region=" + region +
                ", operatingSystem=" + operatingSystem;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
