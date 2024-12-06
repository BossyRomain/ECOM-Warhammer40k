import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { CommandLine } from '../../model/command-line';
import { CartServiceService } from '../../service/cart-service.service';
import { ClientServiceService } from '../../service/client-service.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'cart-item',
  templateUrl: './cart-item.component.html',
  styleUrl: './cart-item.component.css',
  standalone:true,
  imports: [CommonModule, FormsModule]
})
export class CartItemComponent implements OnInit {
  @Input() commandLine?:CommandLine;
  @Input() index?:number;

  deleteEntry:boolean = false;
  lastValue:string = "0";
  quantity:string = "1";
  constructor(private cartService:CartServiceService, private router:Router, public clientService:ClientServiceService){

  }

  ngOnInit(): void {
    if(this.commandLine){
      console.log(this.commandLine.quantity);
      this.quantity = ""+ this.commandLine.quantity ;
    }
    
  }


  changeAmount(newAmount:string){
    if(this.commandLine != undefined && this.index != undefined){
      console.log("index = " + this.index);
      if(newAmount != "" && Number(newAmount) == 0){
        this.lastValue = newAmount;
        this.cartService.deleteLine(this.index);
      }else if(newAmount != ""){
        this.lastValue = newAmount;
        this.commandLine.quantity = this.cartService.updateCart(this.index, Number(newAmount));
      }else{
        this.quantity = this.lastValue;
      }
      
    }
    
    
  }

  sumProduct():number{
    if(this.commandLine){
      let price = this.commandLine?.quantity* this.commandLine?.product.unitPrice;
      this.quantity = "" + this.commandLine?.quantity;
      return price;
    }
    return 0;
    
  }


}
