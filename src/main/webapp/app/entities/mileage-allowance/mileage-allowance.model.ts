export interface IMileageAllowance {
  id: number;
  mileage?: number | null;
  multiplier?: number | null;
}

export type NewMileageAllowance = Omit<IMileageAllowance, 'id'> & { id: null };
