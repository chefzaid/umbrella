import dayjs from 'dayjs/esm';
import { IParameter } from 'app/entities/parameter/parameter.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IEmploymentContract } from 'app/entities/employment-contract/employment-contract.model';
import { IServiceContract } from 'app/entities/service-contract/service-contract.model';
import { IEnterprise } from 'app/entities/enterprise/enterprise.model';

export interface IDocument {
  id: number;
  label?: string | null;
  uploadDate?: dayjs.Dayjs | null;
  issuingDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  file?: string | null;
  fileContentType?: string | null;
  documentType?: IParameter | null;
  employee?: IEmployee | null;
  employmentContract?: IEmploymentContract | null;
  serviceContract?: IServiceContract | null;
  enterprise?: IEnterprise | null;
}

export type NewDocument = Omit<IDocument, 'id'> & { id: null };
