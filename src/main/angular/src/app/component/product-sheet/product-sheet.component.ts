import { Component, input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductServiceService } from '../../service/product-service.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { error } from 'console';
import { CartServiceService } from '../../service/cart-service.service';
import { Product } from '../../model/product';


@Component({
  selector: 'product-sheet',
  standalone: true,
  imports: [ CommonModule],
  templateUrl: './product-sheet.component.html',
  styleUrl: './product-sheet.component.css'
})
export class ProductSheetComponent implements OnInit {
    article?:Product;
    public id:number = 0;
    public productName:string = "Capitaine des Blood Angels";
    public price:number = 34.00;
    public stock:number = 0;
    public allImages:string[] = [
      "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/compte.png",  
      "https://www.warhammer.com/app/resources/catalog/product/920x950/99120101416_BACaptain01.jpg?fm=webp&w=1200&h=1237",
      "https://www.warhammer.com/app/resources/catalog/product/threeSixty/99120101416_WH40kSMBACaptainOTT1360/01-01.jpg?fm=webp&w=670&h=670",
      "https://www.warhammer.com/app/resources/catalog/product/threeSixty/99120101416_WH40kSMBACaptainOTT2360/01-01.jpg?fm=webp&w=670&h=670",
      "https://www.warhammer.com/app/resources/catalog/product/920x950/99120101416_BACaptain01.jpg?fm=webp&w=1200&h=1237"
    ];
    mainImg?:string = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/compte.png";

    description:string = "Une figurine de puissant capitaine Blood Angels. Il est capable de s'adapter à toutes les situations avec un arsenal varié d'armes et de reliques.";
    numberOfArticle:number = 0;

    constructor(private productService: ProductServiceService, private activatedRoute: ActivatedRoute, private route:Router, private cartService:CartServiceService) {}

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

    public addArticleToCart():void{
      console.log("Achat " + this.numberOfArticle);
      if(this.numberOfArticle != 0 && this.article != undefined){
        this.cartService.addProductToCart(0, this.article.id, this.numberOfArticle);
      }
      
    }

    public getObjectById(id:number): void{
      console.log("Get object " + id);
      
      this.allImages = [];
      this.productService.getProductById(id).subscribe( 
        value => {
          this.productName= value.name;
          this.article  = value;
          this.mainImg = this.article.mainImage.url;
          value.images.forEach((temp)=>{
            this.allImages.push(temp.url);
          });
        },
        error => {
          console.log("A problem occured when Accessing to object " + id);
        }
      );    
      
       
    }

    public changeIndex(id:number){
      this.mainImg = this.article?.images[id].url;
    }

}
