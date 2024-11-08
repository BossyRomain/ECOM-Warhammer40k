import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Product } from '../model/product';

@Component({
  selector: 'app-catalog-item',
  templateUrl: './catalog-item.component.html',
  standalone:true,
  styleUrl: './catalog-item.component.css',
})
  
export default class CatalogItemComponent {
  @Input() article!: Product;
}
