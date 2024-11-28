import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CatalogComponent } from '../catalog/catalog.component';
import { CartServiceService } from '../../service/cart-service.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  standalone:true,
  imports:[CommonModule]
})
  
  
export class HeaderComponent {
  cartItemCount: number = 33;
  
  ngOnInit() {
    
  }
  
  constructor(private router: Router, private activatedRoute:ActivatedRoute, private cartService : CartServiceService) { }
  
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
    this.router.navigate(["/catalog/search"], {relativeTo: this.activatedRoute, queryParams: {search:searchText, page:0}, });
    
  }

  public goToMain(){
    this.router.navigate(["/catalog/search"]);
  }

  public onEnter(event:KeyboardEvent, searchText:string){
    if(event.key === "Enter"){
      this.search(searchText);
    }
  }

}
