import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IProspect } from '../prospect.model';

@Component({
  standalone: true,
  selector: 'jhi-prospect-detail',
  templateUrl: './prospect-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProspectDetailComponent {
  prospect = input<IProspect | null>(null);

  previousState(): void {
    window.history.back();
  }
}
