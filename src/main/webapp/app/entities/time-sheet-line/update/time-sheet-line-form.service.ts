import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITimeSheetLine, NewTimeSheetLine } from '../time-sheet-line.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITimeSheetLine for edit and NewTimeSheetLineFormGroupInput for create.
 */
type TimeSheetLineFormGroupInput = ITimeSheetLine | PartialWithRequiredKeyOf<NewTimeSheetLine>;

type TimeSheetLineFormDefaults = Pick<NewTimeSheetLine, 'id'>;

type TimeSheetLineFormGroupContent = {
  id: FormControl<ITimeSheetLine['id'] | NewTimeSheetLine['id']>;
  monthlyDays: FormControl<ITimeSheetLine['monthlyDays']>;
  extraHours: FormControl<ITimeSheetLine['extraHours']>;
  comments: FormControl<ITimeSheetLine['comments']>;
  timeSheet: FormControl<ITimeSheetLine['timeSheet']>;
};

export type TimeSheetLineFormGroup = FormGroup<TimeSheetLineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TimeSheetLineFormService {
  createTimeSheetLineFormGroup(timeSheetLine: TimeSheetLineFormGroupInput = { id: null }): TimeSheetLineFormGroup {
    const timeSheetLineRawValue = {
      ...this.getFormDefaults(),
      ...timeSheetLine,
    };
    return new FormGroup<TimeSheetLineFormGroupContent>({
      id: new FormControl(
        { value: timeSheetLineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      monthlyDays: new FormControl(timeSheetLineRawValue.monthlyDays, {
        validators: [Validators.required],
      }),
      extraHours: new FormControl(timeSheetLineRawValue.extraHours),
      comments: new FormControl(timeSheetLineRawValue.comments),
      timeSheet: new FormControl(timeSheetLineRawValue.timeSheet),
    });
  }

  getTimeSheetLine(form: TimeSheetLineFormGroup): ITimeSheetLine | NewTimeSheetLine {
    return form.getRawValue() as ITimeSheetLine | NewTimeSheetLine;
  }

  resetForm(form: TimeSheetLineFormGroup, timeSheetLine: TimeSheetLineFormGroupInput): void {
    const timeSheetLineRawValue = { ...this.getFormDefaults(), ...timeSheetLine };
    form.reset(
      {
        ...timeSheetLineRawValue,
        id: { value: timeSheetLineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TimeSheetLineFormDefaults {
    return {
      id: null,
    };
  }
}
