import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { map, Observable } from 'rxjs';
import { Cart } from '../model/cart';
import { CommandLine } from '../model/command-line';
import { ClientServiceService } from './client-service.service';
import { ProductServiceService } from './product-service.service';
import { Product } from '../model/product';

@Injectable({
  providedIn: 'root'
})
export class CartServiceService {

  private apiUrl = environment.apiUrl;
  constructor(private http:HttpClient,private productService:ProductServiceService, private clientService:ClientServiceService) { }

  public currentCart: CommandLine[] = [];

  public addProductToCart(clientID:number, productID:number, amount:number){
    if(this.clientService.isConnected()){
      this.http.post(`${this.apiUrl}/api/clients/${clientID}/carts`, productID);
    }else{
      let prod:Product;
      
      this.productService.getProductById(productID).subscribe(
        (data)=>{
          console.log("data: "
            + data
          );
          console.log(this.currentCart.push({id:productID, quantity:amount, product:data}));
          console.log(this.currentCart);
        });
      
    }
    
  }

  public getCartOfClient(clientID: number): Observable<Cart> {
    return this.http.get(`${this.apiUrl}/api/clients/${clientID}/commands`).pipe(
      map((body: any) => {

        let lines: CommandLine[] = [];
        body.commandLines.forEach((element: CommandLine) => {
          lines.push(element);
        });
        let cart: Cart = {id: body.id, articles: lines};
        return cart;
      })
    );
  }
}
