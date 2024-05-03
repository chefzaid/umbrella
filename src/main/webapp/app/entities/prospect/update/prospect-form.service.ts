import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProspect, NewProspect } from '../prospect.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProspect for edit and NewProspectFormGroupInput for create.
 */
type ProspectFormGroupInput = IProspect | PartialWithRequiredKeyOf<NewProspect>;

type ProspectFormDefaults = Pick<NewProspect, 'id'>;

type ProspectFormGroupContent = {
  id: FormControl<IProspect['id'] | NewProspect['id']>;
  dailyRate: FormControl<IProspect['dailyRate']>;
  monthlyExpenses: FormControl<IProspect['monthlyExpenses']>;
  taxRate: FormControl<IProspect['taxRate']>;
  expectedStartDate: FormControl<IProspect['expectedStartDate']>;
  expectedClient: FormControl<IProspect['expectedClient']>;
  notes: FormControl<IProspect['notes']>;
  formula: FormControl<IProspect['formula']>;
};

export type ProspectFormGroup = FormGroup<ProspectFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProspectFormService {
  createProspectFormGroup(prospect: ProspectFormGroupInput = { id: null }): ProspectFormGroup {
    const prospectRawValue = {
      ...this.getFormDefaults(),
      ...prospect,
    };
    return new FormGroup<ProspectFormGroupContent>({
      id: new FormControl(
        { value: prospectRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dailyRate: new FormControl(prospectRawValue.dailyRate),
      monthlyExpenses: new FormControl(prospectRawValue.monthlyExpenses),
      taxRate: new FormControl(prospectRawValue.taxRate),
      expectedStartDate: new FormControl(prospectRawValue.expectedStartDate),
      expectedClient: new FormControl(prospectRawValue.expectedClient),
      notes: new FormControl(prospectRawValue.notes),
      formula: new FormControl(prospectRawValue.formula),
    });
  }

  getProspect(form: ProspectFormGroup): IProspect | NewProspect {
    return form.getRawValue() as IProspect | NewProspect;
  }

  resetForm(form: ProspectFormGroup, prospect: ProspectFormGroupInput): void {
    const prospectRawValue = { ...this.getFormDefaults(), ...prospect };
    form.reset(
      {
        ...prospectRawValue,
        id: { value: prospectRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProspectFormDefaults {
    return {
      id: null,
    };
  }
}
