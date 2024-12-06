import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';  // Importation du router d'Angular
import { FormsModule } from '@angular/forms';  // Importation de FormsModule
import { ClientServiceService } from '../../service/client-service.service';
import { CartServiceService } from '../../service/cart-service.service';
import { BehaviorSubject, filter, first } from 'rxjs';
import { Client } from '../../model/client';
import { CommandLine } from '../../model/command-line';

@Component({
  selector: 'app-connection',
  standalone: true,
  imports : [CommonModule, FormsModule],
  templateUrl: './connection.component.html',
  styleUrls: ['./connection.component.css']
})
export class ConnectionComponent{
  email: string = '';  // Variable pour stocker l'email
  password: string = ''; // Variable pour stocker le mot de passe
  emailError: string = '';  // Message d'erreur pour l'email
  passwordError: string = '';  // Message d'erreur pour le mot de passe

  constructor(private router: Router, private cartService:CartServiceService, private clientService:ClientServiceService) { }
  private clientConnected:boolean = false;
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
  async onSubmit() {
    // On valide le formulaire avant de soumettre
    if (this.validateForm()) {
      await this.cartService.retrieveClientInfo(this.email, this.password);
      if(this.clientService.client){
        let previousCart: {id:number, qty:number}[] = [];
        this.cartService.currentCart.forEach((element) => {
          if(element.id){
            let elm:{id:number, qty:number} = {id:element.id, qty:element.quantity};
            console.log(elm);
            previousCart.push(elm);
          }
        });
        let clientId = this.clientService.client.id;
        this.cartService.getCartOfClient(this.clientService.client.id).subscribe(
          (value) => {
            this.cartService.id = value.id;

            console.log("connection component");
            console.log(value);
            this.cartService.currentCart = [];
            value.commandLines.forEach((elm:CommandLine) => {
              this.cartService.currentCart.push(elm);
            })
            previousCart.forEach((element) => {
              console.log(element);
              this.cartService.addProductToCart(clientId, element.id, element.qty);
              
            })
            
          },
          (error)=>{
            console.error(error);
          }
        );
        
          
        
        
      }
      if(this.clientService.isConnectingFromAnotherPlace()){
        this.router.navigate(["/cart", this.clientService.client?.id]);
      }else{
        this.router.navigate(["/catalog/search"]);
      }
      
      
    } else {
      // Si le formulaire n'est pas valide, on affiche un message d'erreur général
      console.log("Formulaire invalide");
    }
  }
}
