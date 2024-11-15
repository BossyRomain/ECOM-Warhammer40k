import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {provideHttpClient} from '@angular/common/http';
import { ProductSheetComponent } from './component/product-sheet/product-sheet.component';
import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { HeaderComponent } from "./component/header/header.component";
import { CatalogComponent } from './component/catalog/catalog.component';
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    ProductSheetComponent,
    HeaderComponent,
    CatalogComponent,
    AppRoutingModule
],
  providers: [
    provideClientHydration(),
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }