import {Component} from '@angular/core';
import {AllegianceService} from '../../service/allegiance.service';
import {Allegiance} from '../../model/allegiance';
import {Router} from '@angular/router';

@Component({
  selector: 'app-figurine-filter',
  templateUrl: './figurine-filter.component.html',
  styleUrl: './figurine-filter.component.css'
})
export class FigurineFilterComponent {

  readonly GROUPS_IMG_URL: string = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/groups/";
  readonly FACTIONS_IMGS_URL: string = "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/factions/";

  allegiances: Record<string, string[]> = {};

  selectedGroup: string = "";

  selectedFaction: number = 0;

  constructor(private service: AllegianceService, private router: Router) {
    this.service.getAll().subscribe(
      (data: Allegiance[]) => {
        for (const a of data) {
          this.allegiances[a.group] = [];
        }

        for (const a of data) {
          this.allegiances[a.group].push(a.faction);
        }

        this.selectedGroup = Object.keys(this.allegiances)[0];
        this.selectedFaction = 0;
      }
    );
  }

  public getElemName(elem: string): string {
    let arr = elem.split('_');
    for (let i = 0; i < arr.length; i++) {
      let s = arr[i];
      s = s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();
      arr[i] = s;
    }
    return arr.join(" ");
  }

  public getGroupImg(group: string): string {
    return this.GROUPS_IMG_URL + group.toLowerCase().replaceAll(' ', '_') + (this.selectedGroup == group ? '_selected' : '') + '.png';
  }

  public getFactionImg(faction: number): string {
    return this.FACTIONS_IMGS_URL + this.allegiances[this.selectedGroup][faction].toLowerCase() + '.png';
  }

  moveLeft(): void {
    var length = this.allegiances[this.selectedGroup].length;
    this.selectedFaction = (this.selectedFaction + length - 1) % length;
  }

  moveRight(): void {
    var length = this.allegiances[this.selectedGroup].length;
    this.selectedFaction = (this.selectedFaction + 1) % length;
  }

  selectGroup(group: string): void {
    this.selectedGroup = group;
    this.selectedFaction = 0;
  }

  factionSelected(): void {
    const faction = this.allegiances[this.selectedGroup][this.selectedFaction].toLowerCase();
    this.router.navigate(['/catalog/search'], {queryParams: {faction: faction, type: "figurine"}});
  }

  protected readonly Object = Object;
}
