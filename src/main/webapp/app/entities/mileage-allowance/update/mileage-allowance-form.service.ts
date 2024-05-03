import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMileageAllowance, NewMileageAllowance } from '../mileage-allowance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMileageAllowance for edit and NewMileageAllowanceFormGroupInput for create.
 */
type MileageAllowanceFormGroupInput = IMileageAllowance | PartialWithRequiredKeyOf<NewMileageAllowance>;

type MileageAllowanceFormDefaults = Pick<NewMileageAllowance, 'id'>;

type MileageAllowanceFormGroupContent = {
  id: FormControl<IMileageAllowance['id'] | NewMileageAllowance['id']>;
  mileage: FormControl<IMileageAllowance['mileage']>;
  multiplier: FormControl<IMileageAllowance['multiplier']>;
};

export type MileageAllowanceFormGroup = FormGroup<MileageAllowanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MileageAllowanceFormService {
  createMileageAllowanceFormGroup(mileageAllowance: MileageAllowanceFormGroupInput = { id: null }): MileageAllowanceFormGroup {
    const mileageAllowanceRawValue = {
      ...this.getFormDefaults(),
      ...mileageAllowance,
    };
    return new FormGroup<MileageAllowanceFormGroupContent>({
      id: new FormControl(
        { value: mileageAllowanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      mileage: new FormControl(mileageAllowanceRawValue.mileage, {
        validators: [Validators.required],
      }),
      multiplier: new FormControl(mileageAllowanceRawValue.multiplier),
    });
  }

  getMileageAllowance(form: MileageAllowanceFormGroup): IMileageAllowance | NewMileageAllowance {
    return form.getRawValue() as IMileageAllowance | NewMileageAllowance;
  }

  resetForm(form: MileageAllowanceFormGroup, mileageAllowance: MileageAllowanceFormGroupInput): void {
    const mileageAllowanceRawValue = { ...this.getFormDefaults(), ...mileageAllowance };
    form.reset(
      {
        ...mileageAllowanceRawValue,
        id: { value: mileageAllowanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MileageAllowanceFormDefaults {
    return {
      id: null,
    };
  }
}
