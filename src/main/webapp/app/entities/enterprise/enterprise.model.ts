export interface IEnterprise {
  id: number;
  name?: string | null;
  companyStatus?: string | null;
  logo?: string | null;
  logoContentType?: string | null;
  siret?: string | null;
  siren?: string | null;
  salesTaxNumber?: string | null;
  iban?: string | null;
  bicSwift?: string | null;
  website?: string | null;
  defaultInvoiceTerms?: string | null;
}

export type NewEnterprise = Omit<IEnterprise, 'id'> & { id: null };
