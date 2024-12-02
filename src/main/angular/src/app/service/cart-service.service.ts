import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { map, Observable } from 'rxjs';
import { Cart } from '../model/cart';
import { CommandLine } from '../model/command-line';
import { ClientServiceService } from './client-service.service';
import { ProductServiceService } from './product-service.service';
import { Product } from '../model/product';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class CartServiceService {

  private apiUrl = environment.apiUrl;
  constructor(private http:HttpClient,private productService:ProductServiceService, private clientService:ClientServiceService, private router:Router) { }

  public currentCart: CommandLine[] = [];
  public id:number = 0;
  public numberItemsCart() : number {
      return this.currentCart.length;
  }

  public addProductToCart(clientID:number, productID:number, amount:number){
    console.log("connected? " + this.clientService.isConnected());
    if(clientID != 0 && this.clientService.isConnected()){
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.clientService.client?.authToken);
      const params = new HttpParams().set("quantity", amount.toString());
      console.log(headers + "\n" + params);
      this.http.post(`${this.apiUrl}/api/clients/${clientID}/carts/${productID}`, " ",  { headers, params }, ).pipe(
        map((body:any) => {
          let line:CommandLine = body;
          return line;
        })
      ).subscribe(
        (value) => {
          console.log(value);
          this.currentCart.push(value);
        },
        (error) => { console.log(error)}
      )
    }else{
      this.productService.getProductCatalogById(productID).subscribe(
        (data)=>{
          console.log("data: "
            + data
          );
          console.log(this.currentCart.push({id:data.id, quantity:amount, product:data}));
          console.log(this.currentCart);
      });
    }
  }

  public getCartOfClient(clientID: number): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.clientService.client?.authToken);
    return this.http.get(`${this.apiUrl}/api/clients/${clientID}/commands`, { headers }).pipe(
      map((body: any) => {
        return body[0];
      })
    );
  }

  public updateCart(index:number, newAmount:number): number{
    if(this.clientService.isConnected() &&  this.clientService.client){
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.clientService.client?.authToken);
      this.http.put(`${this.apiUrl}/api/clients/${this.clientService.client.id}/carts/${this.currentCart[index].product.id}`, newAmount, { headers }).subscribe(
        (value) => {
          console.log('Everything worked while upgrading');
          console.log(value);
        },
        (error) => {
          console.log("an error happened when updating cart");
        }
      );
    }else{
      this.currentCart[index].quantity =newAmount;
    }
    return newAmount;
      
  }

  public deleteLine(index:number):void{
    console.log("remove item at " + index + " product " + this.currentCart[index].product.id);
    if(this.clientService.isConnected() && this.clientService.client){
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.clientService.client?.authToken);
      console.log(this.currentCart[index].product.id);
      this.http.delete(`${this.apiUrl}/api/clients/${this.clientService.client.id}/carts/${this.currentCart[index].product.id}`, { headers }).subscribe(
        (value) => {
          console.log("Delete done: " + value);
        },
        (error) => {
          console.log("Something went wrong");
        }
      );
      this.currentCart.splice(index, 1);
      this.router.navigate(['/cart', this.clientService.client?.id]);
    }else{
      this.currentCart.splice(index, 1);
    }
  }

  public retrieveClientInfo(email:string, password:string){
    this.clientService.seConnecter(email, password).subscribe(
      (client) => { 
        this.clientService.client = client; 
        this.router.navigate(["/catalog/search"], { queryParams: { search: "", page: 0 }, });
        console.log("Voici le client: " + this.clientService.client.id);
        console.log("token " + this.clientService.client.authToken);
        console.log(client);

        this.getCartOfClient(this.clientService.client.id).subscribe(
          (value) => {
            console.log("Content of saved cart:")
            this.id = value.id;
            console.log("values:\n");
            console.log(value);
            this.currentCart = [];
            value.commandLines.forEach((elm:CommandLine) => {
              this.currentCart.push(elm);
            })
          }
        )
    })
  }

  
}
