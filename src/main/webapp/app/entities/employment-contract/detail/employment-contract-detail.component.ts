import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IEmploymentContract } from '../employment-contract.model';

@Component({
  standalone: true,
  selector: 'jhi-employment-contract-detail',
  templateUrl: './employment-contract-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class EmploymentContractDetailComponent {
  employmentContract = input<IEmploymentContract | null>(null);

  previousState(): void {
    window.history.back();
  }
}
