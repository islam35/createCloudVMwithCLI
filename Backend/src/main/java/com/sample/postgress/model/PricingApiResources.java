package com.sample.postgress.model;

import java.util.ArrayList;
import java.util.List;

public class PricingApiResources {
    String memory ;
    String vcpu;

    String region;

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getVcpu() {
        return vcpu;
    }

    public void setVcpu(String vcpu) {
        this.vcpu = vcpu;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getRegion() {
        return this.region;
    }

    @Override
    public String toString() {
        return "PricingApiResources{" +
                "memory='" + memory + '\'' +
                ", vcpu='" + vcpu + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
