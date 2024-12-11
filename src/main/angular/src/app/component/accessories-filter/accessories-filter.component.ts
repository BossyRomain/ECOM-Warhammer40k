import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-accessories-filter',
  templateUrl: './accessories-filter.component.html',
  styleUrl: './accessories-filter.component.css'
})
export class AccessoriesFilterComponent {

  readonly DICE_IMG_URL: string = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/dice_";

  readonly TERRAIN_IMG_URL: string = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/decor_";

  readonly CODEX_IMG_URL: string = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/book_";

  selectedCategory: string = "dice";

  constructor(private router: Router) {
    this.router.navigate([], {queryParams: {type: this.selectedCategory}});
  }

  public getIconImgUrl(category: string): string {
    let selected = this.selectedCategory == category ? "light" : "dark";
    if (category == "dice") {
      return this.DICE_IMG_URL + selected + ".png";
    } else if (category == "terrain") {
      return this.TERRAIN_IMG_URL + selected + ".png";
    } else if (category == "rules_and_codex") {
      return this.CODEX_IMG_URL + selected + ".png";
    }
    return "";
  }

  public selectCategory(category: string): void {
    this.selectedCategory = category;
    this.router.navigate([], {queryParams: {type: this.selectedCategory}});
  }
}
