import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFormula } from '../formula.model';

@Component({
  standalone: true,
  selector: 'jhi-formula-detail',
  templateUrl: './formula-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FormulaDetailComponent {
  formula = input<IFormula | null>(null);

  previousState(): void {
    window.history.back();
  }
}
