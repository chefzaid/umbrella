import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPaySlip, NewPaySlip } from '../pay-slip.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaySlip for edit and NewPaySlipFormGroupInput for create.
 */
type PaySlipFormGroupInput = IPaySlip | PartialWithRequiredKeyOf<NewPaySlip>;

type PaySlipFormDefaults = Pick<NewPaySlip, 'id'>;

type PaySlipFormGroupContent = {
  id: FormControl<IPaySlip['id'] | NewPaySlip['id']>;
  superGrossSalary: FormControl<IPaySlip['superGrossSalary']>;
  grossSalary: FormControl<IPaySlip['grossSalary']>;
  netSalary: FormControl<IPaySlip['netSalary']>;
  taxRate: FormControl<IPaySlip['taxRate']>;
  amountPaid: FormControl<IPaySlip['amountPaid']>;
  totalExpenses: FormControl<IPaySlip['totalExpenses']>;
  document: FormControl<IPaySlip['document']>;
  employee: FormControl<IPaySlip['employee']>;
};

export type PaySlipFormGroup = FormGroup<PaySlipFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaySlipFormService {
  createPaySlipFormGroup(paySlip: PaySlipFormGroupInput = { id: null }): PaySlipFormGroup {
    const paySlipRawValue = {
      ...this.getFormDefaults(),
      ...paySlip,
    };
    return new FormGroup<PaySlipFormGroupContent>({
      id: new FormControl(
        { value: paySlipRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      superGrossSalary: new FormControl(paySlipRawValue.superGrossSalary),
      grossSalary: new FormControl(paySlipRawValue.grossSalary),
      netSalary: new FormControl(paySlipRawValue.netSalary),
      taxRate: new FormControl(paySlipRawValue.taxRate),
      amountPaid: new FormControl(paySlipRawValue.amountPaid),
      totalExpenses: new FormControl(paySlipRawValue.totalExpenses),
      document: new FormControl(paySlipRawValue.document),
      employee: new FormControl(paySlipRawValue.employee),
    });
  }

  getPaySlip(form: PaySlipFormGroup): IPaySlip | NewPaySlip {
    return form.getRawValue() as IPaySlip | NewPaySlip;
  }

  resetForm(form: PaySlipFormGroup, paySlip: PaySlipFormGroupInput): void {
    const paySlipRawValue = { ...this.getFormDefaults(), ...paySlip };
    form.reset(
      {
        ...paySlipRawValue,
        id: { value: paySlipRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaySlipFormDefaults {
    return {
      id: null,
    };
  }
}
