import { Component } from '@angular/core';

import { Client } from '../../model/client';
import { ClientServiceService } from '../../service/client-service.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-show-user-information',
  templateUrl: './show-user-information.component.html',
  imports: [CommonModule, FormsModule],
  standalone: true,
  styleUrl: './show-user-information.component.css'
})
export class ShowUserInformationComponent {

  client!: Client;

  constructor(private router: Router, private clientService: ClientServiceService) {}

  ngOnInit() {
    if (this.clientService.client != null) {
      this.client = this.clientService.client; 
    }
  }

  modify() {
      this.router.navigate([`/userInformationModify/`, this.client.id])
  }
  
  historique() {
      this.router.navigate([`/history/`, this.client.id]);
    }


}
