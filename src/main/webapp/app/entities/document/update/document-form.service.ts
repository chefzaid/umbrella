import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocument, NewDocument } from '../document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocument for edit and NewDocumentFormGroupInput for create.
 */
type DocumentFormGroupInput = IDocument | PartialWithRequiredKeyOf<NewDocument>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocument | NewDocument> = Omit<T, 'uploadDate' | 'issuingDate' | 'expirationDate'> & {
  uploadDate?: string | null;
  issuingDate?: string | null;
  expirationDate?: string | null;
};

type DocumentFormRawValue = FormValueOf<IDocument>;

type NewDocumentFormRawValue = FormValueOf<NewDocument>;

type DocumentFormDefaults = Pick<NewDocument, 'id' | 'uploadDate' | 'issuingDate' | 'expirationDate'>;

type DocumentFormGroupContent = {
  id: FormControl<DocumentFormRawValue['id'] | NewDocument['id']>;
  label: FormControl<DocumentFormRawValue['label']>;
  uploadDate: FormControl<DocumentFormRawValue['uploadDate']>;
  issuingDate: FormControl<DocumentFormRawValue['issuingDate']>;
  expirationDate: FormControl<DocumentFormRawValue['expirationDate']>;
  file: FormControl<DocumentFormRawValue['file']>;
  fileContentType: FormControl<DocumentFormRawValue['fileContentType']>;
  documentType: FormControl<DocumentFormRawValue['documentType']>;
  employee: FormControl<DocumentFormRawValue['employee']>;
  employmentContract: FormControl<DocumentFormRawValue['employmentContract']>;
  serviceContract: FormControl<DocumentFormRawValue['serviceContract']>;
  enterprise: FormControl<DocumentFormRawValue['enterprise']>;
};

export type DocumentFormGroup = FormGroup<DocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentFormService {
  createDocumentFormGroup(document: DocumentFormGroupInput = { id: null }): DocumentFormGroup {
    const documentRawValue = this.convertDocumentToDocumentRawValue({
      ...this.getFormDefaults(),
      ...document,
    });
    return new FormGroup<DocumentFormGroupContent>({
      id: new FormControl(
        { value: documentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(documentRawValue.label),
      uploadDate: new FormControl(documentRawValue.uploadDate),
      issuingDate: new FormControl(documentRawValue.issuingDate),
      expirationDate: new FormControl(documentRawValue.expirationDate),
      file: new FormControl(documentRawValue.file, {
        validators: [Validators.required],
      }),
      fileContentType: new FormControl(documentRawValue.fileContentType),
      documentType: new FormControl(documentRawValue.documentType),
      employee: new FormControl(documentRawValue.employee),
      employmentContract: new FormControl(documentRawValue.employmentContract),
      serviceContract: new FormControl(documentRawValue.serviceContract),
      enterprise: new FormControl(documentRawValue.enterprise),
    });
  }

  getDocument(form: DocumentFormGroup): IDocument | NewDocument {
    return this.convertDocumentRawValueToDocument(form.getRawValue() as DocumentFormRawValue | NewDocumentFormRawValue);
  }

  resetForm(form: DocumentFormGroup, document: DocumentFormGroupInput): void {
    const documentRawValue = this.convertDocumentToDocumentRawValue({ ...this.getFormDefaults(), ...document });
    form.reset(
      {
        ...documentRawValue,
        id: { value: documentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      uploadDate: currentTime,
      issuingDate: currentTime,
      expirationDate: currentTime,
    };
  }

  private convertDocumentRawValueToDocument(rawDocument: DocumentFormRawValue | NewDocumentFormRawValue): IDocument | NewDocument {
    return {
      ...rawDocument,
      uploadDate: dayjs(rawDocument.uploadDate, DATE_TIME_FORMAT),
      issuingDate: dayjs(rawDocument.issuingDate, DATE_TIME_FORMAT),
      expirationDate: dayjs(rawDocument.expirationDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentToDocumentRawValue(
    document: IDocument | (Partial<NewDocument> & DocumentFormDefaults),
  ): DocumentFormRawValue | PartialWithRequiredKeyOf<NewDocumentFormRawValue> {
    return {
      ...document,
      uploadDate: document.uploadDate ? document.uploadDate.format(DATE_TIME_FORMAT) : undefined,
      issuingDate: document.issuingDate ? document.issuingDate.format(DATE_TIME_FORMAT) : undefined,
      expirationDate: document.expirationDate ? document.expirationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
