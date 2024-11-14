import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {provideHttpClient} from '@angular/common/http';
import { ProductSheetComponent } from './component/product-sheet/product-sheet.component';
import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import HeaderComponent from "./component/header/header.component";
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    ProductSheetComponent,
    HeaderComponent
],
  providers: [
    provideClientHydration(),
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }