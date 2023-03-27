import { Prices } from "./prices"
import { Pricing } from "./pricing"

export interface ResultMap {
    skuOrInstanceName:string,
    vendorName:string,
    region:string,
    vcpu: string,
    memory: string,
    prices: Prices,
    os: string,
}
