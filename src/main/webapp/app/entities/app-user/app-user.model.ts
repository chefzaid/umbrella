import dayjs from 'dayjs/esm';
import { IContact } from 'app/entities/contact/contact.model';

export interface IAppUser {
  id: number;
  username?: string | null;
  password?: string | null;
  photo?: string | null;
  photoContentType?: string | null;
  isAdmin?: boolean | null;
  creationDate?: dayjs.Dayjs | null;
  contact?: IContact | null;
}

export type NewAppUser = Omit<IAppUser, 'id'> & { id: null };
