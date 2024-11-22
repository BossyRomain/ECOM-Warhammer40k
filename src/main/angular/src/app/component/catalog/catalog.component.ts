import { Component, Input, OnInit } from '@angular/core';
import { ProductCatalog } from '../../model/product-catalog';
import { ProductServiceService } from '../../service/product-service.service';
import { CatalogItemComponent } from '../catalog-item/catalog-item.component';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, NavigationStart, Params, Router } from '@angular/router';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.css',
  standalone: true,
  imports: [CommonModule, CatalogItemComponent]
})
  


export class CatalogComponent implements OnInit {
  
  constructor(private productService: ProductServiceService, private router: Router, private activatedRoute:ActivatedRoute) {

    this.ngOnInit()
  }

  @Input() productList: ProductCatalog[] = [] 

  ngOnInit(): void {
    console.log("path: " + this.activatedRoute.routeConfig?.path);
    console.log("route: " + this.activatedRoute.outlet);
    this.activatedRoute.params.subscribe((params:Params) =>
    {
      if(params["query"]){
        this.loadDefaultSearch(params["query"]);
      }else{
        this.loadCatalog();
      }    
  });
    
  }

  loadCatalog(): void {
    this.productService.getProductsCatalogue().subscribe(
      data => this.productList = data,
      error => console.error('Erreur lors du chargement du catalogue :', error)
    );
  }

  public loadDefaultSearch(query:string): void{
    this.loadSearch(query, 1);
  }

  public loadSearch(query:string, pageN:number){
    this.productService.searchProducts(query).subscribe(
      data => this.productList = data,
      error => console.error('Erreur lors du chargement du catalogue :', error)
    );
  }

  previousPage(): void{
    //charger page précédente avec la search courante (si y'a une page suivante)
  }

  nextPage(): void{
    //charger page suivante avec la search courante (si y'a une page suivante)
  }

  onKeyDown(event: KeyboardEvent) {
    const key = event.key;
    // Si la touche n'est pas un chiffre, un backspace, ou un delete, on empêche la saisie
    if (!/^\d$/.test(key) && key !== 'Backspace' && key !== 'Delete') {
      event.preventDefault();
    }
    if (key === "Enter") {
      //Charger la page
    }
  }

}
