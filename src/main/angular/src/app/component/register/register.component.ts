import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone : true,
  imports : [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user = {
    username: '',
    firstName: '',
    email: '',
    dob: '',
    password: '',
    confirmPassword: '',
    newsletter: false
  };

  // Définir un type flexible pour 'errors' avec une index signature
  errors: { [key: string]: string } = {
    username: '',
    firstName: '',
    email: '',
    dob: '',
    password: '',
    confirmPassword: '',
    newsletter: ''
  };


  // Fonction pour réinitialiser les erreurs
  clearError(field: string) {
    this.errors[field] = ''; // Utilisation de la notation entre crochets pour accéder à chaque erreur
  }

  // Fonction de soumission du formulaire
  onSubmit() {
    // Logique de validation et soumission ici
    console.log(this.user);

    // Exemple de validation manuelle :
    if (!this.user.username) {
      this.errors['username'] = 'Le nom d\'utilisateur est requis';
    }

    if (!this.user.email) {
      this.errors['email'] = 'L\'adresse e-mail est requise';
    }

    if (!this.user.password) {
      this.errors['password'] = 'Le mot de passe est requis';
    } else if (this.user.password !== this.user.confirmPassword) {
      this.errors['confirmPassword'] = 'Les mots de passe ne correspondent pas';
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

    // Si tout est valide, procéder à l'envoi ou à l'authentification
    alert('Inscription réussie !');
  }
}
