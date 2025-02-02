import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { ProductServiceService } from './service/product-service.service';
import { ProductSheetComponent } from './component/product-sheet/product-sheet.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone:false,
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'angular';
  constructor(private router: Router){}
  
  onClick(elm:number){
    this.router.navigate(['/product', elm]);
  }

}
