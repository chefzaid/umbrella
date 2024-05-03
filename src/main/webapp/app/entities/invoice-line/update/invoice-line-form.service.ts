import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInvoiceLine, NewInvoiceLine } from '../invoice-line.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceLine for edit and NewInvoiceLineFormGroupInput for create.
 */
type InvoiceLineFormGroupInput = IInvoiceLine | PartialWithRequiredKeyOf<NewInvoiceLine>;

type InvoiceLineFormDefaults = Pick<NewInvoiceLine, 'id'>;

type InvoiceLineFormGroupContent = {
  id: FormControl<IInvoiceLine['id'] | NewInvoiceLine['id']>;
  label: FormControl<IInvoiceLine['label']>;
  description: FormControl<IInvoiceLine['description']>;
  price: FormControl<IInvoiceLine['price']>;
  quantity: FormControl<IInvoiceLine['quantity']>;
  invoice: FormControl<IInvoiceLine['invoice']>;
};

export type InvoiceLineFormGroup = FormGroup<InvoiceLineFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceLineFormService {
  createInvoiceLineFormGroup(invoiceLine: InvoiceLineFormGroupInput = { id: null }): InvoiceLineFormGroup {
    const invoiceLineRawValue = {
      ...this.getFormDefaults(),
      ...invoiceLine,
    };
    return new FormGroup<InvoiceLineFormGroupContent>({
      id: new FormControl(
        { value: invoiceLineRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      label: new FormControl(invoiceLineRawValue.label, {
        validators: [Validators.required],
      }),
      description: new FormControl(invoiceLineRawValue.description),
      price: new FormControl(invoiceLineRawValue.price, {
        validators: [Validators.required],
      }),
      quantity: new FormControl(invoiceLineRawValue.quantity),
      invoice: new FormControl(invoiceLineRawValue.invoice),
    });
  }

  getInvoiceLine(form: InvoiceLineFormGroup): IInvoiceLine | NewInvoiceLine {
    return form.getRawValue() as IInvoiceLine | NewInvoiceLine;
  }

  resetForm(form: InvoiceLineFormGroup, invoiceLine: InvoiceLineFormGroupInput): void {
    const invoiceLineRawValue = { ...this.getFormDefaults(), ...invoiceLine };
    form.reset(
      {
        ...invoiceLineRawValue,
        id: { value: invoiceLineRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceLineFormDefaults {
    return {
      id: null,
    };
  }
}
