import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IServiceContract, NewServiceContract } from '../service-contract.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceContract for edit and NewServiceContractFormGroupInput for create.
 */
type ServiceContractFormGroupInput = IServiceContract | PartialWithRequiredKeyOf<NewServiceContract>;

type ServiceContractFormDefaults = Pick<NewServiceContract, 'id' | 'signedBySupplier' | 'signedByClient'>;

type ServiceContractFormGroupContent = {
  id: FormControl<IServiceContract['id'] | NewServiceContract['id']>;
  serviceLabel: FormControl<IServiceContract['serviceLabel']>;
  dailyRate: FormControl<IServiceContract['dailyRate']>;
  startDate: FormControl<IServiceContract['startDate']>;
  endDate: FormControl<IServiceContract['endDate']>;
  extensionTerms: FormControl<IServiceContract['extensionTerms']>;
  signedBySupplier: FormControl<IServiceContract['signedBySupplier']>;
  signedByClient: FormControl<IServiceContract['signedByClient']>;
  employee: FormControl<IServiceContract['employee']>;
  serviceContract: FormControl<IServiceContract['serviceContract']>;
};

export type ServiceContractFormGroup = FormGroup<ServiceContractFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServiceContractFormService {
  createServiceContractFormGroup(serviceContract: ServiceContractFormGroupInput = { id: null }): ServiceContractFormGroup {
    const serviceContractRawValue = {
      ...this.getFormDefaults(),
      ...serviceContract,
    };
    return new FormGroup<ServiceContractFormGroupContent>({
      id: new FormControl(
        { value: serviceContractRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      serviceLabel: new FormControl(serviceContractRawValue.serviceLabel),
      dailyRate: new FormControl(serviceContractRawValue.dailyRate),
      startDate: new FormControl(serviceContractRawValue.startDate),
      endDate: new FormControl(serviceContractRawValue.endDate),
      extensionTerms: new FormControl(serviceContractRawValue.extensionTerms),
      signedBySupplier: new FormControl(serviceContractRawValue.signedBySupplier),
      signedByClient: new FormControl(serviceContractRawValue.signedByClient),
      employee: new FormControl(serviceContractRawValue.employee),
      serviceContract: new FormControl(serviceContractRawValue.serviceContract),
    });
  }

  getServiceContract(form: ServiceContractFormGroup): IServiceContract | NewServiceContract {
    return form.getRawValue() as IServiceContract | NewServiceContract;
  }

  resetForm(form: ServiceContractFormGroup, serviceContract: ServiceContractFormGroupInput): void {
    const serviceContractRawValue = { ...this.getFormDefaults(), ...serviceContract };
    form.reset(
      {
        ...serviceContractRawValue,
        id: { value: serviceContractRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ServiceContractFormDefaults {
    return {
      id: null,
      signedBySupplier: false,
      signedByClient: false,
    };
  }
}
