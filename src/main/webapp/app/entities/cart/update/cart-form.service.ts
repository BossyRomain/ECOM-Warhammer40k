import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICart, NewCart } from '../cart.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICart for edit and NewCartFormGroupInput for create.
 */
type CartFormGroupInput = ICart | PartialWithRequiredKeyOf<NewCart>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICart | NewCart> = Omit<T, 'buyingDate'> & {
  buyingDate?: string | null;
};

type CartFormRawValue = FormValueOf<ICart>;

type NewCartFormRawValue = FormValueOf<NewCart>;

type CartFormDefaults = Pick<NewCart, 'id' | 'buyingDate' | 'paid'>;

type CartFormGroupContent = {
  id: FormControl<CartFormRawValue['id'] | NewCart['id']>;
  buyingDate: FormControl<CartFormRawValue['buyingDate']>;
  paid: FormControl<CartFormRawValue['paid']>;
  client: FormControl<CartFormRawValue['client']>;
};

export type CartFormGroup = FormGroup<CartFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CartFormService {
  createCartFormGroup(cart: CartFormGroupInput = { id: null }): CartFormGroup {
    const cartRawValue = this.convertCartToCartRawValue({
      ...this.getFormDefaults(),
      ...cart,
    });
    return new FormGroup<CartFormGroupContent>({
      id: new FormControl(
        { value: cartRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      buyingDate: new FormControl(cartRawValue.buyingDate),
      paid: new FormControl(cartRawValue.paid),
      client: new FormControl(cartRawValue.client),
    });
  }

  getCart(form: CartFormGroup): ICart | NewCart {
    return this.convertCartRawValueToCart(form.getRawValue() as CartFormRawValue | NewCartFormRawValue);
  }

  resetForm(form: CartFormGroup, cart: CartFormGroupInput): void {
    const cartRawValue = this.convertCartToCartRawValue({ ...this.getFormDefaults(), ...cart });
    form.reset(
      {
        ...cartRawValue,
        id: { value: cartRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CartFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      buyingDate: currentTime,
      paid: false,
    };
  }

  private convertCartRawValueToCart(rawCart: CartFormRawValue | NewCartFormRawValue): ICart | NewCart {
    return {
      ...rawCart,
      buyingDate: dayjs(rawCart.buyingDate, DATE_TIME_FORMAT),
    };
  }

  private convertCartToCartRawValue(
    cart: ICart | (Partial<NewCart> & CartFormDefaults),
  ): CartFormRawValue | PartialWithRequiredKeyOf<NewCartFormRawValue> {
    return {
      ...cart,
      buyingDate: cart.buyingDate ? cart.buyingDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
