import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Product } from '../model/product';
import { environment } from '../../environment/environment';
import { ProductCatalog } from '../model/product-catalog';

@Injectable({
  providedIn: 'root'
})
export class ProductServiceService {

  constructor(private http: HttpClient) {
    
  }

  private apiUrl = environment.apiUrl;

  public getProductById(id:number):Observable<Product>{
    console.log(`${this.apiUrl}/api/products/${id}`);
    return this.http.get(`${this.apiUrl}/api/products/${id}`)
    .pipe(
      map((body:any) => 
        {
          console.log("The body is");
          console.log(body);
          const product = {id:body.id, name:body.name, stock: body.stock, price: body.price, url:body.url, description:body.description};
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
        return response;
      })
    );
  }
}
