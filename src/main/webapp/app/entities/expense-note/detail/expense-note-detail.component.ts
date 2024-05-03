import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IExpenseNote } from '../expense-note.model';

@Component({
  standalone: true,
  selector: 'jhi-expense-note-detail',
  templateUrl: './expense-note-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ExpenseNoteDetailComponent {
  expenseNote = input<IExpenseNote | null>(null);

  previousState(): void {
    window.history.back();
  }
}
