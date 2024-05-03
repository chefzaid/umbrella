import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IContact, NewContact } from '../contact.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContact for edit and NewContactFormGroupInput for create.
 */
type ContactFormGroupInput = IContact | PartialWithRequiredKeyOf<NewContact>;

type ContactFormDefaults = Pick<NewContact, 'id'>;

type ContactFormGroupContent = {
  id: FormControl<IContact['id'] | NewContact['id']>;
  firstName: FormControl<IContact['firstName']>;
  lastName: FormControl<IContact['lastName']>;
  email: FormControl<IContact['email']>;
  phoneNumber: FormControl<IContact['phoneNumber']>;
  jobTitle: FormControl<IContact['jobTitle']>;
  address: FormControl<IContact['address']>;
  client: FormControl<IContact['client']>;
};

export type ContactFormGroup = FormGroup<ContactFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContactFormService {
  createContactFormGroup(contact: ContactFormGroupInput = { id: null }): ContactFormGroup {
    const contactRawValue = {
      ...this.getFormDefaults(),
      ...contact,
    };
    return new FormGroup<ContactFormGroupContent>({
      id: new FormControl(
        { value: contactRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(contactRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(contactRawValue.lastName, {
        validators: [Validators.required],
      }),
      email: new FormControl(contactRawValue.email, {
        validators: [Validators.required],
      }),
      phoneNumber: new FormControl(contactRawValue.phoneNumber, {
        validators: [Validators.required],
      }),
      jobTitle: new FormControl(contactRawValue.jobTitle),
      address: new FormControl(contactRawValue.address),
      client: new FormControl(contactRawValue.client),
    });
  }

  getContact(form: ContactFormGroup): IContact | NewContact {
    return form.getRawValue() as IContact | NewContact;
  }

  resetForm(form: ContactFormGroup, contact: ContactFormGroupInput): void {
    const contactRawValue = { ...this.getFormDefaults(), ...contact };
    form.reset(
      {
        ...contactRawValue,
        id: { value: contactRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContactFormDefaults {
    return {
      id: null,
    };
  }
}
