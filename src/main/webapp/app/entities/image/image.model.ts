import { IProduct } from 'app/entities/product/product.model';

export interface IImage {
  id: number;
  url?: string | null;
  description?: string | null;
  product?: IProduct | null;
}

export type NewImage = Omit<IImage, 'id'> & { id: null };
