import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Product } from '../model/product';
import { environment } from '../../environment/environment';
import { Image } from '../model/image';
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
          let array: Image[] = [];
          body.images.forEach((element:Image) => {
            array.push(element);
          });
          const product = {
            id:body.id, 
            name:body.name, 
            stock: body.stock, 
            price: body.price, 
            url:body.url, 
            description:body.description, 
            images:array
          };
          return product ;
        }
      )      
    );
  }
}
