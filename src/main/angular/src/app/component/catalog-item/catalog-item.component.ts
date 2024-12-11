import {Component, Input, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ProductCatalog} from '../../model/product-catalog';
import {Router} from '@angular/router';
import {CartServiceService} from '../../service/cart-service.service';
import {ClientServiceService} from '../../service/client-service.service';
import {ProductServiceService} from '../../service/product-service.service';


@Component({
  selector: 'app-catalog-item',
  templateUrl: './catalog-item.component.html',
  standalone: true,
  imports: [CommonModule],
  styleUrl: './catalog-item.component.css',
})

export class CatalogItemComponent implements OnInit {

  @Input() article!: ProductCatalog;
  toasts: string[] = [];

  productTypeUrl: string = "";

  constructor(private router: Router, private cartService: CartServiceService, private clientService: ClientServiceService, private productSerice: ProductServiceService) {
  }

  ngOnInit(): void {
    if (this.article != undefined) {
      switch (this.article.productType) {
        case 'FIGURINE':
          this.productTypeUrl = 'https://ecom-images-storage.s3.eu-north-1.amazonaws.com/figurines.png';
          break;
        case 'RULES_AND_CODEX':
          this.productTypeUrl = 'https://ecom-images-storage.s3.eu-north-1.amazonaws.com/book_light.png';
          break;
        case 'DICE':
          this.productTypeUrl = 'https://ecom-images-storage.s3.eu-north-1.amazonaws.com/dice_light.png';
          break;
        case 'TERRAIN':
          this.productTypeUrl = 'https://ecom-images-storage.s3.eu-north-1.amazonaws.com/decor_light.png';
          break;
        case 'PAINT':
          this.productTypeUrl = 'https://ecom-images-storage.s3.eu-north-1.amazonaws.com/peinture.png';
          break;
      }
    }
  }

  public detailedProduct(id: number) {
    this.router.navigate(["/product", id]);
  }

  public addCart(id: number, name: string) {
    console.log("add cart from catalogue " + id);
    console.log("current stock: " + this.article.stock);
    if(this.article.stock == 0){
      console.log(`Warning: this article is momentarily not avaible`);
      this.showToast(`Warning: this article is momentarily not avaible`);
    }else{
      if (this.clientService.isConnected() && this.clientService.client) {
        console.log(name);
        this.cartService.addProductToCart(this.clientService.client.id, id, 1)
      } else {
        this.cartService.addProductToCart(0, id, 1)
      }
      
  
      this.showToast(`${name} has been added to the cart`);
    }
    
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
