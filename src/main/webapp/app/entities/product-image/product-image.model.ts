export interface IProductImage {
  id: number;
  url?: string | null;
  description?: string | null;
}

export type NewProductImage = Omit<IProductImage, 'id'> & { id: null };
