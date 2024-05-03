import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IServiceContract } from '../service-contract.model';

@Component({
  standalone: true,
  selector: 'jhi-service-contract-detail',
  templateUrl: './service-contract-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ServiceContractDetailComponent {
  serviceContract = input<IServiceContract | null>(null);

  previousState(): void {
    window.history.back();
  }
}
