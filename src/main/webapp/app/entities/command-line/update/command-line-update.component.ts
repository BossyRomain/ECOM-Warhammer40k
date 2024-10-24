import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICart } from 'app/entities/cart/cart.model';
import { CartService } from 'app/entities/cart/service/cart.service';
import { CommandLineService } from '../service/command-line.service';
import { ICommandLine } from '../command-line.model';
import { CommandLineFormGroup, CommandLineFormService } from './command-line-form.service';

@Component({
  standalone: true,
  selector: 'jhi-command-line-update',
  templateUrl: './command-line-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CommandLineUpdateComponent implements OnInit {
  isSaving = false;
  commandLine: ICommandLine | null = null;

  productsCollection: IProduct[] = [];
  cartsSharedCollection: ICart[] = [];

  protected commandLineService = inject(CommandLineService);
  protected commandLineFormService = inject(CommandLineFormService);
  protected productService = inject(ProductService);
  protected cartService = inject(CartService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CommandLineFormGroup = this.commandLineFormService.createCommandLineFormGroup();

  compareProduct = (o1: IProduct | null, o2: IProduct | null): boolean => this.productService.compareProduct(o1, o2);

  compareCart = (o1: ICart | null, o2: ICart | null): boolean => this.cartService.compareCart(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commandLine }) => {
      this.commandLine = commandLine;
      if (commandLine) {
        this.updateForm(commandLine);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commandLine = this.commandLineFormService.getCommandLine(this.editForm);
    if (commandLine.id !== null) {
      this.subscribeToSaveResponse(this.commandLineService.update(commandLine));
    } else {
      this.subscribeToSaveResponse(this.commandLineService.create(commandLine));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommandLine>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(commandLine: ICommandLine): void {
    this.commandLine = commandLine;
    this.commandLineFormService.resetForm(this.editForm, commandLine);

    this.productsCollection = this.productService.addProductToCollectionIfMissing<IProduct>(this.productsCollection, commandLine.product);
    this.cartsSharedCollection = this.cartService.addCartToCollectionIfMissing<ICart>(this.cartsSharedCollection, commandLine.cart);
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query({ filter: 'commandline-is-null' })
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing<IProduct>(products, this.commandLine?.product)),
      )
      .subscribe((products: IProduct[]) => (this.productsCollection = products));

    this.cartService
      .query()
      .pipe(map((res: HttpResponse<ICart[]>) => res.body ?? []))
      .pipe(map((carts: ICart[]) => this.cartService.addCartToCollectionIfMissing<ICart>(carts, this.commandLine?.cart)))
      .subscribe((carts: ICart[]) => (this.cartsSharedCollection = carts));
  }
}
