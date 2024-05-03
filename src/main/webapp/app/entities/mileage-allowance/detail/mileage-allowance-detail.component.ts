import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMileageAllowance } from '../mileage-allowance.model';

@Component({
  standalone: true,
  selector: 'jhi-mileage-allowance-detail',
  templateUrl: './mileage-allowance-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MileageAllowanceDetailComponent {
  mileageAllowance = input<IMileageAllowance | null>(null);

  previousState(): void {
    window.history.back();
  }
}
