import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-bank-details',
  standalone: true,
  imports : [CommonModule, FormsModule],
  templateUrl: './bank-details.component.html',
  styleUrl: './bank-details.component.css'
})
export class BankDetailsComponent {
  cardNumber: string = '';
  cardholderName: string = '';
  expirationDate: string = '';
  cvv: string = '';

  cardNumberError: string = '';
  cardholderNameError: string = '';
  expirationDateError: string = '';
  cvvError: string = '';

  // Méthode de validation pour le numéro de carte
  isCardNumberValid(): boolean {
    const cardNumberPattern = /^\d{4} \d{4} \d{4} \d{4}$/;
    return cardNumberPattern.test(this.cardNumber);
  }

  formatCardNumber(): void {
    // Supprimer tout ce qui n'est pas un chiffre
    let formattedValue = this.cardNumber.replace(/\D/g, '');
    // Ajouter un espace tous les 4 caractères
    // Cette expression va insérer un espace tous les 4 chiffres
    // Par exemple: 1234 5678 1234 5678
    if (formattedValue.length > 4) {
      formattedValue = formattedValue.replace(/(\d{4})(?=\d)/g, '$1 ');
    }
    // Limiter la longueur à 19 caractères (format de carte de crédit avec espaces)
    // 16 chiffres + 3 espaces => 19 caractères au total
    this.cardNumber = formattedValue.slice(0, 19);
  }

  formatExpirationDate(): void {
    // Supprimer tout ce qui n'est pas un chiffre
    let formattedValue = this.expirationDate.replace(/\D/g, '');

    // Ajouter un "/" après les 2 premiers chiffres (pour séparer MM/AA)
    if (formattedValue.length > 2) {
      formattedValue = formattedValue.slice(0, 2) + '/' + formattedValue.slice(2, 4);
    }

    // Limiter à 5 caractères au total (2 chiffres pour le mois, "/" et 2 chiffres pour l'année)
    this.expirationDate = formattedValue.slice(0, 5);
  }



  // Méthode de validation pour le nom sur la carte
  isCardholderNameValid(): boolean {
    return this.cardholderName.trim().length > 0;
  }

  // Méthode de validation pour la date d'expiration
  isExpirationDateValid(): boolean {
    const expirationPattern = /^(0[1-9]|1[0-2])\/\d{2}$/; // Format MM/AA
    return expirationPattern.test(this.expirationDate);
  }

  // Méthode de validation pour le CVV
  isCvvValid(): boolean {
    return this.cvv.length === 3;
  }

  // Fonction pour valider le formulaire
  validateForm(): boolean {
    let valid = true;

    // Validation du numéro de carte
    if (!this.isCardNumberValid()) {
      this.cardNumberError = 'Le numéro de carte doit être au format XXXX XXXX XXXX XXXX.';
      valid = false;
    } else {
      this.cardNumberError = '';
    }

    // Validation du nom sur la carte
    if (!this.isCardholderNameValid()) {
      this.cardholderNameError = 'Le nom sur la carte est requis.';
      valid = false;
    } else {
      this.cardholderNameError = '';
    }

    // Validation de la date d'expiration
    if (!this.isExpirationDateValid()) {
      this.expirationDateError = 'La date d\'expiration doit être au format MM/AA.';
      valid = false;
    } else {
      this.expirationDateError = '';
    }

    // Validation du CVV
    if (!this.isCvvValid()) {
      this.cvvError = 'Le CVV doit comporter exactement 3 chiffres.';
      valid = false;
    } else {
      this.cvvError = '';
    }

    return valid;
  }

  // Méthode de soumission du formulaire
  onSubmit(): void {
    if (this.validateForm()) {
      // Si le formulaire est valide, on redirige ou effectue une action
      console.log("Formulaire validé, paiement effectué.");
    } else {
      // Si le formulaire est invalide, rien n'est fait, les erreurs sont affichées sous chaque champ
      console.log("Formulaire invalide.");
    }
  }
}
