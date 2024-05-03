import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IPaySlip } from '../pay-slip.model';

@Component({
  standalone: true,
  selector: 'jhi-pay-slip-detail',
  templateUrl: './pay-slip-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PaySlipDetailComponent {
  paySlip = input<IPaySlip | null>(null);

  previousState(): void {
    window.history.back();
  }
}
