import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExpenseNote, NewExpenseNote } from '../expense-note.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpenseNote for edit and NewExpenseNoteFormGroupInput for create.
 */
type ExpenseNoteFormGroupInput = IExpenseNote | PartialWithRequiredKeyOf<NewExpenseNote>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExpenseNote | NewExpenseNote> = Omit<T, 'submitDate' | 'validationDate'> & {
  submitDate?: string | null;
  validationDate?: string | null;
};

type ExpenseNoteFormRawValue = FormValueOf<IExpenseNote>;

type NewExpenseNoteFormRawValue = FormValueOf<NewExpenseNote>;

type ExpenseNoteFormDefaults = Pick<NewExpenseNote, 'id' | 'submitDate' | 'validationDate'>;

type ExpenseNoteFormGroupContent = {
  id: FormControl<ExpenseNoteFormRawValue['id'] | NewExpenseNote['id']>;
  label: FormControl<ExpenseNoteFormRawValue['label']>;
  concernedMonth: FormControl<ExpenseNoteFormRawValue['concernedMonth']>;
  creationDate: FormControl<ExpenseNoteFormRawValue['creationDate']>;
  submitDate: FormControl<ExpenseNoteFormRawValue['submitDate']>;
  validationDate: FormControl<ExpenseNoteFormRawValue['validationDate']>;
  mileageAllowance: FormControl<ExpenseNoteFormRawValue['mileageAllowance']>;
  document: FormControl<ExpenseNoteFormRawValue['document']>;
  employee: FormControl<ExpenseNoteFormRawValue['employee']>;
};

export type ExpenseNoteFormGroup = FormGroup<ExpenseNoteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseNoteFormService {
  createExpenseNoteFormGroup(expenseNote: ExpenseNoteFormGroupInput = { id: null }): ExpenseNoteFormGroup {
    const expenseNoteRawValue = this.convertExpenseNoteToExpenseNoteRawValue({
      ...this.getFormDefaults(),
      ...expenseNote,
    });
    return new FormGroup<ExpenseNoteFormGroupContent>({
      id: new FormControl(
        { value: expenseNoteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(expenseNoteRawValue.label),
      concernedMonth: new FormControl(expenseNoteRawValue.concernedMonth),
      creationDate: new FormControl(expenseNoteRawValue.creationDate),
      submitDate: new FormControl(expenseNoteRawValue.submitDate),
      validationDate: new FormControl(expenseNoteRawValue.validationDate),
      mileageAllowance: new FormControl(expenseNoteRawValue.mileageAllowance),
      document: new FormControl(expenseNoteRawValue.document),
      employee: new FormControl(expenseNoteRawValue.employee),
    });
  }

  getExpenseNote(form: ExpenseNoteFormGroup): IExpenseNote | NewExpenseNote {
    return this.convertExpenseNoteRawValueToExpenseNote(form.getRawValue() as ExpenseNoteFormRawValue | NewExpenseNoteFormRawValue);
  }

  resetForm(form: ExpenseNoteFormGroup, expenseNote: ExpenseNoteFormGroupInput): void {
    const expenseNoteRawValue = this.convertExpenseNoteToExpenseNoteRawValue({ ...this.getFormDefaults(), ...expenseNote });
    form.reset(
      {
        ...expenseNoteRawValue,
        id: { value: expenseNoteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExpenseNoteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      submitDate: currentTime,
      validationDate: currentTime,
    };
  }

  private convertExpenseNoteRawValueToExpenseNote(
    rawExpenseNote: ExpenseNoteFormRawValue | NewExpenseNoteFormRawValue,
  ): IExpenseNote | NewExpenseNote {
    return {
      ...rawExpenseNote,
      submitDate: dayjs(rawExpenseNote.submitDate, DATE_TIME_FORMAT),
      validationDate: dayjs(rawExpenseNote.validationDate, DATE_TIME_FORMAT),
    };
  }

  private convertExpenseNoteToExpenseNoteRawValue(
    expenseNote: IExpenseNote | (Partial<NewExpenseNote> & ExpenseNoteFormDefaults),
  ): ExpenseNoteFormRawValue | PartialWithRequiredKeyOf<NewExpenseNoteFormRawValue> {
    return {
      ...expenseNote,
      submitDate: expenseNote.submitDate ? expenseNote.submitDate.format(DATE_TIME_FORMAT) : undefined,
      validationDate: expenseNote.validationDate ? expenseNote.validationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
