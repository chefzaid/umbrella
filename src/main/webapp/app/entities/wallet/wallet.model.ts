export interface IWallet {
  id: number;
  totalBalance?: number | null;
  totalProvision?: number | null;
}

export type NewWallet = Omit<IWallet, 'id'> & { id: null };
