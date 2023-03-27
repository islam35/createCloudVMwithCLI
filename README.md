# createCloudVMwithCLI
The purpose of the project: In Azure and AWS Cloud, it is to find the best result by comparing the price according to the user requirements and to create VM instances instead of the user when requested. I was part of a 4 people development team. I took part in every step of a project where all the steps such as idea, solution, design and development belong to us.

VM operations and definitions with basic commands in Azure and AWS (Amazon) Clouds with using PowerShell and CLI.
----Before Creation VM Steps----

1- Sign up Azure or AWS (Amazon) cloud.

  a - Azure Portal: https://portal.azure.com/#home
  
  b - AWS Portal: https://us-east-1.console.aws.amazon.com/ec2/home?region=us-east-1#Home:
  
2-  If you want to use Azure cloud, you have two options;

  a- Using with PowerShell commands;
  
    Run that command on CMD or PowerShell to installation azure libs.
    
    "Install-Module -Name Az -Scope CurrentUser -Repository PSGallery -Force"
    
    If you get an error about policy, run that script
    
    set-ExecutionPolicy RemoteSigned -Scope CurrentUser
      
  b- Using with Azure CLI commands;
  
    Download exe from https://learn.microsoft.com/en-us/cli/azure/install-azure-cli-windows?tabs=azure-cli
       
3- AWS Cloud CLI download and install from https://awscli.amazonaws.com/AWSCLIV2.msi

----Creation VM Steps----

1 - For Azure;

  It is necessary to create one resource group at least. If you have it, you can skip step "a".
  
  a - To Create resource group(you can change name and location)
  
      "New-AzResourceGroup -Name "myResourceGroup" -Location "eastus""
      
  b - Create VM steps;
  
       -If you want to use powershell;
       
          Run "createVMwithpowershell.ps1" script (Edit: resource group, VM name, size, image name, ip, username, password)
          
       -If you want to use Azure CLI;
       
          Run "createVMwithCLI.ps1" script (Edit: resource group, VM name, size, image name, ip, username, password)
          
  c - Get VM Lists;
  
        Run "getVMLists.ps1" script
                
2 - For AWS;

  It is necessary to create one security group at least. If you have it, you can skip step "a".
  
    a - To Create security group(you can change name and location)
    
      "aws ec2 create-security-group --group-name demo-sg --description "AWS ec2 CLI Demo SG""
      
    b - Create VM steps;
    
        "aws ec2 run-instances --image-id ami-xxxxxxxx --instance-type t2.micro --key-name MyKeyPair --security-group-ids sg-903004f8"
        
    c - Important: If the VM cannot be created with these two steps, the id should be created from the settings under the user profile picture from the aws portal and see this link for other settings.
          
More infos:

  For AWS;
  
      https://docs.aws.amazon.com/cli/latest/userguide/cli-services-ec2.html
      
      https://devopscube.com/use-aws-cli-create-ec2-instance/
      
      https://www.youtube.com/watch?v=H6twNlwj9ig
      
  For Azure;
  
      https://learn.microsoft.com/tr-tr/azure/virtual-machines/windows/quick-create-cli?source=recommendations
      
      https://learn.microsoft.com/tr-tr/cli/azure/vm?view=azure-cli-latest
      
      https://www.youtube.com/watch?v=a-V708bIsuA
      
