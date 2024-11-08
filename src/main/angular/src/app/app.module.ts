import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {provideHttpClient} from '@angular/common/http';
import { ProductSheetComponent } from './product-sheet/product-sheet.component';

import { CatalogItemComponent } from './catalog-item/catalog-item.component';
import { HeaderComponent } from './header/header.component';
import { BankDetailsComponent } from './bank-details/bank-details.component';

@NgModule({
  declarations: [
    AppComponent,
    CatalogItemComponent,
    HeaderComponent,
    BankDetailsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ProductSheetComponent
],
  providers: [
    provideClientHydration(),
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
