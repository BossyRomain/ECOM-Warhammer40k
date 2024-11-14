import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Product } from '../../model/Product';
import { CommonModule } from '@angular/common';

export interface Article{
  name: string;
  stock: number;
  price: number;
  description: string;
  urlImage: string;
};

@Component({
  selector: 'app-catalog-item',
  templateUrl: './catalog-item.component.html',
  standalone:true,
  imports:[CommonModule],
  styleUrl: './catalog-item.component.css',
})

export class CatalogItemComponent {
  @Input() article!: Article;
}
