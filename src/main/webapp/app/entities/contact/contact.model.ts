import { IAddress } from 'app/entities/address/address.model';
import { IClient } from 'app/entities/client/client.model';

export interface IContact {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  jobTitle?: string | null;
  address?: IAddress | null;
  client?: IClient | null;
}

export type NewContact = Omit<IContact, 'id'> & { id: null };
