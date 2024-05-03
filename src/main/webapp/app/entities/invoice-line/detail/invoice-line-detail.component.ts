import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IInvoiceLine } from '../invoice-line.model';

@Component({
  standalone: true,
  selector: 'jhi-invoice-line-detail',
  templateUrl: './invoice-line-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class InvoiceLineDetailComponent {
  invoiceLine = input<IInvoiceLine | null>(null);

  previousState(): void {
    window.history.back();
  }
}
