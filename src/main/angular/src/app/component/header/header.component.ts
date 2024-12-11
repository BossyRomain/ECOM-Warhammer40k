import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CartServiceService} from '../../service/cart-service.service';
import {ClientServiceService} from '../../service/client-service.service';
import { Client } from '../../model/client';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  standalone: true,
  imports: [CommonModule]
})


export class HeaderComponent implements OnInit {
  cartItemCount: number = 0;
  isconnected: boolean = false;

  ngOnInit(): void {

    this.cartService.cartItems$.subscribe(count => { //Abonnement a l'observable pour mettre a jour automatiquement le nombre d'article dans le panier
      this.cartItemCount = count;
    });

    this.clientService.isconnected$.subscribe(value => {
      this.isconnected = value;
    });

    this.cartService.cartItems$.subscribe(count => {
      this.activatedRoute.queryParamMap?.subscribe(map => {
        const elem = document.querySelector('.product-type-img');
        if (map.get("type") != null) {
          const type = map.get("type");
          (elem as HTMLElement).style.display = 'block';

          let src = "";
          switch (type) {
            case "dice":
              src = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/dice_dark.png";
              break
            case "figurine":
              src = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/figurines.png";
              break;
            case "paint":
              src = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/peinture.png";
              break;
            case "rules_and_codex":
              src = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/book_dark.png";
              break;
            case "terrain":
              src = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/decor_dark.png";
              break;
          }
          (elem as HTMLImageElement).src = src;
        } else {
          (elem as HTMLElement).style.display = 'none';
        }
      });
    });
  }

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private cartService: CartServiceService, private clientService: ClientServiceService) {
  }

  public access_account() {
    if (this.clientService.isConnected() && this.clientService.client != null) {
      let client: Client = this.clientService.client;
      this.router.navigate([`/userInformation` ,this.clientService.client?.id]);
    }else{
      this.router.navigate(["/account"]);
    }

  }

  public filter() {
    //Open Filter menu
    const elem = document.querySelector('.filters');
    (elem as HTMLElement).style.display = 'flex';
  }

  public closeFilters(): void {
    const elem = document.querySelector('.filters');
    (elem as HTMLElement).style.display = 'none';
  }

  public filterSelected(num: number): void {
    this.closeFilters();
    switch (num) {
      case 0:
        this.router.navigate(["/filter/figurines"]);
        break
      case 1:
        this.router.navigate(["/catalogue/search"], {queryParams: {type: "paint"}});
        break
      case 2:
        this.router.navigate(["/filter/accessories"]);
        break
    }
  }

  public help() {
    this.router.navigate(["/help"]);
  }

  public burger_menu() {
    this.router.navigate(["/product", 0]);
  }

  public accesToCart() {
    if (this.clientService.isConnected()) {
      this.router.navigate(['/cart', this.clientService.client?.id])
    } else {
      this.router.navigate(['/cart', 0]);
    }

  }

  public search(searchText: string) {
    this.router.navigate(["/catalog/search"], {
      relativeTo: this.activatedRoute,
      queryParams: {search: searchText, page: 0},
    });

  }

  public goToMain() {
    this.router.navigate(["/catalog/search"], {relativeTo: this.activatedRoute, queryParams: {search: "", page: 0},});
  }

  public onEnter(event: KeyboardEvent, searchText: string) {
    if (event.key === "Enter") {
      this.search(searchText);
    }
  }

  clearInput(inputElement: HTMLInputElement): void {
    inputElement.value = ''; // Réinitialise la valeur du champ
  }

}
