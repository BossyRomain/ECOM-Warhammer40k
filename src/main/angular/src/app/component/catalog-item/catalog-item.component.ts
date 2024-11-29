import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductCatalog } from '../../model/product-catalog';
import { Router } from '@angular/router';
import { CartServiceService } from '../../service/cart-service.service';
import { ClientServiceService } from '../../service/client-service.service';
import { ToastService } from '../../service/toast.service';


@Component({
  selector: 'app-catalog-item',
  templateUrl: './catalog-item.component.html',
  standalone:true,
  imports:[CommonModule],
  styleUrl: './catalog-item.component.css',
})

export class CatalogItemComponent {
  
  @Input() article!: ProductCatalog;

  constructor(private router: Router, private cartService: CartServiceService, private clientService:ClientServiceService, private toastService: ToastService) { }


  public detailedProduct(id: number) {
    this.router.navigate(["/product", id ]);
  }

  public addCart(id: number) {
    console.log("add cart from catalogue " + id);
    this.cartService.addProductToCart(this.clientService.clientID, id, 1);
    this.toastService.showToast("Votre produit a été ajouté au panier");
  }


}
