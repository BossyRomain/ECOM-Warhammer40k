import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { ProductServiceService } from './service/product-service.service';
import { ProductSheetComponent } from './product-sheet/product-sheet.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [ProductSheetComponent]  
})
export class AppComponent {
  title = 'angular';

}
