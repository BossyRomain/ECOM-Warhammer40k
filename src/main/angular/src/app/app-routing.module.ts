import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProductSheetComponent} from './component/product-sheet/product-sheet.component';
import {CatalogComponent} from './component/catalog/catalog.component';
import {ConnectionComponent} from './component/connection/connection.component';
import {CartComponent} from './component/cart/cart.component';
import {BankDetailsComponent} from './component/bank-details/bank-details.component';
import {RegisterComponent} from './component/register/register.component';
import {HistoryComponent} from './component/history/history.component';
import {FigurineFilterComponent} from './component/figurine-filter/figurine-filter.component';
import {HelpComponent} from './component/help/help.component';
import {AccessoriesFilterComponent} from './component/accessories-filter/accessories-filter.component';

const routes: Routes = [
  {path: "product/:id", component: ProductSheetComponent},
  {path: "account", component: ConnectionComponent},
  {path: "cart/:id", component: CartComponent},
  {path: "catalog/search", component: CatalogComponent},
  {path: "pay", component: BankDetailsComponent},
  {path: "register", component: RegisterComponent},
  {path: "history/:id", component: HistoryComponent},
  {path: "help", component: HelpComponent},
  {path: "filter/figurines", component: FigurineFilterComponent},
  {path: "filter/accessories", component: AccessoriesFilterComponent},
  {path: '**', redirectTo: "catalog/search"}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      scrollPositionRestoration: 'enabled', // Restauration des positions de défilement
      onSameUrlNavigation: 'reload'       // Recharge sur même URL
    })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
