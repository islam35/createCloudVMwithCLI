package com.sample.postgress.model;

public class Prices {
    public String skuOrInstanceName;
    public String region;
    public String prices;
    public String os;
    public String vcpu;
    public String memory;
    public String processor;

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
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

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

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

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Override
    public String toString() {
        return "{\"skuOrInstanceName\":" + skuOrInstanceName +
                ", \"region\":\"" + region + "\""+
                ", \"prices\":" + prices +
                ", \"os\":\"" + os +"\""+
                ", \"vcpu\":\"" + vcpu +"\""+
                ", \"memory\":\"" + memory + "\"}";
    }
}
