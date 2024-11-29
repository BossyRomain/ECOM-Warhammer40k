import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { CommandLine } from '../../model/command-line';

@Component({
  selector: 'cart-item',
  templateUrl: './cart-item.component.html',
  styleUrl: './cart-item.component.css',
  standalone:true,
  imports: [CommonModule]
})
export class CartItemComponent {
  @Input() commandLine?: CommandLine;
  

  changeValue(){
    
  }
}
