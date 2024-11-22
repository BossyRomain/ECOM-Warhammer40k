import { Product } from "./product";

export interface CommandLine {
    id?:number;
    quantity:number;
    product:Product;
}
