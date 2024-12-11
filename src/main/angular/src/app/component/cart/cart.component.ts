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
  public toasts: string[] = [];
  public toasts2: string[] = [];
  ngOnInit(): void {
    console.log(this.cartService.currentCart);
    this.cart = this.cartService.currentCart;

    this.route.queryParams.subscribe(params => {
      this.errorMessage = params['errorMessage'] || null;
      if(params['remove']){
        this.showToast("Your article has been removed")
      }
      if(params['payement']){
        this.showToast("Payment Complete \n An email has been sent to your adress");
      }
      
    });

    this.activatedRoute.params.subscribe(
      (params)=> {
        if(params['id'] != 0){
          console.log("params");
          console.log(params['id']);
          this.cartService.getCartOfClient(params['id']).subscribe(
            (value) => {
              this.cartService.id = value.id;
              console.log(this.cartService.currentCart.length);
              console.log("connection component");
              console.log(value);
              this.cartService.currentCart = [];
              value.commandLines.forEach((elm:CommandLine) => {
                this.cartService.currentCart.push(elm);
              });
              this.cart = this.cartService.currentCart;
            }
  
          );
        }else{
          this.cart = this.cartService.currentCart;
        }
        
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

  private showToast(message: string): void {

    if (this.toasts.length > 0) {
      this.toasts.shift();
    }
    this.toasts.push(message);

    // Supprime le toast après 3 secondes
    setTimeout(() => {
      this.toasts.shift();
    }, 3000);
  }

  private showToast2(message: string): void {

    if (this.toasts.length > 0) {
      this.toasts2.shift();
    }
    this.toasts2.push(message);

    // Supprime le toast après 3 secondes
    setTimeout(() => {
      this.toasts2.shift();
    }, 3000);
  }


}
