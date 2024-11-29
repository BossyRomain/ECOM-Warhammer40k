import { Injectable } from '@angular/core';
import { CommandLine } from '../model/command-line';
import { environment } from '../../environments/environment';
import { Observable, catchError, map } from 'rxjs';
import { Client } from '../model/client';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClientServiceService {

  constructor(private http: HttpClient) {

  }


  private apiUrl = environment.apiUrl;
  public currentCart: CommandLine[] = [];
  private connected:boolean = false;
  public clientID: number = 0;
  public client?: Client;

  

  public isConnected():boolean{
    return this.connected;
  }

  public seConnecter(email: string, password: string): Observable<Client> {
    const url = `${this.apiUrl}/api/clients/login`;
  
    console.log("Appel à l'API dans le service");
    
    return this.http.post<any>(url, { email, password }).pipe(
      map((response: any) => {
        console.log("Réponse de l'API reçue");
        this.connected = true;
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
  
  public disconnect(): void{
    this.connected = false;
  }
}
