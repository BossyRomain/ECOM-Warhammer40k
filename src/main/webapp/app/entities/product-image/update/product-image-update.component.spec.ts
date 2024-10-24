import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ProductImageService } from '../service/product-image.service';
import { IProductImage } from '../product-image.model';
import { ProductImageFormService } from './product-image-form.service';

import { ProductImageUpdateComponent } from './product-image-update.component';

describe('ProductImage Management Update Component', () => {
  let comp: ProductImageUpdateComponent;
  let fixture: ComponentFixture<ProductImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productImageFormService: ProductImageFormService;
  let productImageService: ProductImageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductImageUpdateComponent],
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
      .overrideTemplate(ProductImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productImageFormService = TestBed.inject(ProductImageFormService);
    productImageService = TestBed.inject(ProductImageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const productImage: IProductImage = { id: 456 };

      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      expect(comp.productImage).toEqual(productImage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductImage>>();
      const productImage = { id: 123 };
      jest.spyOn(productImageFormService, 'getProductImage').mockReturnValue(productImage);
      jest.spyOn(productImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productImage }));
      saveSubject.complete();

      // THEN
      expect(productImageFormService.getProductImage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productImageService.update).toHaveBeenCalledWith(expect.objectContaining(productImage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductImage>>();
      const productImage = { id: 123 };
      jest.spyOn(productImageFormService, 'getProductImage').mockReturnValue({ id: null });
      jest.spyOn(productImageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productImage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productImage }));
      saveSubject.complete();

      // THEN
      expect(productImageFormService.getProductImage).toHaveBeenCalled();
      expect(productImageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductImage>>();
      const productImage = { id: 123 };
      jest.spyOn(productImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productImageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
