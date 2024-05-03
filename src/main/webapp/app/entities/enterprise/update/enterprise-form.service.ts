import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEnterprise, NewEnterprise } from '../enterprise.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEnterprise for edit and NewEnterpriseFormGroupInput for create.
 */
type EnterpriseFormGroupInput = IEnterprise | PartialWithRequiredKeyOf<NewEnterprise>;

type EnterpriseFormDefaults = Pick<NewEnterprise, 'id'>;

type EnterpriseFormGroupContent = {
  id: FormControl<IEnterprise['id'] | NewEnterprise['id']>;
  name: FormControl<IEnterprise['name']>;
  companyStatus: FormControl<IEnterprise['companyStatus']>;
  logo: FormControl<IEnterprise['logo']>;
  logoContentType: FormControl<IEnterprise['logoContentType']>;
  siret: FormControl<IEnterprise['siret']>;
  siren: FormControl<IEnterprise['siren']>;
  salesTaxNumber: FormControl<IEnterprise['salesTaxNumber']>;
  iban: FormControl<IEnterprise['iban']>;
  bicSwift: FormControl<IEnterprise['bicSwift']>;
  website: FormControl<IEnterprise['website']>;
  defaultInvoiceTerms: FormControl<IEnterprise['defaultInvoiceTerms']>;
};

export type EnterpriseFormGroup = FormGroup<EnterpriseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnterpriseFormService {
  createEnterpriseFormGroup(enterprise: EnterpriseFormGroupInput = { id: null }): EnterpriseFormGroup {
    const enterpriseRawValue = {
      ...this.getFormDefaults(),
      ...enterprise,
    };
    return new FormGroup<EnterpriseFormGroupContent>({
      id: new FormControl(
        { value: enterpriseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(enterpriseRawValue.name),
      companyStatus: new FormControl(enterpriseRawValue.companyStatus),
      logo: new FormControl(enterpriseRawValue.logo),
      logoContentType: new FormControl(enterpriseRawValue.logoContentType),
      siret: new FormControl(enterpriseRawValue.siret),
      siren: new FormControl(enterpriseRawValue.siren),
      salesTaxNumber: new FormControl(enterpriseRawValue.salesTaxNumber),
      iban: new FormControl(enterpriseRawValue.iban),
      bicSwift: new FormControl(enterpriseRawValue.bicSwift),
      website: new FormControl(enterpriseRawValue.website),
      defaultInvoiceTerms: new FormControl(enterpriseRawValue.defaultInvoiceTerms),
    });
  }

  getEnterprise(form: EnterpriseFormGroup): IEnterprise | NewEnterprise {
    return form.getRawValue() as IEnterprise | NewEnterprise;
  }

  resetForm(form: EnterpriseFormGroup, enterprise: EnterpriseFormGroupInput): void {
    const enterpriseRawValue = { ...this.getFormDefaults(), ...enterprise };
    form.reset(
      {
        ...enterpriseRawValue,
        id: { value: enterpriseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnterpriseFormDefaults {
    return {
      id: null,
    };
  }
}
