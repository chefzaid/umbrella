import { IAddress } from 'app/entities/address/address.model';

export interface IClient {
  id: number;
  name?: string | null;
  companyStatus?: string | null;
  address?: IAddress | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
