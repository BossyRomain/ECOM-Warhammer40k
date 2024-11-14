import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  standalone:true,
  imports:[CommonModule]
})
export class HeaderComponent {
  constructor(private router:Router){}
  onClick(text:string){
    let x = parseInt(text);
    this.router.navigate(['/product', x]);
  }
}
