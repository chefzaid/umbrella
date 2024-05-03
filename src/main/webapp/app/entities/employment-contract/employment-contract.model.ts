import dayjs from 'dayjs/esm';
import { IFormula } from 'app/entities/formula/formula.model';
import { EmploymentContractType } from 'app/entities/enumerations/employment-contract-type.model';

export interface IEmploymentContract {
  id: number;
  type?: keyof typeof EmploymentContractType | null;
  jobTitle?: string | null;
  hireDate?: dayjs.Dayjs | null;
  salary?: number | null;
  yearlyWorkDays?: number | null;
  signedByEmployer?: boolean | null;
  signedByEmployee?: boolean | null;
  forumula?: IFormula | null;
  employmentContract?: IEmploymentContract | null;
}

export type NewEmploymentContract = Omit<IEmploymentContract, 'id'> & { id: null };
