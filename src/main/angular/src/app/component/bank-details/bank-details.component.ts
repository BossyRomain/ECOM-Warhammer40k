import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {CartServiceService} from '../../service/cart-service.service';
import {ClientServiceService} from '../../service/client-service.service';

@Component({
  selector: 'app-bank-details',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bank-details.component.html',
  styleUrl: './bank-details.component.css'
})
export class BankDetailsComponent implements OnInit {

  prix: number = 0;

  constructor(private router: Router, private cartService: CartServiceService, private clientService: ClientServiceService) {
  }

  ngOnInit(): void {
    this.prix = this.cartService.getAmountToPay();
  }

  cardNumber: string = '';
  cardholderName: string = '';
  expirationDate: string = '';
  cvv: string = '';
  shipping: string = '';

  cardNumberError: string = '';
  cardholderNameError: string = '';
  expirationDateError: string = '';
  cvvError: string = '';
  shippingError: string = '';

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
      this.cardNumberError = 'The card number must be in format XXXX XXXX XXXX XXXX.';
      valid = false;
    } else {
      this.cardNumberError = '';
    }

    // Validation du nom sur la carte
    if (!this.isCardholderNameValid()) {
      this.cardholderNameError = 'The card\'s name is required.';
      valid = false;
    } else {
      this.cardholderNameError = '';
    }

    // Validation de la date d'expiration
    if (!this.isExpirationDateValid()) {
      this.expirationDateError = 'The expiration date must be in format MM/AA.';
      valid = false;
    } else {
      this.expirationDateError = '';
    }

    // Validation du CVV
    if (!this.isCvvValid()) {
      this.cvvError = 'The CVV must be 3 digits';
      valid = false;
    } else {
      this.cvvError = '';
    }

    if (this.shipping == "") {
      this.shippingError = 'The shipping address must not be empty';
      valid = false;
    } else {
      this.shippingError = '';
    }

    return valid;
  }

  // Méthode de soumission du formulaire
  onSubmit(): void {
    if (this.validateForm()) {
      let clientid: number = this.clientService.client?.id || 0;
      this.cartService.payCart(this.shipping).subscribe({
        next: () => {
          console.log("Cart payment successful");
          this.router.navigate(["/cart", clientid], {queryParams:{payement:"done"}});
          //
        },
        error: (err: any) => { // Changer Error en any pour accéder aux propriétés "message" et "id"
          console.error("Error during payment:", err.message);

          // Navigation vers la page du panier avec deux queryParams
          this.router.navigate(["/cart", clientid], {
            queryParams: {
              errorMessage: err.message,
              errorId: err.id
            }
          });
        }
      });
    } else {
      console.log("Formulaire invalide.");
    }
  }

}
