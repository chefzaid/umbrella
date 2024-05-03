import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAppUser, NewAppUser } from '../app-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppUser for edit and NewAppUserFormGroupInput for create.
 */
type AppUserFormGroupInput = IAppUser | PartialWithRequiredKeyOf<NewAppUser>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAppUser | NewAppUser> = Omit<T, 'creationDate'> & {
  creationDate?: string | null;
};

type AppUserFormRawValue = FormValueOf<IAppUser>;

type NewAppUserFormRawValue = FormValueOf<NewAppUser>;

type AppUserFormDefaults = Pick<NewAppUser, 'id' | 'isAdmin' | 'creationDate'>;

type AppUserFormGroupContent = {
  id: FormControl<AppUserFormRawValue['id'] | NewAppUser['id']>;
  username: FormControl<AppUserFormRawValue['username']>;
  password: FormControl<AppUserFormRawValue['password']>;
  photo: FormControl<AppUserFormRawValue['photo']>;
  photoContentType: FormControl<AppUserFormRawValue['photoContentType']>;
  isAdmin: FormControl<AppUserFormRawValue['isAdmin']>;
  creationDate: FormControl<AppUserFormRawValue['creationDate']>;
  contact: FormControl<AppUserFormRawValue['contact']>;
};

export type AppUserFormGroup = FormGroup<AppUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppUserFormService {
  createAppUserFormGroup(appUser: AppUserFormGroupInput = { id: null }): AppUserFormGroup {
    const appUserRawValue = this.convertAppUserToAppUserRawValue({
      ...this.getFormDefaults(),
      ...appUser,
    });
    return new FormGroup<AppUserFormGroupContent>({
      id: new FormControl(
        { value: appUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      username: new FormControl(appUserRawValue.username, {
        validators: [Validators.required],
      }),
      password: new FormControl(appUserRawValue.password, {
        validators: [Validators.required],
      }),
      photo: new FormControl(appUserRawValue.photo),
      photoContentType: new FormControl(appUserRawValue.photoContentType),
      isAdmin: new FormControl(appUserRawValue.isAdmin),
      creationDate: new FormControl(appUserRawValue.creationDate),
      contact: new FormControl(appUserRawValue.contact),
    });
  }

  getAppUser(form: AppUserFormGroup): IAppUser | NewAppUser {
    return this.convertAppUserRawValueToAppUser(form.getRawValue() as AppUserFormRawValue | NewAppUserFormRawValue);
  }

  resetForm(form: AppUserFormGroup, appUser: AppUserFormGroupInput): void {
    const appUserRawValue = this.convertAppUserToAppUserRawValue({ ...this.getFormDefaults(), ...appUser });
    form.reset(
      {
        ...appUserRawValue,
        id: { value: appUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppUserFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isAdmin: false,
      creationDate: currentTime,
    };
  }

  private convertAppUserRawValueToAppUser(rawAppUser: AppUserFormRawValue | NewAppUserFormRawValue): IAppUser | NewAppUser {
    return {
      ...rawAppUser,
      creationDate: dayjs(rawAppUser.creationDate, DATE_TIME_FORMAT),
    };
  }

  private convertAppUserToAppUserRawValue(
    appUser: IAppUser | (Partial<NewAppUser> & AppUserFormDefaults),
  ): AppUserFormRawValue | PartialWithRequiredKeyOf<NewAppUserFormRawValue> {
    return {
      ...appUser,
      creationDate: appUser.creationDate ? appUser.creationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
