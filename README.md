# createCloudVMwithCLI
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
    
3- 

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
   /aws/service/ami-amazon-linux-latest/al2022-ami-kernel-default-x86_64
