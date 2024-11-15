import { Component, Input, OnInit } from '@angular/core';
import { ProductCatalog } from '../../model/product-catalog';
import { ProductServiceService } from '../../service/product-service.service';
import { CatalogItemComponent } from '../catalog-item/catalog-item.component';
import { CommonModule } from '@angular/common';
import { NavigationStart, Router } from '@angular/router';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.css',
  standalone: true,
  imports: [CommonModule, CatalogItemComponent]
})
  


export class CatalogComponent implements OnInit {
  
  constructor(private productService: ProductServiceService, private router: Router) {

    this.ngOnInit()
  }

  @Input() productList: ProductCatalog[] = [] 

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
    console.log("AAAAAAAAA\nAAAAA\n\nAAAAA\nAAAAA\nAAAAA\nAAAAAAAAAA\nAAAAA\nAAAAA\nAAAAA\nAAAAAAAAAAAAAAAAAAA\nAAAAAA")
  }

  loadCatalog(): void {
    this.productService.getProductsCatalogue().subscribe(
      data => this.productList = data,
      error => console.error('Erreur lors du chargement du catalogue :', error)
    );
  }

}
