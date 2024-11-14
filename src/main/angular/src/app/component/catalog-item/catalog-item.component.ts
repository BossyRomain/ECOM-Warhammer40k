import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductCatalog } from '../../model/product-catalog';


@Component({
  selector: 'app-catalog-item',
  templateUrl: './catalog-item.component.html',
  standalone:true,
  imports:[CommonModule],
  styleUrl: './catalog-item.component.css',
})

export class CatalogItemComponent {
  
  @Input() article!: ProductCatalog;
}
