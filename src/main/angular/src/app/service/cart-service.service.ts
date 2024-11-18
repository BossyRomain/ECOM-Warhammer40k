import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {map, Observable} from 'rxjs';
import {Cart} from '../model/cart';
import {CommandLine} from '../model/command-line';

@Injectable({
  providedIn: 'root'
})
export class CartServiceService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {
  }

  public currentCart: CommandLine[] = [];

  public addProductToCart(clientID: number, productID: number) {
    this.http.post(`${this.apiUrl}/api/clients/${clientID}/carts`, productID);
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
