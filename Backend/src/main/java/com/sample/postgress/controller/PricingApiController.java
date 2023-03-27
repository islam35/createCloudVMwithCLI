package com.sample.postgress.controller;


import com.sample.postgress.model.CreateVM;
import com.sample.postgress.model.PricingApi;
import com.sample.postgress.model.VMDetails;
import com.sample.postgress.service.PricingApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class PricingApiController {

    //@Autowired
    //UserInfo user;
    @Resource
    PricingApiService pricingApiService;

    @GetMapping("/getprice")
    public Map<String, String> priciceList(String vcpu, String region, String memory, String  operatingSystem) throws IOException {
        System.out.println("Hellooo");

        PricingApi pricingApi = new PricingApi();
        pricingApi.setMemory(memory);
        pricingApi.setVcpu(vcpu);
        pricingApi.setRegion(region);
        pricingApi.setOperatingSystem(operatingSystem);

        return pricingApiService.getPriceList(pricingApi);
    }
    @GetMapping("/getmapping")
    public Map<String, List<String>> getMapping( String region) throws IOException {
        return pricingApiService.getMapping(region);
    }

    @PostMapping("/createvm")
    public String createVm(@RequestBody CreateVM createVM) throws IOException {
        return pricingApiService.createVm(createVM.getUserName(), createVM.getVendor(), createVM.getRegion(),
                createVM.getSkuOrInstance(), createVM.getOs(), createVM.getVmName(), createVM.getAdminName(), createVM.getGroup());
    }

    @GetMapping("/getvms")
    public Map<String, List<String>> getVMs( String userName) throws IOException {
        return pricingApiService.getVMs(userName);
    }

}
