import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ProductCatalog} from '../../model/product-catalog';
import {ProductServiceService} from '../../service/product-service.service';
import {CatalogItemComponent} from '../catalog-item/catalog-item.component';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrl: './catalog.component.css',
  standalone: true,
  imports: [CommonModule, CatalogItemComponent]
})

export class CatalogComponent implements OnInit, AfterViewInit {

  constructor(private productService: ProductServiceService, private router: Router, private activatedRoute: ActivatedRoute) {

    this.ngOnInit()
  }

  public lastPage: number = 0;
  private numPage: number = 1;
  @Input() productList: ProductCatalog[] = [];
  @ViewChild('pageNumber') myInput!: ElementRef<HTMLInputElement>;
  private search: string = "";
  private faction: string = "";
  private type: string = "";

  ngOnInit(): void {
    console.log("path: " + this.activatedRoute.routeConfig?.path);
    console.log("route: " + this.activatedRoute.outlet);
    this.activatedRoute.queryParamMap.subscribe((params) => {
      this.search = params.get("search") || "";
      this.numPage = Number(params.get("page")) || 0;
      this.faction = params.get("faction") || "";
      this.type = params.get("type") || "";
      if (this.myInput) {
        this.myInput.nativeElement.value = `${this.numPage + 1}`;
      }
      console.log("search = " + this.search + " page = " + this.numPage + " faction = " + this.faction + " type = " + this.type);
      this.loadSearch(this.search, this.numPage, this.faction, this.type);
    });
  }


  ngAfterViewInit(): void {
    if (this.myInput) {
      this.myInput.nativeElement.value = `${this.numPage + 1}`;
    }
  }

  public loadSearch(query: string, pageN: number = 0, faction: string, type: string) {
    this.productService.searchProducts(query, faction, type, pageN).subscribe(
      (data) => {
        this.productList = data, this.lastPage = this.productService.getMaxPages()
      },
      (error) => {
        console.error('Erreur lors du chargement du catalogue :', error)
      }
    );

  }

  previousPage(): void {
    //charger page précédente avec la search courante (si y'a une page suivante)
    if (this.numPage - 1 >= 0) {
      this.router.navigate([], {
        relativeTo: this.activatedRoute,
        queryParams: {page: this.numPage - 1},
        queryParamsHandling: 'merge'
      });
    }

  }

  nextPage(): void {
    //charger page suivante avec la search courante (si y'a une page suivante)
    if (this.numPage + 1 < this.productService.getMaxPages()) {
      this.router.navigate([], {
        relativeTo: this.activatedRoute,
        queryParams: {page: this.numPage + 1},
        queryParamsHandling: 'merge'
      });
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
      this.numPage = Number(this.myInput.nativeElement.value) - 1;
      this.loadSearch(this.search, Number(this.myInput.nativeElement.value) - 1, this.faction, this.type);
    }
  }

}
