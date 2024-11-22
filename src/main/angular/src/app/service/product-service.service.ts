import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Product } from '../model/product';
import { environment } from '../../environments/environment';
import { ProductCatalog } from '../model/product-catalog';

import {Image} from '../model/image';

@Injectable({
  providedIn: 'root'
})
export class ProductServiceService {

  constructor(private http: HttpClient) {

  }

  private apiUrl = environment.apiUrl;

  public getProductById(id: number): Observable<Product> {
    console.log(`${this.apiUrl}/api/products/${id}`);
    return this.http.get(`${this.apiUrl}/api/products/${id}`)
    .pipe(
      map((body:any) => 
        {
          console.log("body");
          console.log(body);
          let array: Image[] = [];
          body.images.forEach((element:Image) => {
            array.push(element);
          });
          const product = {
            id:body.id, 
            name:body.name, 
            stock: body.stock, 
            price: body.unitPrice, 
            mainImage:body.catalogueImg, 
            description:body.description, 
            images:array
          };
          return product ;
        }
      )      
    );
  }

  public getProductsCatalogue(page: number = 0, size: number = 10): Observable<ProductCatalog[]> {
    const url = `${this.apiUrl}/api/products/catalogue?page=${page}&size=${size}`;
    console.log(`Appel de l'API : ${url}`);
    
    return this.http.get<ProductCatalog[]>(url).pipe(
      map((response: any) => {
        console.log('Réponse de l\'API:', response);
        return response.content.map((item: any) => ({
          id: item.id,
          name: item.name,
          stock: item.stock,
          unitPrice: item.unitPrice,
          productType: item.productType,
          catalogueImg: {
            url: item.catalogueImg.url, 
            altText: item.catalogueImg.altText
          }
        }));
      })
    );
  }

  public searchProducts( search:string, page: number = 0, size: number = 10): Observable<ProductCatalog[]>{
    const url = `${this.apiUrl}/api/products/search?page=${page}&size=${size}&query=${search}`;
    console.log(`Appel de l'API : ${url}`);
    
    return this.http.get<ProductCatalog[]>(url).pipe(
      map((response: any) => {
        console.log('Réponse de l\'API:', response);
        return response.content.map((item: any) => ({
          id: item.id,
          name: item.name,
          stock: item.stock,
          unitPrice: item.unitPrice,
          productType: item.productType,
          catalogueImg: {
            url: item.catalogueImg.url, 
            altText: item.catalogueImg.altText
          }
        }));
      })
    );
  }


  private mapResponseToProductCatalog(response: any): ProductCatalog[] {
    // Vérifiez si les données existent et effectuez une transformation prudente
    return response?.content?.map((item: any) => ({
      id: item.id,
      name: item.name,
      stock: item.stock,
      unitPrice: item.unitPrice,
      productType: item.productType,
      catalogueImg: {
        url: item.catalogueImg.url, // Vérification des sous-propriétés
        altText: item.catalogueImg.altText
      }
    })); // Retourne un tableau vide si le contenu est nul ou indéfini
  }


}
