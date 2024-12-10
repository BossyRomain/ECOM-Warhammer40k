import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CartServiceService } from '../../service/cart-service.service';
import { CommandLine } from '../../model/command-line';
import { ClientServiceService } from '../../service/client-service.service';
import { CommonModule } from '@angular/common';
import { CartItemComponent } from '../cart-item/cart-item.component';

@Component({
  selector: 'cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css',
  standalone: true,
  imports: [CommonModule, CartItemComponent]
})
export class CartComponent implements OnInit{

  constructor(private route: ActivatedRoute,
    public cartService: CartServiceService,
    private clientService: ClientServiceService,
    private activatedRoute: ActivatedRoute,
    private router: Router) { }
  
  public cart: CommandLine[] = [];
  public errorMessage: string | null = null;

  ngOnInit(): void {
    console.log(this.cartService.currentCart);
    this.cart = this.cartService.currentCart;

    this.route.queryParams.subscribe(params => {
      this.errorMessage = params['errorMessage'] || null;
    });

    this.activatedRoute.params.subscribe(
      (params)=> {
        console.log(params['id']);
        this.cartService.getCartOfClient(params['id']);
        this.cart = this.cartService.currentCart;
      }
    )
  }



  public pay(){
    if(this.clientService.isConnected()){
      this.router.navigate(["/pay"]);
    }else{
      this.clientService.connectFromCart();
      console.log("eroor log loglog ");
      this.router.navigate(["/account"]);
    
    }
    
  }


}
