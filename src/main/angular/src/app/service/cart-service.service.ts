import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Product } from '../model/product';
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class CartServiceService {

  private apiUrl = environment.apiUrl;
  constructor(private http:HttpClient) { }

  public addProductToCart(product:Product){
    this.http.post(`${this.apiUrl}/api/clients/${product.id}/carts`, product);
  }
}
