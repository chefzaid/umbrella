export interface IParameterGroup {
  id: number;
  label?: string | null;
  description?: string | null;
}

export type NewParameterGroup = Omit<IParameterGroup, 'id'> & { id: null };
