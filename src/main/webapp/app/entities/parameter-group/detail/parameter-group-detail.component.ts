import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IParameterGroup } from '../parameter-group.model';

@Component({
  standalone: true,
  selector: 'jhi-parameter-group-detail',
  templateUrl: './parameter-group-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ParameterGroupDetailComponent {
  parameterGroup = input<IParameterGroup | null>(null);

  previousState(): void {
    window.history.back();
  }
}
