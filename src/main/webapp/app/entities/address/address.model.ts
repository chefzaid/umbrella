export interface IAddress {
  id: number;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  country?: string | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
