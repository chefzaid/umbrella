import dayjs from 'dayjs/esm';
import { IProject } from 'app/entities/project/project.model';
import { IDocument } from 'app/entities/document/document.model';

export interface IInvoice {
  id: number;
  invoiceNumber?: string | null;
  label?: string | null;
  issueDate?: dayjs.Dayjs | null;
  currency?: string | null;
  salesTaxPct?: number | null;
  terms?: string | null;
  poject?: IProject | null;
  document?: IDocument | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
