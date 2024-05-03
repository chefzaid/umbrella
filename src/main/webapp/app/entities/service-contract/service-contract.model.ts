import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/client/client.model';

export interface IServiceContract {
  id: number;
  serviceLabel?: string | null;
  dailyRate?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  extensionTerms?: string | null;
  signedBySupplier?: boolean | null;
  signedByClient?: boolean | null;
  employee?: IClient | null;
  serviceContract?: IServiceContract | null;
}

export type NewServiceContract = Omit<IServiceContract, 'id'> & { id: null };
