import { Image } from "./image";

export interface Product {
    id:number;
    name:string;
    stock:number;
    price:number;
    description:string;
    mainImage:Image;
    images:Image[];
}
