export interface IFormula {
  id: number;
  label?: string | null;
  adminFeesPct?: number | null;
  additionalFeesPct?: number | null;
}

export type NewFormula = Omit<IFormula, 'id'> & { id: null };
