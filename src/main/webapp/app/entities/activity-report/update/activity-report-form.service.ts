import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IActivityReport, NewActivityReport } from '../activity-report.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IActivityReport for edit and NewActivityReportFormGroupInput for create.
 */
type ActivityReportFormGroupInput = IActivityReport | PartialWithRequiredKeyOf<NewActivityReport>;

type ActivityReportFormDefaults = Pick<NewActivityReport, 'id'>;

type ActivityReportFormGroupContent = {
  id: FormControl<IActivityReport['id'] | NewActivityReport['id']>;
  month: FormControl<IActivityReport['month']>;
  balance: FormControl<IActivityReport['balance']>;
  comments: FormControl<IActivityReport['comments']>;
  employee: FormControl<IActivityReport['employee']>;
};

export type ActivityReportFormGroup = FormGroup<ActivityReportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ActivityReportFormService {
  createActivityReportFormGroup(activityReport: ActivityReportFormGroupInput = { id: null }): ActivityReportFormGroup {
    const activityReportRawValue = {
      ...this.getFormDefaults(),
      ...activityReport,
    };
    return new FormGroup<ActivityReportFormGroupContent>({
      id: new FormControl(
        { value: activityReportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      month: new FormControl(activityReportRawValue.month),
      balance: new FormControl(activityReportRawValue.balance),
      comments: new FormControl(activityReportRawValue.comments),
      employee: new FormControl(activityReportRawValue.employee),
    });
  }

  getActivityReport(form: ActivityReportFormGroup): IActivityReport | NewActivityReport {
    return form.getRawValue() as IActivityReport | NewActivityReport;
  }

  resetForm(form: ActivityReportFormGroup, activityReport: ActivityReportFormGroupInput): void {
    const activityReportRawValue = { ...this.getFormDefaults(), ...activityReport };
    form.reset(
      {
        ...activityReportRawValue,
        id: { value: activityReportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ActivityReportFormDefaults {
    return {
      id: null,
    };
  }
}
