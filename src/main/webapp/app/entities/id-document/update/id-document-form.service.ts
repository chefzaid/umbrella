import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIdDocument, NewIdDocument } from '../id-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIdDocument for edit and NewIdDocumentFormGroupInput for create.
 */
type IdDocumentFormGroupInput = IIdDocument | PartialWithRequiredKeyOf<NewIdDocument>;

type IdDocumentFormDefaults = Pick<NewIdDocument, 'id'>;

type IdDocumentFormGroupContent = {
  id: FormControl<IIdDocument['id'] | NewIdDocument['id']>;
  idType: FormControl<IIdDocument['idType']>;
  idNumber: FormControl<IIdDocument['idNumber']>;
  document: FormControl<IIdDocument['document']>;
};

export type IdDocumentFormGroup = FormGroup<IdDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IdDocumentFormService {
  createIdDocumentFormGroup(idDocument: IdDocumentFormGroupInput = { id: null }): IdDocumentFormGroup {
    const idDocumentRawValue = {
      ...this.getFormDefaults(),
      ...idDocument,
    };
    return new FormGroup<IdDocumentFormGroupContent>({
      id: new FormControl(
        { value: idDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      idType: new FormControl(idDocumentRawValue.idType, {
        validators: [Validators.required],
      }),
      idNumber: new FormControl(idDocumentRawValue.idNumber, {
        validators: [Validators.required],
      }),
      document: new FormControl(idDocumentRawValue.document),
    });
  }

  getIdDocument(form: IdDocumentFormGroup): IIdDocument | NewIdDocument {
    return form.getRawValue() as IIdDocument | NewIdDocument;
  }

  resetForm(form: IdDocumentFormGroup, idDocument: IdDocumentFormGroupInput): void {
    const idDocumentRawValue = { ...this.getFormDefaults(), ...idDocument };
    form.reset(
      {
        ...idDocumentRawValue,
        id: { value: idDocumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): IdDocumentFormDefaults {
    return {
      id: null,
    };
  }
}
