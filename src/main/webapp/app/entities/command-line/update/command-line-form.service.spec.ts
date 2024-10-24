import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../command-line.test-samples';

import { CommandLineFormService } from './command-line-form.service';

describe('CommandLine Form Service', () => {
  let service: CommandLineFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommandLineFormService);
  });

  describe('Service methods', () => {
    describe('createCommandLineFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCommandLineFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quanity: expect.any(Object),
            product: expect.any(Object),
            cart: expect.any(Object),
          }),
        );
      });

      it('passing ICommandLine should create a new form with FormGroup', () => {
        const formGroup = service.createCommandLineFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quanity: expect.any(Object),
            product: expect.any(Object),
            cart: expect.any(Object),
          }),
        );
      });
    });

    describe('getCommandLine', () => {
      it('should return NewCommandLine for default CommandLine initial value', () => {
        const formGroup = service.createCommandLineFormGroup(sampleWithNewData);

        const commandLine = service.getCommandLine(formGroup) as any;

        expect(commandLine).toMatchObject(sampleWithNewData);
      });

      it('should return NewCommandLine for empty CommandLine initial value', () => {
        const formGroup = service.createCommandLineFormGroup();

        const commandLine = service.getCommandLine(formGroup) as any;

        expect(commandLine).toMatchObject({});
      });

      it('should return ICommandLine', () => {
        const formGroup = service.createCommandLineFormGroup(sampleWithRequiredData);

        const commandLine = service.getCommandLine(formGroup) as any;

        expect(commandLine).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICommandLine should not enable id FormControl', () => {
        const formGroup = service.createCommandLineFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCommandLine should disable id FormControl', () => {
        const formGroup = service.createCommandLineFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
