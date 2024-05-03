import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoice | NewInvoice> = Omit<T, 'issueDate'> & {
  issueDate?: string | null;
};

type InvoiceFormRawValue = FormValueOf<IInvoice>;

type NewInvoiceFormRawValue = FormValueOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id' | 'issueDate'>;

type InvoiceFormGroupContent = {
  id: FormControl<InvoiceFormRawValue['id'] | NewInvoice['id']>;
  invoiceNumber: FormControl<InvoiceFormRawValue['invoiceNumber']>;
  label: FormControl<InvoiceFormRawValue['label']>;
  issueDate: FormControl<InvoiceFormRawValue['issueDate']>;
  currency: FormControl<InvoiceFormRawValue['currency']>;
  salesTaxPct: FormControl<InvoiceFormRawValue['salesTaxPct']>;
  terms: FormControl<InvoiceFormRawValue['terms']>;
  poject: FormControl<InvoiceFormRawValue['poject']>;
  document: FormControl<InvoiceFormRawValue['document']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...invoice,
    });
    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      invoiceNumber: new FormControl(invoiceRawValue.invoiceNumber, {
        validators: [Validators.required],
      }),
      label: new FormControl(invoiceRawValue.label),
      issueDate: new FormControl(invoiceRawValue.issueDate, {
        validators: [Validators.required],
      }),
      currency: new FormControl(invoiceRawValue.currency),
      salesTaxPct: new FormControl(invoiceRawValue.salesTaxPct),
      terms: new FormControl(invoiceRawValue.terms),
      poject: new FormControl(invoiceRawValue.poject),
      document: new FormControl(invoiceRawValue.document),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return this.convertInvoiceRawValueToInvoice(form.getRawValue() as InvoiceFormRawValue | NewInvoiceFormRawValue);
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({ ...this.getFormDefaults(), ...invoice });
    form.reset(
      {
        ...invoiceRawValue,
        id: { value: invoiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issueDate: currentTime,
    };
  }

  private convertInvoiceRawValueToInvoice(rawInvoice: InvoiceFormRawValue | NewInvoiceFormRawValue): IInvoice | NewInvoice {
    return {
      ...rawInvoice,
      issueDate: dayjs(rawInvoice.issueDate, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceToInvoiceRawValue(
    invoice: IInvoice | (Partial<NewInvoice> & InvoiceFormDefaults),
  ): InvoiceFormRawValue | PartialWithRequiredKeyOf<NewInvoiceFormRawValue> {
    return {
      ...invoice,
      issueDate: invoice.issueDate ? invoice.issueDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
