import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IClient, NewClient } from '../client.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClient for edit and NewClientFormGroupInput for create.
 */
type ClientFormGroupInput = IClient | PartialWithRequiredKeyOf<NewClient>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IClient | NewClient> = Omit<T, 'birthDay'> & {
  birthDay?: string | null;
};

type ClientFormRawValue = FormValueOf<IClient>;

type NewClientFormRawValue = FormValueOf<NewClient>;

type ClientFormDefaults = Pick<NewClient, 'id' | 'birthDay' | 'newsLetter'>;

type ClientFormGroupContent = {
  id: FormControl<ClientFormRawValue['id'] | NewClient['id']>;
  firstName: FormControl<ClientFormRawValue['firstName']>;
  lastName: FormControl<ClientFormRawValue['lastName']>;
  birthDay: FormControl<ClientFormRawValue['birthDay']>;
  newsLetter: FormControl<ClientFormRawValue['newsLetter']>;
  user: FormControl<ClientFormRawValue['user']>;
};

export type ClientFormGroup = FormGroup<ClientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClientFormService {
  createClientFormGroup(client: ClientFormGroupInput = { id: null }): ClientFormGroup {
    const clientRawValue = this.convertClientToClientRawValue({
      ...this.getFormDefaults(),
      ...client,
    });
    return new FormGroup<ClientFormGroupContent>({
      id: new FormControl(
        { value: clientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(clientRawValue.firstName),
      lastName: new FormControl(clientRawValue.lastName),
      birthDay: new FormControl(clientRawValue.birthDay),
      newsLetter: new FormControl(clientRawValue.newsLetter),
      user: new FormControl(clientRawValue.user),
    });
  }

  getClient(form: ClientFormGroup): IClient | NewClient {
    return this.convertClientRawValueToClient(form.getRawValue() as ClientFormRawValue | NewClientFormRawValue);
  }

  resetForm(form: ClientFormGroup, client: ClientFormGroupInput): void {
    const clientRawValue = this.convertClientToClientRawValue({ ...this.getFormDefaults(), ...client });
    form.reset(
      {
        ...clientRawValue,
        id: { value: clientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClientFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      birthDay: currentTime,
      newsLetter: false,
    };
  }

  private convertClientRawValueToClient(rawClient: ClientFormRawValue | NewClientFormRawValue): IClient | NewClient {
    return {
      ...rawClient,
      birthDay: dayjs(rawClient.birthDay, DATE_TIME_FORMAT),
    };
  }

  private convertClientToClientRawValue(
    client: IClient | (Partial<NewClient> & ClientFormDefaults),
  ): ClientFormRawValue | PartialWithRequiredKeyOf<NewClientFormRawValue> {
    return {
      ...client,
      birthDay: client.birthDay ? client.birthDay.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}