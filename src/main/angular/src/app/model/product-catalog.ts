import { CatalogueImg } from "./catalogue-img";

export interface ProductCatalog {
    catalogueImg: CatalogueImg;
    id: number;
    name: string;
    productType: string;
    stock: number;
    unitPrice: number;
    

}