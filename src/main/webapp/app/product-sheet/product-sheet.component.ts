import { Component } from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatTab, MatTabsModule} from '@angular/material/tabs';
import { NgFor } from '@angular/common';

@Component({
  selector: 'jhi-product-sheet',
  standalone: true,
  imports: [MatButtonModule, MatCardModule, MatTabsModule, MatTab, NgFor],
  templateUrl: './product-sheet.component.html',
  styleUrl: './product-sheet.component.scss'
})
export class ProductSheetComponent {
    productName:String = "Capitaine des Blood Angels";
    price:number = 34.00;
    mainImg:String = "https://www.warhammer.com/app/resources/catalog/pr…99120101416_BACaptain01.jpg?fm=webp&w=1200&h=1237";
    allImages:String[] = [
      "https://www.warhammer.com/app/resources/catalog/pr…99120101416_BACaptain01.jpg?fm=webp&w=1200&h=1237",  
      "https://www.warhammer.com/app/resources/catalog/pr…_WH40kSMBACaptainOTT1360/01-01.jpg?fm=webp&w=1200"
    ];
    

    public changeMainImage(nextLink:string){
        this.mainImg = nextLink;
    }

}
