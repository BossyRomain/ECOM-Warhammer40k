import { Component } from '@angular/core';
import { Input } from '@angular/core';


export interface Article{
  name: string;
  stock: number;
  price: number;
  description: string;
  urlImage: string;
};

@Component({
  selector: 'app-catalog-item',
  standalone:false,
  templateUrl: './catalog-item.component.html',
  styleUrl: './catalog-item.component.css',
})

export class CatalogItemComponent {
  @Input() article!: Article;
}
