import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';  // Importation du router d'Angular
import { FormsModule } from '@angular/forms';  // Importation de FormsModule
import { ClientServiceService } from '../../service/client-service.service';
import { CartServiceService } from '../../service/cart-service.service';

@Component({
  selector: 'app-connection',
  standalone: true,
  imports : [CommonModule, FormsModule],
  templateUrl: './connection.component.html',
  styleUrls: ['./connection.component.css']
})
export class ConnectionComponent {
  email: string = '';  // Variable pour stocker l'email
  password: string = ''; // Variable pour stocker le mot de passe
  emailError: string = '';  // Message d'erreur pour l'email
  passwordError: string = '';  // Message d'erreur pour le mot de passe

  constructor(private router: Router, private cartService:CartServiceService) { }

  // Méthode pour vérifier si l'email est valide
  isEmailValid(): boolean {
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailPattern.test(this.email);
  }

  // Méthode pour vérifier si le mot de passe est renseigné
  isPasswordValid(): boolean {
    return this.password.length > 0;
  }

  validateForm(): boolean {
    let valid = true;

    // Vérification de l'email
    if (!this.email) {
      this.emailError = 'L\'email est requis.';
      valid = false;
    } else if (!this.isEmailValid()) {
      this.emailError = 'L\'email n\'est pas valide.';
      valid = false;
    } else {
      this.emailError = ''; // Si l'email est valide, on efface l'erreur
    }

    // Vérification de la forme du mot de passe 
    if (!this.isPasswordValid()) {
      this.passwordError = 'Le mot de passe est requis.';
      valid = false;
    } else {
      this.passwordError = ''; // Si le mot de passe est renseigné, on efface l'erreur
    }

    return valid;
  }

  // Méthode pour gérer la soumission du formulaire
  onSubmit(): void {
    // On valide le formulaire avant de soumettre
    if (this.validateForm()) {
      console.log("Appel au server client pour se connecter");
      this.cartService.retrieveClientInfo(this.email, this.password);
    } else {
      // Si le formulaire n'est pas valide, on affiche un message d'erreur général
      console.log("Formulaire invalide");
    }
  }

  onRegister(): void{
    this.router.navigate(["/register"]);
  }
}
