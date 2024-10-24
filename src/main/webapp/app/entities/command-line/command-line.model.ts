import { IProduct } from 'app/entities/product/product.model';
import { ICart } from 'app/entities/cart/cart.model';

export interface ICommandLine {
  id: number;
  quanity?: number | null;
  product?: IProduct | null;
  cart?: ICart | null;
}

export type NewCommandLine = Omit<ICommandLine, 'id'> & { id: null };
