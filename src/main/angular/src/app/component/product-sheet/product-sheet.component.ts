import { Component, input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductServiceService } from '../../service/product-service.service';
import { ActivatedRoute, Params } from '@angular/router';
import { error } from 'console';

@Component({
  selector: 'product-sheet',
  standalone: true,
  imports: [ CommonModule],
  templateUrl: './product-sheet.component.html',
  styleUrl: './product-sheet.component.css'
})
export class ProductSheetComponent implements OnInit {
    public id:number = 0;
    public productName:string = "Capitaine des Blood Angels";
    public price:number = 34.00;
    public stock:number = 0;
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

    constructor(private productService: ProductServiceService, private activatedRoute: ActivatedRoute) {}

    ngOnInit(){
      this.activatedRoute.params.subscribe((params: Params) => {
        let userId = params['id'];
        this.getObjectById(userId);
        });
        
    }

    public validateNumber(event: Event):void{
      const inputValue = (event.target as HTMLInputElement).valueAsNumber;
      this.numberOfArticle = inputValue;
    }

    public addArticleToCart(htmlInput:HTMLInputElement):void{
      console.log("Achat");
      let amountOfProduct = Number(htmlInput.value);
      if(amountOfProduct == 0){
        console.log("No object selected");
      }else{
        this.productService.getProductById(amountOfProduct).subscribe(
          value => {
            this.id = value.id;
            this.productName = value.name;
            this.price = value.price;
            this.stock = value.stock;
            this.description = value.description;
          }
        )
        this.numberOfArticle = 0;
        htmlInput.value = "0";
      }

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
          this.allImages = value.images;
        },
        error => {
          console.log("A problem occured when Accessing to object " + id);
        }
      )      
    }

    public changeIndex(id:number){
      this.mainImg=this.allImages[id];
    }

}
