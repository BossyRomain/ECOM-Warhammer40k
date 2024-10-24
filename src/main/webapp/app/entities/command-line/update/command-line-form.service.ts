import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICommandLine, NewCommandLine } from '../command-line.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICommandLine for edit and NewCommandLineFormGroupInput for create.
 */
type CommandLineFormGroupInput = ICommandLine | PartialWithRequiredKeyOf<NewCommandLine>;

type CommandLineFormDefaults = Pick<NewCommandLine, 'id'>;

type CommandLineFormGroupContent = {
  id: FormControl<ICommandLine['id'] | NewCommandLine['id']>;
  quanity: FormControl<ICommandLine['quanity']>;
  product: FormControl<ICommandLine['product']>;
  cart: FormControl<ICommandLine['cart']>;
};

export type CommandLineFormGroup = FormGroup<CommandLineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommandLineFormService {
  createCommandLineFormGroup(commandLine: CommandLineFormGroupInput = { id: null }): CommandLineFormGroup {
    const commandLineRawValue = {
      ...this.getFormDefaults(),
      ...commandLine,
    };
    return new FormGroup<CommandLineFormGroupContent>({
      id: new FormControl(
        { value: commandLineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quanity: new FormControl(commandLineRawValue.quanity),
      product: new FormControl(commandLineRawValue.product),
      cart: new FormControl(commandLineRawValue.cart),
    });
  }

  getCommandLine(form: CommandLineFormGroup): ICommandLine | NewCommandLine {
    return form.getRawValue() as ICommandLine | NewCommandLine;
  }

  resetForm(form: CommandLineFormGroup, commandLine: CommandLineFormGroupInput): void {
    const commandLineRawValue = { ...this.getFormDefaults(), ...commandLine };
    form.reset(
      {
        ...commandLineRawValue,
        id: { value: commandLineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CommandLineFormDefaults {
    return {
      id: null,
    };
  }
}
