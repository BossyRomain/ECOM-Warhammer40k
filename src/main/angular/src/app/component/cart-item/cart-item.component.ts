import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { CommandLine } from '../../model/command-line';
import { CartServiceService } from '../../service/cart-service.service';
import { ClientServiceService } from '../../service/client-service.service';

@Component({
  selector: 'cart-item',
  templateUrl: './cart-item.component.html',
  styleUrl: './cart-item.component.css',
  standalone:true,
  imports: [CommonModule]
})
export class CartItemComponent {
  @Input() commandLine?:CommandLine;
  @Input() index?:number;

  deleteEntry:boolean = false;
  constructor(private cartService:CartServiceService){

  }
  changeAmount(newAmount:string){
    if(this.commandLine != undefined && this.index != undefined){
      if(Number(newAmount) == 0){
        this.deleteEntry = true;
      }else{
        this.commandLine.quantity = this.cartService.updateCart(this.index, Number(newAmount));
      }
      
    }
    
    
  }

  sumProduct():number{
    if(this.commandLine){
      let price = this.commandLine?.quantity* this.commandLine?.product.price;
      return price;
    }
    return 0;
    
  }


}
