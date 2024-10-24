import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProductImage } from 'app/entities/product-image/product-image.model';
import { ProductImageService } from 'app/entities/product-image/service/product-image.service';
import { IAllegiance } from 'app/entities/allegiance/allegiance.model';
import { AllegianceService } from 'app/entities/allegiance/service/allegiance.service';
import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';
import { ProductFormService } from './product-form.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productFormService: ProductFormService;
  let productService: ProductService;
  let productImageService: ProductImageService;
  let allegianceService: AllegianceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFormService = TestBed.inject(ProductFormService);
    productService = TestBed.inject(ProductService);
    productImageService = TestBed.inject(ProductImageService);
    allegianceService = TestBed.inject(AllegianceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call imageCatalogue query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const imageCatalogue: IProductImage = { id: 7310 };
      product.imageCatalogue = imageCatalogue;

      const imageCatalogueCollection: IProductImage[] = [{ id: 10650 }];
      jest.spyOn(productImageService, 'query').mockReturnValue(of(new HttpResponse({ body: imageCatalogueCollection })));
      const expectedCollection: IProductImage[] = [imageCatalogue, ...imageCatalogueCollection];
      jest.spyOn(productImageService, 'addProductImageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(productImageService.query).toHaveBeenCalled();
      expect(productImageService.addProductImageToCollectionIfMissing).toHaveBeenCalledWith(imageCatalogueCollection, imageCatalogue);
      expect(comp.imageCataloguesCollection).toEqual(expectedCollection);
    });

    it('Should call Allegiance query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const allegiance: IAllegiance = { id: 2485 };
      product.allegiance = allegiance;

      const allegianceCollection: IAllegiance[] = [{ id: 18249 }];
      jest.spyOn(allegianceService, 'query').mockReturnValue(of(new HttpResponse({ body: allegianceCollection })));
      const additionalAllegiances = [allegiance];
      const expectedCollection: IAllegiance[] = [...additionalAllegiances, ...allegianceCollection];
      jest.spyOn(allegianceService, 'addAllegianceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(allegianceService.query).toHaveBeenCalled();
      expect(allegianceService.addAllegianceToCollectionIfMissing).toHaveBeenCalledWith(
        allegianceCollection,
        ...additionalAllegiances.map(expect.objectContaining),
      );
      expect(comp.allegiancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: 456 };
      const imageCatalogue: IProductImage = { id: 24749 };
      product.imageCatalogue = imageCatalogue;
      const allegiance: IAllegiance = { id: 9452 };
      product.allegiance = allegiance;

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.imageCataloguesCollection).toContain(imageCatalogue);
      expect(comp.allegiancesSharedCollection).toContain(allegiance);
      expect(comp.product).toEqual(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue(product);
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(expect.objectContaining(product));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue({ id: null });
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(productService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProductImage', () => {
      it('Should forward to productImageService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productImageService, 'compareProductImage');
        comp.compareProductImage(entity, entity2);
        expect(productImageService.compareProductImage).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAllegiance', () => {
      it('Should forward to allegianceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(allegianceService, 'compareAllegiance');
        comp.compareAllegiance(entity, entity2);
        expect(allegianceService.compareAllegiance).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
