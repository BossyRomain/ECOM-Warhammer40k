import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CatalogComponent } from '../catalog/catalog.component';
import { CartServiceService } from '../../service/cart-service.service';
import { ClientServiceService } from '../../service/client-service.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  standalone:true,
  imports:[CommonModule]
})
  
  
export class HeaderComponent {
  cartItemCount: number = 0;
  isconnected: boolean = false;
  
  ngOnInit(): void {

    this.cartService.cartItems$.subscribe(count => { //Abonnement a l'observable pour mettre a jour automatiquement le nombre d'article dans le panier
      this.cartItemCount = count;
    });

    this.clientService.isconnected$.subscribe(value =>{
      this.isconnected = value;
      })


  }
  
  constructor(private router: Router, private activatedRoute:ActivatedRoute, private cartService : CartServiceService, private clientService:ClientServiceService) { }
  
  public access_account() {
    if(this.clientService.isConnected()){
      //this.router.navigate(["/account"]);
      this.router.navigate(["/history", this.clientService.client?.id]);
    }else{
      this.router.navigate(["/account"]);
    }
    
  }

  public filter() {
    //Open Filter menu
  }

  public help() {
    this.router.navigate(["/help"]);
  }

  public burger_menu() {
    this.router.navigate(["/product", 0]);
  }

  public accesToCart() {
    if(this.clientService.isConnected()){
      this.router.navigate(['/cart', this.clientService.client?.id])
    }else{
      this.router.navigate(['/cart', 0]);
    }
    
  }
  public search(searchText: string) {
    this.router.navigate(["/catalog/search"], {relativeTo: this.activatedRoute, queryParams: {search:searchText, page:0}, });
    
  }

  public goToMain(){
    this.router.navigate(["/catalog/search"], {relativeTo: this.activatedRoute, queryParams: {search:"", page:0}, });
  }

  public onEnter(event:KeyboardEvent, searchText:string){
    if(event.key === "Enter"){
      this.search(searchText);
    }
  }

  clearInput(inputElement: HTMLInputElement): void {
    inputElement.value = ''; // Réinitialise la valeur du champ
  }

}
