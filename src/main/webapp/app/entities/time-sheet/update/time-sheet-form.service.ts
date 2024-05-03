import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITimeSheet, NewTimeSheet } from '../time-sheet.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITimeSheet for edit and NewTimeSheetFormGroupInput for create.
 */
type TimeSheetFormGroupInput = ITimeSheet | PartialWithRequiredKeyOf<NewTimeSheet>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITimeSheet | NewTimeSheet> = Omit<T, 'submitDate' | 'validationDate'> & {
  submitDate?: string | null;
  validationDate?: string | null;
};

type TimeSheetFormRawValue = FormValueOf<ITimeSheet>;

type NewTimeSheetFormRawValue = FormValueOf<NewTimeSheet>;

type TimeSheetFormDefaults = Pick<NewTimeSheet, 'id' | 'submitDate' | 'validationDate'>;

type TimeSheetFormGroupContent = {
  id: FormControl<TimeSheetFormRawValue['id'] | NewTimeSheet['id']>;
  concernedMonth: FormControl<TimeSheetFormRawValue['concernedMonth']>;
  creationDate: FormControl<TimeSheetFormRawValue['creationDate']>;
  submitDate: FormControl<TimeSheetFormRawValue['submitDate']>;
  validationDate: FormControl<TimeSheetFormRawValue['validationDate']>;
  project: FormControl<TimeSheetFormRawValue['project']>;
  document: FormControl<TimeSheetFormRawValue['document']>;
  employee: FormControl<TimeSheetFormRawValue['employee']>;
};

export type TimeSheetFormGroup = FormGroup<TimeSheetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TimeSheetFormService {
  createTimeSheetFormGroup(timeSheet: TimeSheetFormGroupInput = { id: null }): TimeSheetFormGroup {
    const timeSheetRawValue = this.convertTimeSheetToTimeSheetRawValue({
      ...this.getFormDefaults(),
      ...timeSheet,
    });
    return new FormGroup<TimeSheetFormGroupContent>({
      id: new FormControl(
        { value: timeSheetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      concernedMonth: new FormControl(timeSheetRawValue.concernedMonth, {
        validators: [Validators.required],
      }),
      creationDate: new FormControl(timeSheetRawValue.creationDate),
      submitDate: new FormControl(timeSheetRawValue.submitDate),
      validationDate: new FormControl(timeSheetRawValue.validationDate),
      project: new FormControl(timeSheetRawValue.project),
      document: new FormControl(timeSheetRawValue.document),
      employee: new FormControl(timeSheetRawValue.employee),
    });
  }

  getTimeSheet(form: TimeSheetFormGroup): ITimeSheet | NewTimeSheet {
    return this.convertTimeSheetRawValueToTimeSheet(form.getRawValue() as TimeSheetFormRawValue | NewTimeSheetFormRawValue);
  }

  resetForm(form: TimeSheetFormGroup, timeSheet: TimeSheetFormGroupInput): void {
    const timeSheetRawValue = this.convertTimeSheetToTimeSheetRawValue({ ...this.getFormDefaults(), ...timeSheet });
    form.reset(
      {
        ...timeSheetRawValue,
        id: { value: timeSheetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TimeSheetFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      submitDate: currentTime,
      validationDate: currentTime,
    };
  }

  private convertTimeSheetRawValueToTimeSheet(rawTimeSheet: TimeSheetFormRawValue | NewTimeSheetFormRawValue): ITimeSheet | NewTimeSheet {
    return {
      ...rawTimeSheet,
      submitDate: dayjs(rawTimeSheet.submitDate, DATE_TIME_FORMAT),
      validationDate: dayjs(rawTimeSheet.validationDate, DATE_TIME_FORMAT),
    };
  }

  private convertTimeSheetToTimeSheetRawValue(
    timeSheet: ITimeSheet | (Partial<NewTimeSheet> & TimeSheetFormDefaults),
  ): TimeSheetFormRawValue | PartialWithRequiredKeyOf<NewTimeSheetFormRawValue> {
    return {
      ...timeSheet,
      submitDate: timeSheet.submitDate ? timeSheet.submitDate.format(DATE_TIME_FORMAT) : undefined,
      validationDate: timeSheet.validationDate ? timeSheet.validationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
