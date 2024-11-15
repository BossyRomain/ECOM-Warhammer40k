import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductSheetComponent } from './component/product-sheet/product-sheet.component';
import { CatalogComponent } from './component/catalog/catalog.component';
import { connect } from 'http2';
import { ConnectionComponent } from './connection/connection.component';

const routes: Routes = [
  { path: "product/:id", component: ProductSheetComponent },
  { path: "catalog", component: CatalogComponent },
  { path: "account", component: ConnectionComponent },
  { path: '**', redirectTo: "catalog"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
