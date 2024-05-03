import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITimeSheetLine } from '../time-sheet-line.model';

@Component({
  standalone: true,
  selector: 'jhi-time-sheet-line-detail',
  templateUrl: './time-sheet-line-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TimeSheetLineDetailComponent {
  timeSheetLine = input<ITimeSheetLine | null>(null);

  previousState(): void {
    window.history.back();
  }
}
