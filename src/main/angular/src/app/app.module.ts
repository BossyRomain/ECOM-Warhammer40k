import {NgModule} from '@angular/core';
import {BrowserModule, provideClientHydration} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {provideHttpClient} from '@angular/common/http';
import {ProductSheetComponent} from './component/product-sheet/product-sheet.component';
import {HeaderComponent} from './component/header/header.component';
import {CartComponent} from './component/cart/cart.component';
import {CartItemComponent} from './component/cart-item/cart-item.component';
import {CatalogComponent} from './component/catalog/catalog.component';
import {BankDetailsComponent} from "./component/bank-details/bank-details.component";
import {FooterComponent} from "./component/footer/footer.component";
import {RegisterComponent} from './component/register/register.component';
import {HistoryComponent} from './component/history/history.component';
import {HistoryItemComponent} from './component/history-item/history-item.component';
import {FigurineFilterComponent} from './component/figurine-filter/figurine-filter.component';
import {AccessoriesFilterComponent} from './component/accessories-filter/accessories-filter.component';


@NgModule({
  declarations: [
    AppComponent,
    FigurineFilterComponent,
    AccessoriesFilterComponent,
  ],
  imports: [
    BrowserModule,
    HistoryItemComponent,
    AppRoutingModule,
    ProductSheetComponent,
    HeaderComponent,
    CatalogComponent,
    AppRoutingModule,
    HistoryComponent,
    BankDetailsComponent,
    FooterComponent,
    RegisterComponent,
    CartComponent,
    CartItemComponent
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
