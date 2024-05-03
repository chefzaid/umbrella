import { IInvoice } from 'app/entities/invoice/invoice.model';

export interface IInvoiceLine {
  id: number;
  label?: string | null;
  description?: string | null;
  price?: number | null;
  quantity?: number | null;
  invoice?: IInvoice | null;
}

export type NewInvoiceLine = Omit<IInvoiceLine, 'id'> & { id: null };
