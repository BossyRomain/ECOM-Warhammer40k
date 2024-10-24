import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AllegianceService } from '../service/allegiance.service';
import { IAllegiance } from '../allegiance.model';
import { AllegianceFormService } from './allegiance-form.service';

import { AllegianceUpdateComponent } from './allegiance-update.component';

describe('Allegiance Management Update Component', () => {
  let comp: AllegianceUpdateComponent;
  let fixture: ComponentFixture<AllegianceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let allegianceFormService: AllegianceFormService;
  let allegianceService: AllegianceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AllegianceUpdateComponent],
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
      .overrideTemplate(AllegianceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AllegianceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    allegianceFormService = TestBed.inject(AllegianceFormService);
    allegianceService = TestBed.inject(AllegianceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const allegiance: IAllegiance = { id: 456 };

      activatedRoute.data = of({ allegiance });
      comp.ngOnInit();

      expect(comp.allegiance).toEqual(allegiance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAllegiance>>();
      const allegiance = { id: 123 };
      jest.spyOn(allegianceFormService, 'getAllegiance').mockReturnValue(allegiance);
      jest.spyOn(allegianceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allegiance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: allegiance }));
      saveSubject.complete();

      // THEN
      expect(allegianceFormService.getAllegiance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(allegianceService.update).toHaveBeenCalledWith(expect.objectContaining(allegiance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAllegiance>>();
      const allegiance = { id: 123 };
      jest.spyOn(allegianceFormService, 'getAllegiance').mockReturnValue({ id: null });
      jest.spyOn(allegianceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allegiance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: allegiance }));
      saveSubject.complete();

      // THEN
      expect(allegianceFormService.getAllegiance).toHaveBeenCalled();
      expect(allegianceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAllegiance>>();
      const allegiance = { id: 123 };
      jest.spyOn(allegianceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allegiance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(allegianceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
