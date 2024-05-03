import dayjs from 'dayjs/esm';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IAddress } from 'app/entities/address/address.model';
import { IEmploymentContract } from 'app/entities/employment-contract/employment-contract.model';
import { IIdDocument } from 'app/entities/id-document/id-document.model';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { MaritalStatus } from 'app/entities/enumerations/marital-status.model';

export interface IEmployee {
  id: number;
  employeeNumber?: number | null;
  birthDate?: dayjs.Dayjs | null;
  birthPlace?: string | null;
  nationality?: string | null;
  gender?: keyof typeof Gender | null;
  maritalStatus?: keyof typeof MaritalStatus | null;
  dependentChildrenNumber?: number | null;
  socialSecurityNumber?: string | null;
  iban?: string | null;
  bicSwift?: string | null;
  user?: IAppUser | null;
  address?: IAddress | null;
  contract?: IEmploymentContract | null;
  idDocument?: IIdDocument | null;
  wallet?: IWallet | null;
  manager?: IEmployee | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
