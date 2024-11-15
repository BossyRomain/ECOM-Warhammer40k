import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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

  constructor(private route: ActivatedRoute, private cartService:CartServiceService, private clientService:ClientServiceService){}
  
  ngOnInit(): void {
    if(this.clientService.isConnected()){
      
    }
  }

  public getCart():CommandLine[]{
    return this.clientService.currentCart;
  } 


}
