import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProductImage } from 'app/entities/product-image/product-image.model';
import { ProductImageService } from 'app/entities/product-image/service/product-image.service';
import { IAllegiance } from 'app/entities/allegiance/allegiance.model';
import { AllegianceService } from 'app/entities/allegiance/service/allegiance.service';
import { ProductType } from 'app/entities/enumerations/product-type.model';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import { ProductFormGroup, ProductFormService } from './product-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;
  productTypeValues = Object.keys(ProductType);

  imageCataloguesCollection: IProductImage[] = [];
  allegiancesSharedCollection: IAllegiance[] = [];

  protected productService = inject(ProductService);
  protected productFormService = inject(ProductFormService);
  protected productImageService = inject(ProductImageService);
  protected allegianceService = inject(AllegianceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  compareProductImage = (o1: IProductImage | null, o2: IProductImage | null): boolean =>
    this.productImageService.compareProductImage(o1, o2);

  compareAllegiance = (o1: IAllegiance | null, o2: IAllegiance | null): boolean => this.allegianceService.compareAllegiance(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.productFormService.getProduct(this.editForm);
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);

    this.imageCataloguesCollection = this.productImageService.addProductImageToCollectionIfMissing<IProductImage>(
      this.imageCataloguesCollection,
      product.imageCatalogue,
    );
    this.allegiancesSharedCollection = this.allegianceService.addAllegianceToCollectionIfMissing<IAllegiance>(
      this.allegiancesSharedCollection,
      product.allegiance,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productImageService
      .query({ filter: 'product-is-null' })
      .pipe(map((res: HttpResponse<IProductImage[]>) => res.body ?? []))
      .pipe(
        map((productImages: IProductImage[]) =>
          this.productImageService.addProductImageToCollectionIfMissing<IProductImage>(productImages, this.product?.imageCatalogue),
        ),
      )
      .subscribe((productImages: IProductImage[]) => (this.imageCataloguesCollection = productImages));

    this.allegianceService
      .query()
      .pipe(map((res: HttpResponse<IAllegiance[]>) => res.body ?? []))
      .pipe(
        map((allegiances: IAllegiance[]) =>
          this.allegianceService.addAllegianceToCollectionIfMissing<IAllegiance>(allegiances, this.product?.allegiance),
        ),
      )
      .subscribe((allegiances: IAllegiance[]) => (this.allegiancesSharedCollection = allegiances));
  }
}
