import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICart, NewCart } from '../cart.model';

export type PartialUpdateCart = Partial<ICart> & Pick<ICart, 'id'>;

type RestOf<T extends ICart | NewCart> = Omit<T, 'buyingDate'> & {
  buyingDate?: string | null;
};

export type RestCart = RestOf<ICart>;

export type NewRestCart = RestOf<NewCart>;

export type PartialUpdateRestCart = RestOf<PartialUpdateCart>;

export type EntityResponseType = HttpResponse<ICart>;
export type EntityArrayResponseType = HttpResponse<ICart[]>;

@Injectable({ providedIn: 'root' })
export class CartService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/carts');

  create(cart: NewCart): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cart);
    return this.http.post<RestCart>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cart: ICart): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cart);
    return this.http
      .put<RestCart>(`${this.resourceUrl}/${this.getCartIdentifier(cart)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cart: PartialUpdateCart): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cart);
    return this.http
      .patch<RestCart>(`${this.resourceUrl}/${this.getCartIdentifier(cart)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCart>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCart[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCartIdentifier(cart: Pick<ICart, 'id'>): number {
    return cart.id;
  }

  compareCart(o1: Pick<ICart, 'id'> | null, o2: Pick<ICart, 'id'> | null): boolean {
    return o1 && o2 ? this.getCartIdentifier(o1) === this.getCartIdentifier(o2) : o1 === o2;
  }

  addCartToCollectionIfMissing<Type extends Pick<ICart, 'id'>>(
    cartCollection: Type[],
    ...cartsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const carts: Type[] = cartsToCheck.filter(isPresent);
    if (carts.length > 0) {
      const cartCollectionIdentifiers = cartCollection.map(cartItem => this.getCartIdentifier(cartItem));
      const cartsToAdd = carts.filter(cartItem => {
        const cartIdentifier = this.getCartIdentifier(cartItem);
        if (cartCollectionIdentifiers.includes(cartIdentifier)) {
          return false;
        }
        cartCollectionIdentifiers.push(cartIdentifier);
        return true;
      });
      return [...cartsToAdd, ...cartCollection];
    }
    return cartCollection;
  }

  protected convertDateFromClient<T extends ICart | NewCart | PartialUpdateCart>(cart: T): RestOf<T> {
    return {
      ...cart,
      buyingDate: cart.buyingDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCart: RestCart): ICart {
    return {
      ...restCart,
      buyingDate: restCart.buyingDate ? dayjs(restCart.buyingDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCart>): HttpResponse<ICart> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCart[]>): HttpResponse<ICart[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
