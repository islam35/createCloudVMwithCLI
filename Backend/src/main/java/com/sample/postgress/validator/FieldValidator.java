package com.sample.postgress.validator;

import com.sample.postgress.model.PricingApi;

public class FieldValidator {
    public FieldValidator() {
    }
    public  boolean ValidateFields(PricingApi pricingApi){
        if (pricingApi != null &&
                !pricingApi.getRegion().isEmpty() &&
                !pricingApi.getMemory().isEmpty() &&
                !pricingApi.getVcpu().isEmpty() &&
                !pricingApi.getOperatingSystem().isEmpty()){
            return  true;
        }

        return false;

    }
}
