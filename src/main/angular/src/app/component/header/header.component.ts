import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CatalogComponent } from '../catalog/catalog.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  standalone:true,
  imports:[CommonModule]
})
  
  
export class HeaderComponent {
  
  constructor(private router: Router) { }
  
  public access_account() {
    this.router.navigate(["/account"]);
  }

  public burger_menu() {
    this.router.navigate(["/product", 0]);
  }

  public accesToCart() {
    this.router.navigate(['/cart', 0]);
  }
  public search(searchText: string) {
    this.router.navigate(["/catalog/search", searchText]);
    
  }

  public onEnter(event:KeyboardEvent, searchText:string){
    if(event.key === "Enter"){
      this.search(searchText);
    }
  }

}
