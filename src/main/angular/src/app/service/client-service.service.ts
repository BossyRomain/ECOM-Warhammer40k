import {Injectable} from '@angular/core';
import {CommandLine} from '../model/command-line';
import {environment} from '../../environments/environment';
import {BehaviorSubject, map, Observable} from 'rxjs';
import {Client} from '../model/client';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Cart} from '../model/cart';

@Injectable({
  providedIn: 'root'
})
export class ClientServiceService {

  constructor(private http: HttpClient) {

  }


  private apiUrl = environment.apiUrl;
  public currentCart: CommandLine[] = [];
  private connected: boolean = false;
  public client?: Client;
  private connectFromAnotherPlace: boolean = false;
  

  private isconnected = new BehaviorSubject<boolean>(false); // Contient le nombre d'articles dans le panier
  isconnected$ = this.isconnected.asObservable(); 


  public isConnected(): boolean {
    return this.connected;
  }


  public seConnecter(email: string, password: string): Observable<Client> {
    const url = `${this.apiUrl}/api/clients/login`;

    console.log("Appel à l'API dans le service");

    return this.http.post<any>(url, {email, password}).pipe(
      map((response: any) => {
        this.isConnect(true);
        return {
          id: response.id,
          user: {
            id: response.user.id,
            username: response.user.username,
            password: response.user.password,
            authority: response.user.authority,
          },
          cart: response.currentCart, // Remplir si nécessaire
          firstName: response.firstName,
          lastName: response.lastName,
          birthday: response.birthday,
          newsletter: response.newsletter,
          authToken: response.authToken,
        }; // Typecasting en `Client`
      })
    );
  }

  public inscription(firstName: string, lastName: string, email: string, password: string, birthday: string, newsLetter: boolean): Observable<Client> {
    const url = `${this.apiUrl}/api/clients/signup`;
    console.log("Inscription en cours");

    return this.http.post<any>(url, {email, password, firstName, lastName, birthday, newsLetter}).pipe(
      map((response: any) => {
        console.log("Réponse de l'API reçue");
        this.isConnect(true);
        return {
          id: response.id,
          user: {
            id: response.user.id,
            username: response.user.username,
            password: response.user.password,
            authority: response.user.authority,
          },
          cart: response.currentCart, // Remplir si nécessaire
          firstName: response.firstName,
          lastName: response.lastName,
          birthday: response.birthday,
          newsletter: response.newsletter,
          authToken: response.authToken,
        }; // Typecasting en `Client`
      })
    );
  }

  public getHistory(): Observable<Cart[]> {
    const url = `${this.apiUrl}/api/clients/login`;

    if (this.isConnected() && this.client) {
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.client.authToken);
      return this.http.get(`${this.apiUrl}/api/clients/${this.client.id}/commands`, {headers}).pipe(
        map(
          (body: any) => {
            let history: Cart[] = [];
            body.forEach((element:any) => {
              history.push({id:element.id, articles:element.commandLines});
            })
            return history;
          }
        )
      )
    } else {
      return new Observable();
    }
  }

  public updateClient(clientId: number, clientUpdateDTO: any): Observable<Client>  {
    if (this.isConnected() && this.client) {
      const url = `${this.apiUrl}/api/clients/${clientId}`;
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + this.client.authToken);
      return this.http.put<any>(url, clientUpdateDTO, { headers }).pipe(
        map((response: any) => {
          console.log("Réponse de l'API reçue");
          return {
            id: response.id,
            user: {
              id: response.user.id,
              username: response.user.username,
              password: response.user.password,
              authority: response.user.authority,
            },
            cart: response.currentCart, // Remplir si nécessaire
            firstName: response.firstName,
            lastName: response.lastName,
            birthday: response.birthday,
            newsletter: response.newsletter,
            authToken: response.authToken,
          }; // Typecasting en `Client`
        })
      );
    } else {
      return new Observable();
    }
  }

  public disconnect(): void {
    this.isConnect(false);
  }

  private isConnect(value: boolean): void {
    this.isconnected.next(value);
    this.connected = value;
  }

  public connectFromCart(){
    this.connectFromAnotherPlace = true;
  }

  public isConnectingFromAnotherPlace():boolean{
    return this.connectFromAnotherPlace;
  }
}
