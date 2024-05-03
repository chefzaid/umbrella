export interface IExpenseType {
  id: number;
  label?: string | null;
  description?: string | null;
  reimbursmentPct?: number | null;
}

export type NewExpenseType = Omit<IExpenseType, 'id'> & { id: null };
