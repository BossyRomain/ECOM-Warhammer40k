import { CatalogueImg } from "./catalogue-img";

export interface ProductCatalog {

    id: number;
    name: string;
    stock: number;
    unitPrice: number;
    catalogueImg: CatalogueImg;

}
