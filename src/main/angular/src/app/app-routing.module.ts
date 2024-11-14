import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductSheetComponent } from './component/product-sheet/product-sheet.component';

const routes: Routes = [{path:"product/:id", component:ProductSheetComponent}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
