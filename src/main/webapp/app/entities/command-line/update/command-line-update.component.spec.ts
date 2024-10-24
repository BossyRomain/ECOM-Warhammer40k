import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICart } from 'app/entities/cart/cart.model';
import { CartService } from 'app/entities/cart/service/cart.service';
import { ICommandLine } from '../command-line.model';
import { CommandLineService } from '../service/command-line.service';
import { CommandLineFormService } from './command-line-form.service';

import { CommandLineUpdateComponent } from './command-line-update.component';

describe('CommandLine Management Update Component', () => {
  let comp: CommandLineUpdateComponent;
  let fixture: ComponentFixture<CommandLineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commandLineFormService: CommandLineFormService;
  let commandLineService: CommandLineService;
  let productService: ProductService;
  let cartService: CartService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommandLineUpdateComponent],
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
      .overrideTemplate(CommandLineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommandLineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commandLineFormService = TestBed.inject(CommandLineFormService);
    commandLineService = TestBed.inject(CommandLineService);
    productService = TestBed.inject(ProductService);
    cartService = TestBed.inject(CartService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call product query and add missing value', () => {
      const commandLine: ICommandLine = { id: 456 };
      const product: IProduct = { id: 30039 };
      commandLine.product = product;

      const productCollection: IProduct[] = [{ id: 8306 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const expectedCollection: IProduct[] = [product, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandLine });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, product);
      expect(comp.productsCollection).toEqual(expectedCollection);
    });

    it('Should call Cart query and add missing value', () => {
      const commandLine: ICommandLine = { id: 456 };
      const cart: ICart = { id: 9787 };
      commandLine.cart = cart;

      const cartCollection: ICart[] = [{ id: 32765 }];
      jest.spyOn(cartService, 'query').mockReturnValue(of(new HttpResponse({ body: cartCollection })));
      const additionalCarts = [cart];
      const expectedCollection: ICart[] = [...additionalCarts, ...cartCollection];
      jest.spyOn(cartService, 'addCartToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commandLine });
      comp.ngOnInit();

      expect(cartService.query).toHaveBeenCalled();
      expect(cartService.addCartToCollectionIfMissing).toHaveBeenCalledWith(
        cartCollection,
        ...additionalCarts.map(expect.objectContaining),
      );
      expect(comp.cartsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commandLine: ICommandLine = { id: 456 };
      const product: IProduct = { id: 22136 };
      commandLine.product = product;
      const cart: ICart = { id: 15097 };
      commandLine.cart = cart;

      activatedRoute.data = of({ commandLine });
      comp.ngOnInit();

      expect(comp.productsCollection).toContain(product);
      expect(comp.cartsSharedCollection).toContain(cart);
      expect(comp.commandLine).toEqual(commandLine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommandLine>>();
      const commandLine = { id: 123 };
      jest.spyOn(commandLineFormService, 'getCommandLine').mockReturnValue(commandLine);
      jest.spyOn(commandLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commandLine }));
      saveSubject.complete();

      // THEN
      expect(commandLineFormService.getCommandLine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(commandLineService.update).toHaveBeenCalledWith(expect.objectContaining(commandLine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommandLine>>();
      const commandLine = { id: 123 };
      jest.spyOn(commandLineFormService, 'getCommandLine').mockReturnValue({ id: null });
      jest.spyOn(commandLineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandLine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commandLine }));
      saveSubject.complete();

      // THEN
      expect(commandLineFormService.getCommandLine).toHaveBeenCalled();
      expect(commandLineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommandLine>>();
      const commandLine = { id: 123 };
      jest.spyOn(commandLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commandLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commandLineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCart', () => {
      it('Should forward to cartService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cartService, 'compareCart');
        comp.compareCart(entity, entity2);
        expect(cartService.compareCart).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
