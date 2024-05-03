import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IParameterGroup, NewParameterGroup } from '../parameter-group.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParameterGroup for edit and NewParameterGroupFormGroupInput for create.
 */
type ParameterGroupFormGroupInput = IParameterGroup | PartialWithRequiredKeyOf<NewParameterGroup>;

type ParameterGroupFormDefaults = Pick<NewParameterGroup, 'id'>;

type ParameterGroupFormGroupContent = {
  id: FormControl<IParameterGroup['id'] | NewParameterGroup['id']>;
  label: FormControl<IParameterGroup['label']>;
  description: FormControl<IParameterGroup['description']>;
};

export type ParameterGroupFormGroup = FormGroup<ParameterGroupFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParameterGroupFormService {
  createParameterGroupFormGroup(parameterGroup: ParameterGroupFormGroupInput = { id: null }): ParameterGroupFormGroup {
    const parameterGroupRawValue = {
      ...this.getFormDefaults(),
      ...parameterGroup,
    };
    return new FormGroup<ParameterGroupFormGroupContent>({
      id: new FormControl(
        { value: parameterGroupRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(parameterGroupRawValue.label),
      description: new FormControl(parameterGroupRawValue.description),
    });
  }

  getParameterGroup(form: ParameterGroupFormGroup): IParameterGroup | NewParameterGroup {
    return form.getRawValue() as IParameterGroup | NewParameterGroup;
  }

  resetForm(form: ParameterGroupFormGroup, parameterGroup: ParameterGroupFormGroupInput): void {
    const parameterGroupRawValue = { ...this.getFormDefaults(), ...parameterGroup };
    form.reset(
      {
        ...parameterGroupRawValue,
        id: { value: parameterGroupRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ParameterGroupFormDefaults {
    return {
      id: null,
    };
  }
}
