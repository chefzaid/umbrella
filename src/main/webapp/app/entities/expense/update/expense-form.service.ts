import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExpense, NewExpense } from '../expense.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpense for edit and NewExpenseFormGroupInput for create.
 */
type ExpenseFormGroupInput = IExpense | PartialWithRequiredKeyOf<NewExpense>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExpense | NewExpense> = Omit<T, 'submitDate' | 'validationDate'> & {
  submitDate?: string | null;
  validationDate?: string | null;
};

type ExpenseFormRawValue = FormValueOf<IExpense>;

type NewExpenseFormRawValue = FormValueOf<NewExpense>;

type ExpenseFormDefaults = Pick<NewExpense, 'id' | 'rebillableToClient' | 'submitDate' | 'validationDate'>;

type ExpenseFormGroupContent = {
  id: FormControl<ExpenseFormRawValue['id'] | NewExpense['id']>;
  label: FormControl<ExpenseFormRawValue['label']>;
  description: FormControl<ExpenseFormRawValue['description']>;
  amount: FormControl<ExpenseFormRawValue['amount']>;
  currency: FormControl<ExpenseFormRawValue['currency']>;
  tax: FormControl<ExpenseFormRawValue['tax']>;
  expenseDate: FormControl<ExpenseFormRawValue['expenseDate']>;
  rebillableToClient: FormControl<ExpenseFormRawValue['rebillableToClient']>;
  comment: FormControl<ExpenseFormRawValue['comment']>;
  submitDate: FormControl<ExpenseFormRawValue['submitDate']>;
  validationDate: FormControl<ExpenseFormRawValue['validationDate']>;
  paymentMethod: FormControl<ExpenseFormRawValue['paymentMethod']>;
  project: FormControl<ExpenseFormRawValue['project']>;
  expenseNote: FormControl<ExpenseFormRawValue['expenseNote']>;
};

export type ExpenseFormGroup = FormGroup<ExpenseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseFormService {
  createExpenseFormGroup(expense: ExpenseFormGroupInput = { id: null }): ExpenseFormGroup {
    const expenseRawValue = this.convertExpenseToExpenseRawValue({
      ...this.getFormDefaults(),
      ...expense,
    });
    return new FormGroup<ExpenseFormGroupContent>({
      id: new FormControl(
        { value: expenseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(expenseRawValue.label, {
        validators: [Validators.required],
      }),
      description: new FormControl(expenseRawValue.description),
      amount: new FormControl(expenseRawValue.amount, {
        validators: [Validators.required],
      }),
      currency: new FormControl(expenseRawValue.currency),
      tax: new FormControl(expenseRawValue.tax),
      expenseDate: new FormControl(expenseRawValue.expenseDate),
      rebillableToClient: new FormControl(expenseRawValue.rebillableToClient),
      comment: new FormControl(expenseRawValue.comment),
      submitDate: new FormControl(expenseRawValue.submitDate),
      validationDate: new FormControl(expenseRawValue.validationDate),
      paymentMethod: new FormControl(expenseRawValue.paymentMethod),
      project: new FormControl(expenseRawValue.project),
      expenseNote: new FormControl(expenseRawValue.expenseNote),
    });
  }

  getExpense(form: ExpenseFormGroup): IExpense | NewExpense {
    return this.convertExpenseRawValueToExpense(form.getRawValue() as ExpenseFormRawValue | NewExpenseFormRawValue);
  }

  resetForm(form: ExpenseFormGroup, expense: ExpenseFormGroupInput): void {
    const expenseRawValue = this.convertExpenseToExpenseRawValue({ ...this.getFormDefaults(), ...expense });
    form.reset(
      {
        ...expenseRawValue,
        id: { value: expenseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExpenseFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      rebillableToClient: false,
      submitDate: currentTime,
      validationDate: currentTime,
    };
  }

  private convertExpenseRawValueToExpense(rawExpense: ExpenseFormRawValue | NewExpenseFormRawValue): IExpense | NewExpense {
    return {
      ...rawExpense,
      submitDate: dayjs(rawExpense.submitDate, DATE_TIME_FORMAT),
      validationDate: dayjs(rawExpense.validationDate, DATE_TIME_FORMAT),
    };
  }

  private convertExpenseToExpenseRawValue(
    expense: IExpense | (Partial<NewExpense> & ExpenseFormDefaults),
  ): ExpenseFormRawValue | PartialWithRequiredKeyOf<NewExpenseFormRawValue> {
    return {
      ...expense,
      submitDate: expense.submitDate ? expense.submitDate.format(DATE_TIME_FORMAT) : undefined,
      validationDate: expense.validationDate ? expense.validationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
