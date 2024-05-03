import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExpenseType, NewExpenseType } from '../expense-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpenseType for edit and NewExpenseTypeFormGroupInput for create.
 */
type ExpenseTypeFormGroupInput = IExpenseType | PartialWithRequiredKeyOf<NewExpenseType>;

type ExpenseTypeFormDefaults = Pick<NewExpenseType, 'id'>;

type ExpenseTypeFormGroupContent = {
  id: FormControl<IExpenseType['id'] | NewExpenseType['id']>;
  label: FormControl<IExpenseType['label']>;
  description: FormControl<IExpenseType['description']>;
  reimbursmentPct: FormControl<IExpenseType['reimbursmentPct']>;
};

export type ExpenseTypeFormGroup = FormGroup<ExpenseTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseTypeFormService {
  createExpenseTypeFormGroup(expenseType: ExpenseTypeFormGroupInput = { id: null }): ExpenseTypeFormGroup {
    const expenseTypeRawValue = {
      ...this.getFormDefaults(),
      ...expenseType,
    };
    return new FormGroup<ExpenseTypeFormGroupContent>({
      id: new FormControl(
        { value: expenseTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(expenseTypeRawValue.label),
      description: new FormControl(expenseTypeRawValue.description),
      reimbursmentPct: new FormControl(expenseTypeRawValue.reimbursmentPct),
    });
  }

  getExpenseType(form: ExpenseTypeFormGroup): IExpenseType | NewExpenseType {
    return form.getRawValue() as IExpenseType | NewExpenseType;
  }

  resetForm(form: ExpenseTypeFormGroup, expenseType: ExpenseTypeFormGroupInput): void {
    const expenseTypeRawValue = { ...this.getFormDefaults(), ...expenseType };
    form.reset(
      {
        ...expenseTypeRawValue,
        id: { value: expenseTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExpenseTypeFormDefaults {
    return {
      id: null,
    };
  }
}
