import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClientServiceService } from '../../service/client-service.service';
import { Client } from '../../model/client';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  imports: [CommonModule, FormsModule],
  styleUrl: './update-user.component.css',
  standalone: true
})
export class UpdateUserComponent implements OnInit{
  user = {
    lastName: '',
    firstName: '',
    dob: '',
  };

  // Définir un type flexible pour 'errors' avec une index signature
  errors: { [key: string]: string } = {
    lastName: '',
    firstName: '',
    dob: '',
  };

  client!: Client;

  constructor(private router: Router, private clientService: ClientServiceService) {}

  ngOnInit() {
    if (this.clientService.client != null) {
      this.client = this.clientService.client; 
    }
  }
    // Fonction pour réinitialiser les erreurs
    clearError(field: string) {
      this.errors[field] = ''; // Utilisation de la notation entre crochets pour accéder à chaque erreur
    }

  modify() {
    if (!this.user.lastName) {
      this.errors['lastName'] = 'Le nom d\'utilisateur est requis';
    }

    if (!this.user.firstName) {
      this.errors['firstName'] = 'Le prénom est requis';
    }

    if (!this.user.dob) {
      this.errors['dob'] = 'La date de naissance est requise';
    }

    // Vérification si des erreurs existent avant d'envoyer
    if (Object.values(this.errors).some(error => error !== '')) {
      return; // Empêcher la soumission si des erreurs existent
    }


    const clientUpdateDTO = {
      firstName: this.user.firstName,
      lastName: this.user.lastName,
      birthday: this.user.dob
    };

    // Appel au service pour mettre à jour le client
    this.clientService.updateClient(this.client.id, clientUpdateDTO).subscribe(
        (client) => {
          this.clientService.client = client;
          this.router.navigate([`/userInformation` ,this.client.id]);
      },
      (error) => {
        console.error('Erreur lors de la mise à jour du client :', error);
        this.errors['global'] = 'Une erreur est survenue lors de la mise à jour.';
      }
    );

    


    this.router.navigate(["/catalogue/search"])
  }

  cancel() {
    this.router.navigate([`/userInformation` ,this.clientService.client?.id]);
  }
}

