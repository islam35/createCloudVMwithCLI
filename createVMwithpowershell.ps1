$username = 'username'
$password = 'password'

$SecurePassword = ConvertTo-SecureString $password -AsPlainText -Force

$cred = New-Object System.Management.Automation.PSCredential($username, $SecurePassword)

New-AzVm -ResourceGroupName 'myResourceGroup1' -Name 'myVM' -Location 'westus' -Image 'Canonical:UbuntuServer:18.04-LTS:18.04.201806130' -size 'Standard_B1s' -Credential $cred