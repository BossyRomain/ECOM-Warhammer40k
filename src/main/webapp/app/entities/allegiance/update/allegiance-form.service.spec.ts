import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../allegiance.test-samples';

import { AllegianceFormService } from './allegiance-form.service';

describe('Allegiance Form Service', () => {
  let service: AllegianceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AllegianceFormService);
  });

  describe('Service methods', () => {
    describe('createAllegianceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAllegianceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            group: expect.any(Object),
            faction: expect.any(Object),
          }),
        );
      });

      it('passing IAllegiance should create a new form with FormGroup', () => {
        const formGroup = service.createAllegianceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            group: expect.any(Object),
            faction: expect.any(Object),
          }),
        );
      });
    });

    describe('getAllegiance', () => {
      it('should return NewAllegiance for default Allegiance initial value', () => {
        const formGroup = service.createAllegianceFormGroup(sampleWithNewData);

        const allegiance = service.getAllegiance(formGroup) as any;

        expect(allegiance).toMatchObject(sampleWithNewData);
      });

      it('should return NewAllegiance for empty Allegiance initial value', () => {
        const formGroup = service.createAllegianceFormGroup();

        const allegiance = service.getAllegiance(formGroup) as any;

        expect(allegiance).toMatchObject({});
      });

      it('should return IAllegiance', () => {
        const formGroup = service.createAllegianceFormGroup(sampleWithRequiredData);

        const allegiance = service.getAllegiance(formGroup) as any;

        expect(allegiance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAllegiance should not enable id FormControl', () => {
        const formGroup = service.createAllegianceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAllegiance should disable id FormControl', () => {
        const formGroup = service.createAllegianceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
