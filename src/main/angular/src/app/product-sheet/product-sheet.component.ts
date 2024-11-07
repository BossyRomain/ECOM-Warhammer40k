import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductServiceService } from '../service/product-service.service';
import { Observable } from 'rxjs';
import { Console, error } from 'console';



@Component({
  selector: 'product-sheet',
  standalone: true,
  imports: [ CommonModule],
  templateUrl: './product-sheet.component.html',
  styleUrl: './product-sheet.component.css'
})
export class ProductSheetComponent {
    public id:number = 0;
    public productName:string = "Capitaine des Blood Angels";
    public price:number = 34.00;
    public allImages:string[] = [
      "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/compte.png",  
      "https://www.warhammer.com/app/resources/catalog/product/920x950/99120101416_BACaptain01.jpg?fm=webp&w=1200&h=1237",
      "https://www.warhammer.com/app/resources/catalog/product/threeSixty/99120101416_WH40kSMBACaptainOTT1360/01-01.jpg?fm=webp&w=670&h=670",
      "https://www.warhammer.com/app/resources/catalog/product/threeSixty/99120101416_WH40kSMBACaptainOTT2360/01-01.jpg?fm=webp&w=670&h=670",
      "https://www.warhammer.com/app/resources/catalog/product/920x950/99120101416_BACaptain01.jpg?fm=webp&w=1200&h=1237",
    ];
    mainImg:string = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/compte.png";

    description:string = "Une figurine de puissant capitaine Blood Angels. Il est capable de s'adapter à toutes les situations avec un arsenal varié d'armes et de reliques.";
    numberOfArticle:number = 0;

    constructor(private productService: ProductServiceService) {}

    public validateNumber(event: Event):void{
      const inputValue = (event.target as HTMLInputElement).valueAsNumber;
      this.numberOfArticle = inputValue;
    }

    public addArticleToCart(htmlInput:HTMLInputElement):void{
      console.log("Achat");
      this.numberOfArticle = 0;
      htmlInput.value = "0";
    }

    public mockGet(htmlInput:HTMLInputElement){
      this.getObjectById(1);
    }

    public getObjectById(id:number): void{
      console.log("Get object " + id);
      this.productService.getProductById(id).subscribe( 
        value => {
          this.id = value.id;
          this.productName = value.name;
          this.price = value.price;
          this.mainImg = value.url;
        }
      )      
    }

    public changeIndex(id:number){
      this.mainImg=this.allImages[id];
    }

}
