import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductCatalog } from '../../model/product-catalog';
import { Router } from '@angular/router';


@Component({
  selector: 'app-catalog-item',
  templateUrl: './catalog-item.component.html',
  standalone:true,
  imports:[CommonModule],
  styleUrl: './catalog-item.component.css',
})

export class CatalogItemComponent {
  
  @Input() article!: ProductCatalog;

  constructor(private router: Router) { }


  public detailedProduct(id: number) {
    this.router.navigate(["/product", id ]);
  }

  public addCart(id: number) {
    //A faire
  }


}
