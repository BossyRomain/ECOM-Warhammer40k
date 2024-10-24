import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAllegiance, NewAllegiance } from '../allegiance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAllegiance for edit and NewAllegianceFormGroupInput for create.
 */
type AllegianceFormGroupInput = IAllegiance | PartialWithRequiredKeyOf<NewAllegiance>;

type AllegianceFormDefaults = Pick<NewAllegiance, 'id'>;

type AllegianceFormGroupContent = {
  id: FormControl<IAllegiance['id'] | NewAllegiance['id']>;
  group: FormControl<IAllegiance['group']>;
  faction: FormControl<IAllegiance['faction']>;
};

export type AllegianceFormGroup = FormGroup<AllegianceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AllegianceFormService {
  createAllegianceFormGroup(allegiance: AllegianceFormGroupInput = { id: null }): AllegianceFormGroup {
    const allegianceRawValue = {
      ...this.getFormDefaults(),
      ...allegiance,
    };
    return new FormGroup<AllegianceFormGroupContent>({
      id: new FormControl(
        { value: allegianceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      group: new FormControl(allegianceRawValue.group),
      faction: new FormControl(allegianceRawValue.faction),
    });
  }

  getAllegiance(form: AllegianceFormGroup): IAllegiance | NewAllegiance {
    return form.getRawValue() as IAllegiance | NewAllegiance;
  }

  resetForm(form: AllegianceFormGroup, allegiance: AllegianceFormGroupInput): void {
    const allegianceRawValue = { ...this.getFormDefaults(), ...allegiance };
    form.reset(
      {
        ...allegianceRawValue,
        id: { value: allegianceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AllegianceFormDefaults {
    return {
      id: null,
    };
  }
}
