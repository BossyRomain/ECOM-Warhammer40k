import dayjs from 'dayjs/esm';
import { IProductImage } from 'app/entities/product-image/product-image.model';
import { IAllegiance } from 'app/entities/allegiance/allegiance.model';
import { ProductType } from 'app/entities/enumerations/product-type.model';

export interface IProduct {
  id: number;
  name?: string | null;
  stock?: number | null;
  price?: number | null;
  description?: string | null;
  type?: keyof typeof ProductType | null;
  releaseDate?: dayjs.Dayjs | null;
  imageCatalogue?: IProductImage | null;
  allegiance?: IAllegiance | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
