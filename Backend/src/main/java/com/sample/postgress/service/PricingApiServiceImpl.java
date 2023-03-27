package com.sample.postgress.service;

import com.sample.postgress.dao.PricingApiDao;
import com.sample.postgress.model.PricingApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class PricingApiServiceImpl implements PricingApiService{

    @Resource
    PricingApiDao pricingApiDao;

    @Override
    public Map<String, String> getPriceList(PricingApi pricingApi) throws IOException {
        return pricingApiDao.getPriceList(pricingApi);
    }

    @Override
    public Map<String, List<String>> getMapping(String region) {
        return pricingApiDao.getMapping(region);
    }

    @Override
    public Map<String, List<String>> GetAvailableResources(String providerType, String region) {
        return pricingApiDao.GetAvailableResources(providerType, region);
    }

    @Override
    public String createVm(String userName, String vendor, String region,  String skuOrInstanceName, String os, String vmName, String adminName, String groupName) {
        return pricingApiDao.createVm(userName, vendor, region, skuOrInstanceName, os, vmName, adminName, groupName);
    }

    @Override
    public Map<String, List<String>> getVMs(String userName) {
        return pricingApiDao.getVMs(userName);
    }

}
