import { Product } from "./product";
import { ProductCatalog } from "./product-catalog";

export interface CommandLine {
    id?:number;
    quantity:number;
    product:ProductCatalog;
}
