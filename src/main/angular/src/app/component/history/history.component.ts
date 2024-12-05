import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { HistoryItemComponent } from '../history-item/history-item.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientServiceService } from '../../service/client-service.service';
import { Cart } from '../../model/cart';
@Component({
  selector: 'history',
  templateUrl: './history.component.html',
  styleUrl: './history.component.css',
  standalone: true,
  imports: [CommonModule, HistoryItemComponent]
})
export class HistoryComponent implements OnInit {
  constructor(private activatedRoute:ActivatedRoute, private router:Router, private clientService:ClientServiceService){

  }

  public carts:Cart[] = [];

  ngOnInit(){
    this.activatedRoute.params.subscribe(
      (params) => {
        this.clientService.getHistory().subscribe(
          (value)=>{
            this.carts = value;
          }
        )
      }
    );
  }

  public onClick(id:number){
    console.log("Cart = " + id);
  }


}
