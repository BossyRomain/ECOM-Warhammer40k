import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { BehaviorSubject, catchError, lastValueFrom, map, Observable, throwError } from 'rxjs';
import { Cart } from '../model/cart';
import { CommandLine } from '../model/command-line';
import { ClientServiceService } from './client-service.service';
import { ProductServiceService } from './product-service.service';
import { Product } from '../model/product';
import { Router } from '@angular/router';
import { cpuUsage } from 'process';
import { Client } from '../model/client';

@Injectable({
  providedIn: 'root'
})
export class CartServiceService {

  private apiUrl = environment.apiUrl;
  constructor(private http:HttpClient,private productService:ProductServiceService, private clientService:ClientServiceService, private router:Router) { }
  private isConnected = new BehaviorSubject<boolean>(false);
  isConnected$ = this.isConnected.asObservable();
  public currentCart: CommandLine[] = [];
  
  public id:number = 0;



  private cartItems = new BehaviorSubject<number>(0); // Contient le nombre d'articles dans le panier
  cartItems$ = this.cartItems.asObservable(); 




  public addProductToCart(clientID:number, productID:number, amount:number){
    let exist = this.containsElement(productID);
    if(exist != -1){ //If the article is already in the cart
      console.log("updating object " + productID + " with value " + (this.currentCart[exist].quantity + amount))
      this.updateCart(exist, this.currentCart[exist].quantity + amount);
    }else{
      if(clientID != 0 && this.clientService.isConnected()){
        const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.clientService.client?.authToken);
        const params = new HttpParams().set("quantity", amount.toString());
        this.http.post(`${this.apiUrl}/api/clients/${clientID}/carts/${productID}`, " ",  { headers, params }, ).pipe(
          map((body:any) => {
            let line:CommandLine = body;
            return line;
          })
        ).subscribe(
          (value) => {
            this.currentCart.push(value);
            this.updateCartLength();
          },
          (error) => { console.error("cart service: " + String(error))}
        ) 
      }else{
        this.productService.getProductCatalogById(productID).subscribe(
          (data)=>{
            this.currentCart.push({id:productID, quantity:amount, product:data});
        });
        
      }
    }
  }

  public getCartOfClient(clientID: number): Observable<any> {
    const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.clientService.client?.authToken);
    return this.http.get(`${this.apiUrl}/api/clients/${clientID}/commands`, { headers }).pipe(
      map((body: any) => {
        const cart = body[0];
        this.currentCart = []; // Réinitialise le panier actuel
        cart.commandLines.forEach((elm: CommandLine) => {
          this.currentCart.push(elm); // Remplit le panier
        });
        this.updateCartLength(); // Correctement mis à jour après la modification
        return cart;
      })
    );
  }
  
  public updateCartWithOldAmount(index:number, newAmount:number, oldAmount:number):number{
    return this.updateCart(index, newAmount + oldAmount);
  }

  public updateCart(index:number, newAmount:number): number{
    if(this.clientService.isConnected() &&  this.clientService.client){
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.clientService.client?.authToken);
      this.http.put(`${this.apiUrl}/api/clients/${this.clientService.client.id}/carts/${this.currentCart[index].product.id}`, newAmount, { headers }).subscribe(
        (value) => {
          console.log('Everything worked while upgrading');
          console.log(value);
          if(this.clientService.client){
            this.getCartOfClient(this.clientService.client?.id).subscribe((value) => {
              this.id = value.id;
              console.log(value);
              this.currentCart = [];
              value.commandLines.forEach((elm:CommandLine) => {
                this.currentCart.push(elm);
              })
              
            },
            (error)=>{
              console.error(error);
            });
          }
          
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
    if(this.clientService.isConnected() && this.clientService.client){
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.clientService.client?.authToken);
      this.http.delete(`${this.apiUrl}/api/clients/${this.clientService.client.id}/carts/${this.currentCart[index].product.id}`, { headers }).subscribe(
        (value) => {
        },
        (error) => {
        }
      );
      this.currentCart.splice(index, 1);
      
    }else{
      this.currentCart.splice(index, 1);
    }
    this.updateCartLength();
  }

  public retrieveClientInfo(email:string, password:string):Promise<Client>{
    return lastValueFrom(this.clientService.seConnecter(email, password).pipe(map((client) => this.clientService.client = client)));
  }

  public containsElement(id:number):number{
    let i = 0;
    while(i < this.currentCart.length && this.currentCart[i].product.id != id){
      i++;
    }
    if(i >= this.currentCart.length){
      i = -1;
    }
    return i;
  }

  public getAmountOfProduct(id:number):number{
    let index = this.containsElement(id);
    return this.currentCart[index].quantity;
  } 

  public getSum(): number{
    let sum = 0;
    this.currentCart.forEach((temp) => {
      sum += temp.product.unitPrice * temp.quantity;
    })
    return sum;
  }
  public getAmountToPay(): number{
      let sum : number = 0;
      this.currentCart.forEach((temp) => {
        sum += temp.product.unitPrice * temp.quantity;
      })
      return sum;
  }
  
  public payCart(): Observable<any>{
      if (this.clientService.isConnected() && this.clientService.client) {
        const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.clientService.client?.authToken);
        return this.http.get<number>(`${this.apiUrl}/api/clients/${this.clientService.client.id}/carts/pay`, { headers }).pipe(
          catchError((error: HttpErrorResponse) => {
            let errorMessage = 'An unexpected error occurred.';
            if (error.status === 400) {
              let errorMessage = error.error.message || 'Not enough stock for one or more products.';
            }
            return throwError(() => new Error(errorMessage));
          })
        );
      } else {
        return throwError(() => new Error('User is not authenticated.'));
      }
    }
    
  public clearCart(){
    let i = 0;
    let l = this.currentCart.length;
    for(; i < l; i++){
      this.deleteLine(0);
    }
  }


  updateCartLength(): void {
    let length : number = this.currentCart.length
    this.cartItems.next(length);
  }
}
