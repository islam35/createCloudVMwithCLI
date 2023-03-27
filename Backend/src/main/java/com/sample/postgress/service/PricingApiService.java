package com.sample.postgress.service;

import com.sample.postgress.model.PricingApi;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PricingApiService {

    Map<String, String> getPriceList(PricingApi pricingApi) throws IOException;

    Map<String, List<String>> getMapping(String region);
    Map<String, List<String>> GetAvailableResources(String providerType, String region);
    String createVm(String userName, String vendor, String region,
                    String skuOrInstanceName, String os, String vmName, String adminName, String groupName);

    Map<String, List<String>> getVMs(String userName);
}
