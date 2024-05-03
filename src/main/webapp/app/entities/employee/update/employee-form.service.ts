import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployee, NewEmployee } from '../employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployee for edit and NewEmployeeFormGroupInput for create.
 */
type EmployeeFormGroupInput = IEmployee | PartialWithRequiredKeyOf<NewEmployee>;

type EmployeeFormDefaults = Pick<NewEmployee, 'id'>;

type EmployeeFormGroupContent = {
  id: FormControl<IEmployee['id'] | NewEmployee['id']>;
  employeeNumber: FormControl<IEmployee['employeeNumber']>;
  birthDate: FormControl<IEmployee['birthDate']>;
  birthPlace: FormControl<IEmployee['birthPlace']>;
  nationality: FormControl<IEmployee['nationality']>;
  gender: FormControl<IEmployee['gender']>;
  maritalStatus: FormControl<IEmployee['maritalStatus']>;
  dependentChildrenNumber: FormControl<IEmployee['dependentChildrenNumber']>;
  socialSecurityNumber: FormControl<IEmployee['socialSecurityNumber']>;
  iban: FormControl<IEmployee['iban']>;
  bicSwift: FormControl<IEmployee['bicSwift']>;
  user: FormControl<IEmployee['user']>;
  address: FormControl<IEmployee['address']>;
  contract: FormControl<IEmployee['contract']>;
  idDocument: FormControl<IEmployee['idDocument']>;
  wallet: FormControl<IEmployee['wallet']>;
  manager: FormControl<IEmployee['manager']>;
};

export type EmployeeFormGroup = FormGroup<EmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeFormService {
  createEmployeeFormGroup(employee: EmployeeFormGroupInput = { id: null }): EmployeeFormGroup {
    const employeeRawValue = {
      ...this.getFormDefaults(),
      ...employee,
    };
    return new FormGroup<EmployeeFormGroupContent>({
      id: new FormControl(
        { value: employeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      employeeNumber: new FormControl(employeeRawValue.employeeNumber, {
        validators: [Validators.required],
      }),
      birthDate: new FormControl(employeeRawValue.birthDate, {
        validators: [Validators.required],
      }),
      birthPlace: new FormControl(employeeRawValue.birthPlace, {
        validators: [Validators.required],
      }),
      nationality: new FormControl(employeeRawValue.nationality),
      gender: new FormControl(employeeRawValue.gender),
      maritalStatus: new FormControl(employeeRawValue.maritalStatus),
      dependentChildrenNumber: new FormControl(employeeRawValue.dependentChildrenNumber),
      socialSecurityNumber: new FormControl(employeeRawValue.socialSecurityNumber),
      iban: new FormControl(employeeRawValue.iban),
      bicSwift: new FormControl(employeeRawValue.bicSwift),
      user: new FormControl(employeeRawValue.user),
      address: new FormControl(employeeRawValue.address),
      contract: new FormControl(employeeRawValue.contract),
      idDocument: new FormControl(employeeRawValue.idDocument),
      wallet: new FormControl(employeeRawValue.wallet),
      manager: new FormControl(employeeRawValue.manager),
    });
  }

  getEmployee(form: EmployeeFormGroup): IEmployee | NewEmployee {
    return form.getRawValue() as IEmployee | NewEmployee;
  }

  resetForm(form: EmployeeFormGroup, employee: EmployeeFormGroupInput): void {
    const employeeRawValue = { ...this.getFormDefaults(), ...employee };
    form.reset(
      {
        ...employeeRawValue,
        id: { value: employeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmployeeFormDefaults {
    return {
      id: null,
    };
  }
}
