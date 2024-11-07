import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Product } from '../model/product';


@Injectable({
  providedIn: 'root'
})
export class ProductServiceService {

  constructor(private http: HttpClient) {
    
  }


  public getProductById(id:number):Observable<Product>{
    console.log(`http://localhost:4200/api/products/${id}`);
    return this.http.get(`http://localhost:4200/api/products/${id}`)
    .pipe(
      map((body:any) => 
        {
          console.log("The body is");
          console.log(body);
          const product = {id: body.content[0].id, name: body.content[0].name, stock: body.content[0].stock, price: body.content[0].price, url:body.content[0].url};
          return product ;
        }
      )      
    );
  }
}
