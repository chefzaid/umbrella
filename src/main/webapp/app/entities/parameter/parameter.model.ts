import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IEnterprise } from 'app/entities/enterprise/enterprise.model';

export interface IParameter {
  id: number;
  label?: string | null;
  value?: string | null;
  appUser?: IAppUser | null;
  enterprise?: IEnterprise | null;
}

export type NewParameter = Omit<IParameter, 'id'> & { id: null };
