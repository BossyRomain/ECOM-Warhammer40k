import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import { BankDetailsComponent } from "./bank-details/bank-details.component";
import { HeaderComponent } from "./header/header.component";

@Component({
  selector: 'app-root',
  standalone:false,
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'angular';

  constructor(private http: HttpClient) {
  }

  getCount(): Observable<any> {
    return this.http.get("http://localhost:8080/api/products");
  }

  onClick(): void {
    console.log("click");
    this.getCount().subscribe(response => {
      console.log(response)
    });
  }
}
