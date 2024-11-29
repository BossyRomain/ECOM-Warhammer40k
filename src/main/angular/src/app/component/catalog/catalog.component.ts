import { AfterViewChecked, AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
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
  


export class CatalogComponent implements OnInit, AfterViewInit {
  
  constructor(private productService: ProductServiceService, private router: Router, private activatedRoute:ActivatedRoute) {

    this.ngOnInit()
  }
  public lastPage: number = 0;
  private numPage:number = 1;
  @Input() productList: ProductCatalog[] = [];
  @ViewChild('pageNumber') myInput!: ElementRef<HTMLInputElement>;
  private search:string = "";
  ngOnInit(): void {
    console.log("path: " + this.activatedRoute.routeConfig?.path);
    console.log("route: " + this.activatedRoute.outlet);
    this.activatedRoute.queryParamMap.subscribe((params)=>{
      this.search = params.get("search") || "";
      this.numPage = Number(params.get("page")) || 0;
      if(this.myInput){
        this.myInput.nativeElement.value = `${this.numPage +1}`;
      }
      console.log("search = " + this.search +  " page = " + this.numPage );
      this.loadSearch(this.search, this.numPage);
        

    });
    
  }


  ngAfterViewInit(): void {
    if (this.myInput) {
      this.myInput.nativeElement.value = `${this.numPage +1}`;
    }
  }


  loadCatalog(): void {
    this.productService.getProductsCatalogue().subscribe(
      data => this.productList = data,
      error => console.error('Erreur lors du chargement du catalogue :', error)
    );
  }


  public loadSearch(query:string, pageN:number=0){
    this.productService.searchProducts(query, pageN).subscribe(
      (data) => { this.productList = data, this.lastPage = this.productService.getMaxPages() },
      (error) => { console.error('Erreur lors du chargement du catalogue :', error) }
    );

  }

  previousPage(): void{
    //charger page précédente avec la search courante (si y'a une page suivante)
    if(this.numPage -1  >= 0){
      this.router.navigate(["/catalog/search"], {relativeTo: this.activatedRoute, queryParams: {search:this.search, page:this.numPage-1} });
    }
    
  }

  nextPage(): void{
    //charger page suivante avec la search courante (si y'a une page suivante)
    if(this.numPage +1  < this.productService.getMaxPages()){
      this.router.navigate(["/catalog/search"], {relativeTo: this.activatedRoute, queryParams: {search:this.search, page:this.numPage+1} });
    }
    
  }

  onKeyDown(event: KeyboardEvent) {
    const key = event.key;
    // Si la touche n'est pas un chiffre, un backspace, ou un delete, on empêche la saisie
    console.log("input");
    if (!/^\d$/.test(key) && key !== 'Backspace' && key !== 'Delete') {
      console.log("interdit");
      event.preventDefault();
    }
    if (key === "Enter") {
      this.numPage =  Number(this.myInput.nativeElement.value)-1;
      this.loadSearch(this.search, Number(this.myInput.nativeElement.value)-1);
    }
  }

}
