import { CommonModule } from '@angular/common';
import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { Cart } from '../../model/cart';

@Component({
  selector: 'history-item',
  templateUrl: './history-item.component.html',
  styleUrl: './history-item.component.css',
  imports: [CommonModule],
  standalone: true
})
export class HistoryItemComponent {
  @Input() cart!:Cart;
  @Input() index!:number;
  @Output() click = new EventEmitter<void>();

  sumProduct():number {
    let sum:number = 0;
    this.cart?.articles.forEach((element)=> sum += element.quantity * element.product.unitPrice);
    return sum;
  }

}
