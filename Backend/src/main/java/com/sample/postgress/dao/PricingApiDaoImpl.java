package com.sample.postgress.dao;

import com.sample.postgress.mapper.PricingApiMapper;
import com.sample.postgress.mapper.PricingApiResourceMapper;
import com.sample.postgress.mapper.VMDetailsMapper;
import com.sample.postgress.model.Prices;
import com.sample.postgress.model.PricingApi;
import com.sample.postgress.model.PricingApiResources;
import com.sample.postgress.model.VMDetails;
import com.sample.postgress.validator.FieldValidator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Repository
public class PricingApiDaoImpl  implements  PricingApiDao{
    public PricingApiDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }
    NamedParameterJdbcTemplate template;
    private String vcpuForAws,
            memoryForAws,
            vcpuForAzure,
            memoryForAzure,
            regionForAzure,
            regionForAws;
    final String awsExe = "\"C:\\Program Files\\Amazon\\AWSCLIV2\\aws.exe\"";
    static Map<String, List<String>> euAwsMap = null;
    static Map<String, List<String>> euAzureMap = null;
    static Map<String, List<String>> apAwsMap = null;
    static Map<String, List<String>> apAzureMap = null;
    static Map<String, List<String>> usAwsMap = null;
    static Map<String, List<String>> usAzureMap = null;

    @Override
    public Map<String, String> getPriceList(PricingApi pricingApi)  {
        initializeMaps();
        FieldValidator fieldValidator = new FieldValidator();
        if (!fieldValidator.ValidateFields(pricingApi))
            return null;

        if(pricingApi.getRegion().equals("Europe"))
        {
            regionForAzure = "westeurope";
            regionForAws = "eu-west";
        } else if (pricingApi.getRegion().equals("Asia")) {
            regionForAzure = "eastasia";
            regionForAws = "ap-south";
        }
        else {
            regionForAzure = "eastus";
            regionForAws = "us-east";
        }

        PricingApiResources pricingApiResources = new PricingApiResources();
        pricingApiResources.setVcpu(pricingApi.getVcpu());
        pricingApiResources.setMemory(pricingApi.getMemory());
        pricingApiResources.setRegion(pricingApi.getRegion());
        PricingApiResources tempResources = null;

        tempResources = FindBestMatchingForAws(pricingApiResources);

        HashMap<String, String> result = new HashMap<>();
        pricingApi.setVcpu(tempResources.getVcpu());
        pricingApi.setMemory(tempResources.getMemory());
        result.put("AWS", GetPricesForAws(pricingApi));

        tempResources = FindBestMatchingForAzure(pricingApiResources);
        pricingApi.setVcpu(tempResources.getVcpu());
        pricingApi.setMemory(tempResources.getMemory());

        result.put("AZURE", GetPricesForAzure(pricingApi));

        return result;
    }

    @Override
    public Map<String, List<String>> getMapping(String region) {

        List<PricingApiResources> resources = new LinkedList<>();
        final String sql = "select distinct vcpu, memory from dropdown_map where region=:region ;";
        Map<String,Object> params =new HashMap<String,Object>();
        params.put("region", region);
        resources = template.query(sql, params, new PricingApiResourceMapper());

        Map<String, List<String>> map = new HashMap<>();
        for(PricingApiResources res: resources){
            map.computeIfAbsent(res.getVcpu(), k-> new ArrayList<>()).add(res.getMemory());
        }

        return map;
    }

    @Override
    public Map<String, List<String>> GetAvailableResources(String providerType, String region) {
        List<PricingApiResources> resources = new LinkedList<>();
        final String azureSql = "select distinct vcpu, memory, region from azure_map where region=:region;";
        final String awsSql = "select distinct vcpu, memory, region from aws_map where region=:region;";
        final String sql = providerType == "AWS"? awsSql: azureSql;
        Map<String,Object> params =new HashMap<String,Object>();
        params.put("region", region);

        resources = template.query(sql, params, new PricingApiResourceMapper());

        Map<String, List<String>> map = new HashMap<>();
        for(PricingApiResources res: resources){
            map.computeIfAbsent(res.getVcpu(), k-> new ArrayList<>()).add(res.getMemory());
        }
        return map;

    }

    private String GetPricesForAzure(PricingApi pricingApi) {

        final String winSql =
                "select azure_aws_data.region as region, azure_map.skuname as instancetype, prices from azure_map, azure_aws_data where azure_map.vcpu = :vcpu and azure_map.memory = :memory and azure_aws_data.sku LIKE '%' || (azure_map.skuname)  || '%' and azure_aws_data.region= :region and azure_aws_data.attributes->>'productName' LIKE '%Windows%' and azure_aws_data.attributes->> 'skuName' not like '%Low Priority' and azure_aws_data.attributes->>'skuName' not like '%Spot';";
        final String LinSql =
                "select azure_aws_data.region as region, azure_map.skuname as instancetype, prices from azure_map, azure_aws_data where azure_map.vcpu = :vcpu and azure_map.memory = :memory and azure_aws_data.sku LIKE '%' || (azure_map.skuname)  || '%' and azure_aws_data.region= :region and azure_aws_data.attributes->>'productName' not LIKE '%Windows%' and azure_aws_data.attributes->> 'skuName' not like '%Low Priority' and azure_aws_data.attributes->>'skuName' not like '%Spot';";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("vcpu", pricingApi.getVcpu());
        map.put("memory", pricingApi.getMemory());
        map.put("region", regionForAzure);
        Prices selectedPrice =  new Prices();;

        boolean firstPrice = true;
        List<Prices> prices = template.query(pricingApi.getOperatingSystem().contains("Windows")? winSql:LinSql, map, new PricingApiMapper());
        if(prices.size()>0)
        {
            System.out.println("AZURE Prices: Success!" );
            float  minPrice = 0, prc;
            for (Prices price: prices){
                if (prices.size()>1) {
                    String str = price.getPrices().substring(price.getPrices().indexOf("\"USD\": \"") +8, price.getPrices().indexOf("\", \"unit\""));
                    prc = Float.valueOf(str);
                    if(firstPrice){
                        minPrice = prc;
                        selectedPrice = price;
                        firstPrice = false;
                    }
                    else{
                        if(prc < minPrice)
                        {
                            minPrice = prc;
                            selectedPrice = price;
                        }
                    }
                }
                else {
                    selectedPrice = price;
                }
            }
        }
        else
            System.out.println("Azure Failed: " );

        if(selectedPrice.getPrices()!=null && !selectedPrice.getPrices().isEmpty()) {

            String[] split = selectedPrice.getPrices().split("\\[");
            int idx =0;
            String parse;
            if(split.length>2){
                idx = split[2].indexOf("}")+1;
                parse = split[2].substring(0 ,  idx);
                }
            else{
                idx = split[1].indexOf("}")+1;
                parse = split[1].substring(0 ,  idx);
            }
            selectedPrice.setPrices(parse);
            selectedPrice.setOs(pricingApi.getOperatingSystem());
            selectedPrice.setVcpu(pricingApi.getVcpu());
            selectedPrice.setMemory(pricingApi.getMemory() +" GiB");
            selectedPrice.setSkuOrInstanceName("\""+selectedPrice.getSkuOrInstanceName()+"\"");
            System.out.println(selectedPrice.toString());

        }

        return selectedPrice.toString();
    }

    private String GetPricesForAws(PricingApi pricingApi) {
        final String winSql =
                "select attributes->'instanceType' as instancetype, region, prices, attributes ->'processorArchitecture' as processor from azure_aws_data where \"vendorName\"='aws' and productfamily = 'Compute Instance' and region like '%' || :region || '%' and attributes->>'vcpu' = :vcpu and attributes->>'memory' = :memory and attributes->>'operatingSystem' like '%Windows%' and attributes->>'capacitystatus' = 'UnusedCapacityReservation'and attributes->>'preInstalledSw' = 'NA';";
        final String linSql =
                "select attributes->'instanceType' as instancetype, region, prices, attributes ->'processorArchitecture' as processor from azure_aws_data where \"vendorName\"='aws' and productfamily = 'Compute Instance' and region like '%' || :region || '%' and attributes->>'vcpu' = :vcpu and attributes->>'memory' = :memory and attributes->>'operatingSystem' not like '%Windows%' and attributes->>'capacitystatus' = 'UnusedCapacityReservation'and attributes->>'preInstalledSw' = 'NA';";

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("vcpu", pricingApi.getVcpu());
        map.put("memory", pricingApi.getMemory() + " GiB");
        map.put("region", regionForAws);

        List<Prices> prices = template.query(pricingApi.getOperatingSystem().contains("Windows")?winSql:linSql, map, new PricingApiMapper());
        Prices selectedPrice = new Prices();

        boolean firstPrice = true;
        if(prices.size()>0)
        {
            System.out.println("AWS Prices: Success!");
            float  minPrice = 0, prc;
            for (Prices price: prices){
                if (prices.size()>1) {
                    String str = price.getPrices().substring(price.getPrices().indexOf("\"USD\": \"") +8, price.getPrices().indexOf("\", \"unit\""));
                     prc = Float.valueOf(str);
                     if(firstPrice){
                        minPrice = prc;
                        selectedPrice = price;
                        firstPrice = false;
                    }
                    else{
                        if(prc < minPrice)
                        {
                            minPrice = prc;
                            selectedPrice = price;
                        }
                    }
                }
                else{
                    selectedPrice = price;
                }
            }
            String parse = selectedPrice.getPrices().substring(selectedPrice.getPrices().lastIndexOf("{"), selectedPrice.getPrices().indexOf("}")+1);

            selectedPrice.setPrices(parse);
            selectedPrice.setVcpu(pricingApi.getVcpu());
            selectedPrice.setMemory(pricingApi.getMemory() +" GiB");
            if(pricingApi.getOperatingSystem().contains("Linux"))
                selectedPrice.setOs(pricingApi.getOperatingSystem() + " " + selectedPrice.getProcessor().replace("\"",""));
            else {
                selectedPrice.setOs(pricingApi.getOperatingSystem());
            }
            }
        else{

            System.out.println("AWS Failed: " + pricingApi.toString());
        }
        System.out.println("AWS : " + selectedPrice.toString());
        return selectedPrice.toString();
    }



    public void initializeMaps()
    {
        if (euAwsMap ==null || euAzureMap== null){
            euAwsMap = GetAvailableResources("AWS", "Europe");
            euAzureMap = GetAvailableResources("AZURE", "Europe");
        }
        if (apAwsMap ==null || apAzureMap== null){
            apAwsMap = GetAvailableResources("AWS", "Asia");
            apAzureMap = GetAvailableResources("AZURE", "Asia");
        }
        if (usAwsMap ==null || usAzureMap== null){
            usAwsMap = GetAvailableResources("AWS", "US");
            usAzureMap = GetAvailableResources("AZURE", "US");
        }
    }

    public PricingApiResources FindBestMatchingForAzure(PricingApiResources pricingApiResources){
        System.out.println("FindBestMatchingForAzure-  Vcpu: "+ pricingApiResources.getVcpu() + " memory: "+ pricingApiResources.getMemory());
        switch (pricingApiResources.getRegion()){
            case "Asia":
                pricingApiResources.setRegion("Asia");
                return getPricingApiResources(pricingApiResources, apAzureMap);
            case "Europe":
                pricingApiResources.setRegion("Europe");
                return getPricingApiResources(pricingApiResources, euAzureMap);
            default:
                pricingApiResources.setRegion("US");
                return getPricingApiResources(pricingApiResources, usAzureMap);
        }
    }


    public PricingApiResources FindBestMatchingForAws(PricingApiResources pricingApiResources){
        System.out.println("FindBestMatchingForAws-  Vcpu: "+ pricingApiResources.getVcpu() + " memory: "+ pricingApiResources.getMemory());
        switch (pricingApiResources.getRegion()){
            case "Asia":
                pricingApiResources.setRegion("Asia");
                return getPricingApiResources(pricingApiResources, apAwsMap);
            case "Europe":
                pricingApiResources.setRegion("Europe");
                return getPricingApiResources(pricingApiResources, euAwsMap);
            default:
                pricingApiResources.setRegion("US");
                return getPricingApiResources(pricingApiResources, usAwsMap);
        }
    }

    private PricingApiResources getPricingApiResources(PricingApiResources pricingApiResources, Map<String, List<String>> map) {
        if(map.containsKey(pricingApiResources.getVcpu()) &&
            map.get(pricingApiResources.getVcpu()).contains(pricingApiResources.getMemory())){
                //Do nothing
                System.out.println("getPricingApiResources: Best Match");
                return  pricingApiResources;
        }
        else{
            PricingApiResources pricingApiRes = new PricingApiResources();
            Map<Integer, List<Float>> tempMap = new HashMap<>();

            String vcpu = pricingApiResources.getVcpu();
            String memory = pricingApiResources.getMemory();

            for(Map.Entry<String, List<String>> list : map.entrySet()){
                for(String mem : list.getValue() ){
                    int in = Integer.parseInt(list.getKey());
                    tempMap.computeIfAbsent(in, x-> new ArrayList<Float>()).add(Float.valueOf(mem));
                }
            }
            //Get Best Available VCPU
            if(!map.containsKey(pricingApiResources.getVcpu())){

                tempMap.computeIfAbsent(Integer.parseInt(pricingApiResources.getVcpu()),x-> new ArrayList<Float>()).add(Float.valueOf(pricingApiResources.getMemory()));

                List<Integer> sortedKeys=new ArrayList(tempMap.keySet());
                Collections.sort(sortedKeys);
                int i = sortedKeys.indexOf(Integer.parseInt(pricingApiResources.getVcpu()));
                if(sortedKeys.size()>1){
                    if ( i+1 >= sortedKeys.size()){
                        vcpu = String.valueOf(sortedKeys.get(i -1));
                    }
                    else {
                        int inn = sortedKeys.get(i+1);
                        vcpu = String.valueOf(sortedKeys.get(i +1));
                    }
                }
            }

            //Get Best Available memory
            List<Float> mem = tempMap.get(Integer.parseInt(vcpu));
            float fMemory ;
            boolean dotted = false;
            if(mem!= null && mem.size()>=1) {
                fMemory = Float.valueOf(memory);

                int i = 0;
                if (!mem.contains(fMemory)) {
                    mem.add(fMemory);
                    Collections.sort(mem);
                    i = mem.indexOf(fMemory);
                    if ((i +1) >= mem.size()) {
                        memory = String.valueOf(mem.get(i - 1));
                    } else {
                        memory = String.valueOf(mem.get(i + 1));
                    }
                    if(memory.contains(".0")){
                        memory = memory.substring(0, memory.indexOf(".")) ;
                    }
                }
            }

            pricingApiRes.setVcpu(vcpu);
            pricingApiRes.setMemory(memory);
            System.out.println("getPricingApiResources: Matching with "+ pricingApiRes.toString());
            return pricingApiRes;
        }
    }

    @Override
    public String createVm( String userName, String vendor, String region,  String skuOrInstanceName, String os, String vmName, String adminName, String groupName) {
        if (vendor.equals("AWS")){
            System.out.println("AWS VM Creation is invoked.");
            return createAWSVM(userName, region, skuOrInstanceName, os, vmName, adminName, groupName).toString();
        }
        if (vendor.equals("Azure")){
            System.out.println("Azure VM Creation is invoked.");
            return createaAzureVM(userName, region, skuOrInstanceName, os, vmName, adminName, groupName).toString();
        }
        VMDetails vmDetails = new VMDetails();
        vmDetails.setCreationError("Failed to Create VMs... Selection does not contain AWS or Azure!");
        return vmDetails.toString();
    }

    @Override
    public Map<String, List<String>> getVMs(String userName) {
        List<VMDetails> resources = new LinkedList<>();
        final String getAWS = "select * from createdvms where username=:username and vendorname = 'AWS';";
        final String getAzure = "select * from createdvms where username=:username and vendorname = 'Azure';";
        Map<String,Object> params =new HashMap<String,Object>();
        params.put("username", userName);

        resources = template.query(getAWS, params, new VMDetailsMapper());

        Map<String, List<String>> map = new HashMap<>();
        for(VMDetails res: resources){
            System.out.println(res.toString());
            map.computeIfAbsent("AWS", k-> new ArrayList<>()).add(res.toString());
        }

        resources = template.query(getAzure, params, new VMDetailsMapper());

        for(VMDetails res: resources){
            System.out.println(res.toString());
            map.computeIfAbsent("Azure", k-> new ArrayList<>()).add(res.toString());
        }
        return map;
    }

    private VMDetails createAWSVM(String userName, String region, String skuOrInstanceName, String os, String vmName, String adminName, String groupName){

        String amiForLinuxArm = "/aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-arm64-gp2";
        String amiForLinux = "/aws/service/ami-amazon-linux-latest/al2022-ami-kernel-default-x86_64";
        String amiForWindows = "/aws/service/ami-windows-latest/EC2LaunchV2-Windows_Server-2016-English-Core-Base";
        String ami;
        if(os.contains("Windows")){
            ami = amiForWindows;
        }
        else {
            if(os.contains("32-bit or 64-bit")){
                ami = amiForLinux;
            }
            else {
                ami = amiForLinuxArm;
            }
        }
        Random rand = new Random(); //instance of random class
        int upperbound = 2000;
        //generate random values from 0-24
        int int_random = rand.nextInt(upperbound);
        groupName = groupName + int_random;
        vmName = vmName + int_random;

        VMDetails vmDetails = new VMDetails();
        String s =null;
        StringBuffer error = new StringBuffer();

        String amiID = null;
        try {
            amiID = getAmiIDForRegion(ami, region);
        } catch (IOException e) {
            error.append("Failed during getting availbale ami ID: " +e.getMessage());
            vmDetails.setCreationError(error.toString());
            return vmDetails;
        }

        if (amiID!=null){

            String cmd = awsExe + " ec2 run-instances --image-id "+ amiID
                    +" --instance-type "+skuOrInstanceName
                    +" --region " + region;

            System.out.println("Command is being Executed: " + cmd);
            try {
                Process createVMRes = Runtime.getRuntime().exec(cmd);

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(createVMRes.getInputStream()));

                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(createVMRes.getErrorStream()));

                // Read the output from the command
                System.out.println("create AWS VM command Output:\n");
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                    if (s.contains("\"InstanceId\"")){
                        vmDetails.setVmInstanceId(s.substring(s.indexOf(":")+3, s.lastIndexOf("\"")));
                    }
                    else if (s.contains("\"InstanceType\"")){
                        vmDetails.setSkuOrInstanceName(s.substring(s.indexOf(":")+3, s.lastIndexOf("\"")));
                    }
                    else if (s.contains("\"LaunchTime\"")){
                        vmDetails.setCreationTime(s.substring(s.indexOf(":")+3, s.lastIndexOf("\"")));
                    }
                    else if (s.contains("\"AvailabilityZone\"")){
                        vmDetails.setRegion(s.substring(s.indexOf(":")+3, s.lastIndexOf("\"")));
                    }
                    else if (s.contains("\"GroupName\"")){
                        vmDetails.setGroupName(s.substring(s.indexOf(":")+3, s.lastIndexOf("\""))); ;
                    }
                }
                while ((s=stdError.readLine()) !=null){
                    error.append(s);
                    System.out.println(s);
                }
            } catch (IOException e) {
                error.append(e.getMessage());
            }

        }
        else {
            error.append("ERROR: AMI ID couldn't be retrieved");
            vmDetails.setCreationError(error.toString());
            return vmDetails;
        }

        if(error.length()>0)
        {
            vmDetails.setCreationError(error.toString());
            return vmDetails;
        }

        vmDetails.setUserName(userName);
        vmDetails.setVmName(vmName);
        vmDetails.setVendorName("AWS");
        vmDetails.setOs(os.contains("Linux")? "Linux amzn2-ami-hvm-x86_64-gp2": "Windows EC2LaunchV2-Windows_Server-2016-English-Core-Base");
        addVmToDB(vmDetails);
        addVMName(region, vmDetails.getVmInstanceId(), vmDetails.getVmName());
        return vmDetails;
    }

    private void addVMName(String region, String vmInstanceID, String vmName) {
        String cmd = awsExe + " ec2 create-tags --region "+ region +" --resources " + vmInstanceID
                + "  --tags Key=Name,Value=" + vmName;
        try {
            Process addVMNameCmd = Runtime.getRuntime().exec(cmd);

        } catch (IOException e) {
            //do nothing
            System.out.println(" add VM name Failed");
        }
    }
        private void addVmToDB(VMDetails vmDetails) {
        final String sql = "insert into createdvms(username, vminstanceid, os, region, creationtime, groupname, vendorname, skuOrInstanceName, vmname) " +
                "values(:username, :vminstanceid, :os, :region, :creationtime, :groupname, :vendorname, :skuOrInstanceName, :vmname)";


        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("username", vmDetails.getUserName())
                .addValue("vminstanceid", vmDetails.getVmInstanceId())
                .addValue("os", vmDetails.getOs())
                .addValue("region", vmDetails.getRegion())
                .addValue("creationtime", vmDetails.getCreationTime())
                .addValue("groupname", vmDetails.getGroupName())
                .addValue("vendorname", vmDetails.getVendorName())
                .addValue("skuOrInstanceName", vmDetails.getSkuOrInstanceName())
                .addValue("vmname", vmDetails.getVmName());
        template.update(sql,param, holder);
    }

    private String getAmiIDForRegion(String ami, String region) throws IOException {
        System.out.println("Getting AMI ID for " + ami );
        String cmd = awsExe + " ssm get-parameters --names "+ ami + " --region " + region;

        String s =null;
        String res = null;

            Process createVMRes = Runtime.getRuntime().exec(cmd);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(createVMRes.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(createVMRes.getErrorStream()));

            // Read the output from the command
            System.out.println("Get AMI ID Command Output:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                if (s.contains("\"Value\":")){

                    res =  s.substring(s.indexOf(":") +3, s.lastIndexOf("\""));
                    System.out.println("Success:  " + res);
                }
            }
            while ((s=stdError.readLine()) !=null){
                System.out.println(s);
            }
        return res;
    }

    private VMDetails createaAzureVM(String userName, String region, String skuOrInstanceName, String os, String vmName, String adminName, String groupName) {


        VMDetails vmDetails = new VMDetails();
        Random rand = new Random(); //instance of random class
        int upperbound = 2000;
        //generate random values from 0-24
        int int_random = rand.nextInt(upperbound);
        groupName = groupName + int_random;
        vmName = vmName + int_random;


        String res = createGroupForAzure(region, groupName);
        if (!res.contains("Success") ){
            vmDetails.setCreationError(res);
            return vmDetails;
        }
        StringBuffer error = new StringBuffer();
        String imageName =os.contains("Windows")? "Win2019Datacenter": "UbuntuLTS";
        String cred = "(New-Object System.Management.Automation.PSCredential('sedatkanatli',  (ConvertTo-SecureString 'RAPtor1234.!' -AsPlainText -Force)))";
        String createVMCmd = "powershell.exe New-AzVm -ResourceGroupName "+ groupName +" -Name "+ vmName +
                " -Location "+ region +" -Image "+ imageName+ " -size " + skuOrInstanceName + " -Credential " + cred ;

        String s =null;
        try {
            Process createVMRes = Runtime.getRuntime().exec(createVMCmd);
            System.out.println("This Command is being executed: "+ createVMCmd);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(createVMRes.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(createVMRes.getErrorStream()));

            // Read the output from the command
            System.out.println("Here is the standard output of the create VM command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                if (s.contains("VmId")){
                    vmDetails.setVmInstanceId(s.substring(s.indexOf(":")+1, s.length()));
                }
                else if(s.contains("TimeCreated")){
                    vmDetails.setCreationTime(s.substring(s.indexOf(":")+1, s.length()));
                }
            }
            while ((s=stdError.readLine()) !=null){
                error.append(s);
            }
        } catch (IOException e) {
            error.append("Exception Occured while creating Azure VM\n"+e.getMessage());
        }
        if(error.length()>0){
            vmDetails.setCreationError(error.toString());
            return  vmDetails;
        }
        vmDetails.setRegion(region);
        vmDetails.setUserName(userName);
        vmDetails.setGroupName(groupName);
        vmDetails.setVendorName("Azure");
        vmDetails.setOs(os +" "+imageName);
        vmDetails.setSkuOrInstanceName(skuOrInstanceName);
        vmDetails.setVmName(vmName);
        addVmToDB(vmDetails);
        return vmDetails;
    }

    private boolean execute(BufferedReader stdInput, BufferedReader stdError) throws IOException {
        String s;
        s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
            if (s.contains("Error")){
                return false;
            }
        }
        while ((s=stdError.readLine()) !=null){
            System.out.println(s);
        }
        return true;
    }

    private String createGroupForAzure(String region, String groupName) {

        if(!subscribe()){
            System.out.println("Cannot Select Subscription to continue");
        }
        StringBuffer error = new StringBuffer();
        if(!isGroupPresent(groupName)) {
            String createGroup = "powershell.exe New-AzResourceGroup -Location " + region + " -Name " + groupName;
            String s = null;
            try {
                Process getSubsRes = Runtime.getRuntime().exec(createGroup);

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(getSubsRes.getInputStream()));

                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(getSubsRes.getErrorStream()));

                // Read the output from the command
                System.out.println("Here is the standard output of the get Subscription command:\n");
                s = null;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);

                }
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                    error.append(s);
                }

            } catch (IOException e) {
                error.append("Exception occured while creating "+ groupName+" group for Azure\n"+e.getMessage());
                return error.toString();
            }
        }
        return "Success";
    }

    private boolean isGroupPresent(String groupName) {
        String checkGroup = "powershell.exe Get-AzResourceGroup -Name " + groupName;

        String s = null;
        try {
            Process checkSubsRes = Runtime.getRuntime().exec(checkGroup);
            BufferedReader checkStdIn = new BufferedReader(new
                    InputStreamReader(checkSubsRes.getInputStream()));
            BufferedReader checkStdError = new BufferedReader(new
                    InputStreamReader(checkSubsRes.getErrorStream()));

            // Read the output from the command
            System.out.println("Here is the standard output of the check Group existence command:\n");
            s = null;
            while ((s = checkStdIn.readLine()) != null) {
                System.out.println(s);
                if (s.contains("Succeeded")) {
                    System.out.println("Group Already Exist!");
                    return true;
                }
            }
            while ((s = checkStdError.readLine()) != null) {
                System.out.println(s);
                return false;
            }
        }catch (IOException e) {
            return false;
        }
            return false;
    }
    private boolean subscribe() {
        String selSubs = "powershell.exe Select-AzSubscription c6b59071-0529-4cc7-99fd-2c38b55d9ee5";
        String s =null;
        try {
            Process getSubsRes = Runtime.getRuntime().exec(selSubs);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(getSubsRes.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(getSubsRes.getErrorStream()));

            // Read the output from the command
            System.out.println("Here is the standard output of the get Subscription command:\n");
            return execute(stdInput, stdError);

        } catch (IOException e) {
            return false;
        }
    }


}

