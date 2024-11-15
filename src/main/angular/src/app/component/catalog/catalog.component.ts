import { Component, OnInit } from '@angular/core';
import { ProductCatalog } from '../../model/product-catalog';
import { ProductServiceService } from '../../service/product-service.service';
import { CatalogItemComponent } from '../catalog-item/catalog-item.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.css',
  standalone: true,
  imports: [CommonModule, CatalogItemComponent]
})
export class CatalogComponent implements OnInit {
  
  constructor(private productService: ProductServiceService) {}

  productList: ProductCatalog[] = [] 
  
  ngOnInit(): void {
    this.productService.getProductsCatalogue().subscribe(
      data => {
        this.productList = data;  // Remplissage de productList avec les données de l'API
        console.log('Liste des produits:', this.productList);
      },
      (error) => {
        console.error('Erreur lors de la récupération des produits:', error);  // Gestion des erreurs
      }
    );
  }

}
