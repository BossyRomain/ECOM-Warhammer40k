import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-menu-burger',
  standalone : true,
  imports : [CommonModule],
  templateUrl: './menu-burger.component.html',
  styleUrls: ['./menu-burger.component.css']
})
export class MenuBurgerComponent {
  menus = [
    {
      name: 'Figurines',
      options: [
        { name: 'Allégeances', isOpen: false },
        { name: 'Toutes les figurines', isOpen: false }
      ],
      isOpen: false,  // Par défaut, les sous-menus sont fermés
      iconURl : "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/figurines.png"
    },
    {
      name: 'Peintures',
      isOpen: false,
      iconURl : "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/peinture.png"
    },
    {
      name: 'Accessoires',
      isOpen: false,
      iconURl : "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/accessoires.png"
    },
    {
      name: 'Aide',
      isOpen: false,
      iconURl : "https://ecom-images-storage.s3.eu-north-1.amazonaws.com/interrogation_point.png"
    }
  ];

  toggleSubMenu(menu: any) {
    // Ferme tous les menus sauf celui sur lequel on a cliqué
    this.menus.forEach(m => {
      if (m !== menu) {
        m.isOpen = false;
      }
    });

    // Toggle l'état du menu cliqué
    menu.isOpen = !menu.isOpen;
  }

  toggleSubOption(option: any) {
    // Toggle l'état de l'option (ouvrir/fermer le sous-menu imbriqué)
    option.isOpen = !option.isOpen;
  }
}
