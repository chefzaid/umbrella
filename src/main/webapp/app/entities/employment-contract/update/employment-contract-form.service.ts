import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmploymentContract, NewEmploymentContract } from '../employment-contract.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmploymentContract for edit and NewEmploymentContractFormGroupInput for create.
 */
type EmploymentContractFormGroupInput = IEmploymentContract | PartialWithRequiredKeyOf<NewEmploymentContract>;

type EmploymentContractFormDefaults = Pick<NewEmploymentContract, 'id' | 'signedByEmployer' | 'signedByEmployee'>;

type EmploymentContractFormGroupContent = {
  id: FormControl<IEmploymentContract['id'] | NewEmploymentContract['id']>;
  type: FormControl<IEmploymentContract['type']>;
  jobTitle: FormControl<IEmploymentContract['jobTitle']>;
  hireDate: FormControl<IEmploymentContract['hireDate']>;
  salary: FormControl<IEmploymentContract['salary']>;
  yearlyWorkDays: FormControl<IEmploymentContract['yearlyWorkDays']>;
  signedByEmployer: FormControl<IEmploymentContract['signedByEmployer']>;
  signedByEmployee: FormControl<IEmploymentContract['signedByEmployee']>;
  forumula: FormControl<IEmploymentContract['forumula']>;
  employmentContract: FormControl<IEmploymentContract['employmentContract']>;
};

export type EmploymentContractFormGroup = FormGroup<EmploymentContractFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmploymentContractFormService {
  createEmploymentContractFormGroup(employmentContract: EmploymentContractFormGroupInput = { id: null }): EmploymentContractFormGroup {
    const employmentContractRawValue = {
      ...this.getFormDefaults(),
      ...employmentContract,
    };
    return new FormGroup<EmploymentContractFormGroupContent>({
      id: new FormControl(
        { value: employmentContractRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(employmentContractRawValue.type),
      jobTitle: new FormControl(employmentContractRawValue.jobTitle),
      hireDate: new FormControl(employmentContractRawValue.hireDate),
      salary: new FormControl(employmentContractRawValue.salary),
      yearlyWorkDays: new FormControl(employmentContractRawValue.yearlyWorkDays),
      signedByEmployer: new FormControl(employmentContractRawValue.signedByEmployer),
      signedByEmployee: new FormControl(employmentContractRawValue.signedByEmployee),
      forumula: new FormControl(employmentContractRawValue.forumula),
      employmentContract: new FormControl(employmentContractRawValue.employmentContract),
    });
  }

  getEmploymentContract(form: EmploymentContractFormGroup): IEmploymentContract | NewEmploymentContract {
    return form.getRawValue() as IEmploymentContract | NewEmploymentContract;
  }

  resetForm(form: EmploymentContractFormGroup, employmentContract: EmploymentContractFormGroupInput): void {
    const employmentContractRawValue = { ...this.getFormDefaults(), ...employmentContract };
    form.reset(
      {
        ...employmentContractRawValue,
        id: { value: employmentContractRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmploymentContractFormDefaults {
    return {
      id: null,
      signedByEmployer: false,
      signedByEmployee: false,
    };
  }
}
