export interface Response {
    cpu: string,
    memory: string[]
  }

export interface getVMMap{
  vmName: string,
  vmInstanceId: string,
  skuOrInstanceName: string,
  region: string,
  os: string,
  creationTime: string,
  groupName: string,
  creationError: string,
}

export interface createVMMap{
  vendorName: string,
  vmName: string,
  skuOrInstanceName: string,
  region: string,
  os: string,
  groupName: string,
  adminName: string,
}
export interface createVMResultMap{
  vmName: string,
  vmInstanceId: string,
  skuOrInstanceName: string,
  region: string,
  creationTime: string,
  os:string,
  groupName:string,
  creationError:string

}