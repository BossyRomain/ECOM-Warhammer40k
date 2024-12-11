import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ProductServiceService} from '../../service/product-service.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {CartServiceService} from '../../service/cart-service.service';
import {Product} from '../../model/product';
import {ClientServiceService} from '../../service/client-service.service';


@Component({
  selector: 'product-sheet',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-sheet.component.html',
  styleUrl: './product-sheet.component.css'
})
export class ProductSheetComponent implements OnInit, AfterViewInit {
  article!: Product;
  public id: number = 0;
  public productName: string = "Capitaine des Blood Angels";
  public price: number = 34.00;
  public stock: number = 0;
  public allImages: string[] = [
    "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/compte.png",
    "https://www.warhammer.com/app/resources/catalog/product/920x950/99120101416_BACaptain01.jpg?fm=webp&w=1200&h=1237",
    "https://www.warhammer.com/app/resources/catalog/product/threeSixty/99120101416_WH40kSMBACaptainOTT1360/01-01.jpg?fm=webp&w=670&h=670",
    "https://www.warhammer.com/app/resources/catalog/product/threeSixty/99120101416_WH40kSMBACaptainOTT2360/01-01.jpg?fm=webp&w=670&h=670",
    "https://www.warhammer.com/app/resources/catalog/product/920x950/99120101416_BACaptain01.jpg?fm=webp&w=1200&h=1237",
  ];
  mainImg?: string = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/compte.png";

  description: string = "Une figurine de puissant capitaine Blood Angels. Il est capable de s'adapter à toutes les situations avec un arsenal varié d'armes et de reliques.";
  numberOfArticle: number = 0;

  toasts: string[] = [];

  public setImages:String[] = []
  public setIndex:number = 0;
  public imagesIndex:number = 0;
  public maxSize:number = 3;
  @ViewChild('inputNumberOfElement') numberInput!: ElementRef;
  constructor(private productService: ProductServiceService, private activatedRoute: ActivatedRoute, private route: Router, private cartService: CartServiceService, private clientService: ClientServiceService) {
  }

  ngAfterViewInit(): void {

    if(this.article && this.cartService.containsElement(this.article.id)){
      this.numberInput.nativeElement.value = this.cartService.getAmountOfProduct(this.article.id);
    }
  }

  ngOnInit() {

    this.activatedRoute.params.subscribe((params: Params) => {
      let userId = params['id'];
      this.getObjectById(userId);
      if(this.article && this.cartService.containsElement(this.article.id)){
        this.numberInput.nativeElement.value = this.cartService.getAmountOfProduct(this.article.id);
      }

    });

  }

  public nextImage(){
    console.log("next: " + this.imagesIndex);
    if(this.imagesIndex < this.article.images.length - this.maxSize){
      this.imagesIndex++;
      let i = 0;
        this.setImages = [];
        while(i <  this.maxSize){
          this.setImages.push(this.article.images[this.imagesIndex+i].url);
          i++;
        }
    }
  }

  public previousImage(){
    console.log("prev: " + this.imagesIndex);
    if(this.imagesIndex != 0){
      this.imagesIndex--;
      let i = this.imagesIndex;
        this.setImages = [];
        while(i < this.imagesIndex + this.maxSize){
          this.setImages.push(this.article.images[i].url);
          i++;
        }
    }
  }

  public validateNumber(event: Event): void {
    const inputValue = (event.target as HTMLInputElement).valueAsNumber;
    this.numberOfArticle = inputValue;
  }

  public addArticleToCart(): void {
    if (this.numberOfArticle != 0 && this.article != undefined) {
      if(this.article.stock == 0){
        this.showToast(`Warning: this article is momentarily not avaible`);
      }else{
        this.cartService.addProductToCart(this.clientService.client ? this.clientService.client.id : 0, this.article.id, this.numberOfArticle);
        this.showToast(`${this.numberOfArticle} ${this.article.name} ` + (this.numberOfArticle == 1 ? "has" : "have") + ` added to the cart`);
      }
      
    }

  }

  private showToast(message: string): void {

    if (this.toasts.length > 0) {
      this.toasts.shift();
    }
    console.log("Ajout du toast");
    this.toasts.push(message);

    // Supprime le toast après 3 secondes
    setTimeout(() => {
      console.log("delete du toast");
      this.toasts.shift();
    }, 3000);
  }


  
  


  public getObjectById(id: number): void {
    console.log("Get object " + id);

    this.allImages = [];
    this.productService.getProductById(id).subscribe(
      value => {
        this.productName = value.name;
        this.article = value;
        this.article.description = this.article?.description.replaceAll("\n", "<br>");
        this.mainImg = this.article.mainImage.url;
        value.images.forEach((temp) => {
          this.allImages.push(temp.url);
        });
        let i = 0;
        while(this.article && i < this.article.images.length && i < this.maxSize){
          console.log(this.article.images)
          this.setImages.push(this.article.images[i].url);
          i++;
        }
        this.maxSize = i;
      },
      error => {
        console.log("A problem occured when Accessing to object " + id);
      }
    );


  }

  public changeIndex(id: number) {
    this.mainImg = this.article?.images[id].url;
  }

}
