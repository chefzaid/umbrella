import { IWallet, NewWallet } from './wallet.model';

export const sampleWithRequiredData: IWallet = {
  id: 25107,
};

export const sampleWithPartialData: IWallet = {
  id: 5628,
};

export const sampleWithFullData: IWallet = {
  id: 25066,
  totalBalance: 31836.91,
  totalProvision: 30358.81,
};

export const sampleWithNewData: NewWallet = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
