import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductCatalog } from '../../model/product-catalog';
import { Router } from '@angular/router';
import { CartServiceService } from '../../service/cart-service.service';
import { ClientServiceService } from '../../service/client-service.service';
import { ProductServiceService } from '../../service/product-service.service';


@Component({
  selector: 'app-catalog-item',
  templateUrl: './catalog-item.component.html',
  standalone:true,
  imports:[CommonModule],
  styleUrl: './catalog-item.component.css',
})

export class CatalogItemComponent {
  
  @Input() article!: ProductCatalog;
  toasts: string[] = [];

  constructor(private router: Router, private cartService: CartServiceService, private clientService: ClientServiceService, private productSerice: ProductServiceService) {
    
  }

  public detailedProduct(id: number) {
    this.router.navigate(["/product", id ]);
  }

  public addCart(id: number, name: string) {
    console.log("add cart from catalogue " + id);
    if(this.clientService.isConnected()&& this.clientService.client){
      this.cartService.addProductToCart(this.clientService.client.id, id, 1)
    }else{
      this.cartService.addProductToCart(0, id, 1)
    }
    this.cartService.updateCartLength();

    this.showToast(`${name} a été ajouté au panier`);
  }

  private showToast(message: string): void {
    
    if (this.toasts.length > 0) {
      this.toasts.shift();
    }
    this.toasts.push(message);

    // Supprime le toast après 3 secondes
    setTimeout(() => {
      this.toasts.shift();
    }, 3000);
  }

  


}
