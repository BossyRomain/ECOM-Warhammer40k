import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-bank-details',
  standalone:false,
  templateUrl: './bank-details.component.html',
  standalone:true,
  imports: [CommonModule],
  styleUrl: './bank-details.component.css'
})
export class BankDetailsComponent {

}
